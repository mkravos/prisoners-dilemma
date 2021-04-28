/*
    PDGame.java
    Mike Kravos
    2020-09-27

    Decision processing for the Prisoner's Dilemma game.
*/

import java.util.Scanner;
import java.util.ArrayList;

public class PDGame {
    private GameStat gsPtr = new GameStat(); // declare gamestat reference object
    private ArrayList<String> strats = new ArrayList<String>(); // declare strategies arraylist
    private ArrayList<Integer> hist = new ArrayList<Integer>(); // declare history arraylist
    private int cstrat = 0; // declare computer strategy data member
    private int cdecision = 0; // declare a computer decision data member

    public PDGame() { // populates strategies arraylist with the 3 implemented strategies
        strats.add("Tit-For-Tat");
        strats.add("Tit-For-Two-Tats");
        strats.add("Random");
    }

    public String playRound(int decision) {
        if (decision == 1 || decision == 2) { // Check if decision is valid
            if(cstrat == 1) { // If computer strategy is 1, call TitForTat
                TitForTat(decision); // run titfortat strategy
            } else if (cstrat == 2) { // If computer strategy is 2, call TitForTwoTats
                TitForTwoTats(decision); // run titfortwotats strategy
            } else if (cstrat == 3) { // If computer strategy is 3, call Random
                Random(decision); // run random strategy
            }

            if(decision==1 && cdecision == 1) { // If both remained silent
                return "You and your partner remain silent.\nYou both get 2 years in prison.";
            } else if(decision==2 && cdecision==2) { // If both testified
                return "You and your partner testify against eachother.\nYou both get 3 years in prison.";
            } else if(decision==1 && cdecision==2) { // If player stayed silent and computer testified
                return "You remain silent and your partner testifies against you.\nYou get 5 years in prison and they get 1.";
            } else if(decision==2 && cdecision==1) { // If player testified and computer stayed silent
                return "You testify against your partner and they remain silent.\nYou get 1 year in prison and they get 5.";
            }
        }
        return "wtf";
    }

    // Tit-For-Tat – Cooperate on the first move, otherwise play the player's last move.
    public void TitForTat(int decision) {
        hist.add(decision); // add move to player history arraylist
        if(gsPtr.getRound() == 1) { // If it is round 1
            cdecision = 1;
            if(decision == 1) { // If player stays silent
                gsPtr.update(2,2); // Both get 2 years
            } else { // If player testifies
                gsPtr.update(1,5); // Player gets 1 year, computer gets 5 years
            }
        } else { // If it is round 2-5
            if(hist.get(gsPtr.getRound()-1) == 1) { // If player stayed silent, computer stays silent
                cdecision = 1;
                if(decision == 1) gsPtr.update(2,2); // Both get 2 years if player stays silent this round
                else gsPtr.update(1,5); // Otherwise, player gets 1 year and computer gets 5 years
            } else { // If player testified, computer testifies.
                cdecision = 2;
                if(decision == 2) gsPtr.update(3,3); // Both get 3 years if they both testify this round
                else gsPtr.update(5,1); // Otherwise, player gets 5 years and computer gets 1 year
            }
        }
    }

    // Tit-For-Two-Tats – Cooperate on the first 2 moves, otherwise betray if the player's last 2 moves were betrayals.
    public void TitForTwoTats(int decision) {
        hist.add(decision); // add move to player history arraylist
        if(gsPtr.getRound() == 1 || gsPtr.getRound() == 2) { // If it is round 1 or 2
            cdecision = 1;
            if(decision == 1) { // If player stays silent
                gsPtr.update(2,2); // Both get 2 years
            } else { // If player testifies
                gsPtr.update(1,5); // Player gets 1 year, computer gets 5 years
            }
        } else { // If it is round 3-5
            if(hist.get(gsPtr.getRound()-2) == 1 && hist.get(gsPtr.getRound()-3) == 1) { // If player stayed silent twice, computer stays silent.
                cdecision = 1;
                if(decision == 1) gsPtr.update(2,2); // Both get 2 years if player stays silent this round
                else gsPtr.update(1,5); // Otherwise, player gets 1 year and computer gets 5 years
            } else { // If player testified twice, computer testifies.
                cdecision = 2;
                if(decision == 2) gsPtr.update(3,3); // Both get 3 years if they both testify this round
                else gsPtr.update(5,1); // Otherwise, player gets 5 years and computer gets 1 year
            }
        }
    }

    // Random –  Random number of 1 or 2.
    public void Random(int decision) {
        cdecision = (int)(Math.random()*2+1); // picks randomly between decision 1 and 2 for computer
        if(cdecision == 1) { // If computer stays silent
            if(decision == 1) { // If player stays silent
                gsPtr.update(2,2); // Both get 2 years
            } else if(decision == 2) { // If player testifies
                gsPtr.update(1,5); // Player gets 1 year, computer gets 5 years
            }
        } else if(cdecision == 2) { // If computer testifies
            if(decision == 1) { // If player stays silent
                gsPtr.update(5,1); // Player gets 5 years, computer gets 1 year
            } else if(decision == 2) { // If player testifies
                gsPtr.update(3,3); // Both get 3 years
            }
        }
    }

    public ArrayList<String> getStrategies() { return strats; } // returns strategies arraylist

    // returns final scores of game
    public String getScores() {
        return "Your total prison sentence is: " + gsPtr.getUserSentence() + "\nYour partner's total prison sentence is: " + gsPtr.getComputerSentence();
    }

    public GameStat getStats() { return gsPtr; } // returns pointer to gamestat object

    // public int getCDecision() { return cdecision; } // Returns computer's decision 

    // Sets cstrat to the appropriate value based on the strategy passed in. Also sends the appropriate strategy string to GameStat.
    public void setStrategy(int strategy) /* throws Exception */ {
        if (strategy == 1) {
            cstrat = 1;
            gsPtr.setComputerStrategy("Tit-For-Tat");
        } else if (strategy == 2) {
            cstrat = 2;
            gsPtr.setComputerStrategy("Tit-For-Two-Tats");
        } else if (strategy == 3) {
            cstrat = 3;
            gsPtr.setComputerStrategy("Random");
        }
    }

}