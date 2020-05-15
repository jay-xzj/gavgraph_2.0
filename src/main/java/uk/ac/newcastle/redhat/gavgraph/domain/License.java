package uk.ac.newcastle.redhat.gavgraph.domain;

import org.neo4j.ogm.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.relationship.HasLicense;

import java.util.Collection;

@NodeEntity(label = "LICENSE")
public class License {

    @Id
    @GeneratedValue
    @Index
    private Long id;

    @Property(name = "name")
    private String name;

    @Property(name = "url")
    private String url;

    @Property(name = "distribution")
    private String distribution;

    //license_incoming
    @Relationship(type = "HAS_LICENSE",direction = Relationship.INCOMING)
    private Collection<HasLicense> hasLicenses;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDistribution() {
        return distribution;
    }

    public Collection<HasLicense> getHasLicenses() {
        return hasLicenses;
    }
}
