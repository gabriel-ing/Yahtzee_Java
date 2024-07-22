import java.util.ArrayList;
import java.util.Random;


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
}
