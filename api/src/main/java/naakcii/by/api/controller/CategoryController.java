package naakcii.by.api.controller;

import naakcii.by.api.service.CategoryService;
import naakcii.by.api.service.modelDTO.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping({"/api"})
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.create(categoryDTO);
    }

    @GetMapping(path = {"/{id}"})
    public CategoryDTO findOne(@PathVariable("id") int id) {
        return categoryService.findById(id);
    }

    @PutMapping
    public CategoryDTO update(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.update(categoryDTO);
    }

    @DeleteMapping(path = {"/{id}"})
    public CategoryDTO delete(@PathVariable("id") int id) {
        return categoryService.delete(id);
    }

    @GetMapping(path = {"getCategory"})
    public List<CategoryDTO> findAll() {
        return categoryService.findAll();
    }
}
