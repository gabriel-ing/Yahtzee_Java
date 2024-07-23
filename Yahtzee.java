import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.imageio.ImageIO;
import javax.swing.*;


class Yahtzee{
    static int nRerolls = 0; 
    private static ArrayList<Integer> rolls = new ArrayList<Integer>();
    private static LinkedHashMap<String, Integer> scores = new LinkedHashMap<>();
    private static ArrayList<LinkedHashMap<String, Integer>> addedScores = new ArrayList<>();
    private static int turns = 0;

    private static int numPlayers = 0;
    private static int playerTurn = 0;
    private static ArrayList<String> playerNames = new ArrayList<>(); 
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
        showDialog(frame);
        System.out.println(playerNames);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,800);



        
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
        GridLayout scoresLayout = new GridLayout(14, 2+numPlayers);
        scoresLayout.setHgap(3);
        scoresPanel.setLayout(scoresLayout);
        JLabel handLabel = new JLabel("Scores");
        scoresPanel.add(handLabel);
        for (String player: playerNames){
            System.out.println(player);
            JLabel playerLabel = new JLabel(player);
            scoresPanel.add(playerLabel);
            addedScores.add(new LinkedHashMap<String, Integer>());
        }
        JLabel emptyLabel = new JLabel("");
        scoresPanel.add(emptyLabel);
        scoresPanel.revalidate();
        scoresPanel.repaint();

        ArrayList<JLabel> scoresHeadings = new ArrayList<>();
        ArrayList<LinkedHashMap<String, JLabel>> scoresLabels = new ArrayList<>(numPlayers);
        LinkedHashMap<String, JButton> keepButtons = new LinkedHashMap<>();
        for (int i=0; i<numPlayers; i++){
            scoresLabels.add(new LinkedHashMap<String, JLabel>());
        }
        for  (String key : emptyScores.keySet()){
            //System.out.println(key);
            JLabel scoreHeading = new JLabel(key);

            JButton keepButton = new JButton("Keep");

            scoresHeadings.add(scoreHeading);
            scoresPanel.add(scoreHeading);
            for (int i=0; i<numPlayers; i++){
                JLabel scoreLabel = new JLabel("");
                scoresPanel.add(scoreLabel);
                scoresLabels.get(i).put(key, scoreLabel);
            }
            keepButtons.put(key, keepButton);
            keepButton.setEnabled(false);
            
            
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
                    System.out.println(playerNames);
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
                    YahtzeeFunctions.setScoreLabels(scores, scoresLabels.get(playerTurn), addedScores.get(playerTurn));
                    for (String key: keepButtons.keySet()){
                        if (!addedScores.get(playerTurn).containsKey(key)){
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
                    YahtzeeFunctions.setScoreLabels(scores, scoresLabels.get(playerTurn), addedScores.get(playerTurn));

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
                    scoresLabels.get(playerTurn).get(key).setForeground(new Color(0,0,0));
                    addedScores.get(playerTurn).put(key, value);
                    


                    if (turns<12 || !(playerTurn==numPlayers-1)){
                        for (JCheckBox box: rerollBoxes){
                            box.setSelected(false);}
                        for (JButton b: keepButtons.values()){
                            b.setEnabled(false);
                        }
                        rollsLabel.setText("Roll Now");
                        nRerolls = 0;
                        rollDice.setEnabled(true);
                        
                    } else {
                        JPanel finalScoresPanel = new JPanel();
                        GridLayout finalScoresLayout = new GridLayout(3, numPlayers+2);
                        finalScoresLayout.setHgap(3);
                        finalScoresPanel.setLayout(finalScoresLayout);
                        
                        
                        String[] scoreHeadings = {"Top Section Score", "Bottom Section Score", "Final Score"};
                        String[] topHalfScores = new String[3];
                        String[] bottomHalfScores = new String[3];
                        String[] finalOverallScores = new String[3];
                        
                        for (int i= 0; i<numPlayers; i++){
                            int[] finalScores = YahtzeeFunctions.evaluateScores(addedScores.get(i));

                            topHalfScores[i]=  Integer.toString(finalScores[0]);
                            bottomHalfScores[i] =  Integer.toString(finalScores[1]);
                            finalOverallScores[i] = Integer.toString(finalScores[2]);
                            }
                        String[][] finalScoresArrayList = {topHalfScores, bottomHalfScores, finalOverallScores};
                        for (int i=0; i<3; i++){
                            finalScoresPanel.add(new JLabel(scoreHeadings[i]));
                            for (int j=0; j<numPlayers; j++){
                                finalScoresPanel.add(new JLabel(finalScoresArrayList[i][j]));
                            }
                            finalScoresPanel.add(new JLabel(""));
                        }
                        newGameButton.setText("New Game");
                        frame.add(finalScoresPanel);
                        frame.revalidate();
                        frame.repaint(); 
                    }
                    for (String key: scoresLabels.get(playerTurn).keySet()){
                        if (!addedScores.get(playerTurn).containsKey(key)){
                            scoresLabels.get(playerTurn).get(key).setText("");
                        }
                    }
                    if (playerTurn<numPlayers-1){
                        playerTurn++;
                        
                    }else{
                        playerTurn=0;
                        turns++;
                    }

                }   
            });

        }
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                turns=0; 
                playerNames.clear();
                rolls.clear();
                addedScores.clear();
                for (String key: scoresLabels.get(playerTurn).keySet()){
                    scoresLabels.get(playerTurn).get(key).setText("");
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
                showDialog(frame);
            }
        });

    }
    private static void showDialog(JFrame parentFrame){
        JPanel panel = new JPanel();
        JLabel welcomeLabel = new JLabel("Welcome to Yahtzee!");
        JLabel selectPlayersLabel = new JLabel("Select the number of players:");
        panel.add(welcomeLabel);
        panel.add(selectPlayersLabel);

        Integer[] numbers = {1,2,3,4,5,6,7,8,9};
        JComboBox<Integer> nPlayersBox = new JComboBox<>(numbers);
        panel.add(nPlayersBox);

        JButton getNumPlayers = new JButton("Select Number");
        panel.add(getNumPlayers);
        //JOptionPane.showConfirmDialog(
        //    parentFrame,
        //    panel,
        //   "Player Information",
        //   JOptionPane.OK_CANCEL_OPTION,
        //    JOptionPane.PLAIN_MESSAGE
        //);

        JDialog inputDialog = new JDialog(parentFrame, "Welcome", true);
        
        inputDialog.setLayout(new BorderLayout());
        inputDialog.setSize(300, 400);
        inputDialog.add(panel,BorderLayout.CENTER);
        inputDialog.setLocationRelativeTo(parentFrame);
        
        getNumPlayers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                System.out.println("Yes");
                Integer selectedNumber = (Integer) nPlayersBox.getSelectedItem();
                numPlayers = selectedNumber.intValue();
                getNumPlayers.setEnabled(false);
                JPanel inputNamesPanel = new JPanel();
                inputNamesPanel.setLayout(new GridLayout(numPlayers, 2));
                JTextField[] nameFields = new JTextField[numPlayers];
                for (int i=0; i<numPlayers; i++){
                    String iString = Integer.toString(i+1);
                    JLabel playerNameLabel = new JLabel("Player "+iString+" Initials: ");
                    JTextField playerNameBox = new JTextField();
                    nameFields[i] = playerNameBox;
                    inputNamesPanel.add(playerNameLabel,BorderLayout.SOUTH);
                    inputNamesPanel.add(playerNameBox, BorderLayout.SOUTH);}
                panel.add(inputNamesPanel);
                
                JPanel buttonPanel = new JPanel();
                JButton enterGameButton = new JButton("Enter Game");

                buttonPanel.add(enterGameButton);

                enterGameButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        for (int j=0; j<numPlayers; j++){
                            String playerName = nameFields[j].getText();
                            if (playerName.equals("")){
                                playerName = "P"+Integer.toString(j+1);
                            } 
                            playerNames.add(playerName);
                        }
                        parentFrame.repaint();
                        inputDialog.dispose();
                    }
                });
                panel.add(buttonPanel);
                panel.revalidate();
                panel.repaint();
                
            }

        });
        inputDialog.setVisible(true);


        

    }
    
}