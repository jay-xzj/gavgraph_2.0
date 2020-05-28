package uk.ac.newcastle.redhat.gavgraph.domain;

import io.swagger.annotations.ApiModelProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@NodeEntity
public class Parent {
    @ApiModelProperty(hidden = true)
    private Long id;

    private String groupId;

    private String artifactId;

    private String version;

    @Relationship(type = "HAS_PARENT",direction = Relationship.INCOMING)
    @ApiModelProperty(hidden = true)
    private Set<Artifact> artifacts;

    public Parent() {
        this.artifacts = new HashSet<>();
    }

    public Parent(String groupId, String artifactId, String version) {
        this();
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

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

    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + getId() +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", artifacts=" + artifacts.size() +
                '}';
    }

    public void updateFrom(Parent parent){
        this.groupId = Optional.ofNullable(parent.groupId).orElse(this.groupId);
        this.artifactId = Optional.ofNullable(parent.artifactId).orElse(this.artifactId);
        this.version = Optional.ofNullable(parent.version).orElse(this.version);
    }

}
