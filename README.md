# Yahtzee_Java

A Java-based Application of the classic game Yahtzee.

The rules for Yahtzee are simple - each player rolls the five dice, and then can reroll any number of the dice two times. The aim is to fill in 13 different 'hands':

Upper section - scored by the total value of the dice showing the required number, i.e. if two 4 are rolled, you can score 8 in the fours box. 

    - Ones
    - Twos
    - Threes
    - Fours
    - Fives
    - Sixes 

A bonus is available if the upper section total is 63 or more. To achieve this, aim to get three of a kind for each box. 

Lower Section:

    - Three-of-a-kind: If there are three of any one digit in the roll, score this as the total sum of all the dice faces. 
    - Four-of-a-kind:  If there are four of any one digit in the roll, score this as the total sum of all the dice faces. 
    - Full House: Three of one digit and two of another. Score 25 points.  
    - Small Straight: Four consecutive digits within the five faces. Score 30 points. 
    - Large Straight: Five consecutive digits within the five faces. Score 40 points. 
    - Yahtzee: Five of the same digit. Score 50 points. 


### Usage 
 Download the files and open the directory in a terminal. Compile Yahtzee.java (`javac Yahtzee.java`) and then run with `java Yahtzee`.  

 A dialog window will open up prompting for the number of players: 

<img src=ExampleImages/initialDialog.png alt='Dialog box asking for the number of players and player names' width=200>

Select the number of players and add in player names (optional), then click `Create Game`, a game will be created: 

<img src=ExampleImages/gameDisplay.png alt='Dialog box asking for the number of players and player names' width=400>

Each turn, Click `Roll Dice`, then select any number dice to reroll using the checkboxes. The scores available for each box will appear in Red. Click the `Keep` button to fill these boxes. 

The final scores will be printed at the end. 
