package TableData;

import java.util.ArrayList;

public class LangaugeData {
    private String resourceKey;
    private String asl = null;
    private String english = null;
    private String spanish = null;

    /**
     * This method returns a string of all the values for the table in the column order
     * @return a string of the values in order
     */
    public String allValues(){
       String toReturn = this.resourceKey;
       toReturn += ", " + this.asl;
       toReturn += ", " + this.english;
       toReturn += ", " + this.spanish;
       return toReturn;
    }

    /**
     * This method validates the languages
     * @param key the resource key
     * @param languages the list of languages
     * @return true if it is valid, false if not
     */
    public boolean validate(String key, String languages){
        if (!validateKey(key)){
            return false;
        }
        if(languages == null){
            return true;
        }
        ArrayList<Object> returned = cleanString(languages);
        if (!(Boolean) returned.get(0)){
            return false;
        }
        String cleanLanguages = (String) returned.get(1);
        String [] allLanguages = cleanLanguages.split(", ");
        if (!matchLanguages(allLanguages)){
            return false;
        }
        return true;
    }

    /**
     * This method matches the inputted languages to the valid categories
     * @param languages the inputted languages from the csv
     * @return true if the languages match,false if not
     */
    private boolean matchLanguages(String [] languages){
        int numTrue = 0;
        for (String lang:languages) {
            if (lang.equalsIgnoreCase("asl")){
                this.asl = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("english")){
                this.english = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("spanish")){
                this.spanish = "true";
                numTrue++;
            }
        }
        //If one or more of the languages didn't match what we expected
        if(numTrue != languages.length){
            return false;
        }
        return true;
    }

    /**
     * This method makes sure the key is a non-null integer
     * @param key the resource key
     * @return True if the key is valid, false if not
     */
    private boolean validateKey(String key){
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
}
