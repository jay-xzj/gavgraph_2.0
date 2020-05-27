package uk.ac.newcastle.redhat.gavgraph.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import uk.ac.newcastle.redhat.gavgraph.domain.License;

public interface LicenseRepository extends Neo4jRepository<License,Long> {
}
