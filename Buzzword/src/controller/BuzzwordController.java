package controller;


import apptemeplate.AppTemplate;
import data.BuzzwordGameData;
import data.BuzzwordUserData;
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

        buzzwordWorkspace.setCurrentState(SIGNINGIN);
        buzzwordWorkspace.reloadWorkspace(gui.getAppPane());

        gui.setHomebtnDisable(false);
        gui.setLoginoutbtnDisable(true);
    }
    @Override
    public void handleHomeRequest(){
        BuzzwordWorkspace buzzwordWorkspace = (BuzzwordWorkspace) app.getWorkspaceComponent();
        AppGUI gui = buzzwordWorkspace.getGui();

        buzzwordWorkspace.setCurrentState(HOME);
        buzzwordWorkspace.reloadWorkspace(gui.getAppPane());

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
