package naakcii.by.api.controller;

import naakcii.by.api.service.CategoryService;
import naakcii.by.api.service.ChainService;
import naakcii.by.api.service.modelDTO.CategoryDTO;
import naakcii.by.api.service.modelDTO.ChainDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping({"/api"})
public class APIController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ChainService chainService;

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.create(categoryDTO);
    }

    @GetMapping(path = {"/{id}"})
    public CategoryDTO findOne(@PathVariable("id") Long id) {
        return categoryService.findById(id);
    }

    @PutMapping
    public CategoryDTO update(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.update(categoryDTO);
    }

    @DeleteMapping(path = {"/{id}"})
    public CategoryDTO delete(@PathVariable("id") Long id) {
        return categoryService.delete(id);
    }

    @GetMapping(path = {"getCategory"})
    public List<CategoryDTO> findAllCategory() {
        return categoryService.findAll();
    }

    @GetMapping(path = {"getChain"})
    public List<ChainDTO> findAllStorage() {
        return chainService.findAll();
    }
}
