package data;

import apptemeplate.AppTemplate;
import components.AppUserDataComponent;
import propertymanager.PropertyManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static settings.AppPropertyType.APP_TITLE;

public class BuzzwordUserData implements AppUserDataComponent {
    private String username;
    private String password;
    private Map<String, Integer> progress;
    private Map<String, int[]> highscore;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, Integer> getProgressMap() {
        return progress;
    }

    public int getProgress(String mode) {
        return progress.get(mode);
    }

    public void setProgress(String mode, int progress) {
        this.progress.put(mode, progress);
    }

    public void setHighscore(String mode, int[] arraylist) {
        this.highscore.put(mode, arraylist);
    }

    public void setHighscore(String mode, int level, int score){
        this.highscore.get(mode)[level] = score;
    }

    public int[] getHighScore(String mode) {
        return highscore.get(mode);
    }

    public BuzzwordUserData() {
        this.username = "";
        this.password = "";
        this.progress = new HashMap<>();
        this.highscore = new HashMap<>();
    }

    public void login(AppTemplate app) {

        PropertyManager pm = PropertyManager.getPropertyManager();
        Path appDirPath = Paths.get(pm.getPropertyValue(APP_TITLE)).toAbsolutePath();
        Path targetPath = appDirPath.resolve("resources/data");

        try {
            File f = new File(targetPath.toAbsolutePath() + File.separator + username + ".json");
            app.getFileComponent().loadUserData(this, f.toPath());

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    public void signup(AppTemplate app) {
        for (String mode : ((BuzzwordGameData) app.getGameDataComponent()).getModeList()) {
            progress.put(mode, 1);
            highscore.put(mode, new int[((BuzzwordGameData) app.getGameDataComponent()).getModeMaxLevel(mode)]);
        }


        save(app);
    }

    public void save(AppTemplate app) {
        PropertyManager pm = PropertyManager.getPropertyManager();
        Path appDirPath = Paths.get(pm.getPropertyValue(APP_TITLE)).toAbsolutePath();
        Path targetPath = appDirPath.resolve("resources/data");

        try {
            File f = new File(targetPath.toAbsolutePath() + File.separator + username + ".json");
            f.createNewFile();
            app.getFileComponent().saveUserData(this, f.toPath());

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void reset() {
        this.username = "";
        this.password = "";
        this.progress = new HashMap<>();
        this.highscore = new HashMap<>();
    }

}
