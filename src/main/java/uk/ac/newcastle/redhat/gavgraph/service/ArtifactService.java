package uk.ac.newcastle.redhat.gavgraph.service;

import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;

import java.util.List;

public interface ArtifactService {

    List<Artifact> findAllZeroDepth();

    Artifact save(Artifact artifact);

    List<Artifact> findByGroupId(String groupId);

    List<Artifact> findArtifactId(String artifactId);

    List<Artifact> findArtifactIdLike(String artifactId);

    List<Artifact> findGroupIdLike(String groupId);
}
