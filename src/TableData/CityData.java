package TableData;

import java.util.ArrayList;

public class CityData {
    private String resourceKey;
    private String boulder = null;
    private String cos = null;
    private String denver = null;
    private String ftcol = null;
    private String grandj = null;
    private String pueblo = null;

    /**
     * This method returns a string of all the values for the table in the column order
     * @return a string of the values in order
     */
    public String allValues(){
        String toReturn = this.resourceKey;
        toReturn += ", " + this.boulder;
        toReturn += ", " + this.cos;
        toReturn += ", " + this.denver;
        toReturn += ", " + this.ftcol;
        toReturn += ", " + this.grandj;
        toReturn += ", " + this.pueblo;
        return toReturn;
    }

    /**
     * This method validates the cities
     * @param key the resource key
     * @param cities the list of cities
     * @return true if it is valid, false if not
     */
    public boolean validate(String key, String cities){
        if (!validateKey(key)){
            return false;
        }
        if(cities == null){
            return true;
        }
        ArrayList<Object> returned = cleanString(cities);
        if (!(Boolean) returned.get(0)){
            return false;
        }
        String cleanCities = (String) returned.get(1);
        String [] allCities = cleanCities.split(", ");
        if (!matchCities(allCities)){
            return false;
        }
        return true;
    }

    /**
     * This method matches the inputted cities to the valid categories
     * @param cities the inputted cities from the csv
     * @return true if the cities match,false if not
     */
    private boolean matchCities(String [] cities){
        int numTrue = 0;
        for (String city:cities) {
            if (city.equalsIgnoreCase("boulder")){
                this.boulder = "true";
                numTrue++;
            } else if (city.equalsIgnoreCase("colorado springs")){
                this.cos = "true";
                numTrue++;
            } else if (city.equalsIgnoreCase("denver")){
                this.denver = "true";
                numTrue++;
            } else if (city.equalsIgnoreCase("fort collins")){
                this.ftcol = "true";
                numTrue++;
            } else if (city.equalsIgnoreCase("grand junction")){
                this.grandj = "true";
                numTrue++;
            } else if (city.equalsIgnoreCase("pueblo")){
                this.pueblo = "true";
                numTrue++;
            }
        }
        //If one or more of the cities didn't match what we expected
        if(numTrue != cities.length){
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
