package ui;


import apptemeplate.AppTemplate;
import buzzword.GameScreenState;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
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


        workspace = new VBox();
    }


    public void setCurrentState(GameScreenState state){this.currentState = state;}

    @Override
    public void initStyle(){
        PropertyManager pm = PropertyManager.getPropertyManager();

        gui.getAppPane().setId(pm.getPropertyValue(ROOT_BORDERPANE_ID));
        gui.getToolbarPane().getStyleClass().setAll(pm.getPropertyValue(SEGMENTED_BUTTON_BAR));
        gui.getToolbarPane().setId(pm.getPropertyValue(TOP_TOOLBAR_ID));

        ObservableList<Node> toolbarChildren = gui.getToolbarPane().getChildren();
        toolbarChildren.get(0).getStyleClass().add(pm.getPropertyValue(FIRST_TOOLBAR_BUTTON));
        toolbarChildren.get(toolbarChildren.size() - 1).getStyleClass().add(pm.getPropertyValue(LAST_TOOLBAR_BUTTON));

        workspace.getStyleClass().add(CLASS_BORDERED_PANE);
//        guiHeadingLabel.getStyleClass().setAll(pm.getPropertyValue(HEADING_LABEL));

    }

    @Override
    public void reloadWorkspace(){

    }


}
