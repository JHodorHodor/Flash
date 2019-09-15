package Controllers;

import Management.TimeManager;
import Types.StatsType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerFinish  extends Controller implements Initializable {

    ControllerFinish(String name, Controller previousController, StatsType statsType) {
        super(name, previousController);
        result = statsType;
    }

    private StatsType result;

    @FXML
    Text language;
    @FXML
    ImageView flag;
    @FXML
    Text ratio;
    @FXML
    Text percent;
    @FXML
    Text time;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLangImage(language, flag);

        ratio.setText(result.getGood() + "/" + result.getAall());
        percent.setText(result.getAall() > 0 ? result.getGood() * 100 / result.getAall() + "%" : "0");
        time.setText(TimeManager.convertTimeToReadable(result.getTtime()));
    }

}
