package controller;

import gui.ActivityTab;
import gui.DatabaseGui;
import gui.SummaryTab;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import model.Activity;
import model.ActivityManager;
import model.Storage;

public class Controller implements EventHandler<ActionEvent> {
    //private instance variables
    private ActivityTab activityTab;
    private SummaryTab summary;
    private DatabaseGui data;
    private ActivityManager activityManager;
    private boolean total = false; //flag to track totals display or not
    private boolean sortByDate = false;  //flag to track sort by date or name

    public Controller() {
        //Initializing activity tab and activity manager
        activityTab = new ActivityTab(this);
        activityManager = new ActivityManager();
        //Initializing database tab pane and summary tab
        data = new DatabaseGui(this);
        summary = new SummaryTab(activityManager, this);
    }



    @Override
    public void handle(ActionEvent actionEvent) {
        String s;
        int cursorPos;
        if (actionEvent.getSource() == activityTab.getAdd()) {
            if (activityTab.isAllFieldsEntered()) {
                //creates new activity and ads it to the activity manager
                activityManager.addActivity(new Activity(activityTab.getActivity().getText(),
                        Integer.parseInt(activityTab.getWeek().getText().replaceAll("[^0-9]", "")),
                        Integer.parseInt(activityTab.getPoints().getText().replaceAll("[^\\d.-]", "")),
                        activityTab.getDate().getValue()));
                activityTab.clear();
            }

        } else if (actionEvent.getSource() == activityTab.getList()) {
            //no tatals will be displayed if list button is pressed
            total = false;
        } else if (actionEvent.getSource() == activityTab.getRemove()) {
            //stores cursor position
            cursorPos = activityTab.getActivityList().getCaretPosition();
            //saves string with data of all activities
            s = activityManager.display(false, "name");
            //checks if there is no activities and alerts a user
            if(activityManager.isEmpty()){
                activityTab.displayInfo("Nothing to remove, add some activities.");
            }else if (cursorPos / 79 == 0) { //check was there a valid activity selected
                activityTab.displayInfo("First left click on the activity you want to remove.");
            } else {
                //gets a activity id from the string and finds the activity with that id and removes it
                activityManager.removeActivity(activityManager.findActivity(
                        Integer.parseInt(s.substring(cursorPos / 79 * 79, cursorPos / 79 * 79 + 3)
                                .strip())));
            }
        } else if (actionEvent.getSource() == activityTab.getPremades()) {
            //if premade activity was selected updates correct txt fields
            Activity a = activityTab.getPremades().getValue();
            activityTab.getActivity().setText(a.getName());
            activityTab.getPoints().setText(Integer.toString(a.getPoint()));
        } else if (actionEvent.getSource() == activityTab.getSummary()) {
            if (!activityManager.isEmpty())
                //sets total flag to true to display totals
                total = true;
        } else if (actionEvent.getSource() == activityTab.getSave()) {
            save(); //saves current activities to file
            activityTab.displayInfo("Activities saved successfully.");
        } else if (actionEvent.getSource() == activityTab.getLoad()) {
            load(); // loads current activities to file
            if (activityManager.isEmpty()) {
                activityTab.displayInfo("No saved activities found.");
            } else {
                activityTab.displayInfo("Activities loaded successfully.");
            }
        } else if (actionEvent.getSource() == summary.getDate()) {
            if (!sortByDate) {
                //sets sortByDate flag to true in summaryTab to sort by date
                sortByDate = true;
            }
        } else if (actionEvent.getSource() == summary.getName()) {
            if (sortByDate)
                //sets sortByDate flag to false in summaryTab to sort by name
                sortByDate = false;
        } else if (actionEvent.getSource() == data.getImportBtn()){
            //if import button is pressed in database tab
            //new db will be created and new table and all activities from
            //activity tab will be saved to db
            if (data.isConnected()) {
                data.createTable();
                data.importActivities(activityManager);
            } else {
                data.displayError("Import could not be made, because no database found.");
            }
        } else if (actionEvent.getSource() == data.getConnect()){
            //attempts to connect to SQL database
            data.connect();

        }else if (actionEvent.getSource() == data.getDisconnectBtn()){
            data.disconnect(); //disconnects
        }else if (actionEvent.getSource() == data.getExportBtn()){
            //all the activities from the database base tab will be saved over activities in ActivityTab
            activityManager = data.getActivities();
            summary.setManager(data.getActivities());
        }else if (actionEvent.getSource() == data.getLoadBtn()){
            //if there was activities saved in SQL database it will load it to
            //activity manager on databaseTab where it can be exported to ActivityTab
            data.load();
        }
        //when ever any action was performed refreshes the activity lists
        refresh(total, "name");
        if (sortByDate)
            summary.sortByDate();
        else
            summary.sortByName();
    }

    //gets output of activities from activity manager
    public void refresh(Boolean total, String sort) {
        activityTab.getActivityList().setText(activityManager.display(total, sort));
    }
    //writes current activities to file
    private void save() {
        Storage.writeToFile("media\\records.ser", activityManager);
    }
    //loads activities from the file
    private void load() {
        ActivityManager a = (ActivityManager) Storage.readFromFile("media\\records.ser");
        if (a == null)
            return;
        //if data was found saves is to activityManager
        activityManager = a;
        //also provides link to the new object for summary tab
        summary.setManager(activityManager);
    }
//Getters
    public DatabaseGui getData() {
        return data;
    }

    public SummaryTab getSummary() {
        return summary;
    }

    public ActivityTab getActivityTab() {
        return activityTab;
    }

}
