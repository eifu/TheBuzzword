package controller;


import apptemeplate.AppTemplate;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import propertymanager.PropertyManager;
import ui.AppGUI;
import ui.AppYesNoCancelSingleton;
import ui.BuzzwordWorkspace;

import static buzzword.GameScreenState.*;
import static settings.AppPropertyType.CHECK_QUIT_MESSAGE;
import static settings.AppPropertyType.CHECK_QUIT_TITLE;
import static settings.InitializationParameters.YES;

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

            // remove gamemode comboBox
            ObservableList<Node> toolbarChiildren = gui.getToolbarPane().getChildren();
            toolbarChiildren.remove(4);

            gui.setLoginoutbtnIcon(false);
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

        PropertyManager pm = PropertyManager.getPropertyManager();
        AppYesNoCancelSingleton yesNoCancelSingleton = AppYesNoCancelSingleton.getSingleton();
        yesNoCancelSingleton.show(pm.getPropertyValue(CHECK_QUIT_TITLE), pm.getPropertyValue(CHECK_QUIT_MESSAGE));

        if (yesNoCancelSingleton.getSelection().equals(YES.getParameter())){
            System.exit(0);
        }

    }



}
