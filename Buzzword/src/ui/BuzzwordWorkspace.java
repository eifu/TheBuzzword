package ui;


import apptemeplate.AppTemplate;
import buzzword.Buzzword;
import buzzword.GameScreenState;
import com.sun.prism.shader.Solid_TextureFirstPassLCD_AlphaTest_Loader;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import data.BuzzwordGameData;
import data.BuzzwordUserData;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import propertymanager.PropertyManager;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.Stack;

import static buzzword.GameScreenState.*;
import static buzzword.BuzzwordProperty.*;
import static settings.AppPropertyType.FACE_ICON;
import static settings.AppPropertyType.PLAYGAME_ICON;
import static settings.AppPropertyType.USER_INFO_TITLE;


public class BuzzwordWorkspace extends AppWorkspaceComponent{

    AppTemplate app;
    AppGUI gui;
    BuzzwordController controller;
    GameScreenState currentState;
    boolean signedIn;

    public BuzzwordWorkspace(AppTemplate app){
        this.app = app;
        this.gui = app.getGui();
        this.controller = (BuzzwordController) gui.getFileController();
        this.currentState = HOME;
        this.workspace = new GameScreen(currentState);
        this.signedIn = false;
        activateWorkspace(gui.getAppPane());
    }

    public void setHandler() {
        switch (currentState){
            case HOME:
                if (signedIn){
                    ObservableList<Node> workspaceHomeChildren = workspace.getChildren();
                    ObservableList<Node> vboxHomeChildren = ((VBox)workspaceHomeChildren.get(0)).getChildren();
                    StackPane s = (StackPane) vboxHomeChildren.get(2);
                    Button gamesStartBtn = (Button)s.getChildren().get(0);

                    if (gui.getToolbarPane().getChildren().size() != 6) { // TODO more efficient and elegant way needed
                        ObservableList<String> options =
                                FXCollections.observableArrayList(
                                        "English Dictionary",
                                        "Places",
                                        "Science",
                                        "Famous People"
                                );// TODO get those data from gamedata
                        ComboBox<String> comboBox = new ComboBox<>(options);
                        comboBox.setValue("English Dictionary");

                        comboBox.setPrefSize(150, 30);
                        comboBox.setLayoutX(26);
                        comboBox.setLayoutY(336);

                        Pane toolbar = gui.getToolbarPane();
                        toolbar.getChildren().addAll(comboBox);
                        try {
                            Button personalBtn = gui.initializeChildButton(FACE_ICON.toString(), false);
                            personalBtn.setOnAction(e -> {
                                PropertyManager pm = PropertyManager.getPropertyManager();
                                AppMessageSingleton dialog = AppMessageSingleton.getSingleton();
                                dialog.show(pm.getPropertyValue(USER_INFO_TITLE), "You are a master of buzzword.");
                            });


                            Label usernameLabel = new Label();
                            String name = ((BuzzwordUserData)app.getUserDataComponent()).getUsername();
                            usernameLabel.setText(name);
//                            usernameLabel.setFont(new Font("Roboto", ));
                            usernameLabel.setTextFill(Paint.valueOf("white"));
                            usernameLabel.setAlignment(Pos.CENTER);
                            usernameLabel.setMinWidth(Region.USE_PREF_SIZE);
                            usernameLabel.setMaxWidth(Region.USE_PREF_SIZE);
                            usernameLabel.setPadding(new Insets(0,10,0,10));
                            HBox face = new HBox();
                            face.setLayoutX(30);
                            face.setLayoutY(244);
                            face.setPrefSize(150, 30);
                            face.setAlignment(Pos.CENTER);
                            face.getChildren().addAll(usernameLabel, personalBtn);
                            toolbar.getChildren().add(face);

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }else {
                        gui.getToolbarPane().getChildren().get(4).setVisible(true);
                    }
                    gamesStartBtn.setOnAction(e ->{
                        setCurrentState(SELECTING);
                        reloadWorkspace(gui.getAppPane());

                        BuzzwordGameData gamedata = (BuzzwordGameData) app.getGameDataComponent();
                        ComboBox<String> gamemodeComboBox = (ComboBox)gui.getToolbarPane().getChildren().get(4);
                        gamedata.setCurrentMode(gamemodeComboBox.getValue());

                        gui.setHomebtnDisable(false);
                        gui.getToolbarPane().getChildren().get(4).setVisible(false);
                    });
                }
                break;
            case SIGNINGIN:
                ObservableList<Node> workspaceChildren = workspace.getChildren();
                ObservableList<Node> vboxChildren = ((VBox)workspaceChildren.get(0)).getChildren();

                ObservableList<Node> gridChildren =  ((GridPane)vboxChildren.get(1)).getChildren();

                Button signUpBtn = (Button)((HBox)gridChildren.get(5)).getChildren().get(0);
                signUpBtn.setOnAction(e ->{
                    setCurrentState(SIGNUP);
                    reloadWorkspace(gui.getAppPane());

                    setHandler();
                });

                Button signInBtn= (Button)((HBox)gridChildren.get(5)).getChildren().get(1);
                signInBtn.setOnAction(e ->{
                    TextField usernameTxt = (TextField) gridChildren.get(2);
                    TextField passwordTxt = (TextField) gridChildren.get(4);

                    String name = usernameTxt.getText();
                    String pass = passwordTxt.getText();
                    if (!name.equals("") && !pass.equals("")){
                        setCurrentState(HOME);
                        reloadWorkspace(gui.getAppPane());
                        signedIn = true;

                        BuzzwordUserData userData = (BuzzwordUserData) app.getUserDataComponent();
                        userData.setUsername(name);
                        userData.setPassword(pass);

                        setHandler();

                        // gameStartBtn config
                        ObservableList<Node> workspaceHomeChildren = workspace.getChildren();
                        ObservableList<Node> vboxHomeChildren = ((VBox)workspaceHomeChildren.get(0)).getChildren();
                        StackPane s = (StackPane) vboxHomeChildren.get(2);
                        s.setVisible(true);

                        // toolbar config
                        gui.setLoginoutbtnIcon(true);
                        gui.setHomebtnDisable(true);
                        gui.setLoginoutbtnDisable(false);


                    }
                });
                break;
            case SIGNUP:
                workspaceChildren = workspace.getChildren();
                vboxChildren = ((VBox)workspaceChildren.get(0)).getChildren();

                gridChildren =  ((GridPane)vboxChildren.get(1)).getChildren();
                signInBtn= (Button)((HBox)gridChildren.get(5)).getChildren().get(0);
                signInBtn.setOnAction(e ->{
                    String name =((TextField)gridChildren.get(2)).getText();
                    String pass =((TextField)gridChildren.get(4)).getText();
                    setCurrentState(HOME);
                    reloadWorkspace(gui.getAppPane());
                    signedIn = true;

                    BuzzwordUserData userData = (BuzzwordUserData) app.getUserDataComponent();
                    userData.setUsername(name);
                    userData.setPassword(pass);

                    setHandler();

                    // gameStartBtn config
                    ObservableList<Node> workspaceHomeChildren = workspace.getChildren();
                    ObservableList<Node> vboxHomeChildren = ((VBox)workspaceHomeChildren.get(0)).getChildren();
                    StackPane s = (StackPane) vboxHomeChildren.get(2);
                    s.setVisible(true);

                    // toolbar config
                    gui.setLoginoutbtnIcon(true);
                    gui.setHomebtnDisable(true);
                    gui.setLoginoutbtnDisable(false);

                });
                break;

            case SELECTING:
                int progress = 4; // TODO need to get from BuzzwordUserData
                int totalLevel = 8; // TODO need to get from BuzzwordGameData
                workspaceChildren = workspace.getChildren();
                vboxChildren = ((VBox)workspaceChildren.get(0)).getChildren();
                ObservableList<Node> levelChildren = ((Pane)vboxChildren.get(2)).getChildren();
                Label l = (Label) vboxChildren.get(1);

                ComboBox comboBox = (ComboBox) gui.getToolbarPane().getChildren().get(4);
                l.setText((String)comboBox.getValue());

                for (int level = 0; level < totalLevel; level ++){
                    StackPane s = (StackPane)levelChildren.get(level);
                    Button b = (Button) s.getChildren().get(0);
                    b.setOnAction(e ->{
                        BuzzwordGameData gamedata = (BuzzwordGameData) app.getGameDataComponent();
                        gamedata.setCurrentLevel(Integer.parseInt(b.getId()));

                        setCurrentState(GAMEPLAY);
                        reloadWorkspace(gui.getAppPane());
                    });
                    if (level < progress) {
                        b.setDisable(false);
                    }
                }
                break;
            case GAMEPLAY:
                PropertyManager pm = PropertyManager.getPropertyManager();

                workspaceChildren = workspace.getChildren();
                BorderPane gameWorkspace = (BorderPane)workspaceChildren.get(0);
                VBox centerVBox = (VBox) gameWorkspace.getCenter();

                Label modeLabel = (Label)centerVBox.getChildren().get(1);
                String mode = "mode: " + ((BuzzwordGameData)app.getGameDataComponent()).getCurrentMode();
                modeLabel.setText(mode);

                Label levelLabel = (Label)centerVBox.getChildren().get(3);
                String level = "Level: " + ((BuzzwordGameData)app.getGameDataComponent()).getCurrentLevel();
                levelLabel.setText(level);

                try {
                    Button playGameBtn = gui.initializeChildButton(PLAYGAME_ICON.toString(), false);
                    playGameBtn.setAlignment(Pos.BOTTOM_CENTER);
                    playGameBtn.setPrefSize(400, 0);
                    playGameBtn.getStyleClass().add(pm.getPropertyValue(PLAY_RESUME_BUTTON));
                    centerVBox.getChildren().add(playGameBtn);
                }catch(Exception e){
                    e.printStackTrace();
                }

        }
    }

    public AppGUI getGui(){return gui;}

    public void setCurrentState(GameScreenState state){this.currentState = state;}

    @Override
    public void initStyle(){
        PropertyManager pm = PropertyManager.getPropertyManager();

        gui.getAppPane().setId(pm.getPropertyValue(ROOT_BORDERPANE_ID));
        gui.getToolbarPane().getStyleClass().setAll(pm.getPropertyValue(SEGMENTED_BUTTON_BAR));

        ObservableList<Node> toolbarChildren = gui.getToolbarPane().getChildren();
        toolbarChildren.get(0).setId(pm.getPropertyValue(TOP_TOOLBAR_ID));

        workspace.getStyleClass().add(CLASS_BORDERED_PANE);

        gui.getQuitbtn().getStyleClass().add(pm.getPropertyValue(QUIT_BUTTON));
        gui.getHelpbtn().getStyleClass().add(pm.getPropertyValue(HELP_BUTTON));

    }

    @Override
    public void reloadWorkspace(BorderPane appPane){
        workspace = ((GameScreen)workspace).change(currentState);
        appPane.setCenter(workspace);
        setHandler();
    }


    public boolean isSignedIn(){return signedIn;}
    public void setSignedIn(boolean o){signedIn=o;}
}
