package uk.ac.newcastle.redhat.gavgraph.web.rest;


import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;
import uk.ac.newcastle.redhat.gavgraph.service.ArtifactService;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/artifact")
@Api(value = "Artifact API", description = "Artifact API for CRUD")
public class ArtifactController {

    @Resource
    private ArtifactService artifactService;

    /*@Resource
    private final Logger log = LogManager.getLogger(ArtifactController.class);*/

    @PostMapping("/add")
    @ApiOperation(value = "Add a new Artifact to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Artifact created successfully."),
            @ApiResponse(code = 400, message = "Invalid Artifact provided in request body"),
            @ApiResponse(code = 409, message = "Artifact provided in request body conflicts with an existing Customer"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public ResponseEntity<Artifact> addArtifact(
            @ApiParam(value = "JSON representation of an artifact to be added to the database", required = true)
                    Artifact artifact) {
        if (artifact == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Artifact save = null;
        try{
            save = artifactService.addArtifact(artifact);
        }catch (ConstraintViolationException ce){
           //logging and throwing new exception
        }catch (Exception e){
            //...
        }
        return new ResponseEntity<>(save, HttpStatus.OK);
    }


    @GetMapping("/findAll")
    @ApiOperation(value = "fetch all artifacts", notes = "return a list of artifacts")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 302, message = "Not Found")
    })
    public ResponseEntity<List<Artifact>> retrieveAllArtifacts(){
        List<Artifact> artifacts = new ArrayList<>();
        try {
            artifacts = artifactService.retrieveAllArtifacts();
        }catch (Exception e){
            e.printStackTrace();
            //log.fatal("Exception occurs while retrieving all artifacts : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(artifacts,HttpStatus.FOUND);
    }



}
