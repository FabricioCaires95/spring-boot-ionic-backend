package br.com.CourseSpringBoot.resources;

import br.com.CourseSpringBoot.domain.Category;
import br.com.CourseSpringBoot.dto.CategoryDTO;
import br.com.CourseSpringBoot.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fabricio
 */

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    @Autowired
    private CategoryService service;

    @ApiOperation("return a category by id ")
    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Integer id){

        Category cat = service.findById(id);

        return ResponseEntity.ok().body(cat);
    }

    @ApiOperation("insert a new category, only adm'users can do that")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CategoryDTO catDTO){

        Category cat = service.toCategory(catDTO);

        cat = service.insert(cat);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(cat.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Integer id){
        Category cat = service.toCategory(categoryDTO);
        cat.setId(id);
        cat = service.update(cat);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "can't delete this resource, because it has products"),
            @ApiResponse(code = 404, message = "code has not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){

        service.delete(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll(){

        List<CategoryDTO> listDTO = service.findAll().stream()
                .map(obj -> new CategoryDTO(obj)).collect(Collectors.toList());

       return ResponseEntity.ok(listDTO);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CategoryDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24")Integer linesPerPage,
            @RequestParam(value = "orderby", defaultValue = "name")String orderby,
            @RequestParam(value = "direction", defaultValue = "ASC")String direction){

        Page<Category> list = service.findPage(page, linesPerPage, orderby, direction);
        Page<CategoryDTO> pageDto = list.map(obj -> new CategoryDTO(obj));

        return ResponseEntity.ok(pageDto);
    }



}
