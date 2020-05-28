package uk.ac.newcastle.redhat.gavgraph.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import uk.ac.newcastle.redhat.gavgraph.domain.Developer;

import java.util.List;

public interface DeveloperRepository extends Neo4jRepository<Developer, Long> {

    Developer findByEmail(String email);

    List<Developer> findAllByNameContains(String name);
}
