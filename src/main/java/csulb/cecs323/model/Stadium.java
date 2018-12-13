package csulb.cecs323.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Class Stadium will be the Table Stadium in the relational database. A program generated PK is defined, along with
 * a 3 attribute CK. 1 relationship is defined and 1 additional nullable attribute is as well.
 */
@Entity(name = "Stadiums")
@Table(name = "Stadiums", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "city", "state"}))
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stadiumID;

    /**this will add the team id column so that we know that this stadium is the teams home*/
    @OneToOne(mappedBy = "stadium", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Team teamHere;

    /**this will help keep track of what games have been played here*/
    @OneToMany(mappedBy = "stadium", cascade = CascadeType.PERSIST)
    private List<Game> games = new ArrayList<>();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    private int numSeats;

    /**
     * Parameterless constructor
     */
    public Stadium() { }

    /**
     * Constructor
     * @param name
     * @param city
     * @param state
     * @param numSeats
     */
    public Stadium(String name, String city, String state, int numSeats, Team team)
    {
        this.teamHere = team;
        this.name = name;
        this.city = city;
        this.state = state;
        this.numSeats = numSeats;
    }

    /**SETTERS will give each attribute information*/
    public void setStadiumID(long stadiumID) { this.stadiumID = stadiumID; }

    public void setTeam(Team team) { this.teamHere = team; }

    public void setName(String name) { this.name = name; }

    public void setCity(String city) { this.city = city; }

    public void setState(String state) { this.state = state; }

    public void setNumSeats(int numSeats) { this.numSeats = numSeats; }

    public void setGames(List<Game> games){this.games = games;}

    /**ADD Games*/

    public void addSingleGame(Game game) {games.add(game);}

    /** GETTERS will help us access specific information */
    public long getStadiumID() { return stadiumID; }

    public Team getTeam() { return teamHere; }

    public String getName() { return name; }

    public String getCity() { return city; }

    public String getState() { return state; }

    public int getNumSeats() { return numSeats; }

    public Game getSingleGame(int i) {return games.get(i);}
}
