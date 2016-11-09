package ui;


import apptemeplate.AppTemplate;
import buzzword.GameScreenState;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import propertymanager.PropertyManager;

import static buzzword.GameScreenState.*;
import static buzzword.BuzzwordProperty.*;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public class BuzzwordWorkspace extends AppWorkspaceComponent{

    AppTemplate app;
    AppGUI gui;
    BuzzwordController controller;
    GameScreenState currentState;

    public BuzzwordWorkspace(AppTemplate app){
        this.app = app;
        this.gui = app.getGui();
        controller = (BuzzwordController) gui.getFileController();
        currentState = HOME;
        workspace = new GameScreen(currentState);
        activateWorkspace(gui.getAppPane());
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
    }


}
