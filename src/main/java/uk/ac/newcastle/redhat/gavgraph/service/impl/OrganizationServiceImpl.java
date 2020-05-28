package uk.ac.newcastle.redhat.gavgraph.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.newcastle.redhat.gavgraph.domain.Organization;
import uk.ac.newcastle.redhat.gavgraph.repository.OrganizationRepository;
import uk.ac.newcastle.redhat.gavgraph.service.OrganizationService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Resource
    private OrganizationRepository organizationRepository;

    @Override
    public Optional<Organization> findById(Long id) {
        return organizationRepository.findById(id);
    }

    @Override
    public List<Organization> findAllByNameContains(String name) {
        return organizationRepository.findAllByNameContains(name);
    }

    @Override
    public Organization findByName(String name) {
        return organizationRepository.findByName(name);
    }

    @Override
    public Organization findByUrl(String url) {
        return organizationRepository.findByUrl(url);
    }

    @Override
    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public List<Organization> findAllZeroDepth() {
        return (List<Organization>) organizationRepository.findAll(0);
    }

    @Override
    public void deleteById(Long id) {
        organizationRepository.deleteById(id);
    }
}
