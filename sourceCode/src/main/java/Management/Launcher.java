package Management;

import Controllers.Controller;
import Controllers.ControllerPrimary;
import Types.WordType;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;

public class Launcher extends Application {

    public static String scenesLocation = "/Scenes/";
    public static String imagesLocation = "/Images/";
    public static String cssLocation = "/Css/";
    public static HashMap<Integer, WordType> words;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Font.loadFont(getClass().getResourceAsStream("/Fonts/Leoscar Serif.ttf"), 50);
        Font.loadFont(getClass().getResourceAsStream("/Fonts/Attentica 4F UltraLight.ttf"), 50);
        Font.loadFont(getClass().getResourceAsStream("/Fonts/c7nazara.ttf"), 50);
        Font.loadFont(getClass().getResourceAsStream("/Fonts/vtks morning rain.ttf"), 50);

        primaryStage.setTitle("Fiszki");
        primaryStage.setResizable(false);
        Controller controllerPrimary = new ControllerPrimary(scenesLocation + "primary.fxml", primaryStage);
        Controller.stageMaster.loadNewScene(controllerPrimary);
    }

    public static void main(String[] args) { launch(args); }

}
