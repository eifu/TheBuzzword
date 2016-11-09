package ui;

import apptemeplate.AppTemplate;
import components.AppStyleArbiter;
import controller.FileController;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import static settings.AppPropertyType.*;
import static  settings.InitializationParameters.APP_IMAGE_DIR_PATH;


public class AppGUI implements AppStyleArbiter{

    private FileController fileController;
    private Stage primaryStage;
    private Scene primaryScene;
    private BorderPane appPane;
    private TilePane toolbarPane;
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
    public TilePane getToolbarPane(){return toolbarPane;}

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
        toolbarPane = new TilePane(Orientation.VERTICAL);
        toolbarPane.setPadding(new Insets(50));
        toolbarPane.setTileAlignment(Pos.CENTER);
        toolbarPane.setPrefColumns(1);
        toolbarPane.setPrefRows(10);
        loginoutbtn = initializeChildButton(toolbarPane, LOGOUTSTATE_ICON.toString(), false);
        homebtn = initializeChildButton(toolbarPane, HOME_ICON.toString(), true);
        helpbtn = initializeChildButton(toolbarPane, HELP_ICON.toString(), false);
        quitbtn = initializeChildButton(toolbarPane, QUIT_ICON.toString(), false);
    }

    public Button initializeChildButton(Pane pane, String icon, boolean disable) throws Exception{
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

            pane.getChildren().add(button);
        }catch (URISyntaxException e){
            e.printStackTrace();
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

        appPane = new BorderPane();
        appPane.setLeft(toolbarPane);
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
}
