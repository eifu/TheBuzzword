package controller;


import apptemeplate.AppTemplate;
import data.BuzzwordGameData;
import data.BuzzwordUserData;

public class BuzzwordController implements FileController{

    private AppTemplate app;
    private BuzzwordGameData gameData;
    private BuzzwordUserData userData;

    public BuzzwordController(AppTemplate app){
        this.app = app;
        this.gameData = null;
        this.userData = null;
    }


    @Override
    public void handleLoginoutRequest(){

    }
    @Override
    public void handleHomeRequest(){

    }

    @Override
    public void handleHelpRequest(){

    }

    @Override
    public void handleQuitRequest(){

    }



}
