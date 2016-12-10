package ui;


import apptemeplate.AppTemplate;
import buzzword.Buzzword;
import buzzword.GameScreenState;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import data.BuzzwordGameData;
import data.BuzzwordUserData;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
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
    Pane posemenu;
    Pane personalInfo;
    Button personalBtn;
    Pane circles; // circle buttons that will be under personalInfo pane.

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

    public GameScreenState getCurrentState() {
        return currentState;
    }

    public boolean isPlayingGame() {
        return gamePlay;
    }

    public void removePosemenu() {
        ((GameScreen) workspace).removePosemenu(posemenu);
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
                    circles = (Pane) vboxHomeChildren.get(1);
                    // let comboBox visible
                    gui.getToolbarPane().getChildren().get(4).setVisible(true);

                    personalBtn = (Button) gui.getToolbarPane().getChildren().get(5);
                    personalBtn.setDisable(false);
                    personalBtn.setOnAction(e2 -> {

                        circles.getChildren().add(personalInfo);
                        personalBtn.setDisable(true);
                    });

//                    ((Button)personalInfo.getChildren().get(0)).setOnAction(e->{
//                        circles.getChildren().remove(personalInfo);
//                        personalBtn.setDisable(false);
//                    });

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

                    BuzzwordGameData gamedata = (BuzzwordGameData) app.getGameDataComponent();
                    if (gamedata.validateUsernamePassword(name, pass)) {

                        setCurrentState(HOME);
                        reloadWorkspace(gui.getAppPane());


                        signedIn = true;

                        BuzzwordUserData userData = (BuzzwordUserData) app.getUserDataComponent();
                        userData.setUsername(name);
                        userData.login(app);


                        renderHome(name);
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
                    BuzzwordGameData gameData = (BuzzwordGameData) app.getGameDataComponent();
                    if (gameData.validateUsername(name)) {
                        gameData.addNamePassMap(name, pass);
                        gameData.save();

                        setCurrentState(HOME);
                        reloadWorkspace(gui.getAppPane());


                        signedIn = true;
                        BuzzwordUserData userData = (BuzzwordUserData) app.getUserDataComponent();
                        userData.setUsername(name);
                        userData.setPassword(pass);
                        userData.signup(app);

                        renderHome(name);
                        setHandler();
                    }
                });
                break;

            case SELECTING:

                personalBtn.setDisable(false);

                int totalLevel = 8; // TODO need to get from BuzzwordGameData

                workspaceChildren = workspace.getChildren();
                vboxChildren = ((VBox) workspaceChildren.get(0)).getChildren();

                ObservableList<Node> levelChildren = ((Pane) vboxChildren.get(2)).getChildren();

                Label gameModeLabel = (Label) vboxChildren.get(1);
                ComboBox comboBox = (ComboBox) gui.getToolbarPane().getChildren().get(4);
                String mode = (String) comboBox.getValue();
                gameModeLabel.setText("mode: " + mode);

                if (mode.equals("countries")){
                    totalLevel = 4;
                }

                circles = (Pane) vboxChildren.get(2);

                BuzzwordUserData userData = (BuzzwordUserData) app.getUserDataComponent();
                int progress = userData.getProgress(mode);

                for (int level = 0; level < totalLevel; level++) {
                    Button b = (Button) levelChildren.get(level);
                    b.setOnAction(e -> {
                        BuzzwordGameData gamedata = (BuzzwordGameData) app.getGameDataComponent();
                        gamedata.setCurrentLevel(Integer.parseInt(b.getId()));


                        setCurrentState(GAMEPLAY);
                        reloadWorkspace(gui.getAppPane());

                        initGamePlay();


                    });
                    if (level < progress) {
                        b.setDisable(false);
                    }
                }
                // TODO find buttons and diable things.
                // this line is not efficient
                for (int level = totalLevel; level < 8; level++){
                    Button b = (Button) levelChildren.get(level);
                    b.setVisible(false);
                }
                break;

            case GAMEPLAY:

                personalBtn.setDisable(true);

                workspaceChildren = workspace.getChildren();
                Pane borderPaneChildren = (Pane) workspaceChildren.get(0);
                VBox centerVBoxChildren = (VBox) borderPaneChildren.getChildren().get(0);

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
    }


    public boolean isSignedIn() {
        return signedIn;
    }

    public void setSignedIn(boolean o) {
        signedIn = o;
    }

    public void renderHome(String name) {
        ObservableList<String> options =
                FXCollections.observableArrayList(((BuzzwordGameData) app.getGameDataComponent()).getModeList());
        ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.setValue(options.get(0));
        comboBox.setPrefSize(150, 30);
        comboBox.setLayoutX(26);
        comboBox.setLayoutY(336);
        gui.getToolbarPane().getChildren().add(comboBox);

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

        personalInfo = new Pane();
        personalInfo.setPrefSize(240, 240);
        personalInfo.setStyle("-fx-background-color: darkblue;" +
                "-fx-opacity: 0.8;");
        personalInfo.setLayoutX(80);
        personalInfo.setLayoutY(60);

        BuzzwordUserData userData = (BuzzwordUserData) app.getUserDataComponent();
        Label nameLbl = new Label(userData.getUsername());
        nameLbl.setTextFill(Paint.valueOf("white"));
        nameLbl.setFont(new Font("ariel", 30));
        nameLbl.setLayoutX(25);
        nameLbl.setLayoutY(25);
        personalInfo.getChildren().add(nameLbl);
        Label levelLbl = new Label("Level");
        levelLbl.setTextFill(Paint.valueOf("white"));
        levelLbl.setFont(new Font("ariel", 20));
        levelLbl.setLayoutX(35);
        levelLbl.setLayoutY(60);
        personalInfo.getChildren().add(levelLbl);
        int count = 1;
        for (String mode : ((BuzzwordGameData) app.getGameDataComponent()).getModeList()) {
            Label modeLbl = new Label(mode + ": " + userData.getProgress(mode) + " out of 8");
            modeLbl.setTextFill(Paint.valueOf("white"));
            modeLbl.setFont(new Font("ariel", 20));
            modeLbl.setLayoutX(30);
            modeLbl.setLayoutY(60 + 25 * count);
            count++;
            personalInfo.getChildren().add(modeLbl);
        }

        try {
            Button close = gui.initializeChildButton(QUIT_ICON.toString(), false);
            close.getStyleClass().add("quit-button");
            close.setLayoutX(190);
            close.setLayoutY(0);
            personalInfo.getChildren().add(close);
            close.setOnAction(e -> {
                circles.getChildren().remove(personalInfo);
                personalBtn.setDisable(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        renderGameScreen();

        // toolbar config
        gui.setLoginoutbtnIcon(true);
        gui.setHomebtnDisable(true);
        gui.setLoginoutbtnDisable(false);
    }

    public void initGamePlay() {

        gamePlay = true;

        PropertyManager pm = PropertyManager.getPropertyManager();

        ObservableList<Node> workspaceChildren = workspace.getChildren();
        Pane gameWorkspace = (Pane) workspaceChildren.get(0);
        VBox centerVBox = (VBox) gameWorkspace.getChildren().get(0);

        Label modeLabel = (Label) centerVBox.getChildren().get(1);
        String mode = "mode: " + ((BuzzwordGameData) app.getGameDataComponent()).getCurrentMode();
        modeLabel.setText(mode);

        circles = (Pane) centerVBox.getChildren().get(2);
        for (int i = 1; i <= 16; i++) {
            Button b = ((Button) circles.lookup("#" + i));
            b.setText("" + ((char) ThreadLocalRandom.current().nextInt(65, 90 + 1)));
        }


        BuzzwordGameData gameData = (BuzzwordGameData) app.getGameDataComponent();

//        // TODO this is specific for countries.
//        int max= 0;
//        if (gameData.getCurrentMode().equals("countries")){
//            max = BuzzwordGameData.MAXNUMBEROFCOUNTRIES;
//        }else if (gameData.getCurrentMode().equals("elements")){
//            max = BuzzwordGameData.MAXNUMBEROFELEMENTS;
//        }else if (gameData.getCurrentMode().equals("places")){
//            max = BuzzwordGameData.MAXNUMBEROFPLACES;
//        }
//
//
//        int letterIndex1 = ThreadLocalRandom.current().nextInt(0,  max + 1);
//
//        int letterIndex2 = letterIndex1;
//        while (letterIndex2 == letterIndex1) {
//            letterIndex2 = ThreadLocalRandom.current().nextInt(0, max + 1);
//        }
//
//
//        String letter1 = gameData.getWord(letterIndex1);
////        System.out.println("letter1 " + letter1);
//
//        String letter2 = gameData.getWord(letterIndex2);
////        System.out.println("2 " + letter2);
//
//
//        Character[][] matrix = new Character[4][4];
//        matrix = setFirstWord(letter1, matrix);
//
//        while (letter1.length() + letter2.length() > 16) {
//            letter2 = gameData.getWord(ThreadLocalRandom.current().nextInt(0, max + 1));
//        }
//
//        if (gameData.getCurrentLevel() > 2) {
//            matrix = setSecondWord(letter2, matrix);
//        }
//        for (int y = 0; y < 4; y++) {
//            for (int x = 0; x < 4; x++) {
//                if (matrix[y][x] == null) {
//                    matrix[y][x] = (char) ThreadLocalRandom.current().nextInt(65, 90 + 1);
//                }
//            }
//        }
//
//        if ((ThreadLocalRandom.current().nextInt(0, 1 + 1)) % 2 == 0) {
//            for (int y = 0; y < 4; y++) {
//                for (int x = 0; x < 2; x++) {
//                    char tmp = matrix[y][x];
//                    matrix[y][x] = matrix[y][3 - x];
//                    matrix[y][3 - x] = tmp;
//                }
//            }
//        }
//
//        switch (ThreadLocalRandom.current().nextInt(0, 3 + 1)) {
//            case 0:
//                for (int i = 0; i < 16; i++) {
//                    Button b = ((Button) circles.lookup("#" + (i + 1)));
//                    b.setText("" + matrix[i / 4][i % 4]);
//                }
//
//                break;
//            case 1:
//                for (int i = 0; i < 16; i++) {
//                    Button b = ((Button) circles.lookup("#" + (i + 1)));
//                    b.setText("" + matrix[3 - i % 4][i / 4]);
//                }
//
//                break;
//            case 2:
//                for (int i = 0; i < 16; i++) {
//                    Button b = ((Button) circles.lookup("#" + (i + 1)));
//                    b.setText(("" + (matrix[3 - i / 4][3 - i % 4])));
//                }
//
//                break;
//            case 3:
//                for (int i = 0; i < 16; i++) {
//                    Button b = ((Button) circles.lookup("#" + (i + 1)));
//                    b.setText("" + matrix[i % 4][3 - i / 4]);
//                }
//                break;
//
//        }


        Label levelLabel = (Label) centerVBox.getChildren().get(3);
        String level = "Level: " + ((BuzzwordGameData) app.getGameDataComponent()).getCurrentLevel();
        levelLabel.setText(level);

        Button playResumeBtn = null;
        Button nextGameBtn = null;
        Button prevGameBtn = null;
        Button quitBtn = null;

        try {
            playResumeBtn = gui.initializeChildButton(RESUMEGAME_ICON.toString(), false);
            nextGameBtn = gui.initializeChildButton(NEXTGAME_ICON.toString(), false);
            prevGameBtn = gui.initializeChildButton(PREVGAME_ICON.toString(), false);
            quitBtn = gui.initializeChildButton(QUIT_ICON.toString(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        playResumeBtn.getStyleClass().add(pm.getPropertyValue(PLAY_RESUME_BUTTON));
        nextGameBtn.getStyleClass().add(pm.getPropertyValue(NEXT_GAME_BUTTON));
        prevGameBtn.getStyleClass().add(pm.getPropertyValue(PREV_GAME_BUTTON));
        quitBtn.getStyleClass().add(pm.getPropertyValue(QUIT_BUTTON));

        if (((BuzzwordGameData) app.getGameDataComponent()).getCurrentLevel() == 1) {
            prevGameBtn.setDisable(true);
        }
        if (((BuzzwordGameData) app.getGameDataComponent()).getCurrentLevel()
                == ((BuzzwordUserData) app.getUserDataComponent()).getProgress(
                ((BuzzwordGameData) app.getGameDataComponent()).getCurrentMode())) {
            nextGameBtn.setDisable(true);
        }

        BorderPane buttons = new BorderPane();
        buttons.setLeft(prevGameBtn);
        buttons.setCenter(playResumeBtn);
        buttons.setRight(nextGameBtn);

        centerVBox.getChildren().add(buttons);

        quitBtn.setLayoutX(545);
        gameWorkspace.getChildren().add(quitBtn);
        quitBtn.setOnAction(e -> {
            if (this.gamePlay) {
                try {
                    ((GameScreen) workspace).pose(posemenu, gui.initializeChildButton(PLAYGAME_ICON.toString(), false));
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                this.gamePlay = false;
            }

            for (int i = 1; i <= 16; i++) {
                ((Button) circles.lookup("#" + i)).setTextFill(Paint.valueOf("transparent"));
            }
            gui.getFileController().handleQuitRequest();

            setHandler();
        });

        Button guitBtnFromGUI = gui.getQuitbtn();
        guitBtnFromGUI.setVisible(false);

        posemenu = new Pane();
        posemenu.setPrefSize(380, 330);
        posemenu.setStyle("-fx-background-color: white;" +
                "-fx-opacity: 0.8;");
        posemenu.setLayoutX(10);
        posemenu.setLayoutY(15);

        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(100);
        boxBlur.setHeight(100);
        boxBlur.setIterations(3);
        posemenu.setEffect(boxBlur);


        Pane rightPane = (Pane) gameWorkspace.getChildren().get(1);
        Label l = (Label)rightPane.lookup("#target");
        if (gameData.getCurrentLevel() < 3){
            l.setText("20 points");
        }else{
            l.setText("40 points");
        }

        setHandler();
    }

    public void renderGamePlay() {

        try {
            if (gamePlay) {
                ((GameScreen) workspace).pose(posemenu, gui.initializeChildButton(PLAYGAME_ICON.toString(), false));
                for (int i = 1; i <= 16; i++) {
                    ((Button) circles.lookup("#" + i)).setTextFill(Paint.valueOf("transparent"));
                }
                this.gamePlay = false;
            } else {
                ((GameScreen) workspace).play(posemenu, gui.initializeChildButton(RESUMEGAME_ICON.toString(), false));
                for (int i = 1; i <= 16; i++) {
                    ((Button) circles.lookup("#" + i)).setTextFill(Paint.valueOf("white"));
                }
                this.gamePlay = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        setHandler();
    }

    private Character[][] setFirstWord(String s, Character[][] matrix) {
        for (int i = 0; i < s.length(); i++) {
            if (i < 4 || (8 <= i && i < 12)) {
                matrix[i / 4][i % 4] = s.charAt(i);
            } else {
                matrix[i / 4][3 - i % 4] = s.charAt(i);
            }
        }

        return matrix;
    }

    private Character[][] setSecondWord(String s, Character[][] matrix) {
        int y;
        int x;
        for (int i = 0; i < s.length(); i++) {
            if (i < 4 || (8 <= i && i < 12)) {
                matrix[3 - (i / 4)][i % 4] = s.charAt(i);
            } else {
                matrix[3 - (i / 4)][3 - i % 4] = s.charAt(i);
            }
        }

        return matrix;
    }
}
