package Controllers;

import Management.*;
import Types.StatsType;
import Types.WordType;
import Types.WordUserType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ControllerPractice extends Controller implements Initializable {

    private int cardsNumber;
    private int transfer;
    private int randomSuper;
    private int randomRegular;

    private int counter = 0;
    private int goodC = 0;
    private int badC = 0;

    private int currentWord;
    private String firstWord;
    private String secondWord;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private boolean result;
    private long startTime;
    private long endTime;

    ControllerPractice(String name, Controller previousController, LinkedList<Integer> arguments) {
        super(name, previousController);
        randomSuper = arguments.get(0);
        randomRegular = arguments.get(1);
        cardsNumber = arguments.get(2);
        transfer = arguments.get(3);
    }

    @FXML
    AnchorPane mainPane;
    @FXML
    Text language;
    @FXML
    ImageView flag;

    @FXML
    Text first;
    @FXML
    Text second;
    @FXML
    TextArea background;
    @FXML
    TextField answer;
    @FXML
    Button submit;
    @FXML
    ImageView outcome;
    @FXML
    Button good;
    @FXML
    Button bad;

    @FXML
    ToggleButton toDelete;

    @FXML
    Text progress;
    @FXML
    Text badCounter;
    @FXML
    Text goodCounter;
    @FXML
    Text percent;

    @FXML
    Button done;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Launcher.words = WordType.toHashMap();
        mainPane.setOnKeyReleased(key -> {
            KeyCode keyCode = key.getCode();
            if (keyCode.equals(KeyCode.DIGIT1) && good.isVisible())
                checkGood();
            else if (keyCode.equals(KeyCode.DIGIT2) && good.isVisible())
                checkBad();
            //else if (keyCode.equals(KeyCode.) && submit.isVisible())
            //    submit();
        });
        setUpKeyboard();
        setUpHelps();

        outcome.setVisible(false);
        done.setVisible(false);
        language.setText(currentLanguage.getLanguage());
        flag.setImage(setImage(currentLanguage.getImage()));

        preparePractice();
        progress.setText("1/" + cardsNumber);

        startTime = System.currentTimeMillis();
        Thread thread = new Thread(this::startPractice);
        thread.start();

    }


    private void preparePractice() {
        Collections.shuffle(allCards.get(0));
        for (int i = 0; i < transfer; i++) {
            Integer id = allCards.get(0).getLast();
            allCards.get(1).add(id);
            allCards.get(0).removeLast();
            personalInfo.get(id).setState(1);
        }
    }

    private void startPractice(){
        practiceRegularCard(1, 1, allCards.get(1));
        practiceRegularCard(2, 2, allCards.get(2));
        practiceRegularCard(3, 1, allCards.get(3));
        practiceRegularCard(4, 2, allCards.get(4));
        practiceRegularCard(5, 1, allCards.get(5));
        practiceRegularCard(6, 2, allCards.get(6));
        practiceOmitCard(7);
        practiceOmitCard(8);
        practiceOmitCard(9);
        practiceRegularCard(10, 2, allCards.get(10));
        practiceOmitCard(11);
        practiceOmitCard(12);
        practiceOmitCard(13);

        practiceRegularCard(14, 1, allCards.get(14));

        Collections.shuffle(allCards.get(15));
        LinkedList<Integer> new15 = new LinkedList<>();
        for(int i = 0; i < randomRegular; i++)
            new15.add(allCards.get(15).get(i));

        practiceRegularCard(15, 0, new15);

        Collections.shuffle(allCards.get(16));
        LinkedList<Integer> new16 = new LinkedList<>();
        for(int i = 0; i < randomSuper; i++)
            new16.add(allCards.get(16).get(i));

        practiceRegularCard(16, 0, new16);


        endTime = System.currentTimeMillis();
        done.setVisible(true);

        setInvisible();
        setInvisibleKeyboard();

    }

    private void practiceRegularCard(int state, int mode, LinkedList<Integer> list) { //1 pol->orig, 2 orig->pol, 0 random
        Collections.shuffle(list);
        for (Integer id : list) {
            answer.setText("");
            currentWord = id;
            WordType word = Launcher.words.get(id);
            WordUserType info = personalInfo.get(id);

            int tmp = mode > 0 ? mode : new Random().nextInt() % 2 + 1;
            if (tmp == 1) {
                firstWord = word.getPolish();
                secondWord = word.getOriginal();
            } else if (tmp == 2) {
                secondWord = word.getPolish();
                firstWord = word.getOriginal();
            }

            first.setText(firstWord);
            second.setText(secondWord);

            submit.setVisible(true);
            second.setVisible(false);
            good.setVisible(false);
            bad.setVisible(false);
            toDelete.setSelected(info.isToDelete());

            waitForSignal(); //wait for submit

            waitForSignal(); //wait for good/bad

            setProgress();
            outcome.setVisible(false);
            if(result && mode != 0) info.setState(state + 1);
            if(result && mode == 0 && info.getState() == 15){
                String history = info.getHistory();
                if(history.length() >= 8)
                    if(history.substring(history.length() - 8).equals("00000000"))
                        info.setState(16);
            }
            if(!result){
                if(state == 10)
                    info.setState(2);
                else if(state == 14)
                    info.setState(3);
                else if(state > 14)
                    info.setState(3);
            }
            String old = info.getHistory() == null ? "" : info.getHistory();
            if(tmp == 1) {
                if (result)
                    info.setHistory(old + '1');
                else
                    info.setHistory(old + '0');
            } else {
                if (result)
                    info.setHistory(old + '3');
                else
                    info.setHistory(old + '2');
            }

        }
    }

    private void practiceOmitCard(int state){
        for (Integer id : allCards.get(state)) {
            WordUserType info = personalInfo.get(id);
            info.setState(state + 1);
        }
    }


    private void setProgress() {
        progress.setText(++counter + 1 + "/" + cardsNumber);
        if(result)
            goodC++;
        else
            badC++;
        goodCounter.setText(String.valueOf(goodC));
        badCounter.setText(String.valueOf(badC));
        percent.setText(100 * goodC / counter + "%");
        //if(counter + 1 == cardsNumber) ---> end
    }


    private void waitForSignal(){
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }

    private void signalReceived(){
        lock.lock();
        condition.signal();
        lock.unlock();
    }


    @FXML
    public void submit(){
        outcome.setVisible(true);
        second.setVisible(true);
        good.setVisible(true);
        bad.setVisible(true);
        submit.setVisible(false);
        answer.setDisable(true);

        if(secondWord.equals(answer.getText())){
            outcome.setImage(setImage("good"));
            bad.setVisible(false);
        } else {
            outcome.setImage(setImage("bad"));
        }

        signalReceived();
    }

    @FXML
    public void checkGood(){
        answer.setDisable(false);
        selectAnswer2();
        result = true;
        signalReceived();
    }

    @FXML
    public void checkBad(){
        answer.setDisable(false);
        selectAnswer2();
        result = false;
        signalReceived();
    }


    @FXML
    public void finish(){
        for(int i = 1; i < 17; i++){
            for(Integer id : allCards.get(i)) {
                try {
                    WordUserType.update(currentLoginDB, id, personalInfo.get(id));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        Long time = (endTime - startTime) / 1000;
        StatsType statsType = new StatsType(currentLoginDB, null, currentLanguageDB, goodC, counter, time.intValue());
        try {
            StatsType.insert(statsType);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Controller.stageMaster.loadNewScene(new ControllerFinish(Launcher.scenesLocation + "finish.fxml", this.getPreviousController(), statsType));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED TO LOAD finish.fxml");
        }
    }

    @FXML
    public void toDelete(){
        personalInfo.get(currentWord).setToDelete(toDelete.isSelected());
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
                answer.setText(answer.getText() + button.getText());
                if(isGreat) shift();
                selectAnswer2();
            });

        }

        if(currentLanguage.getQuantity() == 0)
            shift.setVisible(false);
        else
            shift.setVisible(true);

        answer.setOnKeyReleased(key -> {
            KeyCode keyCode = key.getCode();
            for(int i = 0; i < currentLanguage.getQuantity(); i++) {
                if (keyCode.equals(keys.get(i)) && !answer.isDisabled()) {
                    answer.setText(answer.getText(0, answer.getText().length() - 1));
                    answer.setText(answer.getText() + buttons.get(i).getText());
                    selectAnswer();
                    if (isGreat) shift();
                }
            }
            if(keyCode.equals(KeyCode.DIGIT0) && !answer.isDisabled()){
                answer.setText(answer.getText(0, answer.getText().length() - 1));
                shift();
                selectAnswer();
            } else if(keyCode.equals(KeyCode.ENTER))
                submit();

        });
    }

    @FXML
    public void shift(){
        isGreat = !isGreat;
        for(int i = 0; i < currentLanguage.getQuantity(); i++)
            buttons.get(i).setText(isGreat ? great.get(i) : small.get(i));
    }


    @FXML
    ToggleButton help;
    @FXML
    Text help0;
    @FXML
    Text help1;
    @FXML
    Text help2;
    @FXML
    Text help3;
    @FXML
    Text help4;
    @FXML
    Text help5;
    @FXML
    Text help6;
    @FXML
    Text help7;
    @FXML
    Text help8;
    @FXML
    Text help9;
    @FXML
    Text help10;
    @FXML
    Text help11;
    @FXML
    Text help12;

    private LinkedList<Text> helps;

    private void prepareHelps(){
        helps = new LinkedList<>();
        helps.add(help0);
        helps.add(help1);
        helps.add(help2);
        helps.add(help3);
        helps.add(help4);
        helps.add(help5);
        helps.add(help6);
        helps.add(help7);
        helps.add(help8);
        helps.add(help9);
        helps.add(help10);
        helps.add(help11);
        helps.add(help12);
    }

    private void setUpHelps(){
        prepareHelps();
        help.setSelected(false);
        visibilityOfHelps(false);
    }

    private void visibilityOfHelps(boolean b){
        for(Text t : helps)
            t.setVisible(b);
    }

    @FXML
    public void help(){
        visibilityOfHelps(!help0.isVisible());
    }



    private void selectAnswer(){
        answer.requestFocus();
        answer.selectEnd();
        answer.deselect();
    }

    private void selectAnswer2(){
        answer.requestFocus();
        answer.selectEnd();
    }

    private void setInvisible(){
        first.setVisible(false);
        second.setVisible(false);
        background.setVisible(false);
        answer.setVisible(false);
        submit.setVisible(false);
        good.setVisible(false);
        bad.setVisible(false);
        goodCounter.setVisible(false);
        badCounter.setVisible(false);
        percent.setVisible(false);
        progress.setVisible(false);
        toDelete.setVisible(false);
        setInvisibleHelps();
    }

    private void setInvisibleKeyboard(){
        shift.setVisible(false);
        for(int i = 0; i < currentLanguage.getQuantity(); i++)
            buttons.get(i).setVisible(false);
    }

    private void setInvisibleHelps(){
        help.setVisible(false);
        for(Text t : helps)
            t.setVisible(false);
    }



}

