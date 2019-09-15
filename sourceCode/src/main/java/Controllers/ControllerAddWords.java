package Controllers;

import Management.RegexManager;
import Types.WordType;
import Types.WordUserType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ControllerAddWords extends Controller implements Initializable {

    ControllerAddWords(String name, Controller previousController) {
        super(name, previousController);
    }

    @FXML
    TextField original;
    @FXML
    TextField polish;
    @FXML
    TextArea textArea;
    @FXML
    Button submitOBO;
    @FXML
    Button submitC;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpKeyboard();
    }

    @FXML
    public void submitOBO(){
        try {
            WordType.insert(currentLanguageDB, original.getText(), polish.getText());
            WordUserType.insert(WordType.getIdNew(currentLanguageDB, original.getText(), polish.getText()), currentLoginDB);
        } catch (SQLException e) {
            System.out.println("Duplicate?");
        } finally {
            original.setText("");
            polish.setText("");
        }
    }

    @FXML
    public void submitC(){
        Pair<LinkedList<String>, LinkedList<String>> pair = RegexManager.separateWords(textArea.getText());
        for(int i = 0; i < pair.getValue().size(); i++){
            try {
                WordType.insert(currentLanguageDB, pair.getKey().get(i), pair.getValue().get(i));
                WordUserType.insert(WordType.getIdNew(currentLanguageDB, pair.getKey().get(i), pair.getValue().get(i)), currentLoginDB);
            } catch (SQLException e) {
                System.out.println("Duplicate? (" + pair.getKey() + " | " + pair.getValue() + ")");
            }
        }
        textArea.setText("");
    }


// KEYBOARD

    @FXML
    Button shift;
    @FXML
    Button special1;
    @FXML
    Button special2;
    @FXML
    Button special3;
    @FXML
    Button special4;
    @FXML
    Button special5;
    @FXML
    Button special6;
    @FXML
    Button special7;
    @FXML
    Button special8;
    @FXML
    Button special9;

    private LinkedList<Button> buttons = new LinkedList<>();
    private LinkedList<Object> keys = new LinkedList<>();
    private LinkedList<String> small;
    private LinkedList<String> great;
    private boolean isGreat;

    private void prepareKeys(){
        buttons.add(special1);
        buttons.add(special2);
        buttons.add(special3);
        buttons.add(special4);
        buttons.add(special5);
        buttons.add(special6);
        buttons.add(special7);
        buttons.add(special8);
        buttons.add(special9);
        for(Button b : buttons) b.setVisible(false);

        keys.add(KeyCode.DIGIT1);
        keys.add(KeyCode.DIGIT2);
        keys.add(KeyCode.DIGIT3);
        keys.add(KeyCode.DIGIT4);
        keys.add(KeyCode.DIGIT5);
        keys.add(KeyCode.DIGIT6);
        keys.add(KeyCode.DIGIT7);
        keys.add(KeyCode.DIGIT8);
        keys.add(KeyCode.DIGIT9);

        small = RegexManager.getSymbols(currentLanguage.getSpecialSymbols(), currentLanguage.getQuantity()).getKey();
        great = RegexManager.getSymbols(currentLanguage.getSpecialSymbols(), currentLanguage.getQuantity()).getValue();
        isGreat = false;
    }

    private void setUpKeyboard(){
        prepareKeys();

        for(int i = 0; i < currentLanguage.getQuantity(); i++){
            Button button = buttons.get(i);
            button.setVisible(true);
            button.setText(small.get(i));
            button.setOnAction(event -> {
                original.setText(original.getText() + button.getText());
                if(isGreat) shift();
                selectAnswer2();
            });

        }

        if(currentLanguage.getQuantity() == 0)
            shift.setVisible(false);
        else
            shift.setVisible(true);

        original.setOnKeyReleased(key -> {
            KeyCode keyCode = key.getCode();
            for(int i = 0; i < currentLanguage.getQuantity(); i++) {
                if (keyCode.equals(keys.get(i)) && !original.isDisabled()) {
                    original.setText(original.getText(0, original.getText().length() - 1));
                    original.setText(original.getText() + buttons.get(i).getText());
                    selectAnswer();
                    if (isGreat) shift();
                }
            }
            if(keyCode.equals(KeyCode.DIGIT0) && !original.isDisabled()){
                original.setText(original.getText(0, original.getText().length() - 1));
                shift();
                selectAnswer();
            }
        });
    }

    @FXML
    public void shift(){
        isGreat = !isGreat;
        for(int i = 0; i < currentLanguage.getQuantity(); i++)
            buttons.get(i).setText(isGreat ? great.get(i) : small.get(i));
    }


    private void selectAnswer(){
        original.requestFocus();
        original.selectEnd();
        original.deselect();
    }

    private void selectAnswer2(){
        original.requestFocus();
        original.selectEnd();
    }

}


