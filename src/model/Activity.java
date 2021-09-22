package model;

import java.io.Serializable;
import java.time.LocalDate;
//Activity class stores activity and can be compared to each other by name or date
public class Activity implements Comparable<Activity>, Serializable {
    private LocalDate date;
    private String name;
    private int point, week, id;
    private static int number = 0;
    private boolean sortByName, sortByDate;
    //constructor
    public Activity(String n, int week, int p, LocalDate d){
        this.date = d;
        this.week = week;
        this.name = n;
        this.point = p;
        this.sortByName = true;
        number++;
        id = number;

    }
    //overloaded constructor
    public Activity(String n, int p){
        this.name = n;
        this.point = p;
    }

    //setters and getters
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getWeek() {
        return week;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }

    public int getId() {
        return id;
    }
    //sets flags for sorting
    public void setSortByName(){
        sortByDate = false;
        sortByName = true;
    }
    public void setSortByDate(){
        sortByName = false;
        sortByDate = true;
    }
    //depending what flags where set it will be sorted by name or date
    @Override
    public int compareTo(Activity a){
        if (sortByDate)
            return this.getDate().compareTo(a.getDate());
        return this.getName().compareTo(a.getName());
    }
    @Override
    public String toString(){
        return name;
    }
    public String toOutput(){
        String output = String.format("%-5d%-15d%-30s%-20s%-8d\n", id, week, name, date, point);
        return output;
    }
}
