package gui;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Activity;
import model.ActivityManager;
import model.DatabaseConnection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class DatabaseGui extends Pane {
    //Attributes
    private Button loadBtn, importBtn, exportBtn, connect, disconnect;
    private TextField name, password;
    private Label dbStatus; //label that indicates connection status
    private TextArea activityList;  //Textarea that show loaded activities
    private ActivityManager activities; //Local activity manager
    private EventHandler<ActionEvent> handler;
    private DatabaseConnection db;  //db connection manager
    private BorderPane topSection;
    private boolean connected = false; //flag to track connection status

    //constructor
    public DatabaseGui(EventHandler<ActionEvent> h) {
        handler = h;
        dbStatus = new Label("Status: Not Connected");
        name = new TextField();
        password = new TextField();
        Label nameLabel = new Label("User name:");
        Label pwdLabel = new Label("Password:");
        HBox nameBox = new HBox( nameLabel, name);
        nameBox.setSpacing(5);
        HBox pwdBox = new HBox(pwdLabel, password);
        pwdBox.setSpacing(12);
        topSection = new BorderPane();
        connect = new Button("Connect");
        connect.setOnAction(handler);
        disconnect = new Button("Disconnect");
        disconnect.setOnAction(handler);
        loadBtn = new Button("Load");
        loadBtn.setOnAction(handler);
        topSection.setLeft(new VBox(nameBox, pwdBox, new HBox(connect, disconnect)));
        topSection.setRight(dbStatus);
        importBtn = new Button("Import");
        importBtn.setOnAction(handler);
        exportBtn = new Button("Export");
        exportBtn.setOnAction(handler);
        activityList = new TextArea("Activities to be Displayed");
        activityList.setStyle("-fx-font-family: monospace");
        activityList.setPrefColumnCount(80);
        activities = new ActivityManager();
        db = new DatabaseConnection();
        setup();
    }

    //attempts to connect to database
    public void connect(){
        if (connected){
            displayInfo("Already connected to SQL Database.");
            return;
        }
        if (!db.makeConnection(name.getText(),password.getText())) {
            displayError("Connection to database was unsucessfull. Make sure SQL database is running.");
        }else{
            dbStatus.setText("Status: Connected.");
            connected = true;
        }
    }
    //Disconnects from database
    public void disconnect(){
        if(!connected){
            displayInfo("Not connected to SQL Database");
            return;
        }
        db.closeConnection();
        connected = false;
        dbStatus.setText("Status: Not Connected");
    }

    //Setup gui fro Database tab
    public void setup() {
        BorderPane upper = new BorderPane();
        upper.setTop(topSection);
        upper.setRight(dbStatus);

        BorderPane main = new BorderPane();
        main.setTop(upper);
        HBox storageButtons = new HBox(importBtn, exportBtn, loadBtn);
        storageButtons.setSpacing(5);
        main.setBottom(new VBox(activityList, storageButtons));
        main.setPadding(new Insets(5,5,5,5));
        this.getChildren().add(main);
    }
    //getters ****************

    public boolean isConnected() {
        return connected;
    }

    public Button getConnect() {
        return connect;
    }

    public Button getDisconnectBtn(){
        return disconnect;
    }

    public ActivityManager getActivities() {
        return activities;
    }

    public Button getLoadBtn() {
        return loadBtn;
    }

    public Button getImportBtn() {
        return importBtn;
    }

    public Button getExportBtn() {
        return exportBtn;
    }

    public TextArea getActivityList() {
        return activityList;
    }
    //end of  getters **************
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
    //this method will import activities from activity manager to SQL database
    public void importActivities(ActivityManager a){
        //part of query string
        String query = "INSERT INTO Activities (id, name, week, date, points) \n";
        //cycle through all activities in activity manager
        for (Activity activity : a.getActivities()){
            try{
                //adding values from activity to query string
                String temp = String.format("VALUES (%d, '%s', %s, '%s', %s);",
                        activity.getId(),activity.getName(), activity.getWeek(), activity.getDate(), activity.getPoint());
                //adding activity to DB
                db.executeUpdate(query + temp);
                //addingg activity to local activity manager
                activities.addActivity(activity);

            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        activityList.setText(activities.display( true, "name"));

    }
    //Creates database and the table overwrites all data if there was some from previous executions of the
    //program
    public void createTable(){
        try{
            //deleting db if there was one
            db.executeUpdate("DROP DATABASE IF EXISTS FinalAssignment");
            //creating new one with the same name
            db.executeUpdate("CREATE DATABASE FinalAssignment");
            //setting created db as the on to use
            db.executeUpdate("USE `FinalAssignment`;" );
            //creating table that will store the activities
            db.executeUpdate("CREATE TABLE Activities(" +
                    "id integer NOT NULL," +
                    "name varchar(80) NOT NULL," +
                    "week integer," +
                    "date DATE," +
                    "points integer);" );
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //This method loads activities from database to output textarea
    //if there is any activities saved in SQL Database.
    public void load(){
        //query output list of list of strings
        List<List<String>> l = null;
        //checks if database connected
        if (connected) {
            try {
                l = db.executeQueryForResults("SELECT * FROM activities;");
                System.out.println(l);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else{
            displayError("Not connected to SQL Database.");
        }
        //if there was an output from SQL server
        if (l != null){
            //processing each entry
            for(List<String> activity : l){
                //temp variable to store date segments
                ArrayList<Integer> date = new ArrayList<Integer>();
                //converting date segments to integers
                for(String s : activity.get(3).split("-")){
                    date.add(Integer.parseInt(s));
                }
                //creating activity object and adding them to activity manager
                activities.addActivity( new Activity(activity.get(1), Integer.parseInt(activity.get(2)),
                        Integer.parseInt(activity.get(4)), LocalDate.of(date.get(0),date.get(1), date.get(2))));
            }
        }
        //updating activityList with data loaded from SQL database
        activityList.setText(activities.display(true, "name"));
    }

}
