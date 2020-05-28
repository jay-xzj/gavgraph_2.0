package uk.ac.newcastle.redhat.gavgraph.domain;

import io.swagger.annotations.ApiModelProperty;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@NodeEntity
public class License {

    //automatically configured as graphId
    @ApiModelProperty(hidden = true)
    private Long id;

    private String name;

    private String url;

    private String distribution;

    //license_incoming
    @Relationship(type = "HAS_LICENSE",direction = Relationship.INCOMING)
    @ApiModelProperty(hidden = true)
    private Collection<Artifact> artifacts;

    public License() {
        this.artifacts = new HashSet<>();
    }

    public License(String name, String url, String distribution) {
        this();
        this.name = name;
        this.url = url;
        this.distribution = distribution;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDistribution() {
        return distribution;
    }

    public Collection<Artifact> getArtifacts() {
        return artifacts;
    }

    @Override
    public String toString() {
        return "License{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", distribution='" + distribution + '\'' +
                ", artifacts=" + artifacts.size() +
                '}';
    }

    public void updateFrom(License license){
        this.name = Optional.ofNullable(license.name).orElse(this.name);
        this.url = Optional.ofNullable(license.url).orElse(this.url);
    }
}
