package ui;


import buzzword.GameScreenState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import propertymanager.PropertyManager;



import static settings.AppPropertyType.*;

public class GameScreen extends Pane {


    public GameScreen(GameScreenState state){
        PropertyManager pm = PropertyManager.getPropertyManager();
        switch (state){
            case HOME:
                VBox container = new VBox();
                Label appTitle = new Label(pm.getPropertyValue(APP_TITLE));
                appTitle.setAlignment(Pos.CENTER);
                appTitle.setFont(new Font("Roboto", 50));
                appTitle.setTextFill(Paint.valueOf("white"));
                appTitle.setPrefSize(450, 100);
                Pane circles = new Pane();
                for (int y = 0; y < 4; y ++){
                    for (int x = 0; x < 4; x ++){
                        StackPane stackPane = new StackPane();
                        stackPane.setLayoutX(x*80 +70);
                        stackPane.setLayoutY(y*80 +30);

                        Button btn = new Button();
                        btn.setShape(new Circle(30));
                        btn.setMinSize(2*30, 2*30);
                        btn.setMaxSize(2*30, 2*30);
                        btn.setDisable(true);

                        stackPane.getChildren().add(btn);
                        Label l;
                        switch (x + y*4){
                            case 0:
                                l = new Label("B");
                                l.setFont(new Font("Roboto", 24));
                                l.setTextFill(Paint.valueOf("white"));
                                stackPane.getChildren().add(l);
                                break;
                            case 1:
                                l = new Label("U");
                                l.setFont(new Font("Roboto", 24));
                                l.setTextFill(Paint.valueOf("white"));
                                stackPane.getChildren().add(l);
                                break;
                            case 4:
                                l = new Label("Z");
                                l.setFont(new Font("Roboto", 24));
                                l.setTextFill(Paint.valueOf("white"));
                                stackPane.getChildren().add(l);
                                break;
                            case 5:
                                l = new Label("Z");
                                l.setFont(new Font("Roboto", 24));
                                l.setTextFill(Paint.valueOf("white"));
                                stackPane.getChildren().add(l);
                                break;
                            case 10:
                                l = new Label("W");
                                l.setFont(new Font("Roboto", 24));
                                l.setTextFill(Paint.valueOf("white"));
                                stackPane.getChildren().add(l);
                                break;
                            case 11:
                                l = new Label("O");
                                l.setFont(new Font("Roboto", 24));
                                l.setTextFill(Paint.valueOf("white"));
                                stackPane.getChildren().add(l);
                                break;
                            case 14:
                                l = new Label("R");
                                l.setFont(new Font("Roboto", 24));
                                l.setTextFill(Paint.valueOf("white"));
                                stackPane.getChildren().add(l);
                                break;
                            case 15:
                                l = new Label("D");
                                l.setFont(new Font("Roboto", 24));
                                l.setTextFill(Paint.valueOf("white"));
                                stackPane.getChildren().add(l);
                                break;

                        }

                        circles.getChildren().add(stackPane);
                    }
                }
                StackPane s = new StackPane();
                Button gameStartButton = new Button();
                gameStartButton.setPrefSize(200, 30);
                Label gameStartLabel = new Label("Game Start");
                gameStartLabel.setFont(new Font("Roboto", 24));
                gameStartLabel.setTextFill(Paint.valueOf("white"));

                s.setAlignment(Pos.BOTTOM_CENTER);
                s.getChildren().addAll(gameStartButton, gameStartLabel);
                s.setPrefSize(450, 60);
                s.setVisible(false);
                container.getChildren().addAll(appTitle, circles, s);
                this.getChildren().add(container);
                break;
            case SIGNINGIN:
                VBox signingIncontainer = new VBox();

                Label titleLabel = new Label("Sign In");
                titleLabel.setAlignment(Pos.CENTER);
                titleLabel.setFont(new Font("Roboto", 50));
                titleLabel.setTextFill(Paint.valueOf("white"));
                titleLabel.setPrefSize(450, 100);

                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25,25,25,25));
                Text titleSignIn = new Text("Sign in or sign up for account.");
                titleSignIn.setFill(Paint.valueOf("white"));
                grid.add(titleSignIn, 0, 0, 2, 1);

                Label usernameLabel = new Label("User Name:");
                usernameLabel.setTextFill(Paint.valueOf("white"));
                grid.add(usernameLabel, 0,1);
                TextField usernameTxt = new TextField();
                grid.add(usernameTxt, 1,1);

                Label passwordLabel = new Label("Password:");
                passwordLabel.setTextFill(Paint.valueOf("white"));
                grid.add(passwordLabel, 0,2);
                PasswordField passwordTxt = new PasswordField();
                grid.add(passwordTxt, 1,2);

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
                titleLabel.setPrefSize(450, 100);

                grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25,25,25,25));
                titleSignIn = new Text("Welcome to Buzzword.");
                titleSignIn.setFill(Paint.valueOf("white"));
                grid.add(titleSignIn, 0, 0, 2, 1);

                usernameLabel = new Label("User Name:");
                usernameLabel.setTextFill(Paint.valueOf("white"));
                grid.add(usernameLabel, 0,1);
                usernameTxt = new TextField();
                grid.add(usernameTxt, 1,1);

                passwordLabel = new Label("Password:");
                passwordLabel.setTextFill(Paint.valueOf("white"));
                grid.add(passwordLabel, 0,2);
                passwordTxt = new PasswordField();
                grid.add(passwordTxt, 1,2);

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
                buzzwordTitle.setPrefSize(450, 60);
                buzzwordTitle.setAlignment(Pos.CENTER);
                buzzwordTitle.setFont(new Font("Roboto", 30));
                buzzwordTitle.setTextFill(Paint.valueOf("white"));

                Label modeLabel = new Label("Label");
                modeLabel.setPrefSize(450, 40);
                modeLabel.setAlignment(Pos.CENTER);
                modeLabel.setFont(new Font("Roboto", 16));
                modeLabel.setTextFill(Paint.valueOf("white"));

                circles = new Pane();

                for (int y = 0; y < 2; y ++) {
                    for (int x = 0; x < 4; x++) {
                        StackPane stackPane = new StackPane();
                        stackPane.setLayoutX(x * 80 + 70);
                        stackPane.setLayoutY(y * 80 + 30);

                        Button btn = new Button();
                        btn.setShape(new Circle(30));
                        btn.setMinSize(2 * 30, 2 * 30);
                        btn.setMaxSize(2 * 30, 2 * 30);
                        btn.setDisable(true);
                        btn.setId(""+(1+x+y*4));

                        stackPane.getChildren().add(btn);

                        Label l = new Label(""+(1+x+y*4));
                        l.setFont(new Font("Roboto", 24));
                        l.setTextFill(Paint.valueOf("white"));
                        stackPane.getChildren().add(l);

                        circles.getChildren().add(stackPane);
                    }
                }
                container.getChildren().addAll(buzzwordTitle, modeLabel, circles);
                this.getChildren().add(container);
                break;
            case GAMEPLAY:
                BorderPane gameWorkspace = new BorderPane();

                VBox centerBox = new VBox();
                buzzwordTitle = new Label("Buzzword");
                buzzwordTitle.setPrefSize(450, 60);
                buzzwordTitle.setAlignment(Pos.CENTER);
                buzzwordTitle.setFont(new Font("Roboto", 30));
                buzzwordTitle.setTextFill(Paint.valueOf("white"));

                modeLabel = new Label("Label");
                modeLabel.setPrefSize(450, 40);
                modeLabel.setAlignment(Pos.CENTER);
                modeLabel.setFont(new Font("Roboto", 16));
                modeLabel.setTextFill(Paint.valueOf("white"));

                circles = new Pane();
                for (int y = 0; y < 4; y ++) {
                    for (int x = 0; x < 4; x++) {
                        if (x < 3) {
                            Line hLine = new Line();
                            hLine.setLayoutX(130 + 80 * x);
                            hLine.setLayoutY(60 + 80 * y);
                            hLine.setEndX(20);
                            circles.getChildren().add(hLine);
                        }
                        if (y < 3){
                            Line vLine = new Line();
                            vLine.setLayoutX(90 + 80*x);
                            vLine.setLayoutY(100 + 80*y);
                            vLine.setRotate(90);
                            vLine.setEndX(20);
                            circles.getChildren().add(vLine);
                        }
                        StackPane stackPane = new StackPane();
                        stackPane.setLayoutX(x * 80 + 70);
                        stackPane.setLayoutY(y * 80 + 30);

                        Button btn = new Button();
                        btn.setShape(new Circle(30));
                        btn.setMinSize(2 * 30, 2 * 30);
                        btn.setMaxSize(2 * 30, 2 * 30);
                        btn.setId(""+(1+x+y*4));

                        stackPane.getChildren().add(btn);

//                        Label l = new Label(""+(1+x+y*4));
//                        l.setFont(new Font("Roboto", 24));
//                        l.setTextFill(Paint.valueOf("white"));
//                        stackPane.getChildren().add(l);
                        circles.getChildren().addAll(stackPane);

                    }
                }
                Label levelLabel = new Label();
                levelLabel.setText("test");
                levelLabel.setFont(new Font("Roboto", 16));
                levelLabel.setTextFill(Paint.valueOf("white"));
                levelLabel.setPrefSize(450, 40);
                levelLabel.setAlignment(Pos.CENTER);



                centerBox.getChildren().addAll(buzzwordTitle, modeLabel,circles, levelLabel);
                gameWorkspace.setCenter(centerBox);
                this.getChildren().add(gameWorkspace);
                break;
        }
    }


    public Pane change(GameScreenState state){
        return new GameScreen(state);
    }
}
