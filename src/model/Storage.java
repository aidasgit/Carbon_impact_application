package model;

import java.io.*;

public class Storage {
    private static final String filepath="media\\records.ser";
    // static method that returns object read from file
    // if file was created
    public static Object readFromFile(String fileName) {
        // creates object that points to nothing
        Object o = null;
        // try catch bloc
        try {
            // creating file stream object
            FileInputStream fileIn = new FileInputStream(fileName);
            // Creating objectinputstream object converts file data to object
            ObjectInputStream in = new ObjectInputStream(fileIn);
            // attempts to receive object from a file
            o = in.readObject();
            // closes streams
            in.close();
            fileIn.close();
        } catch (IOException i) {
            // in case error occurred
            // displays error msg
            System.out.println("Filename(" + fileName + ") not found");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // returns object received from the file or null otherwise
        return o;
    }

    // static method that writes object to the file
    public static void writeToFile(String filename, Object o) {
        // try catch block
        try {
            // creating file stream
            FileOutputStream fileOut = new FileOutputStream(filename);
            // creating object stream
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            // outputs object to file stream then file stream to file
            out.writeObject(o);
            // closes both streams
            out.close();
            fileOut.close();
            // displays msg if write was succesfull
            System.out.printf("Serialized data is saved in " + filename);
        } catch (IOException i) {
            // if error occurred while performing write operation
            // deals with the exception
            i.printStackTrace();
        }
    }
}
