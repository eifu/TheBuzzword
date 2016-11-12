package components;

import java.io.IOException;
import java.nio.file.Path;


public interface AppFileComponent {

    void saveUserData(AppUserDataComponent data, Path path);

    void saveGameData(AppGameDataComponent data, Path path);

    void loadUserData(AppUserDataComponent data, Path path) throws IOException;

    void loadGameData(AppGameDataComponent data, Path path) throws IOException;
}
