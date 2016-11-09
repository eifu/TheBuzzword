package buzzword;


import apptemeplate.AppTemplate;
import data.BuzzwordFile;
import data.BuzzwordGameData;
import components.*;
import data.BuzzwordUserData;


public class Buzzword extends AppTemplate{
    public static void main(String[] args){launch(args);}

    @Override
    public AppComponentBuilder makeAppComponentBuilder(){
        return new AppComponentBuilder() {
            @Override
            public AppGameDataComponent buildGameDataComponent() {
                return new BuzzwordGameData(Buzzword.this);
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
                return null;
            }
        };
    }

}
