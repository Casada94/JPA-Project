package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Team will be Table Teams in the relational database. A 1 attribute CK has been defined along with multiple
 * relationships to other entities and two nullable attributes. Lastly and enumerated attribute is used
 */
@Entity(name = "Teams")
@Table(name = "Teams", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teamID")
    private long teamID;

    @OneToMany(mappedBy = "homeTeam", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Game> homeGame = new ArrayList<Game>();

    @OneToMany(mappedBy = "awayTeam", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Game> awayGame = new ArrayList<Game>();

    @OneToMany (mappedBy = "teamA",cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<SponsorAmount> sponsorAmounts = new ArrayList<SponsorAmount>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Stadium stadium;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<TeamPlayers> teamPlayers = new ArrayList<TeamPlayers>();

    @Column(name = "name", nullable = false)
    private String name;

    private String city;
    private String state;

    @Enumerated(EnumType.STRING)
    private Conference conference;

    /**
     * parameterless constructor
     */
    public Team() { }

    /**
     * Constructor
     * @param name
     * @param city
     * @param state
     * @param conference
     */
    public Team(String name, String city, String state, Conference conference) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.conference = conference;
    }

    /** SETTERS */
    public void setTeamID(long teamID) { this.teamID = teamID; }

    public void setName(String name) { this.name = name; }

    public void setCity(String city) {this.city = city; }

    public void setState(String state) {this.state = state; }

    public void setSponsorAmounts(List<SponsorAmount> sponsorAmounts){this.sponsorAmounts = sponsorAmounts;}

    public void setConference(Conference conference) { this.conference = conference; }

    public void setTeamPlayers(List<TeamPlayers> teamPlayers){this.teamPlayers = teamPlayers;}

    public void setHomeGame(List<Game> homeGame){this.homeGame = homeGame;}

    public void setAwayGame(List<Game> awayGame){this.awayGame = awayGame;}

    /** ADD */
    public void addHomeGame(Game home){homeGame.add(home);}

    public void addAwayGame(Game away){awayGame.add(away);}

    public void addTeamPlayer(TeamPlayers teamPlayers){ this.teamPlayers.add(teamPlayers);}

    public void addSponsorAmount(SponsorAmount amount){ this.sponsorAmounts.add(amount);}

    /** GETTERS */
    public long getTeamID() { return teamID; }

    public String getName() { return name; }

    public String getCity() { return city; }

    public String getState() { return state; }

    public Conference getConference() { return conference; }

    public List<SponsorAmount> getSponsorAmounts(){return sponsorAmounts;}

    public SponsorAmount getSingleSponsorAmount(int i){return sponsorAmounts.get(i);}

    public List<TeamPlayers> getTeamPlayers(){return teamPlayers; }

    public TeamPlayers getSingleTeamPlayer(int i){return teamPlayers.get(i);}

    public List<Game> getHomeGame(int i) { return homeGame; }

    public Game getSingleHomeGame(int i) {return homeGame.get(i);}

    public List<Game> getAwayGame(int i) { return awayGame; }

    public Game getSingleAwayGame(int i) {return awayGame.get(i);}

    public Stadium getStadium() { return stadium; }
}
