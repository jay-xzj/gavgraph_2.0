package uk.ac.newcastle.redhat.gavgraph.service;

import uk.ac.newcastle.redhat.gavgraph.domain.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {

    Optional<Organization> findById(Long id);

    List<Organization> findAllByNameContains(String name);

    Organization findByName(String name);

    Organization findByUrl(String url);

    Organization save(Organization organization);

    List<Organization> findAllZeroDepth();

    void deleteById(Long id);
}
