package uz.ecma.apppolymergasserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.apppolymergasserver.payload.ReqCategory;
import uz.ecma.apppolymergasserver.repository.CategoryRepository;
import uz.ecma.apppolymergasserver.service.CategoryService;
import uz.ecma.apppolymergasserver.utils.AppConstants;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping
    public HttpEntity<?> saveCategory(@RequestBody ReqCategory reqCategory) {
        return ResponseEntity.ok().body(categoryService.saveCategory(reqCategory));
    }

    @GetMapping("{id}")
    public HttpEntity<?> getCategory(@PathVariable Integer id) {
        return ResponseEntity.ok().body(categoryService.getCategory(id));
    }

    @GetMapping
    public HttpEntity<?> getCategories(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "searchName", defaultValue = "") String searchName
    ) {
        return ResponseEntity.ok().body(categoryService.getCategories(page, size, searchName));
    }

    @DeleteMapping("{id}")
    public HttpEntity<?> deleteCategory(@PathVariable Integer id) {
        return ResponseEntity.ok().body(categoryService.deleteCategory(id));
    }
}
