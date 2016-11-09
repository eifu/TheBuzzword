package ui;


import apptemeplate.AppTemplate;
import buzzword.GameScreenState;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import propertymanager.PropertyManager;

import static buzzword.GameScreenState.*;
import static buzzword.BuzzwordProperty.*;
import static settings.AppPropertyType.LOGINSTATE_ICON;

import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.net.URISyntaxException;

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

    public void setHandler(GameScreenState state) {
        switch (state){
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
                        gui.setLoginoutbtnIcon(true);

                        gui.setHomebtnDisable(true);
                        gui.setLoginoutbtnDisable(false);
                    }
                });

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
        setHandler(currentState);
    }


}
