package ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import settings.InitializationParameters;

public class AppYesNoCancelSingleton  extends Stage{

    private static AppYesNoCancelSingleton singleton;

    private Scene messageScene;
    private VBox messagePane;
    private Label messageLbl;

    private  Button yesBtn;
    private   Button noBtn;
    private  Button cancelBtn;
    private String selection;


    private AppYesNoCancelSingleton(){};

    public static AppYesNoCancelSingleton getSingleton(){
        if (singleton == null){
            singleton = new AppYesNoCancelSingleton();
        }
        return singleton;
    }

    public void init(Stage owner){

        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);

        messageLbl = new Label();


        yesBtn = new Button(InitializationParameters.YES.getParameter());
        noBtn = new Button(InitializationParameters.NO.getParameter());
        cancelBtn = new Button(InitializationParameters.CANCEL.getParameter());

        EventHandler<ActionEvent> yesNoCancelHandler = event ->{
            AppYesNoCancelSingleton.this.selection = ((Button) event.getSource()).getText();
            AppYesNoCancelSingleton.this.hide();
        };

        yesBtn.setOnAction(yesNoCancelHandler);
        noBtn.setOnAction(yesNoCancelHandler);
        cancelBtn.setOnAction(yesNoCancelHandler);

        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(yesBtn);
        buttonBox.getChildren().add(noBtn);
        buttonBox.getChildren().add(cancelBtn);

        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(messageLbl);
        messagePane.getChildren().add(buttonBox);

        messagePane.setPadding(new Insets(10, 20, 20, 20));
        messagePane.setSpacing(10);

        messageScene = new Scene(messagePane);
        this.setScene(messageScene);
    }

    public String getSelection(){return selection;}

    public void show(String title, String message){
        // setTitle is implemented in Stage class
        setTitle(title);

        messageLbl.setText(message);

        showAndWait();
    }

}
