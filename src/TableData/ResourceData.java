package TableData;

import java.util.ArrayList;
import java.util.Arrays;

public class ResourceData {

    private String resourceKey;
    private String resourceName;
    private String organization;
    private String description;
    private String phoneNumber;
    private String website;
    private String address;
    private String hours;
    private String offersTransportation;
    private String hasRequirements;
    private String requirements;
    private String dateUpdated;

    //TODO update char limit once we have a website prototype
    private static final int CHAR_LIMIT = 600;
    //TODO add free/costs money?

    /**
     * This method returns a string of all the values for the table in the column order
     * @return a string of the values in order
     */
    public String allValues (){
        String toReturn = this.resourceKey;
        toReturn += ", " + this.resourceName;
        toReturn += ", " + this.organization;
        toReturn += ", " + this.description;
        toReturn += ", " + this.phoneNumber;
        toReturn += ", " + this.website;
        toReturn += ", " + this.address;
        toReturn += ", " + this.hours;
        toReturn += ", " + this.offersTransportation;
        toReturn += ", " + this.hasRequirements;
        toReturn += ", " + this.requirements;
        toReturn += ", " + this.dateUpdated;
        return toReturn;
    }

    /**
     * This method removes quotes from the string if they're present
     * @param toClean the string to be cleaned
     * @return a tuple, with a boolean if it worked and the cleaned string
     */
    private ArrayList<Object> cleanString(String toClean){
        char quote = '\"';
        boolean even = true;
        if (toClean.charAt(0) == quote){
            toClean = toClean.substring(1);
            even = false;
        }
        if(toClean.charAt(toClean.length()-1) == quote){
            toClean =  toClean.substring(0, toClean.length()-1);
            even = true;
        }
        if (!even){
            System.out.println("Something strange happened, an uneven number of quotes were detected");
        }
        ArrayList<Object> toReturn = new ArrayList<>();
        toReturn.add(even);
        toReturn.add(toClean);
        return toReturn;
    }

    /**
     * This method adds single quotes around a string
     * @param toQuote String to have quotes put around it
     * @return String with quotes
     */
    private String addQuotes(String toQuote){
        String withQuotes = "'" + toQuote + "'";
        return withQuotes;
    }

    /**
     * This method sets the resource key, ensuring that it is a non-null integer
     * @param key the resource key
     * @return true if key is valid, false otherwise
     */
    public boolean setResourceKey(String key){
        ArrayList<Object> returned = cleanString(key);
        if (!(Boolean) returned.get(0)){
            return false;
        }
        String cleanKey = (String) returned.get(1);
        //Check that the key is an integer
        try {
            int i = Integer.parseInt(cleanKey);
            this.resourceKey = cleanKey;
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * Returns the resource key
     * @return resourceKey as a String
     */
    public String getResourceKey() {
        return resourceKey;
    }

    /**
     * This method sets the resource name, or null if it is null
     * @param resourceName
     * @return true if successful, false if not
     */
    public boolean setResourceName(String resourceName) {
        if (resourceName == null){
            this.resourceName = null;
            return true;
        }
        ArrayList<Object> returned = cleanString(resourceName);
        if (!(Boolean) returned.get(0)){
            return false;
        } else {
            this.resourceName = addQuotes((String) returned.get(1));
            return true;
        }
    }

    /**
     * This method sets the organization name
     * @param organization
     * @return true if successful, false if not
     */
    public boolean setOrganization(String organization) {
        if (organization == null){
            this.organization = null;
            return true;
        }
        ArrayList<Object> returned = cleanString(organization);
        if (!(Boolean) returned.get(0)){
            return false;
        } else {
            this.organization = addQuotes((String) returned.get(1));
            return true;
        }
    }

    /**
     * This method sets the description of the resource, truncating it if it is over CHAR_Limit
     * @param description
     * @return True if successful, false if not
     */
    public boolean setDescription(String description) {
        if (description == null){
            this.description = null;
            return true;
        }
        ArrayList<Object> returned = cleanString(description);
        if (!(Boolean) returned.get(0)){
            return false;
        } else {
            String returnedString = (String) returned.get(1);
            if (returnedString.length() > CHAR_LIMIT){
                this.description = addQuotes(returnedString.substring(0, CHAR_LIMIT));
                return true;
            }
            this.description = addQuotes(returnedString);
            return true;
        }
    }

    /**
     * This method sets the phone number, formatting it to (xxx)-xxx-xxxx
     * and making sure it contains the correct number of digits
     * @param phoneNumber
     * @return True if successful, false if not
     */
    public boolean setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null){
            this.phoneNumber = null;
            return true;
        }
        ArrayList<Object> returned = cleanString(phoneNumber);
        if (!(Boolean) returned.get(0)){
            return false;
        }
        String cleanNumber = (String) returned.get(1);
        ArrayList<Character> number = new ArrayList<>();
        ArrayList<Character> delimiters = new ArrayList<>(Arrays.asList('(', ')', '-'));
        ArrayList<Character> digits = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
        for (int i = 0; i < cleanNumber.length(); i++){
            if (delimiters.contains(cleanNumber.charAt(i))){
                continue;
            } else if (digits.contains(cleanNumber.charAt(i))){
                //check that there are not too many digits in the phone number
                if(number.size() < 10) {
                    number.add(cleanNumber.charAt(i));
                } else {
                    return false;
                }
            } else {
                //if there's a character that's not a digit or a delimiter, something went wrong
                return false;
            }
        }
        if (number.size() != 10){
            return false;
        }
        String digitsAsString = "";
        for (char digit: number){
            digitsAsString += digit;
        }
        //this.phoneNumber = addQuotes(formatPhoneNumber(String.valueOf(number)));
        this.phoneNumber = addQuotes(formatPhoneNumber(digitsAsString));
        return true;
    }

