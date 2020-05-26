package uk.ac.newcastle.redhat.gavgraph.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.newcastle.redhat.gavgraph.domain.License;

public interface LicenseRepository extends CrudRepository<License,Long> {
}
