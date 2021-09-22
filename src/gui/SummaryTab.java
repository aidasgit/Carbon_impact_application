package gui;


import controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.ActivityManager;

public class SummaryTab extends Pane {
    private TextArea output;
    private Button date, name;
    private ActivityManager manager;
    private Controller controller;
    //constructor sets up simple gui
    public SummaryTab(ActivityManager a, Controller c){
        manager = a;
        controller = c;
        output = new TextArea();
        date = new Button("Sort by date");
        date.setOnAction(c);
        name = new Button( "Sort by activity");
        name.setOnAction(c);
        output.setPrefColumnCount(80);
        output.setPrefRowCount(25);
        output.setStyle("-fx-font-family: monospace");
        HBox buttons = new HBox(name, date);
        BorderPane main = new BorderPane();
        main.setTop(buttons);
        main.setPadding(new Insets(5,5,5,5));
        main.setCenter(output);
        this.getChildren().addAll(main);
    }

    public TextArea getOutput() {
        return output;
    }

    public Button getDate() {
        return date;
    }

    public Button getName() {
        return name;
    }
    //two methods that refreshes the output and changes the sorting
    public void sortByDate(){
        output.setText(manager.display(true, "date"));
    }
    public void sortByName(){
        output.setText(manager.display(true, "name"));
    }
    //setter for activity manager
    public void setManager(ActivityManager manager) {
        this.manager = manager;
    }
}
