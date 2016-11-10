package controller;


import apptemeplate.AppTemplate;
import data.BuzzwordGameData;
import data.BuzzwordUserData;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ui.AppGUI;
import ui.BuzzwordWorkspace;

import static buzzword.GameScreenState.*;

public class BuzzwordController implements FileController{

    private AppTemplate app;

    public BuzzwordController(AppTemplate app){
        this.app = app;
    }


    @Override
    public void handleLoginoutRequest(){
        BuzzwordWorkspace buzzwordWorkspace = (BuzzwordWorkspace) app.getWorkspaceComponent();
        AppGUI gui = buzzwordWorkspace.getGui();

        if (!buzzwordWorkspace.isSignedIn()) {
            buzzwordWorkspace.setCurrentState(SIGNINGIN);
            buzzwordWorkspace.reloadWorkspace(gui.getAppPane());

            gui.setHomebtnDisable(false);
            gui.setLoginoutbtnDisable(true);
        }else{
            buzzwordWorkspace.setCurrentState(HOME);
            buzzwordWorkspace.setSignedIn(false);
            buzzwordWorkspace.reloadWorkspace(gui.getAppPane());

            ObservableList<Node> toolbarChiildren = gui.getToolbarPane().getChildren();
            toolbarChiildren.remove(4);
            gui.setHomebtnDisable(true);
            gui.setLoginoutbtnDisable(false);
        }
    }
    @Override
    public void handleHomeRequest(){
        BuzzwordWorkspace buzzwordWorkspace = (BuzzwordWorkspace) app.getWorkspaceComponent();
        AppGUI gui = buzzwordWorkspace.getGui();

        buzzwordWorkspace.setCurrentState(HOME);
        buzzwordWorkspace.reloadWorkspace(gui.getAppPane());

        Pane workspace = buzzwordWorkspace.getWorkspace();

        if (buzzwordWorkspace.isSignedIn()) {
            // gameStartBtn config
            ObservableList<Node> workspaceHomeChildren = workspace.getChildren();
            ObservableList<Node> vboxHomeChildren = ((VBox) workspaceHomeChildren.get(0)).getChildren();
            StackPane s = (StackPane) vboxHomeChildren.get(2);
            s.setVisible(true);
        }
        gui.setHomebtnDisable(true);
        gui.setLoginoutbtnDisable(false);
    }

    @Override
    public void handleHelpRequest(){
        BuzzwordWorkspace buzzwordWorkspace = (BuzzwordWorkspace) app.getWorkspaceComponent();
        AppGUI gui = buzzwordWorkspace.getGui();

        buzzwordWorkspace.setCurrentState(HELP);
        buzzwordWorkspace.reloadWorkspace(gui.getAppPane());
        gui.setHomebtnDisable(false);
    }

    @Override
    public void handleQuitRequest(){

    }



}
