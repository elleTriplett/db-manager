package TableData;

import java.util.ArrayList;

public class ServingData {
    private String resourceKey;
    private String deaf = null;
    private String disabilities = null;
    private String employed = null;
    private String everyone = null;
    private String hivPositive = null;
    private String military = null;
    private String queer = null;
    private String seniors = null;
    private String veterans = null;
    private String women = null;
    private String youth = null;

    /**
     * This method returns a string of all the values for the table in the column order
     * @return a string of the values in order
     */
    public String allValues(){
        String toReturn = this.resourceKey;
        toReturn += ", " + this.deaf;
        toReturn += ", " + this.disabilities;
        toReturn += ", " + this.employed;
        toReturn += ", " + this.everyone;
        toReturn += ", " + this.hivPositive;
        toReturn += ", " + this.military;
        toReturn += ", " + this.queer;
        toReturn += ", " + this.seniors;
        toReturn += ", " + this.veterans;
        toReturn += ", " + this.women;
        toReturn += ", " + this.youth;
        return toReturn;
    }

    /**
     * This method validates the languages
     * @param key the resource key
     * @param serving the list of whom it serves
     * @return true if it is valid, false if not
     */
    public boolean validate(String key, String serving){
        if (!validateKey(key)){
            return false;
        }
        if(serving == null){
            return true;
        }
        ArrayList<Object> returned = cleanString(serving);
        if (!(Boolean) returned.get(0)){
            return false;
        }
        String cleanServing = (String) returned.get(1);
        String [] allServing = cleanServing.split(", ");
        if (!matchServing(allServing)){
            return false;
        }
        return true;
    }

    /**
     * This method matches the inputted languages to the valid categories
     * @param serving the inputted languages from the csv
     * @return true if the languages match,false if not
     */
    private boolean matchServing(String [] serving){
        int numTrue = 0;
        for (String category:serving) {
            if (category.equalsIgnoreCase("deaf")){
                this.deaf = "true";
                numTrue++;
            } else if (category.equalsIgnoreCase("disabilities")){
                this.disabilities = "true";
                numTrue++;
            } else if (category.equalsIgnoreCase("employed")){
                this.employed = "true";
                numTrue++;
            } else if (category.equalsIgnoreCase("everyone")){
                this.everyone = "true";
                numTrue++;
            } else if (category.equalsIgnoreCase("hiv Positive")){
                this.hivPositive = "true";
                numTrue++;
            } else if (category.equalsIgnoreCase("military")) {
                this.military = "true";
                numTrue++;
            } else if (category.equalsIgnoreCase("queer")) {
                this.queer = "true";
                numTrue++;
            } else if (category.equalsIgnoreCase("seniors")){
                this.seniors = "true";
                numTrue++;
            } else if (category.equalsIgnoreCase("veterans")) {
                this.veterans = "true";
                numTrue++;
            } else if (category.equalsIgnoreCase("women")){
                this.women = "true";
                numTrue++;
            } else if (category.equalsIgnoreCase("youth")){
                this.youth = "true";
                numTrue++;
            }
        }
        //If one or more of the category didn't match what we expected
        if(numTrue != serving.length){
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
