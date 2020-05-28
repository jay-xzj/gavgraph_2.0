package uk.ac.newcastle.redhat.gavgraph.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import uk.ac.newcastle.redhat.gavgraph.domain.Developer;
import uk.ac.newcastle.redhat.gavgraph.exception.NotFoundException;
import uk.ac.newcastle.redhat.gavgraph.exception.NullEntityException;
import uk.ac.newcastle.redhat.gavgraph.service.DeveloperService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {

    @Resource
    private DeveloperService developerService;

    @GetMapping("{id}")
    public ResponseEntity<Developer> getById(@PathVariable Long id){
        Developer developer = developerService.findById(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(developer, HttpStatus.OK);
    }

    @GetMapping("getByNameLike/{name}")
    public ResponseEntity<List<Developer>> getByNameLike(@PathVariable String name){
        List<Developer> developers = developerService.findAllByNameContains(name);
        return new ResponseEntity<>(developers, HttpStatus.OK);
    }

    @GetMapping("getByEmail/{email}")
    public ResponseEntity<Developer> getByEmail(@PathVariable String email){
        Developer developer = developerService.findByEmail(email);
        return new ResponseEntity<>(developer, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<Developer> create(@RequestBody Developer developer){
        developer = Optional.ofNullable(developer).orElseThrow(NullEntityException::new);
        Developer save = null;
        try {
            save = developerService.save(developer);
        } catch (RestClientResponseException rcre) {
            rcre.printStackTrace();
        } catch (RestClientException rce) {
            rce.printStackTrace();
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(save,HttpStatus.OK);
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "fetch all artifacts", notes = "return a list of artifacts")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 302, message = "Found")
    })
    public ResponseEntity<List<Developer>> findAll(){
        List<Developer> developers = null;
        try {
            developers = (List<Developer>) developerService.findAllZeroDepth();
        }catch (RestClientResponseException rcre){
            rcre.printStackTrace();

        }catch (RestClientException rce) {
            rce.printStackTrace();
            System.out.println("");
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(developers,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        developerService.deleteById(id);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Developer update(@PathVariable Long id, @RequestBody Developer update) {
        final Developer existing = developerService.findById(id).orElseThrow(NotFoundException::new);
        existing.updateFrom(update);
        return developerService.save(existing);
    }

}
