package uk.ac.newcastle.redhat.gavgraph.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.Parent;
import uk.ac.newcastle.redhat.gavgraph.exception.NotFoundException;
import uk.ac.newcastle.redhat.gavgraph.service.ParentService;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/api/parent")
public class ParentController {

    @Resource
    private ParentService parentService;

    @GetMapping("{id}")
    public ResponseEntity<Parent> getById(@PathVariable Long id){
        Parent parent = parentService.findById(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(parent, HttpStatus.OK);
    }

    @PostMapping("/create")
    @ApiOperation(value = "Add a new Parent to the database")
    public ResponseEntity<Parent> create(
            @RequestBody @ApiParam(value = "JSON representation of an parent to be added to the database", required = true)
                    Parent parent) {
        if (parent == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Parent save = null;
        try{
            save = parentService.save(parent);
            //log.info("An parent created : " + parent);
        }catch (ConstraintViolationException ce){
            //logging and throwing new exception
        }catch (Exception e){
            e.printStackTrace();
            //log.fatal("Exception occurs while creating an parent : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
        }
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "fetch all parents", notes = "return a list of parents")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 302, message = "Found")
    })
    public ResponseEntity<List<Parent>> findAll(){
        List<Parent> parents;
        try {
            parents = parentService.findAllZeroDepth();
        }catch (Exception e){
            e.printStackTrace();
            //log.fatal("Exception occurs while retrieving all parents : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(parents,HttpStatus.OK);
    }

    @GetMapping("findById/{id}")
    @ApiOperation(value = "find parent by id",notes = "return an parent with certain id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "no content"),
            @ApiResponse(code = 302, message = "Not found")
    })
    public ResponseEntity<Parent> findById(@PathVariable @ApiParam Long id){
        Parent parent;
        try{
            parent = parentService.findById(id).orElseThrow(NotFoundException::new);
        }catch (NotFoundException ne){
            ne.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(parent,HttpStatus.FOUND);
    }

    @GetMapping("findByGroupId/{groupId}")
    public ResponseEntity<List<Parent>> findByGroupId(@PathVariable String groupId){
        List<Parent> parents = null;
        try{
            parents = parentService.findByGroupId(groupId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(parents, HttpStatus.OK);
    }

    @GetMapping("findGroupIdLike/{groupId}")
    public ResponseEntity<List<Parent>> findGroupIdLike(@PathVariable @ApiParam(defaultValue = "redhat-7") String groupId){
        List<Parent> parents = null;
        try{
            parents = parentService.findGroupIdLike(groupId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(parents, HttpStatus.OK);
    }

    @GetMapping("findArtifactId/{artifactId}")
    public ResponseEntity<List<Parent>> findByArtifactId(@PathVariable String artifactId){
        List<Parent> parents = null;
        try{
            parents = parentService.findArtifactId(artifactId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(parents, HttpStatus.OK);
    }

    @GetMapping("findArtifactIdLike/{parentId}")
    public ResponseEntity<List<Parent>> findArtifactIdLike(@PathVariable String artifactId){
        List<Parent> parents = null;
        try{
            parents = parentService.findArtifactIdLike(artifactId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(parents, HttpStatus.OK);
    }



    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        parentService.deleteById(id);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Parent update(@PathVariable Long id, @RequestBody Parent update) {
        final Parent existing = parentService.findById(id).orElseThrow(NotFoundException::new);
        existing.updateFrom(update);
        return parentService.save(existing);
    }
}
