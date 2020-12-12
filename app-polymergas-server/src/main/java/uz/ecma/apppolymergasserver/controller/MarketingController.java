package uz.ecma.apppolymergasserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ReqMarketing;
import uz.ecma.apppolymergasserver.service.MarketingService;
import uz.ecma.apppolymergasserver.utils.AppConstants;

import java.util.UUID;

@RequestMapping("/api/marketing")
@RestController
public class MarketingController {

    @Autowired
    MarketingService marketingService;

    @PostMapping
    public HttpEntity<?> saveMarketing(@RequestBody ReqMarketing reqMarketing) {
        ApiResponse response = marketingService.saveMarketing(reqMarketing);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping
    public HttpEntity<?> getMarketings(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return ResponseEntity.ok(marketingService.getMarketings(page, size));
    }

    @DeleteMapping("{id}")
    public HttpEntity<?> deleteMarketing(@PathVariable UUID id) {
        return ResponseEntity.ok().body(marketingService.deleteMarketing(id));
    }
}
