package buzzword;


import apptemeplate.AppTemplate;
import data.BuzzwordFile;
import data.BuzzwordGameData;
import components.*;
import data.BuzzwordUserData;
import propertymanager.PropertyManager;
import ui.BuzzwordWorkspace;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static buzzword.BuzzwordProperty.*;


public class Buzzword extends AppTemplate {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public AppComponentBuilder makeAppComponentBuilder() {
        return new AppComponentBuilder() {
            @Override
            public AppGameDataComponent buildGameDataComponent() throws FileNotFoundException {
                AppGameDataComponent gameDataComponent = new BuzzwordGameData(Buzzword.this, true);

                URL gameDataURL = AppTemplate.class.getClassLoader().getResource("data/gamedata.json");
                // TODO use propertymanager to load the file path

                if (gameDataURL == null){
                    throw new FileNotFoundException("Json resource folder does not exist");
                }

                try {
                    getFileComponent().loadGameData(gameDataComponent, Paths.get(gameDataURL.toURI()));
                }catch(URISyntaxException urie){
                    urie.printStackTrace();
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }

                return gameDataComponent;
            }

            @Override
            public AppUserDataComponent buildUserDataComponent() {
                return new BuzzwordUserData();
            }

            @Override
            public AppFileComponent buildFileComponent() {
                return new BuzzwordFile();
            }

            @Override
            public AppWorkspaceComponent buildWorkspaceComponent() {
                return new BuzzwordWorkspace(Buzzword.this);
            }
        };
    }

    public String getFileControllerClass() {
        return "BuzzwordController";
    }

}
