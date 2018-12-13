package csulb.cecs323.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import java.util.GregorianCalendar;

/**
 * Class Player will be Table Players in the Database
 * In this class ID's, as a PK, are generated by the program. In addition, CK's are defined and the relations and
 * FK's are as well. There are also multiple other attributes specific to this class, which are nullable.
 */

@Entity(name = "Players")
@Table(name = "Players", uniqueConstraints = @UniqueConstraint(columnNames = {"firstName", "lastName", "DateOfBirth"}))
public class Player {

    /**since player is copied into other tables it is more efficient to have a generated id*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long playerID;

    /**first name, last name and date of birthdate together are a candidate keys*/
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "DateOfBirth", nullable = false)
    @Temporal(TemporalType.DATE)
    private GregorianCalendar birthDate;

    /**There are multiple positions that a player can be but they are specific*/
    @Enumerated(EnumType.STRING)
    private Position position;

    private int weight;
    private int height; //in inches
    private double salary;

    /**This will hold the information from teamPlayers of what teams the player has/is playing for*/
    @OneToMany(mappedBy = "player", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<TeamPlayers> team = new ArrayList<>();

    /**this will hold the information of each player in the games*/
    @OneToMany(mappedBy = "player", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<GameStat> gameStats = new ArrayList<>();

    /**
     * Parameterless constructor
     */
    public Player() { }

    /**
     * Constructor for Player class
     * @param firstName
     * @param lastName
     * @param position
     * @param height
     * @param weight
     * @param salary
     * @param birthDate
     */
    public Player(String firstName, String lastName, Position position, int height, int weight, double salary, GregorianCalendar birthDate)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.height = height;
        this.weight = weight;
        this.salary = salary;
        this.birthDate = birthDate;
    }

    public Player(String firstName, String lastName, GregorianCalendar birthDate)
    {
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /** SETTERS will set all of the attributes */
    public void setPlayerId(long ID) { this.playerID = ID; }

    public void setFirstName(String fname) { this.firstName = fname; }

    public void setLastName(String lname) { this.lastName = lname; }

    public void setPosition(Position position) { this.position = position; }

    public void setWeight(int weight) { this.weight = weight; }

    public void setHeight(int height) { this.height = height; }

    public void setBirthDate(GregorianCalendar birthDate) { this.birthDate = birthDate; }

    public void setSalary(double salary) { this.salary = salary; }

    public void setTeam(List<TeamPlayers> team){this.team = team;}

    public void setGameStats(List<GameStat> gameStats){this.gameStats = gameStats;}

    public void addTeam(TeamPlayers team) { this.team.add(team);}

    public void addGame(GameStat gameStat) { this.gameStats.add(gameStat);}

    /** GETTERS will help us retrieve each individual attribute */
    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public long getPlayerId() { return playerID; }

    public Position getPosition() { return position; }

    public int getHeight() { return height; }

    public int getWeight() { return weight; }

    public double getSalary() { return salary; }

    public List<TeamPlayers> getTeam(){return team;}

    public List<GameStat> getGameStat(){return gameStats;}

    public GregorianCalendar getBirthDate() { return birthDate; }
}
