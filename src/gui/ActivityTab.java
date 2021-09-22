package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Activity;
import model.ActivityManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ActivityTab extends Pane {
    //instance attributes
    private DatePicker date;
    private TextField week, activity, points;
    private Button add, remove, list, summary, load, save;
    private TextArea activityList;
    private ActivityManager activities;
    private EventHandler<ActionEvent> handler;
    private ArrayList<Activity> preMadeActivities = new ArrayList<Activity>();
    private ComboBox<Activity> premades;
    //constructor
    public ActivityTab(EventHandler<ActionEvent> h) {
        //sets up event handler
        handler = h;
        //initiates all instance variables
        date = new DatePicker();
        week = new TextField();
        activity = new TextField();
        points = new TextField();
        //creating buttons and adding listener
        add = new Button("Add");
        add.setOnAction(handler);
        remove = new Button("Remove");
        remove.setOnAction(handler);
        list = new Button("List");
        list.setOnAction(handler);
        summary = new Button("Summary");
        summary.setOnAction(handler);
        load = new Button("Load");
        load.setOnAction(handler);
        save = new Button("Save");
        save.setOnAction(handler);
        //setting up textarea where all output will be displayed
        activityList = new TextArea("Activities to be Displayed");
        activityList.setStyle("-fx-font-family: monospace");
        activityList.setPrefColumnCount(80);
        activities = new ActivityManager();
        //loads premades activities
        getPremadeActivities();
        //finishes the rest of gui
        setup();
    }




    public void setup() {

        Label weekLabel = new Label("Week");
        Label dateLabel = new Label("Date");
        Label activityLabel = new Label("Activity");
        Label pointLabel = new Label("Points (-10 to 10)  ");

        HBox buttons = new HBox(add, remove, list, summary);
        buttons.setSpacing(5);
        GridPane input = new GridPane();
        input.add(weekLabel, 0, 0, 1, 1);
        input.add(week, 1, 0, 1, 1);
        input.add(dateLabel, 0, 1, 1, 1);
        input.add(date, 1, 1, 1, 1);
        input.add(activityLabel, 0, 2, 1, 1);
        input.add(activity, 1, 2, 1, 1);
        input.add(pointLabel, 0, 3, 1, 1);
        input.add(points, 1, 3, 1, 1);
        BorderPane upper = new BorderPane();
        upper.setLeft(new VBox(input, buttons));
        premades = new ComboBox<Activity>();
        for(Activity a: preMadeActivities){
            premades.getItems().add(a);
        }
        premades.setOnAction(handler);
        upper.setRight(new VBox(new Label("Pre-made Activities"), premades));

        BorderPane main = new BorderPane();
        main.setTop(upper);
        HBox storageButtons = new HBox(load, save);
        storageButtons.setSpacing(5);
        main.setBottom(new VBox(activityList, storageButtons));
        main.setPadding(new Insets(5,5,5,5));
        this.getChildren().add(main);
    }
    //****************Getters******************************
    public ComboBox<Activity> getPremades() {
        return premades;
    }

    public Button getAdd() {
        return add;
    }

    public Button getRemove() {
        return remove;
    }

    public Button getList() {
        return list;
    }

    public Button getSummary() {
        return summary;
    }

    public Button getLoad() {
        return load;
    }

    public Button getSave() {
        return save;
    }

    public TextArea getActivityList() {
        return activityList;
    }

    public DatePicker getDate() {
        return date;
    }

    public TextField getWeek() {
        return week;
    }

    public TextField getActivity() {
        return activity;
    }

    public TextField getPoints() {
        return points;
    }
    //*********************END OF GETTERS ************************************
    //checks if all text field entered and have a valid value
    public boolean isAllFieldsEntered() {
        boolean result = false;
        if (week.getText() == "") {
            displayError("Week field is empty, activity cannot be created ");
        } else if (date.getValue() == null) {
            displayError("Date field is empty, activity cannot be created ");
        } else if (activity.getText() == "") {
            displayError("Activity field is empty, activity cannot be created ");
        } else if (points.getText() == "") {
            displayError("Points field is empty, activity cannot be created ");
        } else if (week.getText().replaceAll("[^0-9]", "") == "") {
            //checks for  int  in a string "2 nd week", "2" would satisfy the condition
            displayError("Week must have a valid integer, activity cannot be created ");
        } else if (points.getText().replaceAll("[^0-9]", "") == "") {
            //makes sure that points are integer
            displayError("Points must have a valid integer, activity cannot be created ");
        } else if (Integer.parseInt(points.getText().replaceAll("[^0-9]", "")) < -10 ||
                Integer.parseInt(points.getText().replaceAll("[^0-9]", "")) > 10) {
            //also making sure that point is in range
            displayError("Points must have a valid integer in -10 to 10 range ");
        } else
            result = true;
        return result;
    }

    //displays error alert
    public void displayError(String s) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(s);
        a.show();
    }
    //displays info alert
    public void displayInfo(String s) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(s);
        a.show();
    }
    //loads premade activities from the file and stores it in local activity manager
    private void getPremadeActivities(){

        try {
            //Sets up a bufferreader linked to filereader
            BufferedReader reader = new BufferedReader( new FileReader( "media/activities.txt"));
            //line that will store string from the file
            String line = null;
            //temp string array that will store name and points
            String [] temp = new String[1];
            try{
                //attempts to read a line, until there is a line to read. Stores it in line variable
                while ((line = reader.readLine()) != null){
                    //splits line and adds it to temp array
                    temp = line.split(",");
                    //creates activity and stores it in local activity manager
                    preMadeActivities.add(new Activity(temp[0], Integer.parseInt(temp[1].strip())));
                }
            }catch(Exception e ){
                System.out.println(e);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    //clears all text fields
    public void clear(){
        week.setText("");
        date.setValue(null);
        activity.setText("");
        points.setText("");
    }
}
