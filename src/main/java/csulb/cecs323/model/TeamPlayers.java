package csulb.cecs323.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * this classs helps us show the relationship between the teams and players
 * each season the player could be playing for a different team which is why
 * we need the season attribute
 */
@Entity(name = "TeamPlayers")
@IdClass(PK_TeamPlayers.class)
@Table(name = "TeamPlayers")
public class TeamPlayers {

    @Id
    @ManyToOne
    @JoinColumn(name = "Player", nullable = false)
    private Player player;

    @Id
    @ManyToOne
    @JoinColumn(name = "Team", nullable = false)
    private Team team;

    private int season;

    /**
     * parameterless constructor
     */
    public TeamPlayers() { }

    /**
     * Constructor
     * @/aram myKey
     * @param season
     */
    public TeamPlayers(int season)
    {
        this.season = season;
    }

    /** SETTERS sets all of the attributes  */
    public void setPlayer(Player player) { this.player = player; }

    public void setTeam(Team team) { this.team = team; }

    public void setSeason(int season) { this.season = season; }

    /** GETTERS  */
    public Player getPlayer() { return player; }

    public Team getTeam() { return team; }

    public int getSeason() { return season; }
}

/**
 * ID class for the implementation of a composite PK of two FK's
 */
class PK_TeamPlayers  implements Serializable {


    private long player;

    private long team;

    public PK_TeamPlayers(){    }

    public PK_TeamPlayers(long player, long team)
    {
        this.player = player;
        this.team = team;
    }

    /** SETTERS */
    public void setTeam(long team) { this.team = team; }

    public void setPlayer(long player) { this.player = player; }

    /** GETTERS */
    public long getPlayer() { return player; }

    public long getTeam() { return team; }
}