import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.util.List;


public class YahtzeeFunctions{
    public static ArrayList<Integer> rollFiveDice(){
        Random rand = new Random();
        ArrayList<Integer> rolls = new ArrayList<Integer>();
        for (int i=0; i<5; i++){
            rolls.add(rand.nextInt(6) + 1);
        }
        
        return rolls;
    }
    public static ArrayList<Integer> reroll(ArrayList<Integer> diesToReroll, ArrayList<Integer> currentRolls){
        Random rand = new Random();
        for (int i: diesToReroll){
            currentRolls.set(i-1, rand.nextInt(6)+1);
        }
        return currentRolls;
    }
    public static void setScoreLabels(LinkedHashMap<String, Integer> scores, LinkedHashMap<String, JLabel> scoresLabels, LinkedHashMap<String, Integer> addedScores ){
        //System.out.println(scores);
        for (String key: scores.keySet()){
            if (!addedScores.containsKey(key)){
    
                int value = scores.get(key);
                String valueString = Integer.toString(value);
                scoresLabels.get(key).setText(valueString);
                scoresLabels.get(key).setForeground(new Color(255, 20, 20));
                
                }
            }
    }
    public static int[] evaluateScores(LinkedHashMap<String, Integer> addedScores){
        int i = 0;
        int topHalfTotal=0;
        int bottomHalfTotal = 0;
        String[] topHalfKeys = {"Ones", "Twos","Threes", "Fours",
        "Fives", "Sixes"};
        List<String> topHalfKeysList = Arrays.asList(topHalfKeys);
        for (String key: addedScores.keySet()){
            System.out.print(key);
            System.out.print(addedScores.get(key));
            System.out.println("");

            if (topHalfKeysList.contains(key)){
                System.out.println("Tophalf");
                topHalfTotal+= addedScores.get(key);
            } else {
                bottomHalfTotal +=addedScores.get(key);
            }



            i++;
        }
        if (topHalfTotal>=63){
            topHalfTotal+=35;
        }
        int overallTotal = topHalfTotal + bottomHalfTotal;
        int[] finalScores = {topHalfTotal, bottomHalfTotal, overallTotal};
        return finalScores;
    }
}