    /**
     * This method formats a string of digits as a proper phone number (xxx)-xxx-xxxx
     * @param digits the digits of the phone number
     * @return the formatted phone number as a string
     */
    private String formatPhoneNumber(String digits){
        String toReturn = "(";
        toReturn += digits.substring(0,3);
        toReturn += ")-";
        toReturn += digits.substring(3,6);
        toReturn += "-";
        toReturn += digits.substring(6);
        System.out.println("Added phone number: " + toReturn);
        return toReturn;
    }

    /**
     * This method sets the website
     * @param website
     * @return true if successful, false if not
     */
    public boolean setWebsite(String website) {
        if (website == null){
            this.website = null;
            return true;
        }
        ArrayList<Object> returned = cleanString(website);
        if (!(Boolean) returned.get(0)){
            return false;
        }
        this.website = addQuotes((String) returned.get(1));
        return true;
    }

    /**
     * This method sets the address
     * @param address
     * @return true if successful, false if not
     */
    public boolean setAddress(String address) {
        if (address == null){
            this.address = null;
            return true;
        }
        ArrayList<Object> returned = cleanString(address);
        if (!(Boolean) returned.get(0)){
            return false;
        } else {
            this.address = addQuotes((String) returned.get(1));
            return true;
        }
    }

    /**
     * This method sets the hours
     * @param hours
     * @return true if successful, false if not
     */
    public boolean setHours(String hours) {
        if (hours == null){
            this.hours = null;
            return true;
        }
        ArrayList<Object> returned = cleanString(hours);
        if (!(Boolean) returned.get(0)){
            return false;
        } else {
            this.hours = addQuotes((String) returned.get(1));
            return true;
        }
    }

    /**
     * This method sets offers transportation, checking that it is a valid boolean in the form
     * 0, 1, yes, no, true, or false
     * @param offersTransportation
     * @return true if successful, false if not
     */
    public boolean setOffersTransportation(String offersTransportation) {
        if (offersTransportation == null){
            this.offersTransportation = null;
            return true;
        }
        //accepts 0, 1 or 'true', 'false'
        ArrayList<Object> returned = cleanString(offersTransportation);
        if (!(Boolean) returned.get(0)){
            return false;
        }
        String cleanBool = (String) returned.get(1);
        //Check that the data
        try {
            int i = Integer.parseInt(cleanBool);
            if (i == 0 || i == 1){
                this.offersTransportation = cleanBool;
                return true;
            }
        } catch (NumberFormatException e){
        }
        if (cleanBool.equalsIgnoreCase("true")||
        cleanBool.equalsIgnoreCase("false")){
            this.offersTransportation = cleanBool;
            return true;
        }
        if (cleanBool.equalsIgnoreCase("yes")){
            this.offersTransportation = "true";
            return true;
        }
        if (cleanBool.equalsIgnoreCase("no")){
            this.offersTransportation = "false";
            return true;
        }
        return false;
    }

