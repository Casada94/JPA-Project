package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Class Game will be Table Games in the Database
 * In this class foreign keys and PK's are all used and defined along with addition attributes specific to games
 */

@Entity(name = "Games")
@Table(name = "Games", uniqueConstraints = @UniqueConstraint(columnNames = {"season", "date"}))
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long gameID;

    /**each game can have multiple gamestats since they relate to each player*/
    @OneToMany(mappedBy = "game", cascade = CascadeType.PERSIST)
    private List<GameStat> gameStats = new ArrayList<>();

    /**Teams can play in multiple games as home teams*/
    @ManyToOne
    @JoinColumn
    private Team homeTeam;

    /**Teams can play in multiple games as away teams*/
    @ManyToOne
    @JoinColumn
    private Team awayTeam;

    /**Each game is only played at one stadium but a stadium can hold many games*/
    @ManyToOne
    @JoinColumn
    private Stadium stadium;

    /**the season starts at 1 which was in 1918 and continues to count up*/
    @Column(name = "season", nullable = false)
    private int season;

    /**the gameType can be conference, preseason, playoff or a stanley cup final game*/
    @Enumerated(EnumType.STRING)
    private GameType gameType;

    private int homeScore;
    private int awayScore;
    private int totalGameTime;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private GregorianCalendar date;

    /**
     * parameterless constructor
     */
    public Game() {
    }

    /**
     * Constructor for Game class
     * @param season
     * @param date
     * @param gameType
     * @param homeScore
     * @param awayScore
     * @param totalGameTime
     */

    public Game(int season, GregorianCalendar date, GameType gameType, int homeScore, int awayScore, int totalGameTime)
    {
        this.season = season;
        this.date = date;
        this.gameType = gameType;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.totalGameTime = totalGameTime;
    }

    /**SETTERS these will set all the attributes */
    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setGameType(GameType gameType) { this.gameType = gameType; }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public void setTotalGameTime(int totalGameTime) {
        this.totalGameTime = totalGameTime;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public void setGameStat(List<GameStat> gameStats) { this.gameStats = gameStats;}

    /**ADD since it is a list this adds more gameStats to a game*/

    public void addGameStat(GameStat stat){gameStats.add(stat);}

    /** GETTERS will make it so we can return all of the attributes */
    public long getGameID() {
        return gameID;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public int getSeason() {
        return season;
    }

    public GameType getGameType() { return gameType; }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public int getTotalGameTime() {
        return totalGameTime;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public List<GameStat> getGameStat() {
        return gameStats;
    }

    public GameStat getSingularGameStat(int i) {return gameStats.get(i);}
}
