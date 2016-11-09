package ui;


import apptemeplate.AppTemplate;
import buzzword.GameScreenState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import propertymanager.PropertyManager;

import java.util.Stack;

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


                container.getChildren().addAll(appTitle, circles);
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
                grid.add(new TextField(), 1,1);

                grid.add(new Label("Password:"), 0,2);
                grid.add(new TextField(), 1,2);

                StackPane signInStackPane = new StackPane();
                Button signInBtn = new Button("Sign In");
                signInBtn.setTextFill(Paint.valueOf("white"));

                HBox hbBtn = new HBox(10);
                hbBtn.getChildren().add(signInBtn);
                hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
                grid.add(hbBtn, 1, 4);

                signingIncontainer.getChildren().addAll(screenLabel, grid);
                this.getChildren().add(signingIncontainer);
                break;
        }
    }





    public Pane change(GameScreenState state){
        return new GameScreen(state);
    }
}
