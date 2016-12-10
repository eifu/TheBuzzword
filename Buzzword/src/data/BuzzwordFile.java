package data;


import com.fasterxml.jackson.core.*;
import components.AppFileComponent;
import components.AppGameDataComponent;
import components.AppUserDataComponent;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BuzzwordFile implements AppFileComponent {

    public static final String NAME = "NAME";
    public static final String PASS = "PASS";
    public static final String PROGRESS = "PROGRESS";
    public static final String HIGHSCORE = "HIGHSCORE";

    public static final String NAME_PASS_MAP = "NAME_PASS_MAP";
    public static final String MODE_LIST = "MODE_LIST";
    public static final String MODE_WORDS = "MODE_WORDS";

    @Override
    public void saveUserData(AppUserDataComponent data, Path path) {
        BuzzwordUserData userData = (BuzzwordUserData) data;
        Map<String, Integer> progressMap = userData.getProgressMap();

        JsonFactory jsonFactory = new JsonFactory();

        try (OutputStream out = Files.newOutputStream(path)) {

            JsonGenerator generator = jsonFactory.createGenerator(out, JsonEncoding.UTF8);

            generator.writeStartObject();

            generator.writeFieldName(NAME);
            generator.writeString(userData.getUsername());

            generator.writeFieldName(PASS);
            generator.writeString(userData.getPassword());

            generator.writeFieldName(PROGRESS);
            generator.writeStartArray();
            for (String mode : progressMap.keySet()) {
                generator.writeStartArray();
                generator.writeString(mode);
                generator.writeNumber(progressMap.get(mode));
                generator.writeEndArray();
            }
            generator.writeEndArray();

            generator.writeEndObject();

            generator.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }


    }

    @Override
    public void saveGameData(AppGameDataComponent data, Path path) {
        BuzzwordGameData gameData = (BuzzwordGameData) data;
        Map<String, String> usernamePasswordMap = gameData.getUsernamePasswordMap();
        ArrayList<String> modeList = gameData.getModeList();


        JsonFactory jsonFactory = new JsonFactory();

        try (OutputStream out = Files.newOutputStream(path)) {

            JsonGenerator generator = jsonFactory.createGenerator(out, JsonEncoding.UTF8);

            generator.writeStartObject();

            generator.writeFieldName(NAME_PASS_MAP);
            generator.writeStartArray(usernamePasswordMap.size());
            for (String name : usernamePasswordMap.keySet()) {
                generator.writeStartArray();
                generator.writeString(name);
                generator.writeString(usernamePasswordMap.get(name));
                generator.writeEndArray();
            }
            generator.writeEndArray();

            generator.writeEndObject();

            generator.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void loadUserData(AppUserDataComponent data, Path path) throws IOException {
        BuzzwordUserData userData = (BuzzwordUserData) data;
        userData.reset();

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(Files.newInputStream(path));

        while (!jsonParser.isClosed()) {
            JsonToken token = jsonParser.nextToken();
            if (JsonToken.FIELD_NAME.equals(token)) {
                String fieldname = jsonParser.getCurrentName();
                switch (fieldname) {
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
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            jsonParser.nextToken();
                            mode = jsonParser.getText();
                            jsonParser.nextToken();
                            progress = jsonParser.getIntValue();

                            userData.setProgress(mode, progress);
                            jsonParser.nextToken(); // inner "]"
                        }
                        break;
                    case HIGHSCORE:
                        jsonParser.nextToken(); // {

                        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                            if (JsonToken.FIELD_NAME.equals(jsonParser.getCurrentToken())) {
                                mode = jsonParser.getCurrentName();
                                jsonParser.nextToken(); // [
                                ArrayList<Integer> a = new ArrayList<>();
                                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                                    a.add(jsonParser.getIntValue());
                                }
                                userData.setHighscore(mode, a);
                            }
                        }
                        break;
                    default:
                        throw new JsonParseException(jsonParser, "Unable to load JSON data");

                }
            }
        }

    }

    public void loadDynamicGamedata(AppGameDataComponent data, Path path) throws IOException {
        BuzzwordGameData gamedata = (BuzzwordGameData) data;
        gamedata.reset();

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(Files.newInputStream(path));

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
                    default:
                        throw new JsonParseException(jsonParser, "Unable to load JSON data");

                }
            }

        }

    }

    @Override
    public void loadGameData(AppGameDataComponent data, Path path) throws IOException {
        BuzzwordGameData gamedata = (BuzzwordGameData) data;
        gamedata.reset();

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(Files.newInputStream(path));

        while (!jsonParser.isClosed()) {
            JsonToken token = jsonParser.nextToken();
            if (JsonToken.FIELD_NAME.equals(token)) {
                String fieldname = jsonParser.getCurrentName();
                switch (fieldname) {
//                    case NAME_PASS_MAP:
//
//                        jsonParser.nextToken(); // outer "["
//                        String name;
//                        String pass;
//                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) { // inner "["
//                            jsonParser.nextToken();
//                            name = jsonParser.getText();
//                            jsonParser.nextToken();
//                            pass = jsonParser.getText();
//
//                            gamedata.addNamePassMap(name, pass);
//                            jsonParser.nextToken(); // inner "]"
//                        }
//                        break;

                    case MODE_LIST:
                        jsonParser.nextToken(); // outer "["
                        String mode;
                        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                            mode = jsonParser.getText();
                            gamedata.addMode(mode);
                        }
                        break;

                    case MODE_WORDS:
                        jsonParser.nextToken(); // starting object of mode words.
                        String modeName, countryName;
                        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                            modeName = jsonParser.getText();
                            jsonParser.nextToken(); // starting array of mode word list
                            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                                countryName = jsonParser.getText();
                                gamedata.addModeWordSet(modeName, countryName);
                            }
                        }

                        break;

                    default:
                        throw new JsonParseException(jsonParser, "Unable to load JSON data");
                }
            }
        }
    }


}
