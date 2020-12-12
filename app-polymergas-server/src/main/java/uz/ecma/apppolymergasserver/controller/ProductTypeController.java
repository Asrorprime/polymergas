package uz.ecma.apppolymergasserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.apppolymergasserver.payload.ReqProductType;
import uz.ecma.apppolymergasserver.repository.ProductTypeRepository;
import uz.ecma.apppolymergasserver.service.ProductTypeService;
import uz.ecma.apppolymergasserver.utils.AppConstants;

@RestController
@RequestMapping("/api/productType")
public class ProductTypeController {
    @Autowired
    ProductTypeService productTypeService;

    @Autowired
    ProductTypeRepository productTypeRepository;

    @PostMapping
    public HttpEntity<?> saveProductType(@RequestBody ReqProductType reqProductType) {
        return ResponseEntity.ok().body(productTypeService.saveProductType(reqProductType));
    }

    @DeleteMapping("{id}")
    public HttpEntity<?> deleteProductType(@PathVariable Integer id) {
        return ResponseEntity.ok().body(productTypeService.deleteProductType(id));
    }

    @GetMapping("{id}")
    public HttpEntity<?> getProductType(@PathVariable Integer id) {
        return ResponseEntity.ok().body(productTypeService.getProductType(id));
    }

    @GetMapping
    public HttpEntity<?> getProductTypes(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "searchName", defaultValue = "") String searchName
    ) {
        return ResponseEntity.ok().body(productTypeService.getProductTypes(page, size, searchName));
    }
}
