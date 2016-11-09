package propertymanager;

import org.w3c.dom.Document;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import xmlutils.InvalidXMLFileFormatException;
import xmlutils.XMLUtilities;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PropertyManager {

    private static final XMLUtilities   xmlUtilities = new XMLUtilities();

    private static PropertyManager singleton = null;

    private Map<String, String> properties;
    private Map<String, List<String>> propertyOptions;

    public static final String PROPERTY_ELEMENT = "property";
    public static final String PROPERTY_LIST_ELEMENT = "property_list";
    public static final String PROPERTY_OPTION_LIST_ELEMENT = "property_option_list";
    public static final String PROPERTY_OPTION_ELEMENT = "property_option";
    public static final String OPTION_ELEMENT = "option";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String VALUE_ATTRIBUTE = "value";

    public static final String PROPERTIES_RESOURCE_RELATIVE_PATH = "properties";

    private PropertyManager(){
        properties = new HashMap<>();
        propertyOptions = new HashMap<>();
    }

    public String getPropertyValue(String s) {return properties.get(s);}
    public String getPropertyValue(Object o) {return properties.get(o.toString());}

    public static PropertyManager getPropertyManager(){
        if (singleton == null){
            singleton = new PropertyManager();
        }
        return singleton;
    }

    public boolean hasProperty(Object property){return properties.get(property.toString())!= null;}

    public void loadProperties(Class klass, String xmlfilename, String schemafilename) throws InvalidXMLFileFormatException{
        URL xmlFileResource = klass.getClassLoader().getResource(PROPERTIES_RESOURCE_RELATIVE_PATH + File.separator + xmlfilename);
        URL schemaResource = klass.getClassLoader().getResource(PROPERTIES_RESOURCE_RELATIVE_PATH + File.separator + schemafilename);
        Document document = xmlUtilities.loadXMLDocument(xmlFileResource, schemaResource);

        Node propertyListNode = xmlUtilities.getNodeWithName(document, PROPERTY_LIST_ELEMENT);
        ArrayList<Node> propNodes = xmlUtilities.getChildNodesWithName(propertyListNode,PROPERTY_ELEMENT);

        for (Node n : propNodes){
            NamedNodeMap attributes = n.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++){
                String attName = attributes.getNamedItem(NAME_ATTRIBUTE).getTextContent();
                String attValue = attributes.getNamedItem(VALUE_ATTRIBUTE).getTextContent();
                properties.put(attName,attValue);
            }
        }

        Node propertyOptionsListNode = xmlUtilities.getNodeWithName(document, PROPERTY_OPTION_LIST_ELEMENT);
        if (propertyOptionsListNode != null){
            ArrayList<Node> propertyOptionsNodes = xmlUtilities.getChildNodesWithName(propertyOptionsListNode, PROPERTY_OPTION_ELEMENT);
            for (Node n : propertyOptionsNodes){
                NamedNodeMap attributes = n.getAttributes();
                String name = attributes.getNamedItem(NAME_ATTRIBUTE).getNodeValue();
                ArrayList<String> options = new ArrayList<>();
                propertyOptions.put(name, options);
                ArrayList<Node> optionsNodes = xmlUtilities.getChildNodesWithName(n, OPTION_ELEMENT);
                for (Node oNode : optionsNodes){
                    String option = oNode.getTextContent();
                    options.add(option);
                }
            }
        }

    }

}
