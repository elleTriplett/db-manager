import TableData.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class provides the information for all the tables in the database, including what information they contain
 * and how to update them
 */
public class DatabaseManager {
    private static final String PORT_NUMBER = "3307";
    private static final String DATABASE_NAME = "ResourceData";
    private static final String RESOURCE_TABLE_NAME = "Resources";
    private static final String CITY_TABLE_NAME = "Cities";
    private static final String TYPE_TABLE_NAME = "Types";
    private static final String LANGUAGE_TABLE_NAME = "Languages";
    private static final String SERVING_TABLE_NAME = "Serving";
    private static final String PRIMARY_KEY = "resourceKey";
    private static final String RESOURCE_FILE = "TSP Resources";
    private static final ArrayList<String> RESOURCE_CATEGORIES = new ArrayList<>(Arrays.asList(
            PRIMARY_KEY, "resourceName", "organization", "description", "phoneNumber", "website","address",
            "hours", "offersTransportation","hasRequirements", "requirements", "dateUpdated" ));
    private static final ArrayList<String> RESOURCE_CATEGORY_TYPES = new ArrayList<>(Arrays.asList(
            "int", "text", "text", "text", "text", "text", "text", "text", "bit","bit", "text", "date"));
    private static final ArrayList<String> CITY_CATEGORIES = new ArrayList<>(Arrays.asList(
            PRIMARY_KEY, "Boulder", "ColoradoSprings", "Denver", "FortCollins", "GrandJunction", "Pueblo"));
    private static final ArrayList<String> CITY_CATEGORY_TYPES = new ArrayList<>(Arrays.asList(
            "int", "bit", "bit", "bit", "bit", "bit", "bit"));
    private static final ArrayList<String> RESOURCE_TYPE_CATEGORIES = new ArrayList<>(Arrays.asList(
            PRIMARY_KEY, "accessibility", "employment", "food", "healthcare", "housing", "legal",
            "lgbt", "reentry", "transportation", "travel"));

    private static final ArrayList<String> RESOURCE_TYPE_CATEGORY_TYPES = new ArrayList<>(Arrays.asList(
            "int", "bit", "bit", "bit", "bit", "bit", "bit", "bit", "bit", "bit", "bit"));
    private static final ArrayList<String> LANGUAGE_CATEGORIES = new ArrayList<>(Arrays.asList(
            PRIMARY_KEY, "ASL", "English", "Spanish"));
    private static final ArrayList<String> LANGUAGE_CATEGORY_TYPES = new ArrayList<>(Arrays.asList(
            "int", "bit", "bit", "bit"));
    private static final ArrayList<String> SERVING_CATEGORIES = new ArrayList<>(Arrays.asList(
            PRIMARY_KEY, "deaf", "disabilities", "employed", "everyone", "hivPositive", "military",
            "queer", "seniors", "veterans", "youth"));
    private static final ArrayList<String> SERVING_CATEGORY_TYPES = new ArrayList<>(Arrays.asList(
            "int", "bit", "bit", "bit", "bit", "bit", "bit", "bit", "bit", "bit", "bit"));
    private static final int NUM_CATEGORIES = 15;
    private static final String URL = "jdbc:sqlite:C:/sqlite/db/test.db";

    private ArrayList<ResourceData> allResourceData = new ArrayList<>();
    private ArrayList<CityData> allCityData = new ArrayList<>();
    private ArrayList<LangaugeData> allLanguageData = new ArrayList<>();
    private ArrayList<ResourceTypeData> allResourceTypeData = new ArrayList<>();
    private ArrayList<ServingData> allServingData = new ArrayList<>();

    private ArrayList<Integer> allKeys = new ArrayList<>();

