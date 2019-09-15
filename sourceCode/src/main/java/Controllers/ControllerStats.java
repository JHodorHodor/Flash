package Controllers;

import Management.GridInfoManager;
import Management.TimeManager;
import Types.StatsType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerStats extends Controller implements Initializable {

    ControllerStats(String name, Controller previousController) {
        super(name, previousController);
    }

    @FXML
    Text language;
    @FXML
    ImageView flag;
    @FXML
    GridPane gridInfo1;
    @FXML
    GridPane gridInfo2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLangImage(language, flag);
        setUpGridInfo1();
        setUpGridInfo2();
    }



    private void setUpGridInfo1(){
        GridInfoManager gridInfoManager = new GridInfoManager(10,10, gridInfo1, false);

        gridInfoManager.setPane("Start of The Course:",0,0);
        gridInfoManager.setPane("Days of Practice:", 0, 1);
        gridInfoManager.setPane("Last Practice:", 0, 2);
        gridInfoManager.setPane("Current Streak:", 0, 3);
        gridInfoManager.setPane("Learnt Words:", 0, 4);

        Pair<Integer, Integer> pair = TimeManager.computeDates(StatsType.getDates(currentLoginDB, currentLanguageDB));
        gridInfoManager.setPane(currentLangUser.getStart() + " (" + TimeManager.daysAgo(TimeManager.convertToDate(currentLangUser.getStart())) + ")", 1,  0);
        gridInfoManager.setPane(String.valueOf(pair.getValue()), 1, 1);
        gridInfoManager.setPane(currentLangUser.getLast(), 1, 2);
        gridInfoManager.setPane(pair.getKey().toString(), 1, 3);
        gridInfoManager.setPane(String.valueOf(allCards.get(15).size() + allCards.get(16).size()), 1, 4);
    }

    private void setUpGridInfo2(){
        GridInfoManager gridInfoManager = new GridInfoManager(10,10, gridInfo2, true);

        gridInfoManager.setPane("Frequency", 1, 0);
        gridInfoManager.setPane("Correct", 2, 0);
        gridInfoManager.setPane("Words\nPracticed", 3, 0);
        gridInfoManager.setPane("Words\nPracticed\nDaily", 4, 0);
        gridInfoManager.setPane("Time\nSpent", 5, 0);
        gridInfoManager.setPane("Time\nSpent\nDaily", 6, 0);

        gridInfoManager.setPane("All Time:", 0, 1);
        gridInfoManager.setPane("Last Year:", 0, 2);
        gridInfoManager.setPane("Last Month:", 0, 3);
        gridInfoManager.setPane("Last Week:", 0, 4);

        int len = TimeManager.daysAgo(TimeManager.convertToDate(currentLangUser.getStart()));
        int a = len;
        int b = Integer.min(365, len);
        int c = Integer.min(30, len);
        int d = Integer.min(7, len);

        gridInfoManager.setPane(TimeManager.getFrequency(currentLoginDB, currentLanguageDB, a) + "%", 1, 1);
        gridInfoManager.setPane(TimeManager.getFrequency(currentLoginDB, currentLanguageDB, b) + "%", 1, 2);
        gridInfoManager.setPane(TimeManager.getFrequency(currentLoginDB, currentLanguageDB, c) + "%", 1, 3);
        gridInfoManager.setPane(TimeManager.getFrequency(currentLoginDB, currentLanguageDB, d) + "%", 1, 4);

        gridInfoManager.setPane(TimeManager.getCorrectness(currentLoginDB, currentLanguageDB, a) + "%", 2, 1);
        gridInfoManager.setPane(TimeManager.getCorrectness(currentLoginDB, currentLanguageDB, b) + "%", 2, 2);
        gridInfoManager.setPane(TimeManager.getCorrectness(currentLoginDB, currentLanguageDB, c) + "%", 2, 3);
        gridInfoManager.setPane(TimeManager.getCorrectness(currentLoginDB, currentLanguageDB, d) + "%", 2, 4);

        gridInfoManager.setPane(String.valueOf(TimeManager.computeCorrectness(currentLoginDB, currentLanguageDB, a).getValue()), 3, 1);
        gridInfoManager.setPane(String.valueOf(TimeManager.computeCorrectness(currentLoginDB, currentLanguageDB, b).getValue()), 3, 2);
        gridInfoManager.setPane(String.valueOf(TimeManager.computeCorrectness(currentLoginDB, currentLanguageDB, c).getValue()), 3, 3);
        gridInfoManager.setPane(String.valueOf(TimeManager.computeCorrectness(currentLoginDB, currentLanguageDB, d).getValue()), 3, 4);

        gridInfoManager.setPane(String.valueOf(TimeManager.getCorrectnessDaily(currentLoginDB, currentLanguageDB, a)), 4, 1);
        gridInfoManager.setPane(String.valueOf(TimeManager.getCorrectnessDaily(currentLoginDB, currentLanguageDB, b)), 4, 2);
        gridInfoManager.setPane(String.valueOf(TimeManager.getCorrectnessDaily(currentLoginDB, currentLanguageDB, c)), 4, 3);
        gridInfoManager.setPane(String.valueOf(TimeManager.getCorrectnessDaily(currentLoginDB, currentLanguageDB, d)), 4, 4);

        gridInfoManager.setPane(TimeManager.convertTimeToReadable(TimeManager.getTtime(currentLoginDB, currentLanguageDB, a)), 5, 1);
        gridInfoManager.setPane(TimeManager.convertTimeToReadable(TimeManager.getTtime(currentLoginDB, currentLanguageDB, b)), 5, 2);
        gridInfoManager.setPane(TimeManager.convertTimeToReadable(TimeManager.getTtime(currentLoginDB, currentLanguageDB, c)), 5, 3);
        gridInfoManager.setPane(TimeManager.convertTimeToReadable(TimeManager.getTtime(currentLoginDB, currentLanguageDB, d)), 5, 4);

        gridInfoManager.setPane(TimeManager.convertTimeToReadable(TimeManager.getTtimeDaily(currentLoginDB, currentLanguageDB, a)), 6, 1);
        gridInfoManager.setPane(TimeManager.convertTimeToReadable(TimeManager.getTtimeDaily(currentLoginDB, currentLanguageDB, b)), 6, 2);
        gridInfoManager.setPane(TimeManager.convertTimeToReadable(TimeManager.getTtimeDaily(currentLoginDB, currentLanguageDB, c)), 6, 3);
        gridInfoManager.setPane(TimeManager.convertTimeToReadable(TimeManager.getTtimeDaily(currentLoginDB, currentLanguageDB, d)), 6, 4);
    }

}
