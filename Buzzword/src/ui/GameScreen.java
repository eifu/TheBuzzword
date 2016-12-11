package ui;


import buzzword.GameScreenState;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.collections.FXCollections.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import propertymanager.PropertyManager;


import static settings.AppPropertyType.*;

public class GameScreen extends Pane {


    public GameScreen(GameScreenState state) {
        PropertyManager pm = PropertyManager.getPropertyManager();
        switch (state) {
            case HOME:
                VBox container = new VBox();
                Label appTitle = new Label(pm.getPropertyValue(APP_TITLE));
                appTitle.setAlignment(Pos.CENTER);
                appTitle.setFont(new Font("Roboto", 50));
                appTitle.setTextFill(Paint.valueOf("white"));
                appTitle.setPrefSize(400, 100);
                Pane circles = new Pane();
                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {

                        Button btn = new Button();
                        btn.setLayoutX(x * 80 + 50);
                        btn.setLayoutY(y * 80 + 30);
                        btn.setMnemonicParsing(false);
                        btn.setDisable(true);
                        btn.getStyleClass().add("circle-button");

                        switch (x + y * 4) {
                            case 0:
                                btn.setText("B");
                                break;
                            case 1:
                                btn.setText("U");
                                break;
                            case 4:
                                btn.setText("Z");
                                break;
                            case 5:
                                btn.setText("Z");
                                break;
                            case 10:
                                btn.setText("W");
                                break;
                            case 11:
                                btn.setText("O");
                                break;
                            case 14:
                                btn.setText("R");
                                break;
                            case 15:
                                btn.setText("D");
                                break;

                        }
                        circles.getChildren().add(btn);
                    }
                }
                StackPane gameStartButtonStack = new StackPane();
                gameStartButtonStack.setPrefSize(400, 100);

                Button gameStartButton = new Button("Game Start");
                gameStartButton.setTextFill(Paint.valueOf("white"));
                gameStartButton.setFont(new Font("ariel", 25));
                gameStartButton.setMinWidth(Region.USE_PREF_SIZE);
                gameStartButton.setAlignment(Pos.CENTER);
                gameStartButton.getStyleClass().add("gamestart-button");

                gameStartButtonStack.getChildren().addAll(gameStartButton);

                gameStartButtonStack.setVisible(false);
                container.getChildren().addAll(appTitle, circles, gameStartButtonStack);
                this.getChildren().add(container);
                break;

            case SIGNINGIN:
                VBox signingIncontainer = new VBox();

                Label titleLabel = new Label("Sign In");
                titleLabel.setAlignment(Pos.CENTER);
                titleLabel.setFont(new Font("Roboto", 50));
                titleLabel.setTextFill(Paint.valueOf("white"));
                titleLabel.setPrefSize(400, 100);

                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));
                Text titleSignIn = new Text("Sign in or sign up for account.");
                titleSignIn.setFill(Paint.valueOf("white"));
                grid.add(titleSignIn, 0, 0, 2, 1);

                Label usernameLabel = new Label("User Name:");
                usernameLabel.setTextFill(Paint.valueOf("white"));
                grid.add(usernameLabel, 0, 1);
                TextField usernameTxt = new TextField();
                grid.add(usernameTxt, 1, 1);

                Label passwordLabel = new Label("Password:");
                passwordLabel.setTextFill(Paint.valueOf("white"));
                grid.add(passwordLabel, 0, 2);
                PasswordField passwordTxt = new PasswordField();
                grid.add(passwordTxt, 1, 2);

                Button signInBtn = new Button("Sign In");
                signInBtn.setTextFill(Paint.valueOf("white"));

                Button singUpBtn = new Button("Sign Up");
                singUpBtn.setTextFill(Paint.valueOf("white"));

                HBox hbBtn = new HBox(10);
                hbBtn.getChildren().add(singUpBtn);
                hbBtn.getChildren().add(signInBtn);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                grid.add(hbBtn, 1, 4);

                signingIncontainer.getChildren().addAll(titleLabel, grid);
                this.getChildren().add(signingIncontainer);
                break;
            case SIGNUP:
                signingIncontainer = new VBox();

                titleLabel = new Label("Sign Up");
                titleLabel.setAlignment(Pos.CENTER);
                titleLabel.setFont(new Font("Roboto", 50));
                titleLabel.setTextFill(Paint.valueOf("white"));
                titleLabel.setPrefSize(400, 100);

                grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25, 25, 25, 25));
                titleSignIn = new Text("Welcome to Buzzword.");
                titleSignIn.setFill(Paint.valueOf("white"));
                grid.add(titleSignIn, 0, 0, 2, 1);

                usernameLabel = new Label("User Name:");
                usernameLabel.setTextFill(Paint.valueOf("white"));
                grid.add(usernameLabel, 0, 1);
                usernameTxt = new TextField();
                grid.add(usernameTxt, 1, 1);

                passwordLabel = new Label("Password:");
                passwordLabel.setTextFill(Paint.valueOf("white"));
                grid.add(passwordLabel, 0, 2);
                passwordTxt = new PasswordField();
                grid.add(passwordTxt, 1, 2);

                singUpBtn = new Button("Sign Up");
                singUpBtn.setTextFill(Paint.valueOf("white"));

                hbBtn = new HBox(10);
                hbBtn.getChildren().add(singUpBtn);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                grid.add(hbBtn, 1, 4);

                signingIncontainer.getChildren().addAll(titleLabel, grid);
                this.getChildren().add(signingIncontainer);
                break;

            case SELECTING:
                container = new VBox();
                Label buzzwordTitle = new Label("Buzzword");
                buzzwordTitle.setPrefSize(400, 60);
                buzzwordTitle.setAlignment(Pos.CENTER);
                buzzwordTitle.setFont(new Font("Roboto", 30));
                buzzwordTitle.setTextFill(Paint.valueOf("white"));

                Label modeLabel = new Label("Label");
                modeLabel.setPrefSize(400, 40);
                modeLabel.setAlignment(Pos.CENTER);
                modeLabel.setFont(new Font("Roboto", 16));
                modeLabel.setTextFill(Paint.valueOf("white"));

                circles = new Pane();

                for (int y = 0; y < 2; y++) {
                    for (int x = 0; x < 4; x++) {

                        Button btn = new Button();

                        btn.setLayoutX(x * 80 + 50);
                        btn.setLayoutY(y * 80 + 30);
                        btn.setDisable(true);
                        btn.setId("" + (1 + x + y * 4));
                        btn.getStyleClass().add("circle-button");
                        btn.setText("" + (1 + x + y * 4));


                        circles.getChildren().add(btn);
                    }
                }
                container.getChildren().addAll(buzzwordTitle, modeLabel, circles);
                this.getChildren().add(container);
                break;


            case GAMEPLAY:
                Pane gameWorkspace = new Pane();

                VBox centerBox = new VBox();
                centerBox.setLayoutX(0);
                centerBox.setLayoutY(0);
                buzzwordTitle = new Label("Buzzword");
                buzzwordTitle.setPrefSize(400, 60);
                buzzwordTitle.setAlignment(Pos.CENTER);
                buzzwordTitle.setFont(new Font("Roboto", 30));
                buzzwordTitle.setTextFill(Paint.valueOf("white"));

                modeLabel = new Label("Label");
                modeLabel.setPrefSize(400, 40);
                modeLabel.setAlignment(Pos.CENTER);
                modeLabel.setFont(new Font("Roboto", 16));
                modeLabel.setTextFill(Paint.valueOf("white"));

                circles = new Pane();
                circles.setPrefSize(400, 345);
                for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                        if (x < 3) {
                            Line hLine = new Line();
                            hLine.setLayoutX(110 + 80 * x);
                            hLine.setLayoutY(60 + 80 * y);
                            hLine.setEndX(20);
                            hLine.setId("hline" + (x + y * 4));
                            circles.getChildren().add(hLine);
                        }
                        if (y < 3) {
                            Line vLine = new Line();
                            vLine.setLayoutX(70 + 80 * x);
                            vLine.setLayoutY(100 + 80 * y);
                            vLine.setRotate(90);
                            vLine.setEndX(20);
                            vLine.setId("vline" + (x + y * 4));
                            circles.getChildren().add(vLine);
                        }

                        Button btn = new Button();
                        btn.setId("" + (x + y * 4));
                        btn.getStyleClass().add("circle-button");
                        btn.setLayoutX(x * 80 + 50);
                        btn.setLayoutY(y * 80 + 30);

                        circles.getChildren().addAll(btn);

                    }
                }
                Label levelLabel = new Label();
                levelLabel.setText("test");
                levelLabel.setFont(new Font("Roboto", 16));
                levelLabel.setTextFill(Paint.valueOf("white"));
                levelLabel.setPrefSize(400, 40);
                levelLabel.setAlignment(Pos.CENTER);


                centerBox.getChildren().addAll(buzzwordTitle, modeLabel, circles, levelLabel);
                gameWorkspace.getChildren().add(centerBox);

                Pane rightWorkspace = new Pane();
                rightWorkspace.setLayoutX(400);
                rightWorkspace.setLayoutY(20);
                Rectangle timeOuterRect = new Rectangle();
                timeOuterRect.setHeight(25);
                timeOuterRect.setWidth(180);
                timeOuterRect.setArcHeight(5.0);
                timeOuterRect.setArcWidth(5.0);
                timeOuterRect.setLayoutX(0);
                timeOuterRect.setLayoutY(80);
                timeOuterRect.setStyle("-fx-fill: rgb(127, 148, 176)");
                HBox timerLabelHBox = new HBox();
                timerLabelHBox.setId("timer");
                timerLabelHBox.setLayoutY(83);

                VBox textInputVBox = new VBox();
                Label textInputVBoxLabel = new Label("Text input");
                textInputVBoxLabel.setTextFill(Paint.valueOf("white"));
                TextField textInput = new TextField();
                textInput.setId("textinput");
                textInput.setMaxWidth(180);
                textInputVBox.getChildren().addAll(textInputVBoxLabel, textInput);
                textInputVBox.setLayoutX(0);
                textInputVBox.setLayoutY(140);

                VBox scoreVBox = new VBox();

                TableView<Word> scoreTable = new TableView<>();
                TableColumn columnWord = new TableColumn("Word");
                columnWord.setCellValueFactory(
                        new PropertyValueFactory<>("word")
                );
                columnWord.setMinWidth(89);
                TableColumn columnPoint = new TableColumn("Point");
                columnPoint.setCellValueFactory(
                        new PropertyValueFactory<>("point")
                );
                columnPoint.setMinWidth(89);
                scoreTable.getColumns().addAll(columnWord, columnPoint);
                final ObservableList<Word> data = FXCollections.observableArrayList();
                scoreTable.setItems(data);
                scoreTable.setPlaceholder(new Label("Buzzword"));
                scoreTable.setPrefSize(180, 160);


                HBox totalHBox = new HBox();
                totalHBox.setStyle("-fx-background-color: darkgrey;");
                totalHBox.setPrefSize(180, 20);
                Label totalLbl = new Label("TOTAL");
                totalLbl.setMinWidth(90);
                totalLbl.setAlignment(Pos.CENTER_LEFT);
                totalHBox.getChildren().add(new Label("TOTAL"));
                Label totalScoreLbl = new Label("0");
                totalScoreLbl.setMinWidth(90);
                totalScoreLbl.setAlignment(Pos.CENTER_RIGHT);
                totalHBox.getChildren().add(totalScoreLbl);
                scoreVBox.getChildren().addAll(scoreTable, totalHBox);
                scoreVBox.setLayoutX(0);
                scoreVBox.setLayoutY(200);
                scoreVBox.setId("scoreVBox");

                Rectangle targetOuterRect = new Rectangle();
                targetOuterRect.setArcWidth(5.0);
                targetOuterRect.setArcHeight(5.0);
                targetOuterRect.setHeight(50);
                targetOuterRect.setWidth(180);
                targetOuterRect.setLayoutY(420);
                targetOuterRect.setStyle("-fx-fill: rgb(127, 148, 176)");
                VBox targetVBox = new VBox();
                Label target = new Label("Target:");
                target.setAlignment(Pos.TOP_LEFT);
                target.setTextFill(Paint.valueOf("white"));
                Label points = new Label("75 points");
                points.setId("target");
                points.setTextFill(Paint.valueOf("white"));
                points.setAlignment(Pos.CENTER);
                targetVBox.getChildren().addAll(target, points);
                targetVBox.setLayoutY(420);

                rightWorkspace.getChildren().addAll(timeOuterRect, timerLabelHBox, textInputVBox, scoreVBox, targetOuterRect, targetVBox);
