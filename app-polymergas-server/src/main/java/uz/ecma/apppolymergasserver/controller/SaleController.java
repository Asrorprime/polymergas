package uz.ecma.apppolymergasserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ApiResponseModel;
import uz.ecma.apppolymergasserver.payload.ReqSale;
import uz.ecma.apppolymergasserver.service.SaleService;
import uz.ecma.apppolymergasserver.utils.AppConstants;

import java.sql.Timestamp;
import java.util.UUID;

@RequestMapping("/api/sale")
@RestController
public class SaleController {

    @Autowired
    SaleService saleService;

    @PostMapping
    public ApiResponse saveSale(@RequestBody ReqSale reqSale) {
        return saleService.saveSale(reqSale);
    }

    @GetMapping("/switchActiveSale/{id}")
    public ApiResponse switchActiveSale(@PathVariable UUID id) {
        return saleService.switchActiveSale(id);
    }

    @GetMapping("/{id}")
    public ApiResponseModel getSale(@PathVariable UUID id) {
        return saleService.getSale(id);
    }


    @GetMapping
    public ApiResponseModel getSales(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                     @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                     @RequestParam(value = "searchName", defaultValue = "") String searchName,
                                     @RequestParam(value = "sortType", defaultValue = "all") String sortType,
                                     @RequestParam(value = "startDate", defaultValue = AppConstants.BEGIN_DATE) Timestamp startDate,
                                     @RequestParam(value = "endDate", defaultValue = AppConstants.END_DATE) Timestamp endDate
    ) {
        return saleService.getSales(page, size, searchName, sortType, startDate, endDate);
    }

    @DeleteMapping("{id}")
    public HttpEntity<?> deleteSale(@PathVariable UUID id) {
        return ResponseEntity.ok().body(saleService.deleteSale(id));
    }

    @DeleteMapping("/saleHistory/{id}")
    public ApiResponse deleteSaleHistory(@PathVariable UUID id) {
        return saleService.deleteSaleHistory(id);
    }

    @GetMapping("/saleHistory/{id}")
    public ApiResponseModel getSaleHistory(@PathVariable UUID id) {
        return saleService.getSaleHistory(id);
    }

    @GetMapping("/saleHistory")
    public ApiResponseModel getSaleHistories(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                             @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                             @RequestParam(value = "searchName", defaultValue = "") String searchName) {
        return saleService.getSaleHistories(page, size, searchName);
    }

    @GetMapping("/participants")
    public ApiResponseModel getParticipants(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam UUID saleId){
        return saleService.getSaleHistoryUsers(saleId,page,size);
    }
}
