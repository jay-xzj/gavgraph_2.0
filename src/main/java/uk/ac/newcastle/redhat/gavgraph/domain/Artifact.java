package uk.ac.newcastle.redhat.gavgraph.domain;

import io.swagger.annotations.ApiModelProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * This class will be like a node in a linked list or a graph,
 * there should be some 前继节点 and 后继节点
 */
@NodeEntity
public class Artifact implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(example = "uk.neo4j.graph")
    private String groupId;

    @ApiModelProperty(example = "neo4j-graph")
    private String artifactId;

    @ApiModelProperty(example = "1.0")
    private String version;

    @ApiModelProperty(example = "true")
    private Boolean availability;

    @ApiModelProperty(example = "runtime")
    private String scope;

    //incoming
    @ApiModelProperty(hidden = true)
    @Relationship(type = "HAS_DEPENDENCY",direction = Relationship.INCOMING)
    private Set<Artifact> artifacts;

    //outgoing
    @ApiModelProperty(hidden = true)
    @Relationship(type = "HAS_DEPENDENCY")//Direction of the relationship. Defaults to OUTGOING.
    private Set<Artifact> dependencies;

    @Relationship(type = "HAS_PARENT")
    @ApiModelProperty(hidden = true)
    private Parent parent;

    @Relationship(type = "HAS_LICENSE")
    @ApiModelProperty(hidden = true)
    private Set<License> licenses;

    @Relationship(type = "HAS_ORG")
    @ApiModelProperty(hidden = true)
    private Organization organization;

    @Relationship(type = "DEVELOPED_BY")
    @ApiModelProperty(hidden = true)
    private Set<Developer> developers;

    public Artifact() {
        this.availability = true;
        this.dependencies = new HashSet<>();
        this.artifacts = new HashSet<>();
        this.licenses = new HashSet<>();
        this.developers = new HashSet<>();
    }

    public Artifact(String groupId, String artifactId, String version,Boolean availability,String scope) {
        this();
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.availability = availability;
        this.scope = scope;
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

    public Boolean getAvailability() {
        return availability;
    }

    public String getScope() {
        return scope;
    }

    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    public Set<Artifact> getDependencies() {
        return dependencies;
    }

    public Parent getParent() {
        return parent;
    }

    public Set<License> getLicenses() {
        return licenses;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Set<Developer> getDevelopers() {
        return developers;
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "id=" + getId() +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", availability=" + availability +
                ", scope='" + scope + '\'' +
                ", artifacts=" + artifacts.size() +
                ", dependencies=" + dependencies.size() +
                ", parent=" + parent +
                ", licenses=" + licenses.size() +
                ", organization=" + organization +
                ", developers=" + developers.size() +
                '}';
    }

    public void updateFrom(Artifact artifact){
        this.groupId = Optional.ofNullable(artifact.groupId).orElse(this.groupId);;
        this.artifactId = Optional.ofNullable(artifact.artifactId).orElse(this.artifactId);
        this.version = Optional.ofNullable(artifact.version).orElse(this.version);
        this.availability = Optional.ofNullable(artifact.availability).orElse(this.availability);
        this.scope = Optional.ofNullable(artifact.scope).orElse(this.scope);
    }
}
