package ui;


import buzzword.GameScreenState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
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
                Label t = new Label("Game Start");
                t.setFont(new Font("Roboto", 24));
                t.setTextFill(Paint.valueOf("white"));

                s.setAlignment(Pos.BOTTOM_CENTER);
                s.getChildren().addAll(gameStartButton, t);
                s.setPrefSize(450, 60);
                s.setVisible(false);
                container.getChildren().addAll(appTitle, circles, s);
                this.getChildren().add(container);
                break;
            case SIGNINGIN:
                VBox signingIncontainer = new VBox();

                Label screenLabel = new Label("Sign In");
                screenLabel.setAlignment(Pos.CENTER);
                screenLabel.setFont(new Font("Roboto", 50));
                screenLabel.setPrefSize(450, 100);

                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(25,25,25,25));
                grid.add(new Text("Welcome"), 0, 0, 2, 1);

                grid.add(new Label("User Name:"), 0,1);
                TextField usernameTxt = new TextField();
                grid.add(usernameTxt, 1,1);

                grid.add(new Label("Password:"), 0,2);
                TextField passwordTxt = new TextField();
                grid.add(passwordTxt, 1,2);

                Button signInBtn = new Button("Sign In");
                signInBtn.setTextFill(Paint.valueOf("white"));

                HBox hbBtn = new HBox(10);
                hbBtn.getChildren().add(signInBtn);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                grid.add(hbBtn, 1, 4);

                signingIncontainer.getChildren().addAll(screenLabel, grid);
                this.getChildren().add(signingIncontainer);
                break;

            case SELECTING:
                container = new VBox();
                Label buzzwordTitle = new Label("Buzzword");
                buzzwordTitle.setPrefSize(450, 60);
                buzzwordTitle.setAlignment(Pos.CENTER);
                buzzwordTitle.setFont(new Font("Roboto", 30));

                Label modeLabel = new Label("Label");
                modeLabel.setPrefSize(450, 40);
                modeLabel.setAlignment(Pos.CENTER);
                modeLabel.setFont(new Font("Roboto", 16));

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

                modeLabel = new Label("Label");
                modeLabel.setPrefSize(450, 40);
                modeLabel.setAlignment(Pos.CENTER);
                modeLabel.setFont(new Font("Roboto", 16));

                circles = new Pane();
                for (int y = 0; y < 4; y ++) {
                    for (int x = 0; x < 4; x++) {
                        StackPane stackPane = new StackPane();
                        stackPane.setLayoutX(x * 80 + 70);
                        stackPane.setLayoutY(y * 80 + 30);

                        Button btn = new Button();
                        btn.setShape(new Circle(30));
                        btn.setMinSize(2 * 30, 2 * 30);
                        btn.setMaxSize(2 * 30, 2 * 30);
                        btn.setDisable(true);

                        stackPane.getChildren().add(btn);

//                        Label l = new Label(""+(1+x+y*4));
//                        l.setFont(new Font("Roboto", 24));
//                        l.setTextFill(Paint.valueOf("white"));
//                        stackPane.getChildren().add(l);
                        circles.getChildren().add(stackPane);
                    }
                }
                Label levelLabel = new Label();
                levelLabel.setText("test");
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
