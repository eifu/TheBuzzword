package ui;


import apptemeplate.AppTemplate;
import buzzword.GameScreenState;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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


                container.getChildren().addAll(appTitle, circles);
                this.getChildren().add(container);
        }
    }





    public Pane change(GameScreenState state){
        return new GameScreen(state);
    }
}
