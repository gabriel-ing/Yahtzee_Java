import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ScoringFunctions{

    private static ArrayList<Integer> rolls;
    private static LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
    
    //public void ScoringFunctions(ArrayList<Integer> rolls){
    //    ArrayList<Integer> this.rolls = rolls;
    //}
    public static void main(String[] digits){
        ArrayList<Integer> list = new ArrayList<>();
        for (char ch : digits[0].toCharArray()) {
            list.add(Character.getNumericValue(ch));
        }
        rolls = list;

        countIndividuals();
        checkStraights();
        checkOfAKinds();
        
        checkFullHouse();
        findChance();
        checkYahtzee();
        
        System.out.println(map);
    }

    private static void countIndividuals(){
        String[] titles = {"Ones", "Twos","Threes", "Fours",
         "Fives", "Sixes"};
        for (int i =1; i<7; i++){
            int count = Collections.frequency(rolls, i);
            int value = count*i;
            map.put(titles[i-1], value);
        }
        ;
    }
    private static void checkStraights(){
        ArrayList<Integer> rollsCopy = new ArrayList<Integer>(rolls);
        Collections.sort(rollsCopy);
        Set<Integer> set = new HashSet<>(rollsCopy);
        rollsCopy.clear();
        rollsCopy.addAll(set);
        if (rollsCopy.size()==4 || rollsCopy.size()==5){
            if (rollsCopy.get(1).equals(rollsCopy.get(0)+1) && 
            rollsCopy.get(2).equals(rollsCopy.get(1)+1) &&
            rollsCopy.get(3).equals(rollsCopy.get(2)+1)){

                map.put("Small Straight", 30);

            }
        } else{
            map.put("Small Straight", 0);
        }
        if (rollsCopy.size()==5){
            if (rollsCopy.get(1).equals(rollsCopy.get(0)+1) && 
            rollsCopy.get(2).equals(rollsCopy.get(1)+1) &&
            rollsCopy.get(3).equals(rollsCopy.get(2)+1) &&
            rollsCopy.get(4).equals(rollsCopy.get(3)+1) ){
    
                map.put("Large Straight", 40);
    
                }
        } else{
            map.put("Large Straight", 0);
        }
    }
    private static void checkOfAKinds(){
        int max_count = 1;
        for (int i = 1; i < 7; i++) {
            int count = Collections.frequency(rolls, i);
            if (count>max_count){
                max_count=count;
            }
        }
        int rollsSum = rolls.get(0)+rolls.get(1)+rolls.get(2)+rolls.get(3)+rolls.get(4);
            
        if (max_count>=3){
            map.put("Three of a kind", rollsSum);
            } else{
                map.put("Three of a kind:", 0);
            }
        if (max_count>=4){
            map.put("Four of a kind", rollsSum);

        } else{
            map.put("Four of a kind", 0);
        }
    }    
    private static void checkSize(){

    }
    private static void checkYahtzee(){
        Set<Integer> set = new HashSet<>(rolls);
        
        if (set.size()==1){
            map.put("Yahtzee", 50);
        } else{
            map.put("Yahtzee", 0);
        }
        }
    
    private static void checkFullHouse(){
        Set<Integer>set = new HashSet<>(rolls);
        ArrayList<Integer> counts = new ArrayList<Integer>();
        if (set.size()==2){
            for (int i: set){
                counts.add(Collections.frequency(rolls, i));
            }
            Collections.sort(counts);
            List<Integer> targetList = new ArrayList<>(Arrays.asList(2, 3));
            if (counts.equals(targetList)){
                map.put("Full House", 25);
            } else{
                map.put("Full House", 0);
            }
        
        System.out.println("counts "+counts);
        } else{
            map.put("Full House", 0);
        }
    }
    private static void findChance(){
        int rollsSum = rolls.get(0)+rolls.get(1)+rolls.get(2)+rolls.get(3)+rolls.get(4);
        map.put("Chance", rollsSum);
    }
}