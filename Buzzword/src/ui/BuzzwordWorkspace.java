package ui;


import apptemeplate.AppTemplate;
import buzzword.Buzzword;
import buzzword.GameScreenState;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import data.BuzzwordGameData;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import propertymanager.PropertyManager;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.Stack;

import static buzzword.GameScreenState.*;
import static buzzword.BuzzwordProperty.*;
import static settings.AppPropertyType.PLAYGAME_ICON;


public class BuzzwordWorkspace extends AppWorkspaceComponent{

    AppTemplate app;
    AppGUI gui;
    BuzzwordController controller;
    GameScreenState currentState;
    boolean signedIn;

    public BuzzwordWorkspace(AppTemplate app){
        this.app = app;
        this.gui = app.getGui();
        this.controller = (BuzzwordController) gui.getFileController();
        this.currentState = HOME;
        this.workspace = new GameScreen(currentState);
        this.signedIn = false;
        activateWorkspace(gui.getAppPane());
    }

    public void setHandler() {
        switch (currentState){
            case HOME:
                if (signedIn){
                    ObservableList<Node> workspaceHomeChildren = workspace.getChildren();
                    ObservableList<Node> vboxHomeChildren = ((VBox)workspaceHomeChildren.get(0)).getChildren();
                    StackPane s = (StackPane) vboxHomeChildren.get(2);
                    Button gamesStartBtn = (Button)s.getChildren().get(0);

                    if (gui.getToolbarPane().getChildren().size() != 5) {
                        ObservableList<String> options =
                                FXCollections.observableArrayList(
                                        "English Dictionary",
                                        "Places",
                                        "Science",
                                        "Famous People"
                                );// TODO get those data from gamedata
                        ComboBox<String> comboBox = new ComboBox<>(options);
                        comboBox.setValue("English Dictionary");

                        comboBox.setPrefSize(150, 30);
                        comboBox.setLayoutX(26);
                        comboBox.setLayoutY(244);
                        Pane toolbar = gui.getToolbarPane();
                        toolbar.getChildren().add(comboBox);
                    }else {
                        gui.getToolbarPane().getChildren().get(4).setVisible(true);
                    }
                    gamesStartBtn.setOnAction(e ->{
                        setCurrentState(SELECTING);
                        reloadWorkspace(gui.getAppPane());

                        BuzzwordGameData gamedata = (BuzzwordGameData) app.getGameDataComponent();
                        ComboBox<String> gamemodeComboBox = (ComboBox)gui.getToolbarPane().getChildren().get(4);
                        gamedata.setCurrentMode(gamemodeComboBox.getValue());

                        gui.setHomebtnDisable(false);
                        gui.getToolbarPane().getChildren().get(4).setVisible(false);
                    });
                }
                break;
            case SIGNINGIN:
                ObservableList<Node> workspaceChildren = workspace.getChildren();
                ObservableList<Node> vboxChildren = ((VBox)workspaceChildren.get(0)).getChildren();

                ObservableList<Node> gridChildren =  ((GridPane)vboxChildren.get(1)).getChildren();

                Button signInBtn= (Button)((HBox)gridChildren.get(5)).getChildren().get(0);
                signInBtn.setOnAction(e ->{
                    TextField usernameTxt = (TextField) gridChildren.get(2);
                    TextField passwordTxt = (TextField) gridChildren.get(4);

                    String name = usernameTxt.getText();
                    String pass = passwordTxt.getText();
                    if (!name.equals("") && !pass.equals("")){
                        setCurrentState(HOME);
                        reloadWorkspace(gui.getAppPane());
                        signedIn = true;
                        setHandler();

                        // gameStartBtn config
                        ObservableList<Node> workspaceHomeChildren = workspace.getChildren();
                        ObservableList<Node> vboxHomeChildren = ((VBox)workspaceHomeChildren.get(0)).getChildren();
                        StackPane s = (StackPane) vboxHomeChildren.get(2);
                        s.setVisible(true);

                        // toolbar config
                        gui.setLoginoutbtnIcon(true);
                        gui.setHomebtnDisable(true);
                        gui.setLoginoutbtnDisable(false);


                    }
                });
                break;
            case SELECTING:
                int progress = 4; // TODO need to get from BuzzwordUserData
                int totalLevel = 8; // TODO need to get from BuzzwordGameData
                workspaceChildren = workspace.getChildren();
                vboxChildren = ((VBox)workspaceChildren.get(0)).getChildren();
                ObservableList<Node> levelChildren = ((Pane)vboxChildren.get(2)).getChildren();
                Label l = (Label) vboxChildren.get(1);

                ComboBox comboBox = (ComboBox) gui.getToolbarPane().getChildren().get(4);
                l.setText((String)comboBox.getValue());

                for (int level = 0; level < totalLevel; level ++){
                    StackPane s = (StackPane)levelChildren.get(level);
                    Button b = (Button) s.getChildren().get(0);
                    b.setOnAction(e ->{
                        BuzzwordGameData gamedata = (BuzzwordGameData) app.getGameDataComponent();
                        gamedata.setCurrentLevel(Integer.parseInt(b.getId()));

                        setCurrentState(GAMEPLAY);
                        reloadWorkspace(gui.getAppPane());
                    });
                    if (level < progress) {
                        b.setDisable(false);
                    }
                }
                break;
            case GAMEPLAY:
                PropertyManager pm = PropertyManager.getPropertyManager();

                workspaceChildren = workspace.getChildren();
                BorderPane gameWorkspace = (BorderPane)workspaceChildren.get(0);
                VBox centerVBox = (VBox) gameWorkspace.getCenter();

                Label modeLabel = (Label)centerVBox.getChildren().get(1);
                String mode = "mode: " + ((BuzzwordGameData)app.getGameDataComponent()).getCurrentMode();
                modeLabel.setText(mode);

                Label levelLabel = (Label)centerVBox.getChildren().get(3);
                String level = "Level: " + ((BuzzwordGameData)app.getGameDataComponent()).getCurrentLevel();
                levelLabel.setText(level);

                try {
                    Button playGameBtn = gui.initializeChildButton(PLAYGAME_ICON.toString(), false);
                    playGameBtn.setAlignment(Pos.BOTTOM_CENTER);
                    playGameBtn.setPrefSize(450, 0);
                    playGameBtn.getStyleClass().add(pm.getPropertyValue(PLAY_RESUME_BUTTON));
                    centerVBox.getChildren().add(playGameBtn);
                }catch(Exception e){
                    e.printStackTrace();
                }

        }
    }

    public AppGUI getGui(){return gui;}

    public void setCurrentState(GameScreenState state){this.currentState = state;}

    @Override
    public void initStyle(){
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
    public void reloadWorkspace(BorderPane appPane){
        workspace = ((GameScreen)workspace).change(currentState);
        appPane.setCenter(workspace);
        setHandler();
    }


    public boolean isSignedIn(){return signedIn;}
    public void setSignedIn(boolean o){signedIn=o;}
}
