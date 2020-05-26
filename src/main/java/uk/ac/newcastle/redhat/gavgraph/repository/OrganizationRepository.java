package uk.ac.newcastle.redhat.gavgraph.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.newcastle.redhat.gavgraph.domain.Organization;

public interface OrganizationRepository extends CrudRepository<Organization,Long> {
}
