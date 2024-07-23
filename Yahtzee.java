import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.awt.*;
import java.io.IOException;


class Yahtzee{
    static int nRerolls = 0; 
    private static ArrayList<Integer> rolls = new ArrayList<Integer>();
    private static LinkedHashMap<String, Integer> scores = new LinkedHashMap<>();
    private static LinkedHashMap<String, Integer> addedScores = new LinkedHashMap<>();
    private static int turns = 0;
    public static void main(String args[]){
        //System.out.println(rolls);
        for (int i=0; i<5; i++){
            rolls.add(1);
        }
        //System.out.println(rolls);
        ScoringFunctions initialScoringFunction = new ScoringFunctions(rolls);
        LinkedHashMap<String, Integer> emptyScores = initialScoringFunction.getEmptyScores();
        //System.out.println(emptyScores);
        JFrame frame = new JFrame("Yahtzee Game");


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,650);



        
        JButton rollDice = new JButton("Roll"); 

        JLabel heading = new JLabel("Press the button to roll dice:");
        JLabel rollsLabel = new JLabel("Roll Now");
        JLabel rerollLabel = new JLabel("Select the dice to REROLL!");



        JPanel dicePanel= new JPanel();
        GridLayout dicePanelLayout = new GridLayout(2,5);
        dicePanelLayout.setHgap(5);
        dicePanelLayout.setVgap(1);
        dicePanel.setLayout(dicePanelLayout);

        JLabel[] rollsLabels = new JLabel[5];
        ArrayList<JCheckBox> rerollBoxes = new ArrayList<>();

        BufferedImage[] dieIcons = new BufferedImage[6];
        for (int i=1; i<7; i++){
            String iString = Integer.toString(i);
            String filePath = "Die_icons/Die"+iString+".png";
            
            try{
            dieIcons[i-1] = ImageIO.read(new File(filePath));
            }
            catch(IOException e){
                e.printStackTrace();
            }
            if (i<6){
            rollsLabels[i-1] = new JLabel(new ImageIcon(dieIcons[i-1]));
            dicePanel.add(rollsLabels[i-1]);
        }}

        //for (int i=0; i<5; i++){
        //    JLabel rollLabel = new JLabel("");
        //    rollsLabels[i] = rollLabel;
        //    dicePanel.add(rollLabel);}
        for (int i=0; i<5; i++){     
            JCheckBox checkBox = new JCheckBox("");
            rerollBoxes.add(checkBox);
            dicePanel.add(checkBox);
        }
        frame.getContentPane().add(heading);
        
        frame.getContentPane().setLayout(new java.awt.FlowLayout());
        frame.getContentPane().add(rollDice);
        //frame.add(rollsLabel);
        frame.getContentPane().add(rerollLabel);
        
        

        
        frame.add(dicePanel);
        JPanel scoresPanel = new JPanel();
        GridLayout scoresLayout = new GridLayout(13, 3);
        scoresPanel.setLayout(scoresLayout);

        ArrayList<JLabel> scoresHeadings = new ArrayList<>();
        LinkedHashMap<String, JLabel> scoresLabels = new LinkedHashMap<>();
        LinkedHashMap<String, JButton> keepButtons = new LinkedHashMap<>();
        for  (String key : emptyScores.keySet()){
            //System.out.println(key);
            JLabel scoreHeading = new JLabel(key);
            JLabel scoreLabel = new JLabel("");
            JButton keepButton = new JButton("Keep");

            scoresHeadings.add(scoreHeading);
            scoresLabels.put(key, scoreLabel);
            keepButtons.put(key, keepButton);
            keepButton.setEnabled(false);
            scoresPanel.add(scoreHeading);
            scoresPanel.add(scoreLabel);
            scoresPanel.add(keepButton);
        }

        frame.add(scoresPanel);
        JButton newGameButton = new JButton("Restart Game");
        frame.add(newGameButton);
        JLabel topHalfScoreLabel = new JLabel(""); 
        JLabel bottomHalfScoreLabel = new JLabel("");
        JLabel overallScoreLabel = new JLabel("");
        frame.add(topHalfScoreLabel);
        frame.add(bottomHalfScoreLabel);
        frame.add(overallScoreLabel);
        frame.setVisible(true);

        
        rollDice.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (nRerolls==0){
                    rolls.clear();
                    rolls = YahtzeeFunctions.rollFiveDice();

                    StringBuilder output = new StringBuilder("Rolled: ");

                    for (int i=0; i<5; i++){
                        //String rollString = Integer.toString(rolls.get(i)); 
                        //rollsLabels[i].setText(rollString);
                        rollsLabels[i].setIcon(new ImageIcon(dieIcons[rolls.get(i)-1]));

                    }
                    ScoringFunctions scoringFunctions = new ScoringFunctions(rolls);
                    scores = scoringFunctions.getScores();
                    YahtzeeFunctions.setScoreLabels(scores, scoresLabels, addedScores);
                    for (String key: keepButtons.keySet()){
                        if (!addedScores.containsKey(key)){
                            JButton button = keepButtons.get(key);
                            button.setEnabled(true);
                        }
                    }
                    }
                
