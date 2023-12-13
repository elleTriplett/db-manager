package TableData;

import java.util.ArrayList;

public class ResourceTypeData {
    private String resourceKey;
    private String accessibility;
    private String employment;
    private String food;
    private String healthcare;
    private String housing;

    private String legal;
    private String lgbt;
    private String reentry;
    private String transportation;
    private String travel;

    /**
     * This method returns a string of all the values for the table in the column order
     * @return a string of the values in order
     */
    public String allValues(){
        String toReturn = this.resourceKey;
        toReturn += ", " + this.accessibility;
        toReturn += ", " + this.employment;
        toReturn += ", " + this.food;
        toReturn += ", " + this.healthcare;
        toReturn += ", " + this.housing;
        toReturn += ", " + this.legal;
        toReturn += ", " + this.lgbt;
        toReturn += ", " + this.reentry;
        toReturn += ", " + this.transportation;
        toReturn += ", " + this.travel;
        return toReturn;
    }

    /**
     * This method validates the types
     * @param key the resource key
     * @param types the list of languages
     * @return true if it is valid, false if not
     */
    public boolean validate(String key, String types){
        if (!validateKey(key)){
            return false;
        }
        if(types == null){
            return true;
        }
        ArrayList<Object> returned = cleanString(types);
        if (!(Boolean) returned.get(0)){
            return false;
        }
        String cleanType = (String) returned.get(1);
        String [] allTypes = cleanType.split(", ");
        if (!matchTypes(allTypes)){
            return false;
        }
        return true;
    }

    /**
     * This method matches the inputted types to the valid categories
     * @param types the inputted types from the csv
     * @return true if the types match,false if not
     */
    private boolean matchTypes(String [] types){
        int numTrue = 0;
        for (String lang:types) {
            if (lang.equalsIgnoreCase("accessibility")){
                this.accessibility = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("employment")) {
                this.employment = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("food")) {
                this.food = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("healthcare")){
                this.healthcare = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("housing")){
                this.housing = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("legal")) {
                this.legal = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("lgbt")) {
                this.lgbt = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("reentry")) {
                this.reentry = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("transportation")){
                this.transportation = "true";
                numTrue++;
            } else if (lang.equalsIgnoreCase("travel")){
                this.travel = "true";
                numTrue++;
            }
        }
        //If one or more of the types didn't match what we expected
        if(numTrue != types.length){
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
