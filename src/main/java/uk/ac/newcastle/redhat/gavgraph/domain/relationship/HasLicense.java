package uk.ac.newcastle.redhat.gavgraph.domain.relationship;

import org.neo4j.ogm.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;
import uk.ac.newcastle.redhat.gavgraph.domain.License;

@RelationshipEntity("HAS_LICENSE")
public class HasLicense {

    @Id
    @GeneratedValue
    Long id;

    @StartNode
    Artifact artifact;
    @EndNode
    License license;

    public Long getId() {
        return id;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public License getLicense() {
        return license;
    }
}
