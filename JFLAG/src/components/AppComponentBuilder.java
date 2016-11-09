package components;


public interface AppComponentBuilder {

    AppGameDataComponent buildGameDataComponent();

    AppUserDataComponent buildUserDataComponent();

    AppFileComponent buildFileComponent();

    AppWorkspaceComponent buildWorkspaceComponent();

}
