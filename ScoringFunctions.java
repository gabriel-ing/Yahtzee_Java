import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ScoringFunctions{

    private  static ArrayList<Integer> rolls;
    private static LinkedHashMap<String, Integer> scores = new LinkedHashMap<>();
    
    public ScoringFunctions(ArrayList<Integer> rolls){
        this.rolls = rolls;
        this.countIndividuals();
        this.checkStraights();
        this.checkOfAKinds();
        this.checkFullHouse();
        this.findChance();
        this.checkYahtzee();
        //System.out.print("Scoringfunctions init scores: ");
        //System.out.println(scores);
    }
    public static void main(String[] digits){
        ArrayList<Integer> list = new ArrayList<>();
        for (char ch : digits[0].toCharArray()) {
            list.add(Character.getNumericValue(ch));
        }
        
        ScoringFunctions scoringFunctions = new ScoringFunctions(list);
        scoringFunctions.countIndividuals();
        scoringFunctions.checkStraights();
        scoringFunctions.checkOfAKinds();
        
        scoringFunctions.checkFullHouse();
        scoringFunctions.findChance();
        scoringFunctions.checkYahtzee();
        
        //System.out.println(scores);
    }
    public LinkedHashMap<String, Integer> getScores(){
        return scores;
    }
    public LinkedHashMap<String, Integer> getEmptyScores(){
        scores.clear(); 
        String[] headings = {"Ones", "Twos", "Threes",
            "Fours",
            "Fives",
            "Sixes",
            "Small Straight",
            "Large Straight",
            "Three of a kind",
            "Four of a kind",
            "Full House",
            "Chance",
            "Yahtzee"};
        for (String heading: headings){
            scores.put(heading, -1);
        }
        return scores;
    }
    private void countIndividuals(){
        String[] titles = {"Ones", "Twos","Threes", "Fours",
         "Fives", "Sixes"};
        //System.out.println(rolls);
        for (int i = 1; i<7; i++){
            int count = Collections.frequency(rolls, i);
            int value = count*i;
            scores.put(titles[i-1], value);
        }
        ;
    }
    private void checkStraights(){
        ArrayList<Integer> rollsCopy = new ArrayList<Integer>(rolls);
        Collections.sort(rollsCopy);
        Set<Integer> set = new HashSet<>(rollsCopy);
        rollsCopy.clear();
        rollsCopy.addAll(set);
        if (rollsCopy.size()==4 || rollsCopy.size()==5){
            if (rollsCopy.get(1).equals(rollsCopy.get(0)+1) && 
            rollsCopy.get(2).equals(rollsCopy.get(1)+1) &&
            rollsCopy.get(3).equals(rollsCopy.get(2)+1)){

                scores.put("Small Straight", 30);

            }
            else{
                scores.put("Small Straight", 0);
            }
        } else{
            scores.put("Small Straight", 0);
        }
        if (rollsCopy.size()==5){
            if (rollsCopy.get(1).equals(rollsCopy.get(0)+1) && 
            rollsCopy.get(2).equals(rollsCopy.get(1)+1) &&
            rollsCopy.get(3).equals(rollsCopy.get(2)+1) &&
            rollsCopy.get(4).equals(rollsCopy.get(3)+1) ){
    
                scores.put("Large Straight", 40);
    
                }
        } else{
            scores.put("Large Straight", 0);
        }
    }
    private void checkOfAKinds(){
        int max_count = 1;
        for (int i = 1; i < 7; i++) {
            int count = Collections.frequency(rolls, i);
            if (count>max_count){
                max_count=count;
            }
        }
        int rollsSum = rolls.get(0)+rolls.get(1)+rolls.get(2)+rolls.get(3)+rolls.get(4);
            
        if (max_count>=3){
            scores.put("Three of a kind", rollsSum);
            } else{
                scores.put("Three of a kind", 0);
            }
        if (max_count>=4){
            scores.put("Four of a kind", rollsSum);

        } else{
            scores.put("Four of a kind", 0);
        }
    }    

    private void checkYahtzee(){
        Set<Integer> set = new HashSet<>(rolls);
        
        if (set.size()==1){
            scores.put("Yahtzee", 50);
        } else{
            scores.put("Yahtzee", 0);
        }
        }
    
    private void checkFullHouse(){
        Set<Integer>set = new HashSet<>(rolls);
        ArrayList<Integer> counts = new ArrayList<Integer>();
        if (set.size()==2){
            for (int i: set){
                counts.add(Collections.frequency(rolls, i));
            }
            Collections.sort(counts);
            List<Integer> targetList = new ArrayList<>(Arrays.asList(2, 3));
            if (counts.equals(targetList)){
                scores.put("Full House", 25);
            } else{
                scores.put("Full House", 0);
            }
        
        //System.out.println("counts "+counts);
        } else{
            scores.put("Full House", 0);
        }
    }
    private void findChance(){
        int rollsSum = rolls.get(0)+rolls.get(1)+rolls.get(2)+rolls.get(3)+rolls.get(4);
        scores.put("Chance", rollsSum);
    }
}