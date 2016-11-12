package data;


import apptemeplate.AppTemplate;
import components.AppGameDataComponent;
import propertymanager.PropertyManager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static settings.AppPropertyType.*;
import static settings.InitializationParameters.APP_IMAGE_DIR_PATH;


public class BuzzwordGameData implements AppGameDataComponent {

    private AppTemplate app;
    private Map<String, MatrixData[]> modeMatrixMap;
    private Map<String, Integer[]> modeTimeLimitMap;
    private String[] modeList;
    private Map<String, String> usernamePasswordMap;
    private Map<String, Path> usernameFilepathMap;

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
            this.modeList = new String[]{"normal", "hard"};
            this.usernamePasswordMap = new HashMap<>();
            this.usernameFilepathMap = new HashMap<>();


        } else {
            this.app = app;
        }
    }

    public void addNamePassMap(String name, String pass){
       this.usernamePasswordMap.put(name, pass);
    }

    @Override
    public void reset() {
    }
}
