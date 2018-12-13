package csulb.cecs323.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class shows the relationship between a team and its sponsors and also
 * shows the total amount donated by each sponsor to that team
 */
@Entity(name = "SponsorAmount")
@IdClass(PK_SponsorAmount.class)
@Table(name = "SponsorAmount")
public class SponsorAmount {

    @Id
    @ManyToOne
    @JoinColumn(name = "sponsorID", nullable = false)
    private Sponsor sponsorA;

    @Id
    @ManyToOne
    @JoinColumn(name = "teamID", nullable = false)
    private Team teamA;

    private double amount;

    /**
     * parameterless constructor
     */
    public SponsorAmount() { }

    /**
     * Construtor
     * @/param myKey
     * @param amount
     */
    public SponsorAmount(double amount)
    {
              this.amount = amount;
    }

    /** SETTERS */
    public void setSponsor(Sponsor sponsor) { this.sponsorA = sponsor; }

    public void setTeam(Team team) { this.teamA = team; }

    public void setAmount(double amount) { this.amount = amount; }

    /** GETTERS   */
    public Sponsor getSponsor() { return sponsorA; }

    public Team getTeam() { return teamA; }

    public double getAmount() { return amount; }
}

/**
 * ID class used for implementing a composite PK made of two FK's
 */
class PK_SponsorAmount implements Serializable {

    private long sponsorA;
    private long teamA;

    public PK_SponsorAmount(){}

    public PK_SponsorAmount(long sponsor, long team)
    {
        this.sponsorA = sponsor;
        this.teamA = team;
    }

    /** SETTERS */
    public void setSponsor(long sponsor) { this.sponsorA = sponsor; }

    public void setTeam(long team) { this.teamA = team; }

    /** GETTERS */
    public long getSponsor() { return sponsorA; }

    public long getTeam() { return teamA; }
}
