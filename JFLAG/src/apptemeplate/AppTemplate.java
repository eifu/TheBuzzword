package apptemeplate;

import components.*;
import javafx.stage.Stage;
import propertymanager.PropertyManager;


import javafx.application.Application;
import settings.InitializationParameters;
import ui.AppGUI;
import ui.AppMessageSingleton;
import ui.AppYesNoCancelSingleton;
import xmlutils.InvalidXMLFileFormatException;

import java.io.File;
import java.net.URL;

import static settings.AppPropertyType.*;
import static settings.InitializationParameters.*;


public abstract class AppTemplate extends Application {

    private final PropertyManager propertyManager = PropertyManager.getPropertyManager();
    private AppGameDataComponent gameDataComponent;
    private AppUserDataComponent userDataComponent;
    private AppFileComponent fileComponent;
    private AppWorkspaceComponent workspaceComponent;
    private AppGUI gui;

    public abstract AppComponentBuilder makeAppComponentBuilder();

    // This method is for AppGUI to recall FilecController remotely
    public String getFileControllerClass() {
        return "FileController";
    }


    public AppGameDataComponent getGameDataComponent(){return gameDataComponent;}
    public AppUserDataComponent getUserDataComponent(){return userDataComponent;}
    public AppFileComponent getFileComponent(){return fileComponent;}
    public AppWorkspaceComponent getWorkspaceComponent(){return workspaceComponent;}
    public AppGUI getGui(){return gui;}

    @Override
    public void start(Stage primaryStage){

        // Create singletons
        AppMessageSingleton messageDialog = AppMessageSingleton.getSingleton();
        AppYesNoCancelSingleton yesnocancelDialog = AppYesNoCancelSingleton.getSingleton();

        // Initialize singletons
        messageDialog.init(primaryStage);
        yesnocancelDialog.init(primaryStage);

        try{
            if(loadProperties(APP_PROPERTIES_XML) && loadProperties(WORKSPACE_PROPERTIES_XML) ){
                AppComponentBuilder builder = makeAppComponentBuilder();

                fileComponent = builder.buildFileComponent();
                gameDataComponent = builder.buildGameDataComponent();
                userDataComponent = builder.buildUserDataComponent();
                gui = (propertyManager.hasProperty(APP_WINDOW_HEIGHT) && propertyManager.hasProperty(APP_WINDOW_WIDTH))
                        ? new AppGUI(primaryStage, propertyManager.getPropertyValue(APP_TITLE), this,
                                    Integer.parseInt(propertyManager.getPropertyValue(APP_WINDOW_WIDTH)),
                                    Integer.parseInt(propertyManager.getPropertyValue(APP_WINDOW_HEIGHT)))
                        : new AppGUI(primaryStage, propertyManager.getPropertyValue(APP_TITLE), this);

                workspaceComponent = builder.buildWorkspaceComponent();

                // Set cssResource to PrimaryScene
                initStyleSheet();

                // AppGUI and AppWorkspaceComponent are inherited from AppStyleArbiter
                gui.initStyle();
                workspaceComponent.initStyle();

            }
        }catch (Exception e){
            AppMessageSingleton dialog = AppMessageSingleton.getSingleton();
            dialog.show(propertyManager.getPropertyValue(PROPERTY_LOAD_ERROR_TITLE),
                        propertyManager.getPropertyValue(PROPERTY_LOAD_ERROR_MESSAGE));
        }

    }

    private boolean loadProperties(InitializationParameters propertyParameter){
        try{
            propertyManager.loadProperties(AppTemplate.class, propertyParameter.getParameter(), PROPERTIES_SCHEMA_XSD.getParameter());
        }catch (InvalidXMLFileFormatException e){
            AppMessageSingleton dialog = AppMessageSingleton.getSingleton();
            dialog.show(propertyManager.getPropertyValue(PROPERTY_LOAD_ERROR_TITLE),
                        propertyManager.getPropertyValue(PROPERTY_LOAD_ERROR_MESSAGE));
            return false;
        }
        return true;
    }

    public void initStyleSheet(){
        URL cssResource = getClass().getClassLoader().getResource(propertyManager.getPropertyValue(APP_PATH_CSS)+ File.separator+propertyManager.getPropertyValue(APP_CSS));
        assert cssResource != null;
        gui.getPrimaryScene().getStylesheets().add(cssResource.toExternalForm());
    }


}
