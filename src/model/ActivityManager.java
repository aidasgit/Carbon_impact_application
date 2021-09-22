package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ActivityManager implements Serializable {
    private ArrayList<Activity> activities;

    public ActivityManager(){
        activities = new ArrayList<Activity>();
    }

    public void addActivity(Activity a){
        activities.add(a);
    }

    public void removeActivity(Activity a){
        activities.remove(a);
    }

    public Activity findActivity(int number){
        if ( !activities.isEmpty())
            for(Activity a :activities){
                if (number == a.getId()){
                    return a;
                }
            }
        return null;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public boolean isEmpty(){
        return activities.isEmpty();
    }
    public String display(Boolean total, String sortBy){
        if(sortBy == "name") {
            for (Activity a : activities)
                a.setSortByName();
        }else {
            for (Activity a : activities)
                a.setSortByDate();
        }
        Collections.sort(activities);
        String output = String.format("%-5s%-15s%-30s%-20s%-8s\n","ID","Week", "Activity", "Date", "Points");
        if (!activities.isEmpty()) {
            for (Activity a : activities)
                output += a.toOutput();
            if (total) {
                output += String.format("%70s%-8d\n","Total: ", getTotal());
            }
            //System.out.println(output);
        }
        else
            output = "Activity list is Empty";
        return output;
    }
    public int getTotal(){
        int sum = 0;
        for(Activity a: activities){
            sum+=a.getPoint();
        }
        return sum;
    }


}
