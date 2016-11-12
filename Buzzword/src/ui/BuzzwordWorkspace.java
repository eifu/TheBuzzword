package ui;


import apptemeplate.AppTemplate;
import buzzword.GameScreenState;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import data.BuzzwordGameData;
import data.BuzzwordUserData;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import propertymanager.PropertyManager;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import static buzzword.GameScreenState.*;
import static buzzword.BuzzwordProperty.*;
import static settings.AppPropertyType.*;


public class BuzzwordWorkspace extends AppWorkspaceComponent {

    AppTemplate app;
    AppGUI gui;
    BuzzwordController controller;
    GameScreenState currentState;
    boolean signedIn;
    boolean gamePlay;

    public BuzzwordWorkspace(AppTemplate app) {
        this.app = app;
        this.gui = app.getGui();
        this.controller = (BuzzwordController) gui.getFileController();
        this.currentState = HOME;
        this.workspace = new GameScreen(currentState);
        this.signedIn = false;
        this.gamePlay = false;
        activateWorkspace(gui.getAppPane());
    }

    public void renderGameScreen() {
        switch (currentState) {
            case HOME:
                ((GameScreen) workspace).home();
                break;
            case SIGNINGIN:
                ((GameScreen) workspace).signingIn();
                break;
            case SIGNUP:
                ((GameScreen) workspace).signUp();
                break;
            case SELECTING:
                ((GameScreen) workspace).selecting();
                break;
            case GAMEPLAY:
                ((GameScreen) workspace).gamePlay();
                break;
        }
    }

