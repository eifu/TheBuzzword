package ui;


import apptemeplate.AppTemplate;
import buzzword.GameScreenState;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import data.TrieWordData;
import propertymanager.PropertyManager;
import data.BuzzwordGameData;
import data.BuzzwordUserData;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


import static buzzword.GameScreenState.*;
import static buzzword.BuzzwordProperty.*;
import static settings.AppPropertyType.*;


public class BuzzwordWorkspace extends AppWorkspaceComponent {

    final static int DEFAULTSTARTTIME = 40;

    AppTemplate app;
    AppGUI gui;
    BuzzwordController controller;
    GameScreenState currentState;
    boolean signedIn; // [home]
    boolean gamePlay; // [gameplay]
    Pane posemenu; // [gameplay]
    Pane personalInfo; // [home/selecting/signingin]
    Button personalBtn; // [home/selecting/signingin] personal Button in toolbar
    Pane circles; // circle buttons that will be under personalInfo pane.
    Timeline timeline; // [gameplay]
    int currentPoints; // [gameplay]
    String currentEntry; // [gameplay]
    TextField textfield;
    IntegerProperty timeremaining = new SimpleIntegerProperty(DEFAULTSTARTTIME);


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

    public Timeline getTimeline() {
        return timeline;
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

                // if user has signed in.
                if (signedIn) {
                    ObservableList<Node> workspaceHomeChildren = workspace.getChildren();
                    ObservableList<Node> vboxHomeChildren = ((VBox) workspaceHomeChildren.get(0)).getChildren();
                    StackPane s = (StackPane) vboxHomeChildren.get(2);
                    Button gameStartBtn = (Button) s.getChildren().get(0);

                    // update circles for personalBtn
                    circles = (Pane) vboxHomeChildren.get(1);
                    // let comboBox visible
                    gui.getToolbarPane().getChildren().get(4).setVisible(true);

                    personalBtn = (Button) gui.getToolbarPane().getChildren().get(5);
                    personalBtn.setDisable(false);
                    personalBtn.setOnAction(e2 -> {
                        BuzzwordGameData buzzwordGameData = (BuzzwordGameData) app.getGameDataComponent();
                        BuzzwordUserData buzzwordUserData = (BuzzwordUserData) app.getUserDataComponent();

                        for (String mode : buzzwordGameData.getModeList()) {
                            Label l = (Label) personalInfo.lookup("#info" + mode);
                            l.setText(mode + ": " + buzzwordUserData.getProgress(mode) + " out of " + buzzwordGameData.getModeMaxLevel(mode));
                        }


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

                    String encryptedPass = encryption(pass);

                    if (gamedata.validateUsernamePassword(name, encryptedPass)) {

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

                        String encryptedPass = encryption(pass);


                        gameData.addNamePassMap(name, encryptedPass);
                        gameData.save();

                        setCurrentState(HOME);
                        reloadWorkspace(gui.getAppPane());


                        signedIn = true;
                        BuzzwordUserData userData = (BuzzwordUserData) app.getUserDataComponent();
                        userData.setUsername(name);
                        userData.setPassword(encryptedPass);
                        userData.signup(app);

                        renderHome(name);
                        setHandler();
                    }
                });
                break;

            case SELECTING:

                personalBtn.setDisable(false);


                workspaceChildren = workspace.getChildren();
                vboxChildren = ((VBox) workspaceChildren.get(0)).getChildren();

                ObservableList<Node> levelChildren = ((Pane) vboxChildren.get(2)).getChildren();

                Label gameModeLabel = (Label) vboxChildren.get(1);
                ComboBox comboBox = (ComboBox) gui.getToolbarPane().getChildren().get(4);
                String mode = (String) comboBox.getValue();
                gameModeLabel.setText("mode: " + mode);

                int totalLevel = ((BuzzwordGameData) app.getGameDataComponent()).getModeMaxLevel(mode);

                // update circles for personalBtn
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
                for (int level = totalLevel; level < 8; level++) {
                    Button b = (Button) levelChildren.get(level);
                    b.setVisible(false);
                }
                break;

            case GAMEPLAY:

                personalBtn.setDisable(true);

                currentPoints = 0;

                workspaceChildren = workspace.getChildren();
                Pane borderPaneChildren = (Pane) workspaceChildren.get(0);
                VBox centerVBoxChildren = (VBox) borderPaneChildren.getChildren().get(0);

                BorderPane buttons = (BorderPane) centerVBoxChildren.getChildren().get(4);

                Button playResumeButton = (Button) buttons.getCenter();
                playResumeButton.setOnAction(e -> {
                    renderGamePlay();
                });

                Button prevButton = (Button) buttons.getLeft();
                prevButton.setOnAction(e -> {
                    int currentLevel = ((BuzzwordGameData) app.getGameDataComponent()).getCurrentLevel();
                    ((BuzzwordGameData) app.getGameDataComponent()).setCurrentLevel(currentLevel - 1);

                    reloadWorkspace(gui.getAppPane());
                    initGamePlay();
                });

                Button nextButton = (Button) buttons.getRight();
                nextButton.setOnAction(e -> {
                    int currentLevel = ((BuzzwordGameData) app.getGameDataComponent()).getCurrentLevel();
                    ((BuzzwordGameData) app.getGameDataComponent()).setCurrentLevel(currentLevel + 1);

                    reloadWorkspace(gui.getAppPane());
                    initGamePlay();
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
            Label modeLbl = new Label();
            modeLbl.setId("info" + mode);
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
        String mode = ((BuzzwordGameData) app.getGameDataComponent()).getCurrentMode();
        modeLabel.setText("mode: " + mode);

        circles = (Pane) centerVBox.getChildren().get(2);

        currentEntry = "";

        DropShadow draggedShadow = new DropShadow();
        draggedShadow.setOffsetX(0);
        draggedShadow.setOffsetY(3);
        draggedShadow.setColor(Color.WHITE);

        char[] grid = new char[16];
        BuzzwordGameData gameData = (BuzzwordGameData) app.getGameDataComponent();
        TrieWordData trie = (TrieWordData) gameData.getModeTrieWordMap().get(mode);
        int num = 0;
        int targetNum = gameData.getCurrentLevel() + 1 > 4 ? 4 : gameData.getCurrentLevel();
        Set tempSet = null;
        while (num < targetNum) {
            for (int i = 0; i < 16; i++) {
                grid[i] = (char) ThreadLocalRandom.current().nextInt(65, 90 + 1);
            }
            tempSet = TrieWordData.countWords(grid, trie);
            num = tempSet.size();
        }
        System.out.println(tempSet);
        gameData.setTargetWordsSet(tempSet);

        ArrayList<Button> draggedButtons = new ArrayList<>();
        ArrayList<Line> draggedLines = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            Button b = ((Button) circles.lookup("#" + i));
            b.setText("" + grid[i]);

            b.setOnMousePressed(e -> {
                b.setEffect(draggedShadow);
            });

            b.setOnDragDetected(e -> {
                b.startFullDrag();
            });

            b.setOnMouseDragEntered(e -> {
                if (!draggedButtons.contains(b)) {

                    Line l = null;
                    if (draggedButtons.isEmpty()) {
                        currentEntry += b.getText();
                        b.setEffect(draggedShadow);
                        draggedButtons.add(b);
                    } else {
                        int lastId = Integer.parseInt(draggedButtons.get(draggedButtons.size() - 1).getId());
                        int thisId = Integer.parseInt(b.getId());
                        if (validateLastIDthisID(lastId, thisId)) {
                            switch (thisId - lastId) {
                                case -1:
                                    l = (Line) circles.lookup("#hline" + b.getId());
                                    break;
                                case 1:
                                    l = (Line) circles.lookup("#hline" + draggedButtons.get(draggedButtons.size() - 1).getId());
                                    break;
                                case -4:
                                    l = (Line) circles.lookup("#vline" + b.getId());
                                    break;
                                case 4:
                                    l = (Line) circles.lookup("#vline" + draggedButtons.get(draggedButtons.size() - 1).getId());
                                    break;
                                default:
                                    l = null;
                            }
                            if (l != null) {
                                l.setStroke(Paint.valueOf("white"));
                                draggedLines.add(l);
                            }

                            currentEntry += b.getText();
                            b.setEffect(draggedShadow);
                            draggedButtons.add(b);
                        }
                    }
                }
            });

            b.setOnMouseDragReleased(ee -> {
                System.out.println(currentEntry);
                if (trie.findWord(currentEntry) && !gameData.hasFound(currentEntry)) {
                    currentPoints += currentEntry.length() * 4;

                    ((GameScreen) workspace).addWord(currentEntry);
                    gameData.found(currentEntry);
                }

                currentEntry = "";
                removeButtonShadow(draggedButtons);
                removeLineShadow(draggedLines);
                draggedButtons.clear();
                draggedLines.clear();
            });
        }


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

            for (int i = 0; i < 16; i++) {
                ((Button) circles.lookup("#" + i)).setTextFill(Paint.valueOf("transparent"));
            }
            gui.getFileController().handleQuitRequest();

            setHandler();
        });

        Button guitBtnFromGUI = gui.getQuitbtn();
        guitBtnFromGUI.setVisible(false);

        posemenu = new Pane();
        posemenu.setPrefSize(340, 330);
        posemenu.setStyle("-fx-background-color: white;" +
                "-fx-opacity: 0.8;");
        posemenu.setLayoutX(30);
        posemenu.setLayoutY(15);

        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(20);
        boxBlur.setHeight(20);
        boxBlur.setIterations(3);
        posemenu.setEffect(boxBlur);


        Pane rightPane = (Pane) gameWorkspace.getChildren().get(1);

        HBox timerLblHBox = (HBox) rightPane.lookup("#timer");
        Label timerLbl1 = new Label("TIME REMAINING: ");
        Label timerLbl2 = new Label();
        Label timerLbl3 = new Label("sec");

        timerLbl1.setTextFill(Paint.valueOf("red"));
        timerLbl2.setTextFill(Paint.valueOf("red"));
        timerLbl3.setTextFill(Paint.valueOf("red"));


        final Integer STARTTIME = 40; // TODO the time setting changes based on level


        timeline = new Timeline();
        timeremaining.set(STARTTIME);


        timerLbl2.textProperty().bind(timeremaining.asString());

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(STARTTIME + 1),
                new KeyValue(timeremaining, 0)
        ));

        Button localPlayResumeBtn = playResumeBtn;
        timeline.setOnFinished(e -> {
            Button b = null;
            boolean win = false;
            String winLoseHighscore = "";

            switch (gameData.getCurrentLevel()) {
                case 1:
                case 2:
                    if (currentPoints > 10) {
                        win = true;
                    }
                    break;
                case 3:
                    if (currentPoints > 20) {
                        win = true;
                    }
                    break;
                case 4:
                    if (currentPoints > 30) {
                        win = true;
                    }
                    break;
                case 5:
                    if (currentPoints > 40) {
                        win = true;
                    }
                    break;
                case 6:
                case 7:
                case 8:
                    if (currentPoints > 50) {
                        win = true;
                    }
                    break;
            }

            try {
                if (win) {
                    b = gui.initializeChildButton(NEXTGAME_ICON.toString(), false);
                    winLoseHighscore = "WIN!\n";
                } else {
                    b = gui.initializeChildButton(REPLAY_ICON.toString(), false);
                    winLoseHighscore = "LOSE!\n";
                }
            } catch (FileNotFoundException f) {
                f.printStackTrace();
                System.exit(1);
            }


            if (currentPoints > ((BuzzwordUserData) app.getUserDataComponent()).getHighScore(mode)[gameData.getCurrentLevel() - 1]) {
                winLoseHighscore += "new record! " + currentPoints;
                ((BuzzwordUserData) app.getUserDataComponent()).setHighscore(mode, gameData.getCurrentLevel() - 1, currentPoints);
            }

            String notFoundWords = "";
            for (String w : gameData.getTargetWordsSet()) {
                if (!gameData.hasFound(w)) {
                    notFoundWords += w + ", ";
                }
            }

            ((GameScreen) workspace).winlose(posemenu, winLoseHighscore, notFoundWords, localPlayResumeBtn, b);

            if (win) {
                b.setOnAction(e2 -> {
                    int progress = ((BuzzwordUserData) app.getUserDataComponent()).getProgress(mode);

                    // if the current level is the user's highest progress, then increment the progress by 1.
                    if (gameData.getCurrentLevel() == progress) {
                        ((BuzzwordUserData) app.getUserDataComponent()).setProgress(mode, progress + 1);
                    }
                    ((BuzzwordGameData) app.getGameDataComponent()).setCurrentLevel(gameData.getCurrentLevel() + 1);
                    reloadWorkspace(gui.getAppPane());
                    initGamePlay();
                });
            } else {
                b.setOnAction(e2 -> {
                    reloadWorkspace(gui.getAppPane());
                    initGamePlay();
                });
            }
        });
        timeline.play();

        timerLblHBox.getChildren().

                addAll(timerLbl1, timerLbl2, timerLbl3);

        textfield = (TextField) rightPane.lookup("#textinput");
        textfield.textProperty().addListener(((observable, oldValue, newValue) -> {
            for (int i = 0; i < 16; i++) {
                Button b = (Button) circles.lookup("#" + i);
                if (b.getText().equals(newValue)) {
                    // TODO make solver and come back again.
                }
            }
        }));

        Label l = (Label) rightPane.lookup("#target");

        switch (gameData.getCurrentLevel()) {
            case 1:
            case 2:
                l.setText("10 points");
                break;
            case 3:
                l.setText("20 points");
                break;
            case 4:
                l.setText("30 points");
                break;
            case 5:
                l.setText("40 points");
                break;
            case 6:
            case 7:
            case 8:
                l.setText("50 points");
                break;

        }

        setHandler();

    }

    public void renderGamePlay() {

        try {
            if (gamePlay) {
                timeline.pause();

                ((GameScreen) workspace).pose(posemenu, gui.initializeChildButton(PLAYGAME_ICON.toString(), false));
                for (int i = 0; i < 16; i++) {
                    ((Button) circles.lookup("#" + i)).setTextFill(Paint.valueOf("transparent"));
                }
                this.gamePlay = false;
            } else {
                timeline.play();


                ((GameScreen) workspace).play(posemenu, gui.initializeChildButton(RESUMEGAME_ICON.toString(), false));
                for (int i = 0; i < 16; i++) {
                    ((Button) circles.lookup("#" + i)).setTextFill(Paint.valueOf("white"));
                }
                this.gamePlay = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setHandler();
    }

    private void removeButtonShadow(ArrayList<Button> a) {
        for (Button c : a) {
            c.setEffect(null);
        }
    }

    private void removeLineShadow(ArrayList<Line> a) {
        for (Line l : a) {
            l.setStroke(Paint.valueOf("black"));
        }
    }

    private boolean validateLastIDthisID(int l, int t) {
        if (l % 4 == 0 && t % 4 == 3) {
            return false;
        } else if (l % 4 == 3 && t % 4 == 0) {
            return false;
        } else {
            switch (t - l) {
                case -5:
                case -4:
                case -3:
                case -1:
                case 1:
                case 3:
                case 4:
                case 5:
                    return true;
                default:
                    return false;
            }
        }
    }

    private String encryption(String pass) {
        String encryptedPass = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());

            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            encryptedPass = sb.toString();


        } catch (NoSuchAlgorithmException nsa) {
            nsa.printStackTrace();
            System.exit(1);
        }
        return encryptedPass;
    }

}

