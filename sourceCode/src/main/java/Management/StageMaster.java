package Management;

import Controllers.Controller;
import Controllers.ControllerPrimary;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class StageMaster {

    @FXML
    private Stage stage;
    @FXML
    public static Scene scene;
    private Controller currentController;

    public StageMaster(Stage stage) {
        this.stage = stage;
    }


    public void loadNewScene(Controller controller) throws IOException {
        this.currentController = controller;
        loadScene(currentController.getName());
    }

    public void loadPreviousScene() throws IOException {
        loadNewScene(currentController.getPreviousController());
    }


    private void loadScene(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(currentController.getClass().getResource(name));
        loader.setController(currentController);
        Pane pane = loader.load();
        scene = new Scene(pane);
        this.stage.setScene(scene);
        stage.show();
    }

}