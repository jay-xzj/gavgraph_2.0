package uk.ac.newcastle.redhat.gavgraph.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import uk.ac.newcastle.redhat.gavgraph.domain.Organization;

import java.util.List;

public interface OrganizationRepository extends Neo4jRepository<Organization,Long> {

    List<Organization> findAllByNameContains(String name);

    Organization findByName(String name);

    Organization findByUrl(String url);
}
