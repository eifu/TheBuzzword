package components;


import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public abstract class AppWorkspaceComponent implements AppStyleArbiter{


    private Pane workspace;
    private boolean workspaceActivated;

    public void activateWorkspace(BorderPane appPane){
        if (!workspaceActivated){
            appPane.setCenter(workspace);
            workspaceActivated = true;
        }
    }

    public void setWorkspace(Pane init){workspace =init;}
    public Pane getWorkspace(){return workspace;}

    public abstract void reloadWorkspace();
}
