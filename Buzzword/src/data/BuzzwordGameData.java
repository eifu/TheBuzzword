package data;


import apptemeplate.AppTemplate;
import components.AppGameDataComponent;
import propertymanager.PropertyManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static settings.AppPropertyType.APP_TITLE;


public class BuzzwordGameData implements AppGameDataComponent {

    public static final int MAXNUMBEROFCOUNTRIES = 160;

    private AppTemplate app;
    private Map<String, MatrixData[]> modeMatrixMap;
    private Map<String, Integer[]> modeTimeLimitMap;
    private ArrayList<String> modeList;
    private Map<String, String> usernamePasswordMap;
    private Map<String, Set<String>> modeWordSetMap;

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

    public ArrayList<String> getModeList() {
        return modeList;
    }

    public Map getUsernamePasswordMap() {
        return usernamePasswordMap;
    }

    public Map getModeWordSetMap() {
        return modeWordSetMap;
    }

    public BuzzwordGameData(AppTemplate app) {
        this(app, false);
    }

    public BuzzwordGameData(AppTemplate app, boolean initiateGame) {
        if (initiateGame) {
            this.app = app;
            this.modeMatrixMap = new HashMap<>();
            this.modeTimeLimitMap = new HashMap<>();
            this.modeList = new ArrayList<>();
            this.usernamePasswordMap = new HashMap<>();
            this.modeWordSetMap = new HashMap<>();

        } else {
            this.app = app;
        }
    }

    public void addNamePassMap(String name, String pass) {
        this.usernamePasswordMap.put(name, pass);
    }

    public void addMode(String mode) {
        this.modeList.add(mode);
    }

    public void addModeWordSet(String mode, String word) {
        if (this.modeWordSetMap.get(mode) == null) {
            Set<String> wordSet = new HashSet<>();
            this.modeWordSetMap.put(mode, wordSet);
        }

        this.modeWordSetMap.get(mode).add(word);
    }

    public boolean validateUsernamePassword(String name, String pass) {
        return this.usernamePasswordMap.get(name) != null && this.usernamePasswordMap.get(name).equals(pass);
    }

    /*
        return true if the name is valid.
     */
    public boolean validateUsername(String name) {
        return this.usernamePasswordMap.get(name) == null;
    }

    @Override
    public void reset() {
    }

    public void save() {

        PropertyManager pm = PropertyManager.getPropertyManager();
        Path appDirPath = Paths.get(pm.getPropertyValue(APP_TITLE)).toAbsolutePath();
        Path targetPath = appDirPath.resolve("resources/data");

        File f = new File(targetPath.toAbsolutePath() + File.separator + "gamedata.json");
        app.getFileComponent().saveGameData(this, f.toPath());
    }


    public String getWord(int index) {
        Set<String> wordSet = modeWordSetMap.get(currentMode);
        int i = 0;
        for (String word : wordSet) {
            if (i == index) {
                return word;
            }
            i = i + 1;
        }
        return "";
    }
}