    public void setHandler() {
        switch (currentState) {
            case HOME:
                if (signedIn) {
                    ObservableList<Node> workspaceHomeChildren = workspace.getChildren();
                    ObservableList<Node> vboxHomeChildren = ((VBox) workspaceHomeChildren.get(0)).getChildren();
                    StackPane s = (StackPane) vboxHomeChildren.get(2);
                    Button gameStartBtn = (Button) s.getChildren().get(0);

                    // let comboBox visible
                    gui.getToolbarPane().getChildren().get(4).setVisible(true);

                    Button personalBtn = (Button) gui.getToolbarPane().getChildren().get(5);
                    personalBtn.setOnAction(e2 -> {
                        PropertyManager pm = PropertyManager.getPropertyManager();
                        AppMessageSingleton dialog = AppMessageSingleton.getSingleton();
                        dialog.show(pm.getPropertyValue(USER_INFO_TITLE), "You are a master of buzzword.");
                    });

                    gameStartBtn.setOnAction(e -> {
                        setCurrentState(SELECTING);
                        reloadWorkspace(gui.getAppPane());

                        setHandler();

                        BuzzwordGameData gamedata = (BuzzwordGameData) app.getGameDataComponent();
                        ComboBox<String> gamemodeComboBox = (ComboBox) gui.getToolbarPane().getChildren().get(4);
                        gamedata.setCurrentMode(gamemodeComboBox.getValue());

                        gui.setHomebtnDisable(false);
                        gui.getToolbarPane().getChildren().get(4).setVisible(false);
                    });
                }
                break;
            case SIGNINGIN:
                ObservableList<Node> workspaceChildren = workspace.getChildren();
                ObservableList<Node> vboxChildren = ((VBox) workspaceChildren.get(0)).getChildren();
                ObservableList<Node> gridChildren = ((GridPane) vboxChildren.get(1)).getChildren();
                Button signUpBtn = (Button) ((HBox) gridChildren.get(5)).getChildren().get(0);

                signUpBtn.setOnAction(e -> {
                    setCurrentState(SIGNUP);
                    reloadWorkspace(gui.getAppPane());

                    setHandler();
                });

                Button signInBtn = (Button) ((HBox) gridChildren.get(5)).getChildren().get(1);
                signInBtn.setOnAction(e -> {
                    TextField usernameTxt = (TextField) gridChildren.get(2);
                    TextField passwordTxt = (TextField) gridChildren.get(4);

                    String name = usernameTxt.getText();
                    String pass = passwordTxt.getText();

                    BuzzwordGameData gamedata = (BuzzwordGameData)app.getGameDataComponent();
                    if (gamedata.validateUsernamePassword(name, pass)) {

                        setCurrentState(HOME);
                        reloadWorkspace(gui.getAppPane());

                        renderHome(name);

                        signedIn = true;

                        BuzzwordUserData userData = (BuzzwordUserData) app.getUserDataComponent();
                        userData.setUsername(name);
                        userData.setPassword(pass);

                        setHandler();

                    }
                });
                break;
            case SIGNUP:
                workspaceChildren = workspace.getChildren();
                vboxChildren = ((VBox) workspaceChildren.get(0)).getChildren();
                gridChildren = ((GridPane) vboxChildren.get(1)).getChildren();

                signInBtn = (Button) ((HBox) gridChildren.get(5)).getChildren().get(0);
                signInBtn.setOnAction(e -> {
                    String name = ((TextField) gridChildren.get(2)).getText();
                    String pass = ((TextField) gridChildren.get(4)).getText();
                    setCurrentState(HOME);
                    reloadWorkspace(gui.getAppPane());

                    renderHome(name);

                    signedIn = true;
                    BuzzwordUserData userData = (BuzzwordUserData) app.getUserDataComponent();
                    userData.setUsername(name);
                    userData.setPassword(pass);

                    setHandler();

                });
                break;

            case SELECTING:
                int progress = 4; // TODO need to get from BuzzwordUserData
                int totalLevel = 8; // TODO need to get from BuzzwordGameData
                workspaceChildren = workspace.getChildren();
                vboxChildren = ((VBox) workspaceChildren.get(0)).getChildren();
                ObservableList<Node> levelChildren = ((Pane) vboxChildren.get(2)).getChildren();

                Label gameModeLabel = (Label) vboxChildren.get(1);
                ComboBox comboBox = (ComboBox) gui.getToolbarPane().getChildren().get(4);
                gameModeLabel.setText((String) comboBox.getValue());

                for (int level = 0; level < totalLevel; level++) {
                    Button b = (Button) levelChildren.get(level);
                    b.setOnAction(e -> {
                        BuzzwordGameData gamedata = (BuzzwordGameData) app.getGameDataComponent();
                        gamedata.setCurrentLevel(Integer.parseInt(b.getId()));


                        setCurrentState(GAMEPLAY);
                        reloadWorkspace(gui.getAppPane());

                        initGamePlay();

                        setHandler();
                    });
                    if (level < progress) {
                        b.setDisable(false);
                    }
                }
                break;

            case GAMEPLAY:
                workspaceChildren = workspace.getChildren();
                BorderPane borderPaneChildren = (BorderPane) workspaceChildren.get(0);
                VBox centerVBoxChildren = (VBox) borderPaneChildren.getCenter();

                BorderPane buttons = (BorderPane) centerVBoxChildren.getChildren().get(4);

                Button playResumeButton = (Button) buttons.getCenter();
                playResumeButton.setOnAction(e -> {
                    renderGamePlay();
                });

        }
    }

    public AppGUI getGui() {
        return gui;
    }

    public void setCurrentState(GameScreenState state) {
        this.currentState = state;
    }

