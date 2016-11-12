package data;

import components.AppUserDataComponent;

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

    public BuzzwordUserData(){
        this.username = "";
        this.password = "";
        progress = new HashMap<>();
        highscore = new HashMap<>();
    }

    @Override
    public void reset(){
        this.username = "";
        this.password = "";
        progress = null;
        highscore = null;
    }

}
