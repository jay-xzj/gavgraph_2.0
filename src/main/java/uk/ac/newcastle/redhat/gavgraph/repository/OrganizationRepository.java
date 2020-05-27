package uk.ac.newcastle.redhat.gavgraph.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import uk.ac.newcastle.redhat.gavgraph.domain.Organization;

public interface OrganizationRepository extends Neo4jRepository<Organization,Long> {
}
