package components;


import java.io.FileNotFoundException;

public interface AppComponentBuilder {

    AppGameDataComponent buildGameDataComponent() throws FileNotFoundException;

    AppUserDataComponent buildUserDataComponent();

    AppFileComponent buildFileComponent();

    AppWorkspaceComponent buildWorkspaceComponent();

}
