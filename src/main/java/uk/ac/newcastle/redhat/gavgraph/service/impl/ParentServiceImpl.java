package uk.ac.newcastle.redhat.gavgraph.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.newcastle.redhat.gavgraph.domain.Parent;
import uk.ac.newcastle.redhat.gavgraph.repository.ParentRepository;
import uk.ac.newcastle.redhat.gavgraph.service.ParentService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class ParentServiceImpl implements ParentService {

    @Resource
    private ParentRepository parentRepository;

    @Override
    public Optional<Parent> findById(Long id) {
        return parentRepository.findById(id);
    }

    @Override
    public Parent save(Parent parent) {
        return parentRepository.save(parent);
    }

    @Override
    public List<Parent> findAllZeroDepth() {
        return (List<Parent>) parentRepository.findAll(0);
    }

    @Override
    public List<Parent> findByGroupId(String groupId) {
        return parentRepository.findByGroupId(groupId);
    }

    @Override
    public List<Parent> findGroupIdLike(String groupId) {
        return parentRepository.findAllByGroupIdContains(groupId);
    }

    @Override
    public List<Parent> findArtifactId(String artifactId) {
        return parentRepository.findByArtifactId(artifactId);
    }

    @Override
    public List<Parent> findArtifactIdLike(String artifactId) {
        return parentRepository.findAllByArtifactIdContains(artifactId);
    }

    @Override
    public void deleteById(Long id) {
        parentRepository.deleteById(id);
    }
}
