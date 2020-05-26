package uk.ac.newcastle.redhat.gavgraph.dto;

import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import uk.ac.newcastle.redhat.gavgraph.domain.*;
import uk.ac.newcastle.redhat.gavgraph.domain.relationship.HasDependency;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ArtifactDto implements Serializable {
    private Long id;

    @Property(name = "groupId")
    private String groupId;

    @Property(name = "artifactId")
    private String artifactId;

    @Property(name = "version")
    private String version;

    @Property(name = "availability")
    private Boolean availability;

    private String scope;

    //incoming
    @Relationship(type = "HAS_DEPENDENCY",direction = Relationship.INCOMING)
    private Set<HasDependency> hasDependencies;

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

    //developer_outgoing
    @Relationship(type = "HAS_DEVELOPER")
    private Developer developer;

    /**
     * depend on a single dependency artifact, so here will be the dependency's groupId, artifactId and version
     */
    /*public void dependOn(String groupId,String artifactId,String version){
        Artifact dependency = new Artifact(groupId, artifactId, version,true,);
        dependencies.add(dependency);
    }*/

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

    public Developer getDeveloper() {
        return developer;
    }

}
