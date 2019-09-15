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
import java.util.*;
import java.util.List;

public class ControllerImport extends Controller implements Initializable {

    ControllerImport(String name, Controller previousController) {
        super(name, previousController);
    }

    private LinkedList<String> inFirst = new LinkedList<>();
    private LinkedList<String> inSecond = new LinkedList<>();
    private HashMap<String, WordType> wordsFromDatabase;
    private String selectedWordKeyFirst;
    private String selectedWordKeySecond;

    @FXML
    ListView<String> fromDatabase;
    @FXML
    ListView<String> toImport;
    @FXML
    Button add;
    @FXML
    Button addAll;
    @FXML
    Button remove;
    @FXML
    Button submit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wordsFromDatabase = WordType.toHashMap(currentLanguageDB);
        for(String s : wordsFromDatabase.keySet())
            if(!personalInfo.keySet().contains(wordsFromDatabase.get(s).getId())) {
                inFirst.add(s);
            }
        refreshLists();

        fromDatabase.setOnKeyPressed(key ->{
            KeyCode keyCode = key.getCode();
            if(keyCode.equals(KeyCode.ENTER) && fromDatabase.getSelectionModel().getSelectedItems().size() > 0)
                add();
        });
        toImport.setOnKeyPressed(key ->{
            KeyCode keyCode = key.getCode();
            if(keyCode.equals(KeyCode.ENTER) && toImport.getSelectionModel().getSelectedItems().size() > 0)
                remove();
        });
    }

    private void setListFirst(){
        List<String> tmp = new LinkedList<>();
        for(String s : inFirst)
            if(!inSecond.contains(s))
                tmp.add(s);
        addAll.setDisable(tmp.size() <= 0);
        fromDatabase.setItems(FXCollections.observableList(tmp));
        add.setDisable(true);
        fromDatabase.setOnMouseClicked(mouseEvent -> {
            if (fromDatabase.getSelectionModel().getSelectedItems().size() == 0) return;
            if (fromDatabase.getSelectionModel().getSelectedItems().get(0) != null) {
                selectedWordKeyFirst = fromDatabase.getSelectionModel().getSelectedItems().get(0);
                add.setDisable(false);
            } else {
                add.setDisable(true);
            }
        });
    }

    private void setListSecond(){
        List<String> tmp = new LinkedList<>();
        for(String s : inSecond)
            if(!inFirst.contains(s))
                tmp.add(s);
        toImport.setItems(FXCollections.observableList(tmp));
        remove.setDisable(true);
        toImport.setOnMouseClicked(mouseEvent -> {
            if (toImport.getSelectionModel().getSelectedItems().size() == 0) return;
            if (toImport.getSelectionModel().getSelectedItems().get(0) != null) {
                selectedWordKeySecond = toImport.getSelectionModel().getSelectedItems().get(0);
                remove.setDisable(false);
            } else {
                remove.setDisable(true);
            }
        });
    }

    @FXML
    public void add(){
        int position = inFirst.indexOf(selectedWordKeyFirst);
        inFirst.remove(selectedWordKeyFirst);
        inSecond.add(selectedWordKeyFirst);
        refreshLists();
        if(fromDatabase.getItems().size() > 0) {
            fromDatabase.getSelectionModel().select(Integer.min(inFirst.size() - 1, position));
            selectedWordKeyFirst = fromDatabase.getSelectionModel().getSelectedItems().get(0);
            add.setDisable(false);
        }
    }

    @FXML
    public void remove(){
        int position = inSecond.indexOf(selectedWordKeySecond);
        inSecond.remove(selectedWordKeySecond);
        inFirst.add(selectedWordKeySecond);
        refreshLists();
        if(toImport.getItems().size() > 0) {
            toImport.getSelectionModel().select(Integer.min(inSecond.size() - 1, position));
            selectedWordKeySecond = toImport.getSelectionModel().getSelectedItems().get(0);
            remove.setDisable(false);
        }
    }

    @FXML
    public void addAll(){
        inSecond.addAll(inFirst);
        inFirst = new LinkedList<>();
        refreshLists();
    }

    @FXML
    public void submit(){
        try {
            invokePopUp("add " + inSecond.size() + " words to your collection?");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void submitYes(){
        for(String key : inSecond){
            try {
                WordUserType.insert(wordsFromDatabase.get(key).getId(), currentLoginDB);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        inSecond = new LinkedList<>();
        refreshLists();
    }


    private void refreshLists(){
        setListFirst();
        setListSecond();
    }
}
