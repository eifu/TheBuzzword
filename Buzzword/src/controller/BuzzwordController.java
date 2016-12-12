package controller;


import apptemeplate.AppTemplate;
import buzzword.GameScreenState;
import data.BuzzwordUserData;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import propertymanager.PropertyManager;
import settings.InitializationParameters;
import ui.AppGUI;
import ui.AppYesNoCancelSingleton;
import ui.BuzzwordWorkspace;

import static buzzword.GameScreenState.*;
import static settings.AppPropertyType.*;
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

            buzzwordWorkspace.setHandler();

            gui.setHomebtnDisable(false);
            gui.setLoginoutbtnDisable(true);

        }else{
            PropertyManager pm = PropertyManager.getPropertyManager();
            AppYesNoCancelSingleton dialog = AppYesNoCancelSingleton.getSingleton();
            dialog.show(pm.getPropertyValue(CHECK_LOGOUT_TITLE),pm.getPropertyValue(CHECK_LOGOUT_MESSAGE), false);

            if (dialog.getSelection().equals(YES.getParameter())) {

                if (buzzwordWorkspace.getCurrentState().equals(GameScreenState.GAMEPLAY)){
                    Button quitBtn = gui.getQuitbtn();
                    quitBtn.setVisible(true);
                }

                ((BuzzwordUserData) app.getUserDataComponent()).save(app);

                buzzwordWorkspace.setCurrentState(HOME);
                buzzwordWorkspace.setSignedIn(false);
                buzzwordWorkspace.reloadWorkspace(gui.getAppPane());

                // remove gamemode comboBox
                ObservableList<Node> toolbarChiildren = gui.getToolbarPane().getChildren();
                toolbarChiildren.remove(4);
                toolbarChiildren.remove(4);

                gui.setLoginoutbtnIcon(false);
                gui.setHomebtnDisable(true);
                gui.setLoginoutbtnDisable(false);
            }
        }
    }

    @Override
    public void handleHomeRequest(){
        BuzzwordWorkspace buzzwordWorkspace = (BuzzwordWorkspace) app.getWorkspaceComponent();
        AppGUI gui = buzzwordWorkspace.getGui();

        if (buzzwordWorkspace.getCurrentState().equals(GameScreenState.GAMEPLAY)){
            if (!buzzwordWorkspace.isPlayingGame()){
                buzzwordWorkspace.removePosemenu();
            }

            Button quitBtn = gui.getQuitbtn();
            quitBtn.setVisible(true);

            Timeline t = buzzwordWorkspace.getTimeline();
            t.pause();
        }

        buzzwordWorkspace.setCurrentState(HOME);
        buzzwordWorkspace.reloadWorkspace(gui.getAppPane());

        buzzwordWorkspace.setHandler();

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

        buzzwordWorkspace.setHandler();


        gui.setHomebtnDisable(false);
    }

    @Override
    public void handleQuitRequest(){

        PropertyManager pm = PropertyManager.getPropertyManager();
        AppYesNoCancelSingleton yesNoCancelSingleton = AppYesNoCancelSingleton.getSingleton();
        yesNoCancelSingleton.show(pm.getPropertyValue(CHECK_QUIT_TITLE), pm.getPropertyValue(CHECK_QUIT_MESSAGE), false);

        if (yesNoCancelSingleton != null && yesNoCancelSingleton.getSelection().equals(YES.getParameter())){
            BuzzwordWorkspace buzzwordWorkspace = (BuzzwordWorkspace) app.getWorkspaceComponent();
            if (buzzwordWorkspace.isSignedIn()){
                ((BuzzwordUserData)app.getUserDataComponent()).save(app);
            }
            System.exit(0);
        }
    }



}
