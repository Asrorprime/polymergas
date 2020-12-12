package uz.ecma.apppolymergasserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.apppolymergasserver.payload.*;
import uz.ecma.apppolymergasserver.repository.ProductRepository;
import uz.ecma.apppolymergasserver.service.ProductService;
import uz.ecma.apppolymergasserver.service.UserInfo;
import uz.ecma.apppolymergasserver.service.WithUser;
import uz.ecma.apppolymergasserver.utils.AppConstants;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/product")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @PostMapping
    public HttpEntity<?> saveProduct(@RequestBody ReqProduct reqProduct) {
        return ResponseEntity.ok().body(productService.saveProduct(reqProduct));
    }

    @PostMapping("/productpricemap")
    public List<ResProductPrice> getResProductPrices(@RequestParam List<UUID> productPriceIds) {
        return productService.getResProductPrices(productPriceIds);
    }

    @GetMapping("/changeProductPriceHaveStatus/{id}")
    public HttpEntity<?> changeProductPriceHaveStatus(@PathVariable UUID id,
                                                      @RequestParam(value = "have", defaultValue = "false") boolean have
    ) {
        return ResponseEntity.ok().body(productService.changeProductPriceHaveStatus(id, have));
    }

    @GetMapping("/productPricesHave")
    public List<ResProductPrice> getResProductPricesHave() {
        return productService.getResProductPricesHave();
    }

    @PostMapping("/productmap")
    public List<ResProduct> getResProduct(@RequestParam List<UUID> productPriceIds) {
        return productService.getResProduct(productPriceIds);
    }

    @GetMapping("/{id}")
    public ApiResponseModel getProduct(@PathVariable UUID id) {
        return new ApiResponseModel(true, "Mahsulot", productService.getProduct(id));
    }


    @GetMapping("/productPrice/{id}")
    public ApiResponseModel getProductPrice(@PathVariable UUID id) {
        return productService.getProductPrice(id);
    }

    @GetMapping
    public ApiResponseModel getProducts(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                        @RequestParam(value = "searchName", defaultValue = "") String searchName,
                                        @RequestParam(value = "sortType", defaultValue = "all") String sortType,
                                        @RequestParam(value = "haveSort", defaultValue = "false") boolean haveSort
    ) {
        return productService.getProducts(page, size, searchName, sortType,haveSort);
    }

    @GetMapping("/productsForSale")
    public ApiResponseModel getProducts() {
        return productService.getProductsForSale();
    }

    @GetMapping("/popular")
    public ApiResponseModel getPopularProducts(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                               @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return productService.getPopularProducts(page, size);
    }


    @DeleteMapping("/{id}")
    public ApiResponse deleteProduct(@PathVariable UUID id) {
        return productService.deleteProduct(id);
    }


    @PostMapping("/todashboard")
    public ResDashboard getProductToDashboard(@RequestBody ReqDashboard reqDashboard, @WithUser UserInfo userInfo) {

        return productService.getProductToDashboard(reqDashboard);
    }


}
