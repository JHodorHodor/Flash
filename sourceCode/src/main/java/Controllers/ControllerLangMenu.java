package Controllers;

import Management.GridManager;
import Management.Launcher;
import Management.RegexManager;
import Types.LangUserType;
import Types.LanguageType;
import Types.UserType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class ControllerLangMenu extends Controller implements Initializable  {

    ControllerLangMenu(String name, Controller previousController) {
        super(name,previousController);
    }

    private String selectedLanguage;
    private HashMap<String,LanguageType> languages;

    @FXML
    Text loggedUser;
    @FXML
    ListView<String> languageList;
    @FXML
    Button addLanguage;
    @FXML
    GridPane gridPane;
    @FXML
    GridPane gridPaneFactory;
    @FXML
    ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        gridPaneFactory.setVisible(false);
        loggedUser.setText(currentLogin + " is logged in.");
        currentUser = UserType.getUser(currentLoginDB);

        setList();
        setGrid();
        scrollPane.setMaxSize(650,520);
    }

    @FXML
    public void addLanguage(){
        ArrayList<Object> arguments = new ArrayList<>();
        arguments.add(currentLogin);
        arguments.add(selectedLanguage);
        try {
            LangUserType.insert(arguments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setList();
        setGrid();

    }

    private void setGrid(){
        GridManager gridManager = new GridManager(gridPaneFactory, gridPane, scrollPane, this);
        gridManager.adjustGridFilesView(currentLoginDB);
    }

    private void setList(){
        languages = LanguageType.toHashMap();
        List<String> tmp = new LinkedList<>();
        ArrayList<String> userLangs = LangUserType.getLangsForUserString(currentLoginDB);
        for(String s : languages.keySet())
            if(!userLangs.contains(s))
                tmp.add(s);
        tmp.sort(String::compareTo);
        languageList.setItems(FXCollections.observableList(tmp));
        addLanguage.setDisable(true);
        languageList.setOnMouseClicked(mouseEvent -> {
            if (languageList.getSelectionModel().getSelectedItems().size() == 0) return;
            if (languageList.getSelectionModel().getSelectedItems().get(0) != null) {
                selectedLanguage = languageList.getSelectionModel().getSelectedItems().get(0);
                addLanguage.setDisable(false);
            } else {
                addLanguage.setDisable(true);
            }
        });
    }

    public void openLanguage(LangUserType language){
        Controller.currentLanguage = languages.get(language.getLanguage());
        Controller.currentLanguageDB = RegexManager.convertIntoPreparedConsistent(currentLanguage.getLanguage());
        Controller.currentLangUser = language;



        try {
            Controller.stageMaster.loadNewScene(new ControllerLanguage(Launcher.scenesLocation + "language.fxml", this));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED TO LOAD language.fxml");
        }
    }

}
