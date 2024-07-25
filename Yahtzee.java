/* Implementation of the classic dice game Yahtzee with a basic user interface
 * The scoring functions and additional game functions (roll dice and evaluate scores) are held in separate files. 
 * This file contains the UI and general game structure.  
 * 
 * Rather than have a game loop, this is structured as a continuous process responding to two different types of button clicks:
 * 
 * Roll the Dice
 * Keep the score
 * 
 * If after three rolls, the roll dice button is turned off until the roll is scores with the keep score buttons.
 * 
 * After the final player presses the final keep score button (13th time) the game ends and the scores are evaluated.  
 * 
 */
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
    // create variables - I have created them here to give them global scope within the file so I can change them from anywhere. 
    //Game parameters
    static int nRerolls = 0; 
    private static ArrayList<Integer> rolls = new ArrayList<Integer>();
    private static LinkedHashMap<String, Integer> scores = new LinkedHashMap<>();
    private static ArrayList<LinkedHashMap<String, Integer>> addedScores = new ArrayList<>();

    private static int turns = 0;
    private static int numPlayers = 0;
    private static int playerTurn = 0;
    private static ArrayList<String> playerNames = new ArrayList<>(); 

    //UI components
    private static BufferedImage[] dieIcons = new BufferedImage[6];
    private static JLabel[] rollsLabels = new JLabel[5];
    private static ArrayList<JLabel> scoresHeadings = new ArrayList<>();
    private static ArrayList<LinkedHashMap<String, JLabel>> scoresLabels = new ArrayList<>(numPlayers);
    private static LinkedHashMap<String, JButton> keepButtons = new LinkedHashMap<>();
    private static JLabel rerollLabel = new JLabel("When you are ready, press to roll dice.");
    private static ArrayList<JCheckBox> rerollBoxes;
    private static JButton rollDice; 
    private static JFrame frame;
    private static String playerTurnText ="Player 1's turn to begin";
    private static JLabel playerTurnLabel = new JLabel(playerTurnText);
    private static GridBagConstraints frameGBC;

    //Creates UI and adds functionality to the buttons
    public void createGame(){
        
        frame = new JFrame("Yahtzee");
        
        //Ensure variables set to zero
        nRerolls = 0;
        
        //Create default rolls
        rolls.clear();
        
        for (int i=0; i<5; i++){
            rolls.add(1);
        }

        // Create the app frame
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 400+numPlayers*60;
        frame.setSize(width,800);
        frame.setLayout(new GridBagLayout());
        
        frameGBC = new GridBagConstraints();

        frameGBC.weightx = 1;
        frameGBC.weighty=1;


        //Create pop-up for player details
        showDialog(frame);
        
        // Create heading panel

        JPanel headingPanel = createHeadingPanel();

        frameGBC.gridx=0;
        frameGBC.gridy=0;
        frame.add(headingPanel, frameGBC);

        // Create dice panel
        JPanel dicePanel = createDice();

        frameGBC.gridx=0;
        frameGBC.gridy=1;
        frame.add(dicePanel, frameGBC);

        //Create Scores Panel
        JPanel scoresPanel = createScoresPanel();
        frameGBC.gridx=0;
        frameGBC.gridy=2;
        frame.add(scoresPanel, frameGBC);

        //Create a button to start a new game
        frameGBC.gridx=0;
        frameGBC.gridy=4;
        JButton newGameButton = new JButton("Restart Game");
        frame.add(newGameButton, frameGBC);
        frame.setVisible(true);

        
        // Add functionality to the roll dice button
        rollDice.addActionListener(this::rollDiceAction);
        
        // create the keep score buttons and add the functionality to these. 
        for (String key: keepButtons.keySet()){
            JButton button = keepButtons.get(key);
            button.addActionListener(e -> keepButtonAction(key));

        }

        // add functionality to the new game  button
        newGameButton.addActionListener(this::newGmeAction);

    }
    private void rollDiceAction(ActionEvent e){
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
            YahtzeeFunctions.setScoreLabels(scores, scoresLabels.get(playerTurn), addedScores.get(playerTurn));
            for (String key: keepButtons.keySet()){
                if (!addedScores.get(playerTurn).containsKey(key)){
                    JButton button = keepButtons.get(key);
                    button.setEnabled(true);
                }
            }
            nRerolls++;
            rerollLabel.setText("Select the Dice to re-roll!");
        }
        
        else if (nRerolls<3){
            
            ArrayList<Integer> diesToReroll = new ArrayList<>();

            for (int i=1; i<6; i++){
                if (rerollBoxes.get(i-1).isSelected()){
                        diesToReroll.add(i);
                        
            }}

            if (diesToReroll.size()>0){
                //System.out.println(rolls);
                ArrayList<Integer> rerolls = YahtzeeFunctions.reroll(diesToReroll, rolls);

                
                for (int j=0; j<5; j++){
                    //System.out.println(j);
                    rollsLabels[j].setIcon(new ImageIcon(dieIcons[rerolls.get(j)-1]));

                }
                ScoringFunctions scoringFunctions = new ScoringFunctions(rolls);
                scores = scoringFunctions.getScores();
                YahtzeeFunctions.setScoreLabels(scores, scoresLabels.get(playerTurn), addedScores.get(playerTurn));
                nRerolls++;
                }
        }
        
        
        if (nRerolls==3){
            rollDice.setEnabled(false);

        }
    };
    public void keepButtonAction(String inputKey){
        rolls.clear();

        int value = scores.get(inputKey);
        keepButtons.get(inputKey).setEnabled(false);
        scoresLabels.get(playerTurn).get(inputKey).setForeground(new Color(0,0,0));
        addedScores.get(playerTurn).put(inputKey, value);
        nRerolls = 0;
        


        if (turns<12 || !(playerTurn==numPlayers-1)){
            for (JCheckBox box: rerollBoxes){
                box.setSelected(false);}
            for (JButton b: keepButtons.values()){
                b.setEnabled(false);
            }
            
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
            //newGameButton.setText("New Game");
            
            frameGBC.gridx=0;
            frameGBC.gridy=3;
            frame.add(finalScoresPanel, frameGBC);
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
            String playerTurnString= playerNames.get(playerTurn)+"'s Turn!";
            playerTurnLabel.setText(playerTurnString);
            
        }else{
            playerTurn=0;
            turns++;
            String playerTurnString= playerNames.get(playerTurn)+"'s Turn!";
            playerTurnLabel.setText(playerTurnString);
        }
        rerollLabel.setText("Press Roll Dice!");

    }   

    public void newGmeAction(ActionEvent e){
        turns=0; 
        playerNames.clear();
        rolls.clear();
        addedScores.clear();
        frame.dispose();
        
        main(new String[] {});
        
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
                //System.out.println("Yes");
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
                JButton enterGameButton = new JButton("Create Game");

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
    private static JPanel createDice(){
        JPanel dicePanel= new JPanel();
        GridLayout dicePanelLayout = new GridLayout(2,5);
        dicePanelLayout.setHgap(5);
        dicePanelLayout.setVgap(1);
        dicePanel.setLayout(dicePanelLayout);
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
        }
    }   
        rerollBoxes = new ArrayList<>();
        for (int j=0; j<5; j++){     
            JCheckBox checkBox = new JCheckBox("");
            rerollBoxes.add(checkBox);
            dicePanel.add(checkBox);
        }
        return dicePanel;
    
    }

    public static JPanel createHeadingPanel(){
        JPanel headingPanel = new JPanel();
        GridBagLayout headingLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        headingPanel.setLayout(headingLayout);
        String welcomeText = "Welcome To Yahtzee! ";
        JLabel heading = new JLabel(welcomeText);

        gbc.gridx = 0;
        gbc.gridy = 0;
        headingPanel.add(heading, gbc);

        playerTurnLabel.setFont(new Font(playerTurnLabel.getFont().getName(), Font.BOLD, 28));
        playerTurnLabel.setForeground(new Color(255, 25,25));
        gbc.gridx = 0;
        gbc.gridy = 1;
        headingPanel.add(playerTurnLabel, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        headingPanel.add(rerollLabel, gbc);
        
        rollDice = new JButton("Roll Dice");
        rollDice.setPreferredSize(new Dimension(200, 100));
        rollDice.setFont(new Font(rollDice.getFont().getName(), Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 3;
        headingPanel.add(rollDice, gbc);
        return headingPanel;
    }
    public static JPanel createScoresPanel(){
        JPanel scoresPanel = new JPanel();

        //Initialise an empty score sheet to help structure the frame.
        ScoringFunctions initialScoringFunction = new ScoringFunctions(rolls);
        LinkedHashMap<String, Integer> emptyScores = initialScoringFunction.getEmptyScores();


        GridLayout scoresLayout = new GridLayout(14, 2+numPlayers);
        scoresLayout.setHgap(3);
        scoresPanel.setLayout(scoresLayout);
        JLabel handLabel = new JLabel("Scores");
        scoresPanel.add(handLabel);
        for (String player: playerNames){
            //System.out.println(player);
            JLabel playerLabel = new JLabel(player);
            scoresPanel.add(playerLabel);
            addedScores.add(new LinkedHashMap<String, Integer>());
        }
        JLabel emptyLabel = new JLabel("");
        scoresPanel.add(emptyLabel);
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
        return scoresPanel;
    }
    public static void main(String args[]){
        Yahtzee game = new Yahtzee();
        game.createGame();
    }
}