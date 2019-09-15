package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPopUp extends Controller implements Initializable {

    ControllerPopUp(String name, Stage stage, String message, Controller controller) {
        super(name, stage);
        this.stage = stage;
        this.message = message;
        this.controller = controller;
    }

    private String message;
    private Controller controller;

    @FXML
    Stage stage;
    @FXML
    Text text;
    @FXML
    Button yes;
    @FXML
    Button no;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text.setText("Are you sure, that you want to " + message);
    }

    @FXML
    public void yes(){
        if(controller instanceof ControllerImport)
            ((ControllerImport) controller).submitYes();
        else if(controller instanceof ControllerDelete)
            ((ControllerDelete) controller).submitYes();
        else if(controller instanceof ControllerLanguage)
            ((ControllerLanguage) controller).submitDelete();
        stage.close();
    }

    @FXML
    public void no(){
        stage.close();
    }
}
