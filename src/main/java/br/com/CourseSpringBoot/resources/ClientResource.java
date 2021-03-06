package br.com.CourseSpringBoot.resources;


import br.com.CourseSpringBoot.domain.Category;
import br.com.CourseSpringBoot.domain.Client;
import br.com.CourseSpringBoot.dto.CategoryDTO;
import br.com.CourseSpringBoot.dto.ClientDTO;
import br.com.CourseSpringBoot.dto.ClientNewDTO;
import br.com.CourseSpringBoot.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fabricio
 */
@RestController
@RequestMapping("/clients")
public class ClientResource {

    @Autowired
    private ClientService service;

    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Integer id){

        Client cli = service.findById(id);

        return ResponseEntity.ok().body(cli);
    }

    @GetMapping("/email")
    public ResponseEntity<Client> findByEmail(@RequestParam(value = "value") String email){

        Client cli = service.findByEmail(email);

        return ResponseEntity.ok().body(cli);
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody ClientNewDTO newdto){

        Client cli = service.toClient(newdto);

        cli = service.insert(cli);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(cli.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody ClientDTO clientDTO, @PathVariable Integer id){
        Client cli = service.toClient(clientDTO);
        cli.setId(id);
        cli = service.update(cli);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){

        service.delete(id);

        return ResponseEntity.noContent().build();

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClientDTO>> findAll(){

        List<ClientDTO> listDTO = service.findAll().stream()
                .map(obj -> new ClientDTO(obj)).collect(Collectors.toList());

        return ResponseEntity.ok(listDTO);
    }



    @GetMapping("/page")
    public ResponseEntity<Page<ClientDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24")Integer linesPerPage,
            @RequestParam(value = "orderby", defaultValue = "name")String orderby,
            @RequestParam(value = "direction", defaultValue = "ASC")String direction){

        Page<Client> list = service.findPage(page, linesPerPage, orderby, direction);
        Page<ClientDTO> pageDto = list.map(obj -> new ClientDTO(obj));

        return ResponseEntity.ok().body(pageDto);
    }

    @PostMapping("/picture")
    public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file") MultipartFile file){

        URI uri = service.uploadProfilePicture(file);

        return ResponseEntity.created(uri).build();

    }

}
