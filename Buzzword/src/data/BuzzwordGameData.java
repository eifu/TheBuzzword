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
    public static final int MAXNUMBEROFELEMENTS = 118;
    public static final int MAXNUMBEROFPLACES = 279;

    private AppTemplate app;
    private ArrayList<String> modeList;
    private Map<String, String> usernamePasswordMap;
    private Map<String, TrieWordData> modeTrieWordMap;
    private Map<String, Integer> modeMaxLevelMap;
    private Set<String> foundWordsSet;

    private int currentLevel;
    private String currentMode;

    public void setModeMaxLevel(String mode, int level){
        modeMaxLevelMap.put(mode, level);
    }

    public int getModeMaxLevel(String mode){
        return modeMaxLevelMap.get(mode);
    }

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

    public Map getModeTrieWordMap() {
        return modeTrieWordMap;
    }

    public BuzzwordGameData(AppTemplate app) {
        this(app, false);
    }

    public BuzzwordGameData(AppTemplate app, boolean initiateGame) {
        if (initiateGame) {
            this.app = app;
            this.modeList = new ArrayList<>();
            this.usernamePasswordMap = new HashMap<>();
            this.modeTrieWordMap = new HashMap<>();
            this.modeMaxLevelMap = new HashMap<>();
            this.foundWordsSet = new HashSet<>();

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
        if (this.modeTrieWordMap.get(mode) == null) {
            TrieWordData wordSet = new TrieWordData();
            this.modeTrieWordMap.put(mode, wordSet);
        }

        this.modeTrieWordMap.get(mode).addWord(word);
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

        File f = new File(targetPath.toAbsolutePath() + File.separator + "DynamicGamedata.json");
        app.getFileComponent().saveGameData(this, f.toPath());
    }


    public boolean hasFound(String w){
        return foundWordsSet.contains(w);
    }

    public void found(String w){
        foundWordsSet.add(w);
    }
}
