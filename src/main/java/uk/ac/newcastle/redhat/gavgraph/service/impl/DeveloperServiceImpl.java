package uk.ac.newcastle.redhat.gavgraph.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.newcastle.redhat.gavgraph.domain.Developer;
import uk.ac.newcastle.redhat.gavgraph.repository.DeveloperRepository;
import uk.ac.newcastle.redhat.gavgraph.service.DeveloperService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    @Resource
    private DeveloperRepository developerRepository;

    @Override
    public Optional<Developer> findById(Long id) {
        return developerRepository.findById(id);
    }

    @Override
    public List<Developer> findAllByNameContains(String name) {
        return developerRepository.findAllByNameContains(name);
    }

    @Override
    public Developer findByEmail(String email) {
        return developerRepository.findByEmail(email);
    }

    @Override
    public Developer save(Developer developer) {
        return developerRepository.save(developer);
    }

    @Override
    public List<Developer> findAllZeroDepth() {
        return (List<Developer>) developerRepository.findAll(0);
    }

    @Override
    public void deleteById(Long id) {
        developerRepository.deleteById(id);
    }
}