//                gameWorkspace.setRight(rightWorkspace);
                rightWorkspace.setId("rightworkspace");
                gameWorkspace.getChildren().add(rightWorkspace);

                this.getChildren().add(gameWorkspace);
                break;
        }
    }


    public Pane change(GameScreenState state) {
        return new GameScreen(state);
    }

    public void home() {
        ObservableList<Node> workspaceHomeChildren = this.getChildren();
        ObservableList<Node> vboxHomeChildren = ((VBox) workspaceHomeChildren.get(0)).getChildren();
        StackPane s = (StackPane) vboxHomeChildren.get(2);
        s.setVisible(true);
    }

    public void signingIn() {

    }

    public void signUp() {

    }

    public void selecting() {

    }

    public void gamePlay() {

    }

    public void pose(Pane posemenu, Button playResumeButton) {
        Pane gameWorkspace = (Pane) this.getChildren().get(0);
        VBox centerVBox = (VBox) gameWorkspace.getChildren().get(0);
        Pane circles = (Pane) centerVBox.getChildren().get(2);

        circles.getChildren().add(posemenu);

        if (playResumeButton != null) {
            playResumeButton.getStyleClass().add("play-resume-game-button");
            BorderPane buttons = (BorderPane) centerVBox.getChildren().get(4);
            buttons.setCenter(playResumeButton);
        }
    }

    public void play(Pane posemenu, Button playResumeButton) {
        Pane gameWorkspace = (Pane) this.getChildren().get(0);
        VBox centerVBox = (VBox) gameWorkspace.getChildren().get(0);
        Pane circles = (Pane) centerVBox.getChildren().get(2);

        circles.getChildren().remove(posemenu);
        if (playResumeButton != null) {
            playResumeButton.getStyleClass().add("play-resume-game-button");
            BorderPane buttons = (BorderPane) centerVBox.getChildren().get(4);
            buttons.setCenter(playResumeButton);
        }
    }

    public void removePosemenu(Pane posemenu) {
        Pane gameWorkspace = (Pane) this.getChildren().get(0);
        VBox centerVBox = (VBox) gameWorkspace.getChildren().get(0);
        Pane circles = (Pane) centerVBox.getChildren().get(2);

        circles.getChildren().remove(posemenu);
    }


    public void winlose(Pane finishmenu, String winlose, String notFoundWords, Button playResumeBtn, Button btn) {
        Pane gameWorkspace = (Pane) this.getChildren().get(0);
        VBox centerVBox = (VBox) gameWorkspace.getChildren().get(0);
        Pane circles = (Pane) centerVBox.getChildren().get(2);

        HBox winloseLblHBox = new HBox();
        winloseLblHBox.setPrefSize(340, 100);
        winloseLblHBox.setLayoutX(30);
        winloseLblHBox.setLayoutY(50);
        winloseLblHBox.setAlignment(Pos.CENTER);

        Label winloseLbl = new Label(winlose);
        winloseLbl.setFont(new Font("Roboto", 40));
        winloseLblHBox.getChildren().add(winloseLbl);

        HBox btnHBox = new HBox();
        btnHBox.setPrefSize(340, 100);
        btnHBox.setLayoutX(30);
        btnHBox.setLayoutY(150);
        btnHBox.setAlignment(Pos.CENTER);

        btnHBox.getChildren().add(btn);

        circles.getChildren().addAll(finishmenu, winloseLblHBox, btnHBox);

        if (!notFoundWords.equals("")){
            StackPane notFoundStack = new StackPane();
            notFoundStack.setPrefSize(340, 50);
            Label notFoundWordsLbl = new Label("words not found\n"+notFoundWords);
            notFoundWordsLbl.setFont(new Font("Roboto", 16));
            notFoundWordsLbl.setAlignment(Pos.CENTER);
            notFoundStack.setLayoutX(30);
            notFoundStack.setLayoutY(280);
            notFoundStack.getChildren().add(notFoundWordsLbl);
            circles.getChildren().add(notFoundStack);
        }

        playResumeBtn.setDisable(true);
    }

    public void addWord(String w){

        Pane gameworkspace = (Pane) this.getChildren().get(0);
        Pane rightPane = (Pane)gameworkspace.lookup("#rightworkspace");
        TableView scoreTable = (TableView)((VBox)rightPane.lookup("#scoreVBox")).getChildren().get(0);
        ObservableList<Word> items = scoreTable.getItems();
        items.add(new Word(w, w.length()*4));
//        scoreTable.setItems(items);
        int totalscore = 0;
        for (Word word : items){
            totalscore += word.getPoint();
        }

        HBox scoreHBox = (HBox) ((VBox)rightPane.lookup("#scoreVBox")).getChildren().get(1);
        Label totalScoreLbl = (Label)scoreHBox.getChildren().get(1);
        totalScoreLbl.setText(totalscore+"");


    }

    public class Word{
        private SimpleStringProperty word;
        private SimpleIntegerProperty point;
        Word(String w, int p){
            word = new SimpleStringProperty(w);
            point = new SimpleIntegerProperty(p);
        }

        public String getWord(){return word.get();}
        public int getPoint(){return point.get();}

        public void setWord(String s){
            word.set(s);
        }
        public void setPoint(int i){
            point.set(i);
        }
    }
}
