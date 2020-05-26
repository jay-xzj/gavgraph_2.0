package uk.ac.newcastle.redhat.gavgraph.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.newcastle.redhat.gavgraph.domain.Parent;

public interface ParentRepository extends CrudRepository<Parent,Long> {
}
