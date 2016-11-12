package data;


import apptemeplate.AppTemplate;
import components.AppGameDataComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class BuzzwordGameData implements AppGameDataComponent {

    private AppTemplate app;
    private Map<String, MatrixData[]> modeMatrixMap;
    private Map<String, Integer[]> modeTimeLimitMap;
    private ArrayList<String> modeList;
    private Map<String, String> usernamePasswordMap;

    private int currentLevel;
    private String currentMode;

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public BuzzwordGameData(AppTemplate app) {
        this(app, false);
    }

    public BuzzwordGameData(AppTemplate app, boolean initiateGame)  {
        if (initiateGame) {
            this.app = app;
            this.modeMatrixMap = new HashMap<>();
            this.modeTimeLimitMap = new HashMap<>();
            this.modeList = new ArrayList<>();
            this.usernamePasswordMap = new HashMap<>();

        } else {
            this.app = app;
        }
    }

    public void addNamePassMap(String name, String pass){
       this.usernamePasswordMap.put(name, pass);
    }

    public void addMode(String mode){
        this.modeList.add(mode);
    }

    @Override
    public void reset() {
    }
}
