package uk.ac.newcastle.redhat.gavgraph.controller;


import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;
import uk.ac.newcastle.redhat.gavgraph.exception.NotFoundException;
import uk.ac.newcastle.redhat.gavgraph.repository.ArtifactRepository;
import uk.ac.newcastle.redhat.gavgraph.service.ArtifactService;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/api/artifacts")
//@Api(value = "Artifact API", description = "Artifact API for CRUD")
public class ArtifactController {

    @Resource
    private ArtifactService artifactService;

    @Resource
    private ArtifactRepository artifactRepository;

    /*@Resource
    private final Logger log = LogManager.getLogger(ArtifactController.class);*/

    @PostMapping("/create")
    @ApiOperation(value = "Add a new Artifact to the database")
   /* @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Artifact created successfully."),
            @ApiResponse(code = 400, message = "Invalid Artifact provided in request body"),
            @ApiResponse(code = 409, message = "Artifact provided in request body conflicts with an existing Customer"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })*/
    public ResponseEntity<Artifact> create(
            @RequestBody @ApiParam(value = "JSON representation of an artifact to be added to the database", required = true,defaultValue = "")
                    Artifact artifact) {
        if (artifact == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Artifact save = null;
        try{
            save = artifactService.save(artifact);
            //log.info("An artifact created : " + artifact);
        }catch (ConstraintViolationException ce){
           //logging and throwing new exception
        }catch (Exception e){
            e.printStackTrace();
            //log.fatal("Exception occurs while creating an artifact : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
        }
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "fetch all artifacts", notes = "return a list of artifacts")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 302, message = "Found")
    })
    public ResponseEntity<List<Artifact>> findAll(){
        List<Artifact> artifacts;
        try {
            artifacts = artifactService.findAll();
        }catch (Exception e){
            e.printStackTrace();
            //log.fatal("Exception occurs while retrieving all artifacts : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(artifacts,HttpStatus.OK);
    }
    @GetMapping("findById/{id}")
    @ApiOperation(value = "find artifact by id",notes = "return an artifact with certain id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "no content"),
            @ApiResponse(code = 302, message = "Not found")
    })
    public ResponseEntity<Artifact> findById(@PathVariable @ApiParam Long id){
        Artifact artifact;
        try{
            artifact = artifactRepository.findById(id).orElseThrow(NotFoundException::new);
        }catch (NotFoundException ne){
            ne.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(artifact,HttpStatus.FOUND);
    }

    @GetMapping("findByGroupId/{groupId}")
    public ResponseEntity<List<Artifact>> findByGroupId(@PathVariable String groupId){
        List<Artifact> artifacts = null;
        try{
            artifacts = artifactService.findByGroupId(groupId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(artifacts, HttpStatus.OK);
    }

    @GetMapping("findArtifactId/{artifactId}")
    public ResponseEntity<List<Artifact>> findArtifactId(@PathVariable String artifactId){
        List<Artifact> artifacts = null;
        try{
            artifacts = artifactService.findArtifactId(artifactId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(artifacts, HttpStatus.OK);
    }

    @GetMapping("findArtifactIdLike/{artifactId}")
    public ResponseEntity<List<Artifact>> findArtifactIdLike(@PathVariable String artifactId){
        List<Artifact> artifacts = null;
        try{
            artifacts = artifactService.findArtifactIdLike(artifactId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(artifacts, HttpStatus.OK);
    }

    @GetMapping("findGroupIdLike/{groupId}")
    public ResponseEntity<List<Artifact>> findGroupIdLike(@PathVariable @ApiParam(defaultValue = "redhat-7") String groupId){
        List<Artifact> artifacts = null;
        try{
            artifacts = artifactService.findGroupIdLike(groupId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(artifacts, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        artifactRepository.deleteById(id);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Artifact update(@PathVariable Long id, @RequestBody Artifact update) {
        final Artifact existing = artifactRepository.findById(id).orElseThrow(NotFoundException::new);
        existing.updateFrom(update);
        return artifactRepository.save(existing);
    }


}