                else if (nRerolls<3){
                    
                    ArrayList<Integer> diesToReroll = new ArrayList<>();
                    for (int i=1; i<6; i++){
                        if (rerollBoxes.get(i-1).isSelected()){
                            diesToReroll.add(i);
                    }
                    //System.out.println(rolls);
                    ArrayList<Integer> rerolls = YahtzeeFunctions.reroll(diesToReroll, rolls);

                    StringBuilder output = new StringBuilder("");

                    for (int roll: rerolls){
                        output.append(roll).append(" ");
                    }
                    output.append('\n');
                    rollsLabel.setText(output.toString());
                    System.out.println("rerollshere");
                    System.out.println(rerolls);
                    for (int j=0; j<5; j++){
                        System.out.println(j);
                        rollsLabels[j].setIcon(new ImageIcon(dieIcons[rerolls.get(j)-1]));

                    }
                    ScoringFunctions scoringFunctions = new ScoringFunctions(rolls);
                    scores = scoringFunctions.getScores();
                    YahtzeeFunctions.setScoreLabels(scores, scoresLabels, addedScores);

                    }
                    
                    
                }
                nRerolls++;
                if (nRerolls==3){
                    rollDice.setEnabled(false);

                }
                
                
            };
        });

        int j = 0;
        for (String key: keepButtons.keySet()){
            JButton button = keepButtons.get(key);
            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    rolls.clear();

                    int value = scores.get(key);
                    button.setEnabled(false);
                    scoresLabels.get(key).setForeground(new Color(0,0,0));
                    addedScores.put(key, value);
                    turns++;

                    if (turns<13){
                        for (JCheckBox box: rerollBoxes){
                            box.setSelected(false);}
                        for (JButton b: keepButtons.values()){
                            b.setEnabled(false);
                        }
                        rollsLabel.setText("Roll Now");
                        nRerolls = 0;
                        rollDice.setEnabled(true);
                        
                    } else {

                        int[] finalScores = YahtzeeFunctions.evaluateScores(addedScores);
                        //StringBuilder topScoresText = new StringBuilder();
                        //StringBuilder finalScoresText = new StringBuilder();
                        //StringBuilder finalScoresText = new StringBuilder();
                        //finalScoresText.append("Top Half Score: ").append(finalScores[0]).append('\n');
                        //finalScoresText.append("Bottom Half Score: ").append(finalScores[1]).append('\n');
                        //finalScoresText.append("Overall Score: ").append(finalScores[2]).append('\n');
                        String topHalfScoreString = Integer.toString(finalScores[0]);
                        String bottomHalfString = Integer.toString(finalScores[1]);
                        String overallString = Integer.toString(finalScores[2]);
                        topHalfScoreLabel.setText("Top Half Score: "+ topHalfScoreString);
                        bottomHalfScoreLabel.setText( "Bottom Half Score: "+bottomHalfString);
                        overallScoreLabel.setText( "Final Score: "+overallString);
                        //JTextArea finalScoresLabel = new JTextArea(finalScoresText.toString());
                        //frame.add(finalScoresLabel);
                        newGameButton.setText("New Game");
                        //frame.revalidate();
                        //frame.repaint();
                    }

                }   
            });

        }
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                turns=0; 
                rolls.clear();
                addedScores.clear();
                for (String key: scoresLabels.keySet()){
                    scoresLabels.get(key).setText("");
                    keepButtons.get(key).setEnabled(true);
                }
                nRerolls=0;
                rollDice.setEnabled(true);
                topHalfScoreLabel.setText("");
                bottomHalfScoreLabel.setText("");
                overallScoreLabel.setText("");
                rollsLabel.setText("Roll Now");

                for (JButton button: keepButtons.values()){
                    button.setEnabled(false);
                }
            }
        });

    }
}