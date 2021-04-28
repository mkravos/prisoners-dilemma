/*
    PDGameGui.java
    Mike Kravos
    2020-09-27

    Interactive GUI for the Prisoner's Dilemma game.
*/

import java.awt.BorderLayout; 
import java.awt.Color;
import static java.awt.Color.WHITE;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class PDGameGui extends JFrame implements ActionListener, ListSelectionListener {
    // Instance variables
    private final int roundsTP = 5; // rounds to play in one game

    private final DefaultListModel<String> listModelPtr = new DefaultListModel<String>();
        // Default List model is the standard "mode" for how a Jlist will be operated, will put in next statement below
    private JList<String> finishedGamesListPtr = new JList<String>(listModelPtr); 
        // this is list on top left side and will show times of games played that user will click to see stats of a game
    private JComboBox<Object> computerStrategyCB = null; 
        // combo box on right side, pointer will be filled in constructor
    private JTextArea gameResultsTA = new JTextArea(15, 30); // this is large text area on right side
    private PDGame currentPDGame = null; // pointer to PDGame object for current game
    private GameStat gameStatsInfo = null; // pointer to GameStat object
    private String gameStartTimeStr = null; // game start time string
    private final HashMap<String, GameStat> stats = new HashMap<>(); // keep same hashmap for games played
    private int computerStrat = 1; // this will be filled in by the choice made by user in combo box
    private JList<String> finishedGamesList = null;
    private String searchKey = null;
    private String decision = null;
    private int roundCtr = 0;
    
    // Text fields, labels and buttons
    private final JTextField roundsTF = new JTextField(10);
    private final JTextField computerStrategyTF = new JTextField(10);
    private final JTextField playerSentenceTF = new JTextField(10);
    private final JTextField computerSentenceTF = new JTextField(10);
    private final JTextField gameWinnerTF = new JTextField(10);
    private final JLabel roundsplayed = new JLabel("Rounds Played");
    private final JLabel computerstrategy = new JLabel("Computer Strategy");
    private final JLabel playersentence = new JLabel("Player Sentence");
    private final JLabel computersentence = new JLabel("Computer Sentence");
    private final JLabel gamewinner = new JLabel("Winner");

    private final JLabel computerStrategyL = new JLabel("Computer Strategy");

    private final JButton startB = new JButton("Start New Game");
    private final JButton silentB = new JButton("Remain Silent");
    private final JButton betrayB = new JButton("Testify");
    private final JLabel decisionL = new JLabel("Your decision this round?");

    // Main function calls createAndShowGUI() function
    public static void main(String[] args) {
        createAndShowGUI();
    }

    // Creates and shows the GUI
    public static void createAndShowGUI() {
        // Create and set up the window
        PDGameGui pdg1 = new PDGameGui(); // call constructor below to set the window to user
        pdg1.addListeners(); // method will add listeners to buttons

        // Display the window and pack together all the panels, etc.
        pdg1.pack(); // pack together the panels
        pdg1.setDefaultCloseOperation(EXIT_ON_CLOSE); // make program end when window closed
        pdg1.setVisible(true); // make the GUI visible
    }

    public PDGameGui() {
        super("Prisoner's Dilemma"); // fills in the menu of jframe with message
        currentPDGame = new PDGame(); // initialize new PDGame object

        setLayout(new BorderLayout()); // set frame to border layout

        // Create custom colors
        Color c1 = new Color(0,204,204);
        Color c2 = new Color(255, 153, 153);

        // Set up left panel
        JPanel panel1 = new JPanel(new BorderLayout(0,10)); // create panel
        panel1.setBackground(c1); // set color of panel
        // JList of Games holding a List Model
        finishedGamesList = finishedGamesListPtr;
        add(panel1, BorderLayout.WEST); // add to frame
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        panel1.setBorder(BorderFactory.createTitledBorder(loweredetched, "List of Games"));

        finishedGamesList.setFont(new Font("SansSerif", Font.BOLD, 24));
        finishedGamesList.setVisibleRowCount(10);
        finishedGamesList.setFixedCellWidth(350);
        finishedGamesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel1.add(new JScrollPane(finishedGamesList), BorderLayout.CENTER);

        JPanel panel2 = new JPanel(new GridLayout(5,2,0,5)); // create grid panel
        panel2.setBackground(c1);
        panel1.add(panel2, BorderLayout.SOUTH); // add to panel 1
        
        // Set up right panel
        JPanel panel3 = new JPanel(new BorderLayout());
        add(panel3, BorderLayout.EAST);
        panel3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        gameResultsTA.setEditable(false);
        JScrollPane gameResultsList = new JScrollPane(gameResultsTA);
        panel3.add(gameResultsList, BorderLayout.SOUTH);

        JPanel panel4 = new JPanel(new GridLayout(2,1));
        panel3.add(panel4, BorderLayout.NORTH);

        JPanel panel5 = new JPanel(new FlowLayout());
        JPanel panel6 = new JPanel(new FlowLayout());
        panel5.setBackground(c2);
        panel6.setBackground(c2);
        panel4.add(panel5);
        panel4.add(panel6);

        panel6.add(decisionL);
        panel6.add(silentB);
        panel6.add(betrayB);

        // fill in other panels, grids, etc.
        roundsTF.setEditable(false);
        panel2.add(roundsplayed);
        panel2.add(roundsTF);
        computerStrategyTF.setEditable(false);
        panel2.add(computerstrategy);
        panel2.add(computerStrategyTF);
        playerSentenceTF.setEditable(false);
        panel2.add(playersentence);
        panel2.add(playerSentenceTF);
        computerSentenceTF.setEditable(false);
        panel2.add(computersentence);
        panel2.add(computerSentenceTF);
        gameWinnerTF.setEditable(false);
        panel2.add(gamewinner);
        panel2.add(gameWinnerTF);

        // Two statements below prepare the combo box with computer strategies, need to comvert the
        // strategies array list to an array, and then it gets placed in combo box
        Object[] strategyArray = currentPDGame.getStrategies().toArray(); // convert AL to array
        computerStrategyCB = new JComboBox<Object>(strategyArray); // place array in combo box
        computerStrategyCB.setEditable(false);
        computerStrategyCB.setSelectedIndex(0); // this sets starting value to first string in array
        panel5.add(computerStrategyL);
        panel5.add(computerStrategyCB);
        panel5.add(startB);

        decisionL.setEnabled(false);
        silentB.setEnabled(false);
        betrayB.setEnabled(false);
    }

    // Hook up listeners to buttons
    public void addListeners() {
        computerStrategyCB.addActionListener(this); // add to combobox
        startB.addActionListener(this); // add to start button
        silentB.addActionListener(this); // add to silent button
        betrayB.addActionListener(this); // add to testify button
        finishedGamesListPtr.addListSelectionListener(this); // the JLIST event listener code is addListSectionListener
    }

    // Handles what button was clicked and what was chosen by combo box, and calls appropriate method.
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startB) {
            startGame();

            computerStrategyL.setEnabled(false);
            computerStrategyCB.setEnabled(false);
            startB.setEnabled(false);

            decisionL.setEnabled(true);
            silentB.setEnabled(true);
            betrayB.setEnabled(true);
        } else if (e.getSource() == silentB) {
            roundCtr++;
            cooperate();
        } else if (e.getSource() == betrayB) {
            roundCtr++;
            betray();
        } else if (e.getSource() == computerStrategyCB) { // when user chooses an item in combo box, this handles it
            computerStrat = computerStrategyCB.getSelectedIndex() + 1; // fills in this variable up top
        }

        if(roundCtr==roundsTP) endGame();
    }

    // Starts a new game session and prompts player
    public void startGame() {
        currentPDGame = new PDGame(); // initialize new PDGame object
        currentPDGame.setStrategy(computerStrat);
        currentPDGame.getStats().setRound(1);
        
        gameStartTimeStr = new Date().toString();
        stats.put(gameStartTimeStr, currentPDGame.getStats());

        gameResultsTA.setText(""); // clear any text left by previous game
        gameResultsTA.append("***Prisoner's Dilemma***\n\n");
        promptPlayer();
    }
    // Prompts player
    public void promptPlayer() {
        gameResultsTA.append("1. Cooperate with your partner and remain silent.\n2. Betray and testify against your partner.\n\nWhat is your decision this round?\n\n");
    }
    // Remain silent
    public void cooperate() {
        currentPDGame.getStats().setRound(roundCtr);
        decision = currentPDGame.playRound(1);
        gameResultsTA.append(decision+"\n\n");
        if(roundCtr!=5) promptPlayer();
    }
    // Testify
    public void betray() {
        currentPDGame.getStats().setRound(roundCtr);
        decision = currentPDGame.playRound(2);
        gameResultsTA.append(decision+"\n\n");
        if(roundCtr!=5) promptPlayer();
    }
    // End the game
    public void endGame() {
        roundCtr = 0; // set round counter back to 0

        computerStrategyL.setEnabled(true);
        computerStrategyCB.setEnabled(true);
        startB.setEnabled(true);

        decisionL.setEnabled(false);
        silentB.setEnabled(false);
        betrayB.setEnabled(false);

        gameResultsTA.append(currentPDGame.getScores());
        listModelPtr.addElement(gameStartTimeStr);
    }

    // User has clicked on a finished game in upper left JList box,
    // this shows results from game
    public void valueChanged(ListSelectionEvent e) {
        if(!finishedGamesList.isSelectionEmpty()) {
            searchKey = (String)finishedGamesList.getSelectedValue(); // get out time of game
            gameStatsInfo = stats.get(searchKey); // get the GameStat object
            int playerSentenceYrs = gameStatsInfo.getUserSentence();
            int computerSentenceYrs = gameStatsInfo.getComputerSentence();

            roundsTF.setFont(new Font("SansSerif", Font.BOLD, 12));
            roundsTF.setText(Integer.valueOf(gameStatsInfo.getRound()).toString());

            computerStrategyTF.setFont(new Font("SansSerif", Font.BOLD, 12));
            computerStrategyTF.setText(gameStatsInfo.getComputerStrategy());

            playerSentenceTF.setFont(new Font("SansSerif", Font.BOLD, 12));
            playerSentenceTF.setText(String.format("%d%s", playerSentenceYrs, ((playerSentenceYrs>1) ? " years" : " year")));
        
            computerSentenceTF.setFont(new Font("SansSerif", Font.BOLD, 12));
            computerSentenceTF.setText(String.format("%d%s", computerSentenceYrs, ((computerSentenceYrs>1) ? " years" : " year")));
        
            gameWinnerTF.setFont(new Font("SansSerif", Font.BOLD, 12));
            gameWinnerTF.setText(gameStatsInfo.getWinner());
        }
    }

}