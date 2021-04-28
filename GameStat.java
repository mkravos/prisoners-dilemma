/*
    GameStat.java
    Mike Kravos
    2020-09-27

    Game statistic processing for the Prisoner's Dilemma game.
*/

public class GameStat {
    private int usentence = 0, csentence = 0, rounds = 0;
    private String cstrategy;

    public void setComputerStrategy(String cs) { cstrategy = cs; } // sets computer strategy string

    public void setRound(int round) { rounds = round; }

    public int getRound() { return rounds; }

    public int getUserSentence() { return usentence; } // returns user's sentence

    public int getComputerSentence() { return csentence; } // returns computer's sentence

    public String getComputerStrategy() { return cstrategy; } // returns computer's strategy

    public void update(int userSentence, int compSentence) { 
        usentence += userSentence;
        csentence += compSentence;
    }

    public String getWinner() { 
        if (usentence > csentence) return "your partner";
        else if (usentence < csentence) return "player";
        else if (usentence == csentence) return "tie";
        else return "wtf";
    }
    
}