    /**
     * This method sets has requirements, checking that it is a valid boolean in the form
     * 0, 1, yes, no, true, or false
     * @param hasRequirements
     * @return True if successful, false if not
     */
    public boolean setHasRequirements(String hasRequirements) {
        if (hasRequirements == null){
            this.hasRequirements = null;
            return true;
        }
        //accepts 0, 1, 'true', 'false'
        ArrayList<Object> returned = cleanString(hasRequirements);
        if (!(Boolean) returned.get(0)){
            return false;
        }
        String cleanBool = (String) returned.get(1);
        //Check that the data
        try {
            int i = Integer.parseInt(cleanBool);
            if (i == 0 || i == 1){
                this.hasRequirements = cleanBool;
                return true;
            }
        } catch (NumberFormatException e){
        }
        if (cleanBool.equalsIgnoreCase("true")||
                cleanBool.equalsIgnoreCase("false")){
            this.hasRequirements = cleanBool;
            return true;
        }
        if (cleanBool.equalsIgnoreCase("yes")){
            this.hasRequirements = "true";
            return true;
        }
        if (cleanBool.equalsIgnoreCase("no")){
            this.hasRequirements = "false";
            return true;
        }
        return false;
    }

    /**
     * This method sets the requirements
     * @param requirements
     * @return true if successful, false if not
     */
    public boolean setRequirements(String requirements) {
        if (requirements == null){
            this.requirements = null;
            return true;
        }
        ArrayList<Object> returned = cleanString(requirements);
        if (!(Boolean) returned.get(0)){
            return false;
        } else {
            this.requirements = addQuotes((String) returned.get(1));
            return true;
        }
    }

    /**
     * This method sets the date updated, making sure it's in the recognizable form YYYY-MM-DD
     * @param dateUpdated as a string
     * @return true if successful, false if not
     */
    public boolean setDateUpdated(String dateUpdated) {
        if (dateUpdated == null){
            this.dateUpdated = null;
            return true;
        }
        ArrayList<Object> returned = cleanString(dateUpdated);
        if (!(Boolean) returned.get(0)){
            return false;
        }
        String dateAsString = (String) returned.get(1);
        String [] separatedByDash = dateAsString.split("-");
        String [] separatedBySlash = dateAsString.split("/");
        String year = null;
        String month = null;
        String day = null;
        if (separatedBySlash.length == 3){
            if (separatedBySlash[0].length() == 4){
                year = separatedBySlash[0];
                month = separatedBySlash[1];
                day = separatedBySlash[2];
            } else if (separatedBySlash[2].length() == 4) {
                month = separatedBySlash[0];
                day = separatedBySlash[1];
                year = separatedBySlash[2];
            }
        } else if (separatedByDash.length == 3) {
            if (separatedByDash[0].length() == 4){
                year = separatedByDash[0];
                month = separatedByDash[1];
                day = separatedByDash[2];
            } else if (separatedByDash[2].length() == 4) {
                month = separatedByDash[0];
                day = separatedByDash[1];
                year = separatedByDash[2];
            }

        }
        if (year == null){
            return false;
        }
        String formattedDate = formatDate(year,month,day);
        this.dateUpdated = addQuotes(formattedDate);
        return true;
    }

    /**
     * This method formats the date in a way the database will recognize it (YYYY-MM-DD)
     * @param year the year as a string
     * @param month the month as a string
     * @param day the day as a string
     * @return the formatted date
     */
    private String formatDate(String year, String month, String day){
        String formattedDate = year + "-" + month + "-" + day;
        return formattedDate;
    }
}
