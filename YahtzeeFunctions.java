import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import javax.swing.*;
import java.awt.*;


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
                scoresLabels.get(key).setForeground(new Color(0, 128, 15));
                
                }
            }
    }
    public static int[] evaluateScores(LinkedHashMap<String, Integer> addedScores){
        int i = 0;
        int topHalfTotal=0;
        int bottomHalfTotal = 0;
        
        for (String key: addedScores.keySet()){
            System.out.print(key);
            System.out.print(addedScores.get(key));
            System.out.println("");
            if (i<6){
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