    /**
     * This method creates the database and tables for the project
     * It creates tables for resource information, language, city, resource type and serving
     */
    public void createDatabaseAndTables() throws ClassNotFoundException {
        String fileName = "";

        Class.forName("org.sqlite.JDBC");
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null){
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The Driver name is  " + meta.getDriverName());
                System.out.println("A New Database has been created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
/*
        //create database
        try(Connection conn = DriverManager.getConnection(url); // MySQL
            Statement stmt = conn.createStatement();
        ) {
            String sql = "create database if not exists " + DATABASE_NAME;
            stmt.execute(sql);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }*/
        //create table
        try (
                Connection conn = DriverManager.getConnection(URL); // MySQL
                Statement stmt = conn.createStatement();
        ) {
            //TODO remove this line
            stmt.execute("DROP TABLE " + RESOURCE_TABLE_NAME);
            String sql = makeTable(RESOURCE_TABLE_NAME, RESOURCE_CATEGORIES, RESOURCE_CATEGORY_TYPES);
            stmt.execute(sql);

            //TODO remove this line
            stmt.execute("DROP TABLE " + CITY_TABLE_NAME);
            sql = makeTable(CITY_TABLE_NAME, CITY_CATEGORIES, CITY_CATEGORY_TYPES);
            stmt.execute(sql);

            //TODO remove this line
            stmt.execute("DROP TABLE " + TYPE_TABLE_NAME);
            sql = makeTable(TYPE_TABLE_NAME, RESOURCE_TYPE_CATEGORIES, RESOURCE_TYPE_CATEGORY_TYPES);
            stmt.execute(sql);

            //TODO remove this line
            stmt.execute("DROP TABLE " + LANGUAGE_TABLE_NAME);
            sql = makeTable(LANGUAGE_TABLE_NAME, LANGUAGE_CATEGORIES, LANGUAGE_CATEGORY_TYPES);
            stmt.execute(sql);

            //TODO remove this line
            stmt.execute("DROP TABLE " + SERVING_TABLE_NAME);
            sql = makeTable(SERVING_TABLE_NAME, SERVING_CATEGORIES, SERVING_CATEGORY_TYPES);
            stmt.execute(sql);

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        readResourceFile();
        updateTables();
    }

    /**
     * This method makes the sql command to create a new table
     * @param tableName the name of the table to be created
     * @param categories the columns of the table to be created
     * @param types the type of data to be stored in each column
     * @return a string of the sql command to create a table
     */
    private String makeTable(String tableName, ArrayList categories, ArrayList types){
        String command = "create table if not exists " + tableName + " (";
        for (int i = 0; i < categories.size(); i++) {
            String toAdd = categories.get(i) + " " + types.get(i) + ", ";
            command += toAdd;
        }
        command += "primary key (";
        command += PRIMARY_KEY;
        command += "));";
        return command;
    }

    /**
     * This method reads in a csv with the resources and processes each row to ensure
     * they are ready to be inputted into the tables
     */
    public void readResourceFile(){
        //TODO allow for uploading file, not having it in
        System.out.println("Reading " + RESOURCE_FILE);
        int numFailed = 0;
        int numTried = 0;
        boolean success;
        try {
            Scanner in = new Scanner(new FileInputStream(RESOURCE_FILE + ".csv"));
            while (in.hasNextLine()) {
                String dataRow = in.nextLine();
                //If the row is entirely empty, skip it
                if(dataRow.equals(",,,,,,,,,,,,,,")){
                    continue;
                }
                //This is the first row, so check that there are the right number of columns
                if (numTried == 0){
                    if (!checkColumns(dataRow)) {
                        break;
                    }
                    numTried ++;
                }
                else {
                    success = processRow(dataRow, numTried);
                    numTried++;
                    if (!success){
                        numFailed++;
                    }
                }
            }
            System.out.println(String.format("Successfully processed %o out of %o rows", (numTried-numFailed-1), (numTried-1)));
        } // catches the file not found exception-shouldn't be one because the file is submitted
        catch (FileNotFoundException e) {
            //TODO figure out how to give user logs, not directly printing?
            System.out.println("Please move a default file into project");
        }
    }

    /**
     * This method goes through a row of the spreadsheet and prepares it to be entered into the database
     * @param dataRow The string representing a row from the csv
     * @param rowNum What row of the spreadsheet we are processing
     * @return true if the row was successfully processed, false if the row was malformed and cannot be processed
     */
    public boolean processRow(String dataRow, int rowNum){
        ArrayList<String> cleanData = separateRow(dataRow);
        //If there are empty cells, this ensures that they get filled with nulls
        if (cleanData.size() < NUM_CATEGORIES){
            while (cleanData.size() != NUM_CATEGORIES){
                cleanData.add(null);
            }
        }

        ResourceData rowResourceData = new ResourceData();
        CityData rowCityData = new CityData();
        LangaugeData rowLanguageData = new LangaugeData();
        ResourceTypeData rowResourceTypeData = new ResourceTypeData();
        ServingData rowServingData = new ServingData();
        Integer rowKey;

        boolean keyWorked = rowResourceData.setResourceKey(String.valueOf(rowNum));
        if (!keyWorked){
            System.out.println(String.format("There was an error with the formatting of key %s in row %o", cleanData.get(0), rowNum));
            return false;
        } else {
            rowKey = Integer.parseInt(rowResourceData.getResourceKey());
            if (allKeys.contains(rowKey)){
                System.out.println(String.format("Key %o in row %o is a duplicate and cannot be added", rowKey, rowNum));
                return false;
            }
            allKeys.add(rowKey);
        }
        //Process the resource name
        if(!rowResourceData.setResourceName(cleanData.get(0))){
            System.out.println(String.format("There was an error with the formatting of the resource name %s in row %o", cleanData.get(0), rowNum));
            return false;
        }
        //Process the organization name
        if(!rowResourceData.setOrganization(cleanData.get(1))){
            System.out.println(String.format("There was an error with the formatting of the organization name %s in row %o", cleanData.get(1), rowNum));
            return false;
        }
        //Process the description
        if(!rowResourceData.setDescription(cleanData.get(2))){
            System.out.println(String.format("There was an error with the formatting of the description %s in row %o", cleanData.get(2), rowNum));
            return false;
        }
        //Process the phone number
        if(!rowResourceData.setPhoneNumber(cleanData.get(3))){
            System.out.println(String.format("There was an error with the formatting of the phone number %s in row %o", cleanData.get(3), rowNum));
            return false;
        }
        //Process the website
        if(!rowResourceData.setWebsite(cleanData.get(4))){
            System.out.println(String.format("There was an error with the formatting of the website %s in row %o", cleanData.get(4), rowNum));
            return false;
        }
        //Process the city
        if(!rowCityData.validate(Integer.toString(rowKey), cleanData.get(5))){
            System.out.println(String.format("There was an error with the formatting of the city %s in row %o", cleanData.get(5), rowNum));
            return false;
        }
        //Process the address
        if(!rowResourceData.setAddress(cleanData.get(6))){
            System.out.println(String.format("There was an error with the formatting of the address %s in row %o", cleanData.get(6), rowNum));
            return false;
        }
        //Process the hours
        if(!rowResourceData.setHours(cleanData.get(7))){
            System.out.println(String.format("There was an error with the formatting of the hours %s in row %o", cleanData.get(7), rowNum));
            return false;
        }
        //Process the resource type
        if(!rowResourceTypeData.validate(Integer.toString(rowKey), cleanData.get(8))){
            System.out.println(String.format("There was an error with the formatting of the resource type(s) %s in row %o", cleanData.get(8), rowNum));
            return false;
        }
        //Process the language type
        if(!rowLanguageData.validate(Integer.toString(rowKey), cleanData.get(9))){
            System.out.println(String.format("There was an error with the formatting of the language(s) %s in row %o", cleanData.get(9), rowNum));
            return false;
        }
        //Process offers transportation
        if(!rowResourceData.setOffersTransportation(cleanData.get(10))){
            System.out.println(String.format("There was an error with the formatting of offers transportation %s in row %o", cleanData.get(10), rowNum));
            return false;
        }
        //Process serving
        if(!rowServingData.validate(Integer.toString(rowKey), cleanData.get(11))){
            System.out.println(String.format("There was an error with the formatting of serving %s in row %o", cleanData.get(11), rowNum));
            return false;
        }
        //Process has requirements
        if(!rowResourceData.setHasRequirements(cleanData.get(12))){
            System.out.println(String.format("There was an error with the formatting of has requirements %s in row %o", cleanData.get(12), rowNum));
            return false;
        }
        //Process the requirements
        if(!rowResourceData.setRequirements(cleanData.get(13))){
            System.out.println(String.format("There was an error with the formatting of the requirements %s in row %o", cleanData.get(13), rowNum));
            return false;
        }
        //Process the date
        if(!rowResourceData.setDateUpdated(cleanData.get(14))){
            System.out.println(String.format("There was an error with the formatting of the date updated %s in row %o", cleanData.get(14), rowNum));
            return false;
        }
        allResourceData.add(rowResourceData);
        allCityData.add(rowCityData);
        allLanguageData.add(rowLanguageData);
        allResourceTypeData.add(rowResourceTypeData);
        allServingData.add(rowServingData);
        return true;
    }

    /**
     * This method checks if the inputted spreadsheet has the right number of columns
     * @param columns The string from the csv of the column headings
     * @return true if there are the correct number of columns, false if there are not
     */
    private boolean checkColumns(String columns){
        ArrayList<String> cleanColumns = separateRow(columns);
        if (cleanColumns.size() != NUM_CATEGORIES){
            System.out.println(NUM_CATEGORIES);
            System.out.println("Spreadsheet has the wrong number of columns. Please ensure that you have the right " +
                    "number of columns in the correct order");
            return false;
        }
        return true;
    }

    /**
     * This method separates a string representing a csv row into the correct number of sections
     * (taking into account the fact that cells may have commas within them)
     * @param dataRow the string representation of a single csv row
     * @return an arraylist of the strings in each csv cell
     */
    private ArrayList<String> separateRow(String dataRow){
        String [] splitData = dataRow.split(",");
        ArrayList<String>  cleanedData = new ArrayList<>();
        boolean split = false;
        String reconstructedString = "";
        char quote = '\"';

        for (String part:splitData) {
            //Check if the box is empty
            if (part.length() == 0){
                cleanedData.add(null);
            } //check if the piece got split up too much
            else if(part.charAt(0) == quote) {
                reconstructedString += part;
                split = true;
            } else if (split) {
                //if we know there's a split, check if the split is over
                if (part.charAt(part.length()-1) == quote){
                    //Add in the comma that got removed
                    reconstructedString += ",";
                    //Add the final piece of the data then add it to the arraylist
                    reconstructedString += part;
                    cleanedData.add(reconstructedString);
                    //Reset everything for next time we find a split
                    split = false;
                    reconstructedString = "";
                } else {
                    reconstructedString += ",";
                    reconstructedString += part;
                }
            } else {
                //if no split and no quotes, just add the part to the clean data
                cleanedData.add(part);
            }
        }
        return cleanedData;
    }

    /**
     * This method updates all the tables with the data that has been processed from the csv file
     */
    public void updateTables(){
        //check if anything is actually going to be added
        if (allResourceData.size() == 0){
            System.out.println("Could not update table as there were no valid rows");
            return;
        }
        try (
                Connection conn = DriverManager.getConnection(URL); // MySQL
                Statement stmt = conn.createStatement();
        ) {
            //TODO before clearing out the table, copy it over to a backup

            //Clear the table so things aren't inputted twice
            //Add every processed row to the table
            for(int i = 0; i < allResourceData.size(); i++){
                ResourceData thisEntry = allResourceData.get(i);
                String sql = "insert into " + RESOURCE_TABLE_NAME + " values (" + thisEntry.allValues() + ")";
                stmt.execute(sql);
            }
            //Clear the table so things aren't inputted twice
            //Add every processed row to the table
            for(int i = 0; i < allCityData.size(); i++){
                CityData thisEntry = allCityData.get(i);
                String sql = "insert into " + CITY_TABLE_NAME + " values (" + thisEntry.allValues() + ")";
                stmt.execute(sql);
            }
            //Clear the table so things aren't inputted twice
            //Add every processed row to the table
            for(int i = 0; i < allResourceTypeData.size(); i++){
                ResourceTypeData thisEntry = allResourceTypeData.get(i);
                String sql = "insert into " + TYPE_TABLE_NAME + " values (" + thisEntry.allValues() + ")";
                stmt.execute(sql);
            }
            //Clear the table so things aren't inputted twice
            //Add every processed row to the table
            for(int i = 0; i < allLanguageData.size(); i++){
                LangaugeData thisEntry = allLanguageData.get(i);
                String sql = "insert into " + LANGUAGE_TABLE_NAME + " values (" + thisEntry.allValues() + ")";
                stmt.execute(sql);
            }
            //Clear the table so things aren't inputted twice
            //Add every processed row to the table
            for(int i = 0; i < allServingData.size(); i++){
                ServingData thisEntry = allServingData.get(i);
                String sql = "insert into " + SERVING_TABLE_NAME + " values (" + thisEntry.allValues() + ")";
                stmt.execute(sql);
            }
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
