package uk.ac.newcastle.redhat.gavgraph.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;
import uk.ac.newcastle.redhat.gavgraph.repository.ArtifactRepository;
import uk.ac.newcastle.redhat.gavgraph.service.ArtifactService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArtifactServiceImpl implements ArtifactService {

    @Resource
    private ArtifactRepository artifactRepository;

    @Override
    public List<Artifact> findAllZeroDepth() {
        return (List<Artifact>) artifactRepository.findAll(0);
    }

    @Override
    public Artifact save(Artifact artifact) {
        return artifactRepository.save(artifact);
    }

    @Override
    public List<Artifact> findByGroupId(String groupId) {
        return artifactRepository.getAllByGroupId(groupId);
    }

    @Override
    public List<Artifact> findArtifactId(String artifactId) {
        List<Artifact> artifacts = artifactRepository.getAllByArtifactId(artifactId);
        return artifacts;
    }

    @Override
    public List<Artifact> findArtifactIdLike(String artifactId) {
        return artifactRepository.getAllByArtifactIdContains(artifactId);
    }

    @Override
    public List<Artifact> findGroupIdLike(String groupId) {
        return artifactRepository.getALlByGroupIdContains(groupId);
    }
}
