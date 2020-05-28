package uk.ac.newcastle.redhat.gavgraph.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.newcastle.redhat.gavgraph.domain.License;
import uk.ac.newcastle.redhat.gavgraph.repository.LicenseRepository;
import uk.ac.newcastle.redhat.gavgraph.service.LicenseService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class LicenseServiceImpl implements LicenseService {

    @Resource
    private LicenseRepository licenseRepository;

    @Override
    public Optional<License> findById(Long id) {
        return licenseRepository.findById(id);
    }

    @Override
    public List<License> findAllByNameContains(String name) {
        return licenseRepository.findAllByNameContains(name);
    }

    @Override
    public License findByName(String name) {
        return licenseRepository.findByName(name);
    }

    @Override
    public License findByUrl(String url) {
        return licenseRepository.findByUrl(url);
    }

    @Override
    public License save(License license) {
        return licenseRepository.save(license);
    }

    @Override
    public List<License> findAllZeroDepth() {
        return (List<License>) licenseRepository.findAll(0);
    }

    @Override
    public void deleteById(Long id) {
        licenseRepository.deleteById(id);
    }
}
