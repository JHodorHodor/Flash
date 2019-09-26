package Controllers;

import Management.Launcher;
import Management.TimeManager;
import Types.LanguageType;
import Types.UserType;
import Types.WordUserType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ControllerLanguage extends Controller implements Initializable {

    ControllerLanguage(String name, Controller previousController) {
        super(name,previousController);
    }

    private Integer randomSuper;
    private Integer randomRegular;
    private Integer cardsNumber;
    private Integer transfer;

    @FXML
    Text language;
    @FXML
    ImageView flag;
    @FXML
    Slider slider;
    @FXML
    Text sliderText;
    @FXML
    Text sliderText2;
    @FXML
    Button start;
    @FXML
    GridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLangImage(language, flag);

        setUpCards();
        setUpGrid();
        setUpDone();
        setUpNumbers(currentUser.getWordsDaily());

        slider.setValue(currentUser.getWordsDaily());
        sliderText.setText(currentUser.getWordsDaily().toString());
        sliderText2.setText("(" + cardsNumber + ")");
        slider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            setUpNumbers(newValue.intValue());
            sliderText.setText(String.valueOf(newValue.intValue()));
            sliderText2.setText("(" + cardsNumber + ")");
        }));

        slider.setMax(allCards.get(0).size() + allCards.get(1).size());
        slider.setMin(allCards.get(1).size());

    }

    private void setUpCards(){
        personalInfo = WordUserType.toHashMap(currentLoginDB, currentLanguageDB);

        allCards = new LinkedList<>();
        for(int i = 0; i < 17; i++)
            allCards.add(new LinkedList<>());
        for(Integer id : personalInfo.keySet())
            allCards.get(personalInfo.get(id).getState()).add(id);


    }

    private void setUpGrid(){
        for(int i = 0; i < 17; i++){
            String id = Integer.valueOf(i).toString();
            Pane pane = new Pane();
            pane.setId(id);
            Label label = new Label(id);
            pane.getChildren().add(label);
            label.setStyle(
                    "-fx-font-family: \"C7nazara\";" +
                    "-fx-text-fill: #b3b3cc;" +
                    "-fx-font-size: 16px;");
            label.setTranslateX(5);
            label.setTranslateY(5);
            gridPane.add(pane, i, 1);
        }
        for(int i = 0; i < 17; i++){
            String id = Integer.valueOf(i).toString();
            String value = Integer.valueOf(allCards.get(i).size()).toString();
            Pane pane = new Pane();
            pane.setId(id);
            Label label = new Label(value);
            pane.getChildren().add(label);
            label.setStyle(
                    "-fx-font-family: \"C7nazara\";" +
                    "-fx-text-fill: #8585ad;" +
                    "-fx-font-size: 13px;");
            label.setTranslateX(5);
            label.setTranslateY(5);
            gridPane.add(pane, i, 2);
        }
    }

    private void setUpDone(){
        if(TimeManager.ifDone(currentLoginDB, currentLanguageDB)){
            start.setVisible(false);
            slider.setVisible(false);
            sliderText.setVisible(false);
            sliderText2.setVisible(false);
        } else {
            start.setVisible(true);
            slider.setVisible(true);
            sliderText.setVisible(true);
            sliderText2.setVisible(true);
        }
    }

    private void setUpNumbers(int value){
        cardsNumber = 0;
        for (int i = 0; i < 17; i++) {
            if ((i > 0 && i < 7) || i == 10 || i == 14) cardsNumber += allCards.get(i).size();
        }
        transfer = Integer.min(allCards.get(0).size() + allCards.get(1).size(), value) - allCards.get(1).size();
        randomSuper = Integer.min(allCards.get(16).size(), value / 15);
        randomRegular = Integer.min(allCards.get(15).size(),value / 7 - randomSuper);
        cardsNumber += (transfer + randomRegular + randomSuper);
    }

    @FXML
    public void start(){
        try {
            UserType.update(currentLoginDB, (int) slider.getValue());
            currentUser.setWordsDaily((int) slider.getValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            LinkedList<Integer> arguments = new LinkedList<>();
            arguments.add(randomSuper);
            arguments.add(randomRegular);
            arguments.add(cardsNumber);
            arguments.add(transfer);
            Controller.stageMaster.loadNewScene(new ControllerPractice(Launcher.scenesLocation + "practice.fxml", this, arguments));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED TO LOAD practice.fxml");
        }
    }

    @FXML
    public void importWords(){
        try {
            Controller.stageMaster.loadNewScene(new ControllerImport(Launcher.scenesLocation + "import.fxml", this));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED TO LOAD import.fxml");
        }
    }

    @FXML
    public void addWords(){
        try {
            Controller.stageMaster.loadNewScene(new ControllerAddWords(Launcher.scenesLocation + "addWords.fxml", this));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED TO LOAD addWords.fxml");
        }
    }

    @FXML
    public void delete(){
        try {
            invokePopUp("remove language " + currentLanguage.getLanguage() + "? " + "\n ---All progress will vanish!!!---");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteWords(){
        try {
            Controller.stageMaster.loadNewScene(new ControllerDelete(Launcher.scenesLocation + "delete.fxml", this));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED TO LOAD delete.fxml");
        }
    }

    @FXML
    public void stats(){
        try {
            Controller.stageMaster.loadNewScene(new ControllerStats(Launcher.scenesLocation + "stats.fxml", this));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED TO LOAD stats.fxml");
        }
    }

    void submitDelete(){
        try {
            LanguageType.delete(currentLoginDB, currentLanguageDB);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            goBack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
