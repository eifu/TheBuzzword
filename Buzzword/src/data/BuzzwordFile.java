package data;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import components.AppFileComponent;
import components.AppGameDataComponent;
import components.AppUserDataComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BuzzwordFile implements AppFileComponent {

    public static final String NAME = "NAME";
    public static final String PASS ="PASS";
    public static final String PROGRESS = "PROGRESS";

    public static final String NAME_PASS_MAP = "NAME_PASS_MAP";
    public static final String MODE_LIST = "MODE_LIST";

    @Override
    public void saveUserData(AppUserDataComponent data, Path path){

    }

    @Override
    public void saveGameData(AppGameDataComponent data, Path path){

    }

    @Override
    public void loadUserData(AppUserDataComponent data, Path path) throws IOException{
        BuzzwordUserData userData = (BuzzwordUserData) data;
        userData.reset();

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(Files.newInputStream(path));

        while (!jsonParser.isClosed()){
            JsonToken token = jsonParser.nextToken();
            if (JsonToken.FIELD_NAME.equals(token)){
                String fieldname = jsonParser.getCurrentName();
                switch (fieldname){
                    case NAME:
                        jsonParser.nextToken();
                        String name = jsonParser.getText();
                        userData.setUsername(name);
                        break;
                    case PASS:
                        jsonParser.nextToken();
                        String pass = jsonParser.getText();
                        userData.setPassword(pass);
                        break;
                    case PROGRESS:
                        jsonParser.nextToken(); // outer "["
                        String mode;
                        int progress;
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY){
                            jsonParser.nextToken();
                            mode = jsonParser.getText();
                            jsonParser.nextToken();
                            progress = jsonParser.getIntValue();

                            userData.setProgress(mode, progress);
                        }

                }
            }
        }

    }

    @Override
    public void loadGameData(AppGameDataComponent data, Path path) throws IOException{
        BuzzwordGameData gamedata = (BuzzwordGameData) data;
        gamedata.reset();

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser  = jsonFactory.createParser(Files.newInputStream(path));

        while (!jsonParser.isClosed()) {
            JsonToken token = jsonParser.nextToken();
            if (JsonToken.FIELD_NAME.equals(token)) {
                String fieldname = jsonParser.getCurrentName();
                switch (fieldname) {
                    case NAME_PASS_MAP:

                        jsonParser.nextToken(); // outer "["
                        String name;
                        String pass;
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) { // inner "["
                            jsonParser.nextToken();
                            name = jsonParser.getText();
                            jsonParser.nextToken();
                            pass = jsonParser.getText();

                            gamedata.addNamePassMap(name, pass);
                            jsonParser.nextToken(); // inner "]"
                        }
                        break;

                    case MODE_LIST:
                        jsonParser.nextToken(); // outer "["
                        String mode;
                        while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            mode = jsonParser.getText();
                            gamedata.addMode(mode);
                        }
                        break;

                    default:
                        throw new JsonParseException(jsonParser, "Unable to load JSON data");
                }
            }
        }
    }


}
