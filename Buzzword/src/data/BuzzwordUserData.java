package data;

import apptemeplate.AppTemplate;
import components.AppUserDataComponent;

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

    public void login(AppTemplate app) throws IOException {

        try {
            app.getFileComponent().loadUserData(this, Paths.get(AppTemplate.class.getClassLoader().getResource("data/test1.json").toURI()));
        }catch(URISyntaxException uris){
            uris.printStackTrace();
        }


    }

    @Override
    public void reset(){
        this.username = "";
        this.password = "";
        progress = null;
        highscore = null;
    }

}
