package uk.ac.newcastle.redhat.gavgraph.service;

import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;

import java.util.List;

public interface ArtifactService {

    List<Artifact> findAll();

    Artifact save(Artifact artifact);
}
