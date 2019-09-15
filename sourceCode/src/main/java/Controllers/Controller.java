package Controllers;

import Management.Database;
import Management.Launcher;
import Management.StageMaster;
import Types.LangUserType;
import Types.LanguageType;
import Types.UserType;
import Types.WordUserType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {

    public static StageMaster stageMaster;
    public static Database database;
    static String currentLogin;
    static String currentLoginDB;
    static UserType currentUser;
    static LanguageType currentLanguage;
    static String currentLanguageDB;
    static LangUserType currentLangUser;
    static LinkedList<LinkedList<Integer>> allCards;
    static HashMap<Integer, WordUserType> personalInfo;

    private String name;
    private Controller previousController;


    Controller(String name, Controller previousController) {
        this.name = name;
        this.previousController = previousController;
    }

    public Controller(String name, Stage stage) {
        this.name = name;
        this.previousController = this;
    }


    @Override
    public abstract void initialize(URL url, ResourceBundle resourceBundle);

    @FXML
    void goBack() throws IOException {
        Controller.stageMaster.loadPreviousScene();
    }


    public String getName() {
        return this.name;
    }

    public Controller getPreviousController(){
        return this.previousController;
    }

    void invokePopUp(String message) throws IOException {
        Stage stage = new Stage();
        StageMaster stageMaster = new StageMaster(stage);
        stageMaster.loadNewScene(new ControllerPopUp(Launcher.scenesLocation + "popUp.fxml", stage, message, this));
    }

    Image setImage(String name){
        return new Image(getClass().getResourceAsStream(Launcher.imagesLocation + name + ".png"));
    }

    void setLangImage(Text language, ImageView flag){
        language.setText(currentLanguage.getLanguage());
        flag.setImage(setImage(currentLanguage.getImage()));
    }

}
