package settings;


public enum InitializationParameters {
    APP_IMAGE_DIR_PATH("images"),
    PROPERTIES_SCHEMA_XSD("properties-schema.xsd"),
    APP_PROPERTIES_XML("app-properties.xml"),
    WORKSPACE_PROPERTIES_XML("workspace-properties.xml"),
    CLOSE_LABEL("CLOSE"),
    YES("Yes"),
    NO("No"),
    CANCEL("Cancel");

    private String parameter;

    InitializationParameters(String parameter){
        this.parameter = parameter;
    }

    public String getParameter(){return parameter;}
}
