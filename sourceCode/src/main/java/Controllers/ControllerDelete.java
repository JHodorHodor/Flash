package Controllers;

import Types.WordType;
import Types.WordUserType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ControllerDelete extends Controller implements Initializable {

    ControllerDelete(String name, Controller previousController) {
        super(name, previousController);
    }

    private LinkedList<String> backList = new LinkedList<>();
    private HashMap<String, WordType> wordsFromDatabase;
    private HashMap<Integer, WordUserType> markedWords;
    private String selectedWordKey;

    @FXML
    ListView<String> list;
    @FXML
    Button unmark;
    @FXML
    Button submit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        markedWords = WordUserType.toHashMaptoDelete(currentLoginDB, currentLanguageDB);
        wordsFromDatabase = WordType.toHashMap(currentLanguageDB);
        for(String s : wordsFromDatabase.keySet())
            if(markedWords.keySet().contains(wordsFromDatabase.get(s).getId())) {
                backList.add(s);
            }
        setList();

        list.setOnKeyPressed(key ->{
            KeyCode keyCode = key.getCode();
            if(keyCode.equals(KeyCode.ENTER) && list.getSelectionModel().getSelectedItems().size() > 0)
                unmark();
        });
    }

    private void setList(){
        list.setItems(FXCollections.observableList(backList));
        unmark.setDisable(true);
        list.setOnMouseClicked(mouseEvent -> {
            if (list.getSelectionModel().getSelectedItems().size() == 0) return;
            if (list.getSelectionModel().getSelectedItems().get(0) != null) {
                selectedWordKey = list.getSelectionModel().getSelectedItems().get(0);
                unmark.setDisable(false);
            } else {
                unmark.setDisable(true);
            }
        });
    }

    @FXML
    public void unmark(){
        int position = backList.indexOf(selectedWordKey);
        backList.remove(selectedWordKey);
        Integer id = wordsFromDatabase.get(selectedWordKey).getId();
        WordUserType toUpdate = markedWords.get(id);
        toUpdate.setToDelete(false);
        try {
            WordUserType.update(currentLoginDB, id, toUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setList();
        if(list.getItems().size() > 0) {
            list.getSelectionModel().select(position);
            selectedWordKey = list.getSelectionModel().getSelectedItems().get(0);
            unmark.setDisable(false);
        }
    }

    @FXML
    public void submit(){
        try {
            invokePopUp("delete " + backList.size() + " words from your collection?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void submitYes(){
        for(String key : backList){
            Integer id = wordsFromDatabase.get(key).getId();
            try {
                WordUserType.delete(currentLoginDB, id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        backList = new LinkedList<>();
        setList();
    }


}
