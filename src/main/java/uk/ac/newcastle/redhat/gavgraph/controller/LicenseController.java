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
import uk.ac.newcastle.redhat.gavgraph.domain.License;
import uk.ac.newcastle.redhat.gavgraph.exception.NotFoundException;
import uk.ac.newcastle.redhat.gavgraph.exception.NullEntityException;
import uk.ac.newcastle.redhat.gavgraph.service.LicenseService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {

    @Resource
    private LicenseService licenseService;

    @GetMapping("{id}")
    public ResponseEntity<License> getById(@PathVariable Long id){
        License license = licenseService.findById(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(license, HttpStatus.OK);
    }

    @GetMapping("getByNameLike/{name}")
    public ResponseEntity<List<License>> getByNameLike(@PathVariable String name){
        List<License> licenses = licenseService.findAllByNameContains(name);
        return new ResponseEntity<>(licenses, HttpStatus.OK);
    }

    @GetMapping("getByName/{name}")
    public ResponseEntity<License> getByName(@PathVariable String name){
        License license = licenseService.findByName(name);
        return new ResponseEntity<>(license, HttpStatus.OK);
    }

    @GetMapping("getByUrl/{url}")
    public ResponseEntity<License> getByUrl(@PathVariable String url){
        License license = licenseService.findByUrl(url);
        return new ResponseEntity<>(license, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<License> create(@RequestBody License license){
        license = Optional.ofNullable(license).orElseThrow(NullEntityException::new);
        License save = null;
        try {
            save = licenseService.save(license);
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
    @ApiOperation(value = "fetch all licnses", notes = "return a list of licenses")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 302, message = "Found")
    })
    public ResponseEntity<List<License>> findAll(){
        List<License> licenses = null;
        try {
            licenses = licenseService.findAllZeroDepth();
        }catch (RestClientResponseException rcre){
            rcre.printStackTrace();

        }catch (RestClientException rce) {
            rce.printStackTrace();

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(licenses,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        licenseService.deleteById(id);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public License update(@PathVariable Long id, @RequestBody License update) {
        final License existing = licenseService.findById(id).orElseThrow(NotFoundException::new);
        existing.updateFrom(update);
        return licenseService.save(existing);
    }
}
