package uk.ac.newcastle.redhat.gavgraph.domain;

import org.neo4j.ogm.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.relationship.HasDependency;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class will be like a node in a linked list or a graph,
 * there should be some 前继节点 and 后继节点
 */
@NodeEntity(label = "artifact")
//logging level ...
public class Artifact implements Serializable {

    //a predecessor is a artifact that depends on this artifact.
    //private @Relationship(type = "DEPEND_ON",direction = Relationship.INCOMING) Artifact predecessor;
    @Id
    @GeneratedValue
    @Index
    private Long id;

    @Property(name = "groupId")
    private String groupId;

    @Property(name = "artifactId")
    private String artifactId;

    @Property(name = "version")
    private String version;

    @Property(name = "availability")
    private Boolean availability;

    //outgoing
    @Relationship(type = "HAS_DEPENDENCY")//Direction of the relationship. Defaults to OUTGOING.
    private Set<Artifact> dependencies = new HashSet<>();

    @Relationship(type = "HAS_PARENT")
    private Parent parent;

    //license_outgoing
    @Relationship(type = "HAS_LICENSE")
    private Set<License> licenses = new HashSet<>();

    //org_outgoing
    @Relationship(type = "HAS_ORG")
    private Organization organization;

    //incoming
    @Relationship(type = "HAS_DEPENDENCY",direction = Relationship.INCOMING)
    private Set<HasDependency> hasDependencies;

    public Artifact(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public Artifact(
            String groupId, String artifactId,
            String version, Boolean availability,
            Set<Artifact> dependencies) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.dependencies = dependencies;
        this.availability = availability;
    }

    public Artifact(
            String groupId, String artifactId,
            String version, Boolean availability,
            HashSet<Artifact> dependencies,Parent parent,
            Set<License> licenses, Organization organization) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.dependencies = dependencies;
        this.availability = availability;
        this.parent = parent;
        this.licenses = licenses;
        this.organization = organization;
    }

    /**
     * depend on a single dependency artifact, so here will be the dependency's groupId, artifactId and version
     */
    public void dependOn(String groupId,String artifactId,String version){
        Artifact dependency = new Artifact(groupId, artifactId, version,true, new HashSet<Artifact>());
        dependencies.add(dependency);
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

    public Set<Artifact> getDependencies() {
        return dependencies;
    }

    public Boolean getAvailability() {
        return availability;
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

    public Set<HasDependency> getHasDependencies() {
        return hasDependencies;
    }
}
