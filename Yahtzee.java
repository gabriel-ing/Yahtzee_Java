import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

class Yahtzee{
    static int nRerolls = 0; 
    private static ArrayList<Integer> rolls = new ArrayList<Integer>();


    public static void main(String args[]){
        JFrame frame = new JFrame("Yahtzee Game");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);



        
        JButton rollDice = new JButton("Roll");

        JLabel heading = new JLabel("Press the button to roll dice:");
        JLabel rollsLabel = new JLabel("");
        JLabel rerollLabel = new JLabel("Select the dice to REROLL!");






        frame.getContentPane().add(heading);
        frame.getContentPane().setLayout(new java.awt.FlowLayout());
        frame.getContentPane().add(rollDice);
        frame.getContentPane().add(rollsLabel);

        frame.getContentPane().add(rerollLabel);

        ArrayList<JCheckBox> rerollBoxes = new ArrayList<>();

        for (int i=1; i<6; i++){
            JCheckBox checkBox = new JCheckBox(""+i);
            rerollBoxes.add(checkBox);
            frame.add(checkBox);
        }

        frame.setVisible(true);

        rollDice.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (nRerolls==0){
                    rolls = YahtzeeFunctions.rollFiveDice();

                    StringBuilder output = new StringBuilder("Rolled: ");

                    for (int roll: rolls){
                        output.append(roll).append(" ");
                    }
                    output.append('\n');
                    rollsLabel.setText(output.toString());
                }
                else if (nRerolls<3){
                    
                    ArrayList<Integer> diesToReroll = new ArrayList<>();
                    for (int i=1; i<6; i++){
                        
                        if (rerollBoxes.get(i-1).isSelected()){
                            diesToReroll.add(i);
                    }
                    System.out.println(rolls);
                    ArrayList<Integer> rerolls = YahtzeeFunctions.reroll(diesToReroll, rolls);
                    StringBuilder output = new StringBuilder("");

                    for (int roll: rerolls){
                        output.append(roll).append(" ");
                    }
                    output.append('\n');
                    rollsLabel.setText(output.toString());
                    }
                    
                    
                }
                nRerolls++;
                
            };
        });

    }

}