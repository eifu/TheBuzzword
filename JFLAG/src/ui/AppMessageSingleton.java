package ui;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import settings.InitializationParameters;

public class AppMessageSingleton extends Stage {

    private static AppMessageSingleton singleton;
    private Label messageLabel;
    private AppMessageSingleton(){};

    public static AppMessageSingleton getSingleton(){
        if (singleton == null){
            singleton = new AppMessageSingleton();
        }
        return singleton;
    }

    public void setMessageLabel(String message){
        messageLabel.setText(message);
    }

    public void init(Stage owner){
        initModality(Modality.WINDOW_MODAL); // ??
        initOwner(owner);

        messageLabel = new Label();

        Button closeBtn = new Button(InitializationParameters.CLOSE_LABEL.getParameter());
        closeBtn.setOnAction(e -> this.close());

        VBox messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(messageLabel);
        messagePane.getChildren().add(closeBtn);

        messagePane.setPadding(new Insets(80, 60, 80, 60));
        messagePane.setSpacing(20);

        Scene messageScene = new Scene(messagePane);

        this.setScene(messageScene);
    }

    public void show(String title, String message){
        setTitle(title);
        setMessageLabel(message);
        showAndWait();
    }

}
