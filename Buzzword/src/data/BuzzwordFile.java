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

    public static final String NAME_PASS_MAP = "NAME_PASS_MAP";

    @Override
    public void saveUserData(AppUserDataComponent data, Path path){

    }

    @Override
    public void saveGameData(AppGameDataComponent data, Path path){

    }

    @Override
    public void loadUserData(AppUserDataComponent data, Path path){


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

                        jsonParser.nextToken(); // first [
                        String name;
                        String pass;
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            jsonParser.nextToken();
                            name = jsonParser.getText();
                            jsonParser.nextToken();
                            pass = jsonParser.getText();

                            gamedata.addNamePassMap(name, pass);
                            jsonParser.nextToken();
                            //jsonParser.nextToken();
                        }
                        break;
                    default:
                        throw new JsonParseException(jsonParser, "Unable to load JSON data");
                }
            }
        }
    }


}
