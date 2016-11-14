package data;

import apptemeplate.AppTemplate;
import components.AppUserDataComponent;
import propertymanager.PropertyManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static settings.AppPropertyType.APP_TITLE;

public class BuzzwordUserData implements AppUserDataComponent {
    private String username;
    private String password;
    private Map<String, Integer> progress;
    private Map<String, Integer> highscore;

    public void setUsername(String username){this.username = username;}
    public String getUsername(){return username;}
    public void setPassword(String password){this.password = password;}
    public String getPassword(){return password;}
    public Map<String, Integer> getProgressMap(){return progress;}
    public int getProgress(String mode){return progress.get(mode);}
    public void setProgress(String mode, int progress){
        this.progress.put(mode, progress);
    }

    public BuzzwordUserData(){
        this.username = "";
        this.password = "";
        progress = new HashMap<>();
        highscore = new HashMap<>();
    }

    public void login(AppTemplate app) {

        try {
            // TODO get file path should be more simple.
            app.getFileComponent().loadUserData(this, new File("data/"+username+".json").toPath());

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void signup(AppTemplate app){
        for (String mode : ((BuzzwordGameData)app.getGameDataComponent()).getModeList()){
            progress.put(mode, 1);
        }
        PropertyManager pm = PropertyManager.getPropertyManager();

        Path appDirPath = Paths.get(pm.getPropertyValue(APP_TITLE)).toAbsolutePath();
        Path targetPath = appDirPath.resolve("resources/data");
        try{
            File f = new File(targetPath.toAbsolutePath()+File.separator+username+".json");
            f.createNewFile();

            app.getFileComponent().saveUserData(this, f.toPath());

        }catch (IOException ioe){
            ioe.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void reset(){
        this.username = "";
        this.password = "";
        progress = new HashMap<>();
        highscore = new HashMap<>();
    }

}