    @Override
    public void initStyle() {
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
    public void reloadWorkspace(BorderPane appPane) {
        workspace = ((GameScreen) workspace).change(currentState);
        appPane.setCenter(workspace);
//        setHandler();
    }


    public boolean isSignedIn() {
        return signedIn;
    }

    public void setSignedIn(boolean o) {
        signedIn = o;
    }

    public void renderHome(String name) {
        ObservableList<String> options =
                FXCollections.observableArrayList(((BuzzwordGameData)app.getGameDataComponent()).getModeList());
        ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.setValue(options.get(0));
        comboBox.setPrefSize(150, 30);
        comboBox.setLayoutX(26);
        comboBox.setLayoutY(336);
        gui.getToolbarPane().getChildren().add(comboBox);

        Button personalBtn;
        try {
            personalBtn = gui.initializeChildButton(name, FACE_ICON.toString(), false);
            personalBtn.setMinWidth(150);
            personalBtn.setMaxWidth(Region.USE_PREF_SIZE);
            personalBtn.setLayoutX(30);
            personalBtn.setLayoutY(244);
            gui.getToolbarPane().getChildren().add(personalBtn);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        renderGameScreen();

        // toolbar config
        gui.setLoginoutbtnIcon(true);
        gui.setHomebtnDisable(true);
        gui.setLoginoutbtnDisable(false);
    }

    public void initGamePlay() {
        PropertyManager pm = PropertyManager.getPropertyManager();

        ObservableList<Node> workspaceChildren = workspace.getChildren();
        BorderPane gameWorkspace = (BorderPane) workspaceChildren.get(0);
        VBox centerVBox = (VBox) gameWorkspace.getCenter();

        Label modeLabel = (Label) centerVBox.getChildren().get(1);
        String mode = "mode: " + ((BuzzwordGameData) app.getGameDataComponent()).getCurrentMode();
        modeLabel.setText(mode);

        Label levelLabel = (Label) centerVBox.getChildren().get(3);
        String level = "Level: " + ((BuzzwordGameData) app.getGameDataComponent()).getCurrentLevel();
        levelLabel.setText(level);

        Button playResumeBtn = null;
        Button nextGameBtn = null;
        Button prevGameBtn = null;

        try {
            playResumeBtn = gui.initializeChildButton(PLAYGAME_ICON.toString(), false);
            nextGameBtn = gui.initializeChildButton(NEXTGAME_ICON.toString(), false);
            prevGameBtn = gui.initializeChildButton(PREVGAME_ICON.toString(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        playResumeBtn.getStyleClass().add(pm.getPropertyValue(PLAY_RESUME_BUTTON));
        nextGameBtn.getStyleClass().add(pm.getPropertyValue(NEXT_GAME_BUTTON));
        prevGameBtn.getStyleClass().add(pm.getPropertyValue(PREV_GAME_BUTTON));

        if (((BuzzwordGameData)app.getGameDataComponent()).getCurrentLevel() == 1){
            prevGameBtn.setDisable(true);
        }
        if (((BuzzwordGameData)app.getGameDataComponent()).getCurrentLevel()
                == ((BuzzwordUserData)app.getUserDataComponent()).getProgress(
                ((BuzzwordGameData) app.getGameDataComponent()).getCurrentMode())){
            nextGameBtn.setDisable(true);
        }

        BorderPane buttons = new BorderPane();
        buttons.setLeft(prevGameBtn);
        buttons.setCenter(playResumeBtn);
        buttons.setRight(nextGameBtn);

        centerVBox.getChildren().add(buttons);
    }

    public void renderGamePlay() {
        ObservableList<Node> workspaceChildren = workspace.getChildren();
        BorderPane borderPaneChildren = (BorderPane) workspaceChildren.get(0);
        VBox centerVBoxChildren = (VBox) borderPaneChildren.getCenter();

        BorderPane buttons = (BorderPane) centerVBoxChildren.getChildren().get(4);

        Button playResumeButton = (Button) buttons.getCenter();
        try {
            if (gamePlay) {
                playResumeButton = gui.initializeChildButton(PLAYGAME_ICON.toString(), false);
                this.gamePlay = false;
            } else {
                playResumeButton = gui.initializeChildButton(RESUMEGAME_ICON.toString(), false);
                this.gamePlay = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        playResumeButton.getStyleClass().add("play-resume-game-button");
        buttons.setCenter(playResumeButton);

        setHandler();
    }
}
