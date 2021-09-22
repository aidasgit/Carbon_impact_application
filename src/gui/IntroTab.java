package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;



public class IntroTab extends Pane {
    public IntroTab(){
        //simple gui interface just to display introduction to the app
        Label title = new Label("Carbon Impact App");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 25");
        TextArea description = new TextArea("Carbon emissions is a real danger for society today. We all must " +
                "take a responsibility and lower our carbon pollution. This app allows you to track and store your " +
                "activities that has impact on carbon emissions.\n" +
                "Functionality:\n" +
                "Add new activity and provide your own score\n" +
                "Add pre-made activity\n" +
                "Remove activity\n" +
                "Get total score\n" +
                "Modify 'activities.txt' in media folder to create new pre-made activities\n" +
                "Sort activities by name or date\n" +
                "Save or load to SQL Database.");
        description.setWrapText(true);
        description.setEditable(false);
        description.setPrefColumnCount(30);
        description.setPrefWidth(610);
        description.setFont(new Font("Ariel", 14));
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(5,5,5,5));
        bPane.setTop(title);
        bPane.setCenter(description);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(description, Pos.CENTER);
        this.getChildren().addAll(bPane);
    }
}
