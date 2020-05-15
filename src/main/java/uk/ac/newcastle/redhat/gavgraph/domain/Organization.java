package uk.ac.newcastle.redhat.gavgraph.domain;

import org.neo4j.ogm.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.relationship.HasDependency;
import uk.ac.newcastle.redhat.gavgraph.domain.relationship.HasOrg;

import java.util.Set;

@NodeEntity(label = "ORGANIZATION")
public class Organization {

    @Id
    @GeneratedValue
    @Index
    private String id;

    @Property(name = "name")
    private String name;

    @Property(name = "url")
    private String url;

    //org_incoming
    @Relationship(type = "HAS_ORG")
    private Set<HasOrg> hasOrgs;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Set<HasOrg> getHasOrgs() {
        return hasOrgs;
    }
}
