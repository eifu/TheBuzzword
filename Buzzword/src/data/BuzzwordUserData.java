package data;

import apptemeplate.AppTemplate;
import components.AppUserDataComponent;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BuzzwordUserData implements AppUserDataComponent {
    private String username;
    private String password;
    private Map<String, Integer> progress;
    private Map<String, Integer> highscore;

    public void setUsername(String username){this.username = username;}
    public String getUsername(){return username;}
    public void setPassword(String password){this.password = password;}
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

    }

    @Override
    public void reset(){
        this.username = "";
        this.password = "";
        progress = new HashMap<>();
        highscore = new HashMap<>();
    }

}
