import java.util.*;

/**
 * Creates a Nominee object which holds all the information of a county and the number of votes per county. 
 * Puts all the votes into the HashMap, add votes from a county, retrieve votes
for a county, calculate total votes, and provides a string representation of the nominee
 *
 * @author (124116)
 * @version (5/2/2024)
 */
public class Nominee
{
    // instance variables - replace the example below with your own
    private String name;
    private HashMap<String, Integer> countyVotes;

    /**
     * Constructor for objects of class Nominee
     */
    public Nominee(String name)
    {
        this.name = name;
        countyVotes = new HashMap<>();
    }

    /**
     * getter method that returms the name of the candidate
     *
     * @param none
     * @return  String - name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * puts the county and votes into the HashMap
     *
     * @param String county, int votes
     * @return void-none
     */
    public void mapCountyVotes(String county, int votes)
    {
        countyVotes.put(county,votes);
    }
    
    /**
     * getter method that returns countyVotes HashMap
     *
     * @param void none
     * @return HashMap<String, Integer> - countyVotes
     */
    public HashMap<String, Integer> getMap()
    {
        return countyVotes;
    }
    
     /**
     * getter method that returns the number of int votes depending on the county
     *
     * @param String county
     * @return int countyVotes.get(county)
     */
    public int getCountyVotes(String county)
    {
       return countyVotes.get(county);
    }
    
     /**
     * gets the total votes, loops through the HashMap and gets all the votes and adds it together
     *
     * @param none
     * @return int total
     */
    public int getTotalVotes()
    {
         int total = 0;
        for (String s: countyVotes.keySet())
        {
            total+=countyVotes.get(s);
        }
        return total;
    }
    
     /**
     * toString method that prints all the info out in a String format 
     *
     * @param none
     * @return String 
     */
    public String toString()
    {
        StringBuilder c = new StringBuilder();
        c.append("Nominee: ").append(name).append("\n");
        for (String county : countyVotes.keySet()) {
            c.append("County: ").append(county).append(" , Votes: ").append(countyVotes.get(county)).append("\n");
        }
        c.append("Total Votes: ").append(getTotalVotes()).append("\n");
        return c.toString();
    }
}
