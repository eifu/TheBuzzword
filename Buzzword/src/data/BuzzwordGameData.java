package data;


import apptemeplate.AppTemplate;
import components.AppGameDataComponent;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BuzzwordGameData implements AppGameDataComponent{

    private AppTemplate app;
    private Map<String, MatrixData[]> modeMatrixMap;
    private Map<String,Integer[]> modeTimeLimitMap;
    private String[] modeList;
    private Map<String, String>usernamePasswordMap;
    private Map<String, Path> usernameFilepathMap;

    private int currentLevel;
    private String currentMode;

    public void setCurrentLevel(int currentLevel){this.currentLevel = currentLevel;}
    public void setCurrentMode(String currentMode){this.currentMode = currentMode;}

    public int getCurrentLevel(){return currentLevel;}
    public String getCurrentMode(){return currentMode;}

    public BuzzwordGameData(AppTemplate app){
        this(app, false);
    }

    public BuzzwordGameData(AppTemplate app, boolean initiateGame) {
        if (initiateGame) {
            this.app = app;
            this.modeMatrixMap = new HashMap<>();
            this.modeTimeLimitMap = new HashMap<>();
            this.modeList = new String[]{"normal", "hard"};
            this.usernamePasswordMap = new HashMap<>();
            this.usernameFilepathMap = new HashMap<>();
        } else {
            this.app = app;
        }
    }

    @Override
    public void reset(){
    }
}
