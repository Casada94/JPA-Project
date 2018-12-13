package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Sponsor will be the Table Sponsors in the relational database. Program generated ID's are used as the PK and
 * two other attrivutes make the CK. 1 additional nullable attribute is defined and a relationship with SponsorAmount
 * is defined as well.
 */

@Entity(name = "Sponsors")
@Table(name = "Sponsors", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "website"}))
public class Sponsor {

    /**The information will be copied multiple times so it is easier to have a generated value*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sponsorID;

    /**Name and website are a candidate key*/
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "website", nullable = false)
    private String website;

    /**This is so that we can hold the information of each sponsor and team relation*/
    @OneToMany(mappedBy = "sponsorA", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<SponsorAmount> payments = new ArrayList<>();

    private String description;

    /**
     * parameterless constructor
     */
    public Sponsor() { }

    /**
     * Constructor
     * @param name
     * @param description
     * @param website
     */
    public Sponsor(String name, String description, String website)
    {
        this.name = name;
        this.description = description;
        this.website = website;
    }

    /**SETTERS will set each attribute*/
    public void setSponsorID(long sponsorID) { this.sponsorID = sponsorID; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public void setWebsite(String website) { this.website = website; }

    public void setPayments(SponsorAmount payment){ payments.add(payment);}

    /**GETTERS will help us retrieve each attribute*/
    public long getSponsorID() { return sponsorID; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getWebsite() { return website; }

    public SponsorAmount getSingularSponsorAmount(int i) {return payments.get(i);}
}
