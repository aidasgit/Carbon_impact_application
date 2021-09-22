package gui;
//Student name: Aidas Karpavicius
//Student no:   R00171054
//This is IntelliJ project
//For this app to work with SQL only active SQL server on default port is required
//default user name must be used in text field
//if it is changed you must enter user name and password accordingly.
//once connected to SQL server
//activities can be imported from activity tab and database will be created.

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //sets title
        primaryStage.setTitle("Final Assignment - Aidas Karpavicius");
        //initiating controller that will be responsible handling user inputs
        Controller c = new Controller();
        //saving handle to activity tab
        ActivityTab p = c.getActivityTab();
        //creating tab pane object
        TabPane tabs = new TabPane();
        //adding intro tab to the tabPane
        tabs.getTabs().add(new Tab("Intro", new IntroTab()));
        //adding activity, summary and database tab to the tabPane
        tabs.getTabs().add(new Tab("Activities", p));
        tabs.getTabs().add(new Tab("Summary", c.getSummary()));
        tabs.getTabs().add(new Tab("SQL Demonstration", c.getData()));
        //disabling tab ability to close
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        //setting tabs size and passing it to scene object and setting stage
        primaryStage.setScene(new Scene( tabs, 620, 475));
        //showing stage
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
