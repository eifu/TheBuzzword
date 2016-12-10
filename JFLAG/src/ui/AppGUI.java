package ui;

import apptemeplate.AppTemplate;
import components.AppStyleArbiter;
import controller.FileController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import propertymanager.PropertyManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static settings.AppPropertyType.*;
import static  settings.InitializationParameters.APP_IMAGE_DIR_PATH;


public class AppGUI implements AppStyleArbiter{

    private FileController fileController;
    private Stage primaryStage;
    private Scene primaryScene;
    private BorderPane appPane;
    private Pane toolbarPane;
    private Button loginoutbtn;
    private Button homebtn;
    private Button helpbtn;
    private Button quitbtn;

    private String appTitle;
    private int appWindowWidth;
    private int appWindowHeight;

    public FileController getFileController(){return fileController;}
    public Stage getPrimaryStage(){return primaryStage;}
    public Scene getPrimaryScene(){return primaryScene;}
    public BorderPane getAppPane(){return appPane;}
    public Pane getToolbarPane(){return toolbarPane;}
    public Button getLoginoutbtn(){return loginoutbtn;}
    public Button getHomebtn(){return homebtn;}
    public Button getHelpbtn(){return helpbtn;}
    public Button getQuitbtn(){return quitbtn;}

    public AppGUI(Stage primaryStage, String title, AppTemplate app) throws  Exception{
        this(primaryStage, title, app, -1, -1);
    }

    public AppGUI(Stage primaryStage, String title, AppTemplate app, int windowWidth, int windowHeight)throws Exception{
        this.appWindowWidth = windowWidth;
        this.appWindowHeight = windowHeight;
        this.primaryStage = primaryStage;
        this.appTitle = title;
        initializeToolbar();
        initializeToolbarHandlers(app);
        initializeWindow();
    }

    private void initializeToolbar()throws Exception{
        toolbarPane = new Pane();
        toolbarPane.setPrefSize(200, 200);

        Rectangle toolbarBackgroundRectangle = new Rectangle();
        toolbarBackgroundRectangle.setHeight(550);
        toolbarBackgroundRectangle.setWidth(145);


        loginoutbtn = initializeChildButton(LOGOUTSTATE_ICON.toString(), false);
        homebtn = initializeChildButton(HOME_ICON.toString(), true);
        helpbtn = initializeChildButton(HELP_ICON.toString(), false);
        quitbtn = initializeChildButton(QUIT_ICON.toString(), false);

        loginoutbtn.setPrefSize(150, 30);
        loginoutbtn.setLayoutX(26);
        loginoutbtn.setLayoutY(83);
        loginoutbtn.setMnemonicParsing(false);

        homebtn.setPrefSize(150, 30);
        homebtn.setLayoutX(26);
        homebtn.setLayoutY(161);
        homebtn.setMnemonicParsing(false);

        helpbtn.setPrefSize(145, 30);
        helpbtn.relocate(0,500);
        toolbarPane.getChildren().addAll(toolbarBackgroundRectangle, loginoutbtn, homebtn, helpbtn);
    }

    public Button initializeChildButton(String text, String icon, boolean disable) throws  Exception{
        Button b = initializeChildButton(icon, disable);
        b.setText(text);
        return b;
    }

    public Button initializeChildButton(String icon, boolean disable) throws FileNotFoundException{
        PropertyManager pm = PropertyManager.getPropertyManager();

        URL imageDirectoryURL = AppTemplate.class.getClassLoader().getResource(APP_IMAGE_DIR_PATH.getParameter());
        if (imageDirectoryURL == null){
            throw new FileNotFoundException("Image resource folder does not exist");
        }

        Button button = new Button();
        try (InputStream imgInputStream = Files.newInputStream(Paths.get(imageDirectoryURL.toURI()).resolve(pm.getPropertyValue(icon)))){
            Image btnimage = new Image(imgInputStream);
            button.setDisable(disable);
            button.setGraphic(new ImageView(btnimage));

        }catch (URISyntaxException e){
            e.printStackTrace();
            System.exit(1);
        }catch (IOException ioe){
            ioe.printStackTrace();
            System.exit(1);
        }

        return button;
    }

    private void initializeToolbarHandlers(AppTemplate app) throws InstantiationException{
        try{
            Method getFileControllerMethod = app.getClass().getMethod("getFileControllerClass");
            String fileControllerClassName = (String) getFileControllerMethod.invoke(app);
            Class<?> klass =     Class.forName("controller."+fileControllerClassName);
            Constructor<?> constructor = klass.getConstructor(AppTemplate.class);
            fileController = (FileController)constructor.newInstance(app);

        }catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        }

        loginoutbtn.setOnAction(e -> fileController.handleLoginoutRequest());
        homebtn.setOnAction(e -> fileController.handleHomeRequest());
        helpbtn.setOnAction(e -> fileController.handleHelpRequest());
        quitbtn.setOnAction(e -> fileController.handleQuitRequest());
    }

    private void initializeWindow() throws IOException {
        PropertyManager pm = PropertyManager.getPropertyManager();

        primaryStage.setTitle(appTitle);

        primaryStage.initStyle(StageStyle.UNDECORATED);

        appPane = new BorderPane();
        appPane.setLeft(toolbarPane);
        appPane.setRight(quitbtn);
        primaryScene = appWindowWidth < 1 || appWindowHeight < 1 ?
                new Scene(appPane)
                :new Scene(appPane, appWindowWidth, appWindowHeight);


        URL imageDirectoryURL = AppTemplate.class.getClassLoader().getResource(APP_IMAGE_DIR_PATH.getParameter());
        if (imageDirectoryURL == null){
            throw new FileNotFoundException("Image resource folder does not exist");
        }
        try (InputStream appLogoStream = Files.newInputStream(Paths.get(imageDirectoryURL.toURI()).resolve(pm.getPropertyValue(APP_LOGO.toString())))){
            primaryStage.getIcons().add(new Image(appLogoStream));
        }catch (URISyntaxException e){
            e.printStackTrace();
        }

        primaryStage.setScene(primaryScene);
        primaryStage.show();

    }

    @Override
    public void initStyle(){

    }


    public void setHomebtnDisable(boolean home){
        homebtn.setDisable(home);
    }

    public void setLoginoutbtnDisable(boolean loginout){
        loginoutbtn.setDisable(loginout);
    }

    public void setLoginoutbtnIcon(boolean in){
        String s;
        if (in){
           s = LOGINSTATE_ICON.toString();
        }else {
            s = LOGOUTSTATE_ICON.toString();
        }

        try{
            loginoutbtn = initializeChildButton(s, false);
        }catch (Exception e){
            e.printStackTrace();
        }

        loginoutbtn.setPrefSize(150, 30);
        loginoutbtn.setLayoutX(26);
        loginoutbtn.setLayoutY(83);
        loginoutbtn.setMnemonicParsing(false);

        loginoutbtn.setOnAction(e->fileController.handleLoginoutRequest());

        ObservableList<Node> toolbarChildern = toolbarPane.getChildren();
        toolbarChildern.set(1,loginoutbtn);
    }
}
