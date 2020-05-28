package uk.ac.newcastle.redhat.gavgraph.domain;

import io.swagger.annotations.ApiModelProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Optional;
import java.util.Set;

@NodeEntity
public class Organization {
    @ApiModelProperty(hidden = true)
    private Long id;

    private String name;

    private String url;

    @Relationship(type = "HAS_ORG",direction = Relationship.INCOMING)
    @ApiModelProperty(hidden = true)
    private Set<Artifact> artifacts;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", artifacts=" + artifacts.size() +
                '}';
    }

    public void updateFrom(Organization organization){
        this.name = Optional.ofNullable(organization.name).orElse(this.name);
        this.url = Optional.ofNullable(organization.url).orElse(this.url);
    }
}
