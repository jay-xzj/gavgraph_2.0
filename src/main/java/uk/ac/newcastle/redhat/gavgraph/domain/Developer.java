package uk.ac.newcastle.redhat.gavgraph.domain;


import io.swagger.annotations.ApiModelProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Developer {

    /*@Id @GeneratedValue @Index*/
    @ApiModelProperty(hidden = true)
    private Long id;

    private String name;

    private String email;

    private String url;

    @Relationship(type = "DEVELOPED_BY", direction = Relationship.INCOMING)
    @ApiModelProperty(hidden = true)
    private Set<Artifact> artifacts;

    public Developer() {
        this.artifacts = new HashSet<>();
    }

    public Developer(String name, String email, String url) {
        this();
        this.name = name;
        this.email = email;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUrl() {
        return url;
    }

    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    @Override
    public String toString() {
        return "Developer{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                ", artifacts=" + artifacts.size() +
                '}';
    }

    public void updateFrom(Developer developer){
        this.email = developer.getEmail();
    }
}
