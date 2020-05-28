package uk.ac.newcastle.redhat.gavgraph.service;

import uk.ac.newcastle.redhat.gavgraph.domain.Parent;

import java.util.List;
import java.util.Optional;

public interface ParentService {

    Optional<Parent> findById(Long id);

    Parent save(Parent parent);

    List<Parent> findAllZeroDepth();

    List<Parent> findByGroupId(String groupId);

    List<Parent> findGroupIdLike(String groupId);

    List<Parent> findArtifactId(String artifactId);

    List<Parent> findArtifactIdLike(String artifactId);

    void deleteById(Long id);
}
