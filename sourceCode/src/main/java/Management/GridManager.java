package Management;

import Controllers.ControllerLangMenu;
import Types.LangUserType;
import Types.LanguageType;
import Types.StatsType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;


public class GridManager {

    public GridManager(GridPane gridPaneFactory, GridPane gridPane, ScrollPane scrollPane, ControllerLangMenu controller) {
        this.gridPaneFactory = gridPaneFactory;
        this.gridPane = gridPane;
        this.scrollPane = scrollPane;
        this.controller = controller;
    }

    @FXML
    ControllerLangMenu controller;
    @FXML
    GridPane gridPaneFactory;
    @FXML
    GridPane gridPane;
    @FXML
    ScrollPane scrollPane;

    public void adjustGridFilesView(String loginDB){

        ArrayList<LangUserType> langs = LangUserType.getLangsForUser(loginDB);
        langs.sort((l1, l2) -> TimeManager.compareLanguages(l1,l2,loginDB));

        int width = 2;
        int numberOfItems = langs.size();
        int height = numberOfItems % width == 0 ? (numberOfItems / width) : (numberOfItems / width) + 1;

        gridPane = generateNewGridPane(width,height);
        scrollPane.setContent(gridPane);
        gridPane.setVisible(true);
        scrollPane.setVisible(true);

        String green =
                "-fx-background-color: rgba(0, 153, 51, 0.7);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color:transparent;" +
                        "-fx-text-fill:white;";
        String red =
                "-fx-background-color: rgba(255, 51, 0, 0.7);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color:transparent;" +
                        "-fx-text-fill:white;";

        int c = 0;
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (c < langs.size()) {
                    LangUserType item = langs.get(c);
                    String language = item.getLanguage();
                    String languageDB = RegexManager.convertIntoPreparedConsistent(language);
                    String last = TimeManager.toString(StatsType.getLast(loginDB, languageDB));

                    if(last == null) last = "-";
                    item.setLast(last);

                    Pane pane = new Pane(); //main cell body
                    pane.setId(language);
                    pane.resize(320,170);


                    //add lang name to cell
                    Label name = new Label(language);
                    name.setTranslateX(40);
                    name.setTranslateY(8);
                    name.setMaxWidth(pane.getWidth() - 40);
                    name.setStyle(
                            "-fx-background-color: transparent;" +
                            "-fx-border-color:transparent;" +
                            "-fx-text-fill: #ffffff;" +
                            "-fx-font-family: C7nazara;" +
                            "-fx-font-size:19");
                    pane.getChildren().add(name);


                    //add image
                    Label flag = new Label("");
                    Image image = RegexManager.setImage(LanguageType.toHashMap().get(language).getImage(), this);
                    flag.setTranslateX(30);
                    flag.setTranslateY(40);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(110);
                    imageView.setFitWidth(180);
                    flag.setGraphic(imageView);
                    pane.getChildren().add(flag);


                    //add streak
                    Pair<Integer,Integer> pair = TimeManager.computeDates(StatsType.getDates(loginDB, languageDB));
                    Label streak = new Label(pair.getKey().toString());
                    streak.setTranslateX(270);
                    streak.setTranslateY(30);
                    streak.setStyle(
                            "-fx-background-color: transparent;" +
                                    "-fx-border-color:transparent;" +
                                    "-fx-text-fill: #ffff00;" +
                                    "-fx-font-family: \"vtks morning rain\";" +
                                    "-fx-font-size:35");
                    pane.getChildren().add(streak);

                    //add image (streak)
                    Label streakImage = new Label("");
                    Image image2 = RegexManager.setImage("streak", this);
                    streakImage.setTranslateX(230);
                    streakImage.setTranslateY(30);
                    ImageView imageView2 = new ImageView(image2);
                    imageView2.setFitHeight(45);
                    imageView2.setFitWidth(35);
                    streakImage.setGraphic(imageView2);
                    pane.getChildren().add(streakImage);


                    //add days
                    Label days = new Label(pair.getValue().toString());
                    days.setTranslateX(260);
                    days.setTranslateY(100);
                    days.setStyle(
                            "-fx-background-color: transparent;" +
                                    "-fx-border-color:transparent;" +
                                    "-fx-text-fill: #cccc00;" +
                                    "-fx-font-family: \"vtks morning rain\";" +
                                    "-fx-font-size:22");
                    pane.getChildren().add(days);


                    //add done
                    if(TimeManager.ifDone(loginDB, languageDB))
                        pane.setStyle(green);
                    else
                        pane.setStyle(red);


                    pane.setOnMouseClicked(event -> controller.openLanguage(item));

                    gridPane.add(pane, j, i);
                    c++;
                }
            }
        }
        gridPane.setLayoutX(30);
        gridPane.setLayoutY(30);
        /*gridPane.setStyle(
                        "    -fx-background-color: transparent;" +
                        "    -fx-padding: 10;"+
                                "-fx-background-radius: 30;"
        );*/

    }

    private GridPane generateNewGridPane(int width, int height){

        double prefHeight = height * 185; //globalne
        double prefWidth = gridPaneFactory.getPrefWidth() * ((double)width / 2); //globalne


        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(gridPaneFactory.getLayoutX());
        gridPane.setLayoutY(gridPaneFactory.getLayoutY());
        gridPane.setOnMouseClicked(gridPaneFactory.getOnMouseClicked());
        gridPane.setPrefHeight(prefHeight);
        gridPane.setPrefWidth(prefWidth);

        LinkedList<ColumnConstraints> columnConstraints = new LinkedList<>();
        for(int i = 0; i < width; i++)
            columnConstraints.add(gridPaneFactory.getColumnConstraints().get(0));
        gridPane.getColumnConstraints().addAll(columnConstraints);

        LinkedList<RowConstraints> rowConstraints = new LinkedList<>();
        for(int i = 0; i < height; i++)
            rowConstraints.add(gridPaneFactory.getRowConstraints().get(0));
        gridPane.getRowConstraints().addAll(rowConstraints);

        gridPane.setVisible(true);
        gridPane.setHgap(25); //globalne
        gridPane.setVgap(25); //globalne
        return gridPane;
    }

}
