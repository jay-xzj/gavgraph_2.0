package uk.ac.newcastle.redhat.gavgraph.service;

import uk.ac.newcastle.redhat.gavgraph.domain.Developer;

import java.util.List;
import java.util.Optional;

public interface DeveloperService {

    Optional<Developer> findById(Long id);

    List<Developer> findAllByNameContains(String name);

    Developer findByEmail(String email);

    Developer save(Developer developer);

    List<Developer> findAllZeroDepth();

    void deleteById(Long id);
}
