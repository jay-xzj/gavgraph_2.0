package uk.ac.newcastle.redhat.gavgraph.domain;

import org.neo4j.ogm.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.relationship.HasParent;

import java.util.Set;


@NodeEntity(label = "PARENT")
public class Parent {

    @Id
    @GeneratedValue
    @Index
    private Long id;

    @Property(name = "name")
    private String groupId;

    @Property(name = "artifactId")
    private String artifactId;

    @Property(name = "version")
    private String version;

    @Relationship(type = "HAS_PARENT",direction = Relationship.INCOMING)
    private Set<HasParent> hasParents;

    public Long getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public Set<HasParent> getHasParents() {
        return hasParents;
    }
}
