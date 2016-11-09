package components;

import java.nio.file.Path;


public interface AppFileComponent {

    void saveUserData(AppUserDataComponent data, Path path);

    void saveGameData(AppGameDataComponent data, Path path);

    void loadUserData(AppUserDataComponent data, Path path);

    void loadGameData(AppGameDataComponent data, Path path);
}
