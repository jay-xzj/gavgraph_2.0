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
import uk.ac.newcastle.redhat.gavgraph.domain.Organization;
import uk.ac.newcastle.redhat.gavgraph.exception.NotFoundException;
import uk.ac.newcastle.redhat.gavgraph.exception.NullEntityException;
import uk.ac.newcastle.redhat.gavgraph.service.OrganizationService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orgs")
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    @GetMapping("{id}")
    public ResponseEntity<Organization> getById(@PathVariable Long id){
        Organization organization = organizationService.findById(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(organization, HttpStatus.OK);
    }

    @GetMapping("getByNameLike/{name}")
    public ResponseEntity<List<Organization>> getByNameLike(@PathVariable String name){
        List<Organization> organizations = organizationService.findAllByNameContains(name);
        return new ResponseEntity<>(organizations, HttpStatus.OK);
    }

    @GetMapping("getByName/{name}")
    public ResponseEntity<Organization> getByName(@PathVariable String name){
        Organization organization = organizationService.findByName(name);
        return new ResponseEntity<>(organization, HttpStatus.OK);
    }

    @GetMapping("getByUrl/{url}")
    public ResponseEntity<Organization> getByUrl(@PathVariable String url){
        Organization organization = organizationService.findByUrl(url);
        return new ResponseEntity<>(organization, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<Organization> create(@RequestBody Organization organization){
        organization = Optional.ofNullable(organization).orElseThrow(NullEntityException::new);
        Organization save = null;
        try {
            save = organizationService.save(organization);
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
    @ApiOperation(value = "fetch all licnses", notes = "return a list of organizations")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 302, message = "Found")
    })
    public ResponseEntity<List<Organization>> findAll(){
        List<Organization> organizations = null;
        try {
            organizations = organizationService.findAllZeroDepth();
        }catch (RestClientResponseException rcre){
            rcre.printStackTrace();

        }catch (RestClientException rce) {
            rce.printStackTrace();

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(organizations,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        organizationService.deleteById(id);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Organization update(@PathVariable Long id, @RequestBody Organization update) {
        final Organization existing = organizationService.findById(id).orElseThrow(NotFoundException::new);
        existing.updateFrom(update);
        return organizationService.save(existing);
    }
}
