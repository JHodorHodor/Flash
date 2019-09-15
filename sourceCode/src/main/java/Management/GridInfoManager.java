package Management;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GridInfoManager {

    public GridInfoManager(int x, int y, GridPane gridPane, boolean ifTopAlso){
        this.x = x;
        this.y = y;
        this.gridPane = gridPane;
        this.ifTopAlso = ifTopAlso;
    }

    private int x;
    private int y;
    private boolean ifTopAlso;
    private GridPane gridPane;

    public void setPane(String text, int i, int j){
        Pane pane = new Pane();
        pane.setId(text);
        Label label = new Label(text);
        pane.getChildren().add(label);
        label.setTranslateX(x);
        label.setTranslateY(y);
        if(i == 0 || (j == 0 && ifTopAlso))
            label.setStyle("-fx-font-size:15;" +
                    "-fx-font-weight:bold;" +
                    "-fx-text-alignment:center;" +
                    "-fx-font-family: \"vtks morning rain\";");
        else
            label.setStyle("-fx-font-size:17;" +
                    "-fx-text-fill:#000099;" +
                    "-fx-font-family: C7nazara;");
        gridPane.add(pane, i, j);
    }
}
