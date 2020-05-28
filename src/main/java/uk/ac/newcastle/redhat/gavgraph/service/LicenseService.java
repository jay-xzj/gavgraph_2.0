package uk.ac.newcastle.redhat.gavgraph.service;

import uk.ac.newcastle.redhat.gavgraph.domain.License;

import java.util.List;
import java.util.Optional;

public interface LicenseService {

    Optional<License> findById(Long id);

    List<License> findAllByNameContains(String name);

    License findByName(String name);

    License findByUrl(String url);

    License save(License license);

    List<License> findAllZeroDepth();

    void deleteById(Long id);
}
