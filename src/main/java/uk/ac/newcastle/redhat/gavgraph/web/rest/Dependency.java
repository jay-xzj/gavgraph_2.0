package uk.ac.newcastle.redhat.gavgraph.web.rest;

import org.neo4j.ogm.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;

@RelationshipEntity("DEPEND_ON")
public class Dependency {
    @Id
    @GeneratedValue
    private Long id;

    //@JsonIgnore
    @StartNode
    private Artifact start;

    @EndNode
    private Artifact end;

}
