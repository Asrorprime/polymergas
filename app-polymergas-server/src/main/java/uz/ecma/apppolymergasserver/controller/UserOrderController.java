package uz.ecma.apppolymergasserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.ecma.apppolymergasserver.payload.*;
import uz.ecma.apppolymergasserver.repository.SelectedProductRepository;
import uz.ecma.apppolymergasserver.repository.UserOrderHistoryRepository;
import uz.ecma.apppolymergasserver.repository.UserOrderRepository;
import uz.ecma.apppolymergasserver.service.UserOrderService;
import uz.ecma.apppolymergasserver.utils.AppConstants;

import java.util.UUID;

@RestController
@RequestMapping("/api/userOrder")
public class UserOrderController {

    @Autowired
    UserOrderService userOrderService;

    @Autowired
    UserOrderHistoryRepository userOrderHistoryRepository;
    @Autowired
    UserOrderRepository userOrderRepository;
    @Autowired
    SelectedProductRepository selectedProductRepository;


    @PostMapping
    public ApiResponseModel createUserOrder(@RequestBody ReqCart reqCart) {
        return userOrderService.createUserOrder(reqCart);
    }

    @PostMapping("/changeStep")
    public ApiResponse changeStepUserOrder(@RequestBody ReqUserOrderHistory reqUserOrderHistory) {
        return userOrderService.changeStepUserOrder(reqUserOrderHistory);
    }

    @PostMapping("/deleteUserOrder")
    public ApiResponse deleteUserOrder(@RequestBody ReqUserOrderHistory reqUserOrderHistory) {
        return userOrderService.deleteUserOrder(reqUserOrderHistory);
    }

    @GetMapping("/{id}")
    public ApiResponseModel getUserOrder(@PathVariable UUID id) {
        return new ApiResponseModel(true, "Buyurtma", userOrderService.getUserOrder(id));
    }

    @PutMapping("/changeToPayed")
    public HttpEntity<?> changeToPayed(
            @RequestBody ReqChangeToPay reqChangeToPay
    ) {
        return ResponseEntity.ok().body(userOrderService.changeToPayed(reqChangeToPay));
    }

    @GetMapping("/userOrderHistory/{id}")
    public ApiResponseModel getUserOrderHistory(@PathVariable UUID id) {
        return new ApiResponseModel(true, "Buyurtma", userOrderService.getUserOrderHistory(id));
    }

    @GetMapping("/orderHistories")
    public ApiResponseModel getUserOrderHistories(@RequestParam(value = "sort", defaultValue = "") String sort, @RequestParam(value = "search", defaultValue = "") String search, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page, @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size, @RequestParam(value = "state") String state) {
        return userOrderService.getUserOrderHistories(sort, search, page, size, state);
    }

    @PostMapping("/userordertodashboard")
    public ResDashboard getDashboardUserOrder(@RequestBody ReqDashboard reqDashboard) {
        return userOrderService.getDashboardUserOrder(reqDashboard);
    }

    @GetMapping("/userOrdersByPhoneNumber")
    public ApiResponseModel getUserOrdersByPhoneNumber(@RequestParam(value = "searchName") String searchName,
                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return userOrderService.getUserOrdersByPhoneNumber(searchName, page, size);
    }

    @PutMapping("/changeOrderSum")
    public HttpEntity<?> changeOrderSum(@RequestBody ReqChangeOrderSum reqChangeOrderSum) {
        return ResponseEntity.ok().body(userOrderService.changeOrderSum(reqChangeOrderSum));
    }

    @DeleteMapping("/delete")
    public HttpEntity<?> deleteO() {
        try {
            selectedProductRepository.deleteAll();
            userOrderHistoryRepository.deleteAll();
            userOrderRepository.deleteAll();
            return ResponseEntity.ok().body("ok");
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.toString());
        }
    }
}
