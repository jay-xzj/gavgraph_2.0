package uk.ac.newcastle.redhat.gavgraph.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.newcastle.redhat.gavgraph.domain.Developer;

public interface DeveloperRepository extends CrudRepository<Developer, Long> {
}
