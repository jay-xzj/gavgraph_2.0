package uk.ac.newcastle.redhat.gavgraph.domain.relationship;

import org.neo4j.ogm.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;
import uk.ac.newcastle.redhat.gavgraph.domain.Organization;

@RelationshipEntity("HAS_ORG")
public class HasOrg {

    @Id
    @GeneratedValue
    Long id;

    @StartNode
    Artifact artifact;
    @EndNode
    Organization organization;

    public Long getId() {
        return id;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public Organization getOrganization() {
        return organization;
    }
}
