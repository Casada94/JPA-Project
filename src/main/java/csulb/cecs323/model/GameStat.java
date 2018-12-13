package csulb.cecs323.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * this shows the relationship between the games and the player so that we can
 * gather stats for the teams and players careers. along with for each game
 */
@Entity(name = "GameStats")
@IdClass(PK_GameStat.class)
@Table(name = "GameStats")
public class GameStat {

    /**each stat is for a specific game and player*/
    @Id
    @ManyToOne
    @JoinColumn(name = "gameID", nullable = true)
    private Game game;

    @Id
    @ManyToOne
    @JoinColumn(name = "playerID")
    private Player player;

    /**some of these will be 0 depending on the players play time and position*/
    private int saves;
    private int goals;
    private int minutesPlayed;
    private int penaltyTime;
    private int shots;

    /**
     * Parameterless constructor
     */
    public GameStat() { }

    /**
     * Constructor for Gamestat class
     * @param saves
     * @param goals
     * @param minutesPlayed
     * @param penaltyTime
     * @param shots
     */
    public GameStat(int saves, int goals, int minutesPlayed, int penaltyTime, int shots)
    {
        this.saves = saves;
        this.goals = goals;
        this.minutesPlayed = minutesPlayed;
        this.penaltyTime = penaltyTime;
        this.shots = shots;
    }

    /** SETTERS */
    public void setPlayer(Player player) { this.player = player; }

    public void setGame(Game game) { this.game = game; }

    public void setSaves(int saves) { this.saves = saves; }

    public void setGoals(int goals) { this.goals = goals; }

    public void setMinutesPlayed(int minutesPlayed) { this.minutesPlayed = minutesPlayed; }

    public void setPenaltyTime(int penaltyTime) { this.penaltyTime = penaltyTime; }

    public void setShots(int shots) { this.shots = shots; }

    /** GETTERS */
    public Player getPlayer() { return player; }

    public Game getGame() { return game; }

    public int getMinutesPlayed() { return minutesPlayed; }

    public int getShots() { return shots; }

    public int getGoals() { return goals; }

    public int getSaves() { return saves; }

    public int getPenaltyTime() { return penaltyTime; }
}


/**
 * ID class for the implementation of Composite PK made of FK's
 */
class PK_GameStat implements Serializable {
    private long game;

    private long player;

    public PK_GameStat(){ }

    public PK_GameStat(long game, long player)
    {
        this.game = game;
        this.player = player;
    }

    /** SETTERS */
    public void setGame(long game) { this.game = game; }

    public void setPlayer(long player) { this.player = player; }

    /** GETTERS */
    public long getGame() { return game; }

    public long getPlayer() { return player; }
}
