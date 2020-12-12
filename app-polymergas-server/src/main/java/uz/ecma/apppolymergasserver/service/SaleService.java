package uz.ecma.apppolymergasserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.ecma.apppolymergasserver.entity.Sale;
import uz.ecma.apppolymergasserver.entity.SaleHistory;
import uz.ecma.apppolymergasserver.exception.ResourceNotFoundException;
import uz.ecma.apppolymergasserver.payload.*;
import uz.ecma.apppolymergasserver.repository.SaleHistoryRepository;
import uz.ecma.apppolymergasserver.repository.SaleRepository;
import uz.ecma.apppolymergasserver.repository.UserOrderRepository;
import uz.ecma.apppolymergasserver.repository.UserRepository;
import uz.ecma.apppolymergasserver.utils.CommonUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    SaleHistoryRepository saleHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserOrderRepository userOrderRepository;

    @Autowired
    UserOrderService userOrderService;

    public ApiResponse saveSale(ReqSale reqSale) {
        try {
            Sale sale = new Sale();
            if (reqSale.getId() != null) {
                sale = saleRepository.findById(reqSale.getId()).orElseThrow(() -> new ResourceNotFoundException("Chegirma topilmadi!", "id", reqSale.getId()));
            }
            sale.setTitleUz(reqSale.getTitleUz());
            sale.setTitleRu(reqSale.getTitleRu());
            sale.setTitleEn(reqSale.getTitleEn());
            sale.setStartDate(reqSale.getStartDate());
            sale.setEndDate(reqSale.getEndDate());
            sale.setDescriptionUz(reqSale.getDescriptionUz());
            sale.setDescriptionRu(reqSale.getDescriptionRu());
            sale.setDescriptionEn(reqSale.getDescriptionEn());
            sale.setActive(reqSale.isActive());
            sale.setBall(Double.parseDouble(reqSale.getBall()));
            saleRepository.save(sale);
            return new ApiResponse("Muvoffaqiyatli yaratildi!", true);

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public ApiResponseModel getSale(UUID id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aksiya topilmadi!", "id", id));
        return new ApiResponseModel(true, "Aksiya", getResSale(sale));
    }

    public ApiResponse switchActiveSale(UUID id) {
        try {
            Sale sale = saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aksiya topilmadi!", "id", id));
            if (sale.isActive()) {
                sale.setActive(false);
            } else {
                sale.setActive(true);
            }
            saleRepository.save(sale);
            return new ApiResponse("Aksiya statusi muvaffaqiyatli o`zgartirildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public ApiResponseModel getSales(int page, int size, String searchName, String sortType, Timestamp startDate, Timestamp endDate) {
        Page<Sale> sales = null;
        if (startDate != null && endDate != null) {
            switch (sortType) {
                case "byActive":
                    sales = saleRepository.findAllByActiveAndSearchNameContainsIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByCreatedAtDesc(true, searchName, startDate, endDate, CommonUtils.getPageableForNative(page, size));
                    break;
                case "byNotActive":
                    sales = saleRepository.findAllByActiveAndSearchNameContainsIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByCreatedAtDesc(false, searchName, startDate, endDate, CommonUtils.getPageableForNative(page, size));
                    break;
                default:
                    sales = saleRepository.findAllBySearchNameContainsIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByCreatedAtDesc(searchName, startDate, endDate, CommonUtils.getPageableForNative(page, size));
                    break;
            }
        } else {
            switch (sortType) {
                case "byActive":
                    sales = saleRepository.findAllByActiveAndSearchNameContainsIgnoreCaseOrderByCreatedAtDesc(true, searchName, CommonUtils.getPageableForNative(page, size));
                    break;
                case "byNotActive":
                    sales = saleRepository.findAllByActiveAndSearchNameContainsIgnoreCaseOrderByCreatedAtDesc(false, searchName, CommonUtils.getPageableForNative(page, size));
                    break;
                default:
                    sales = saleRepository.findAllBySearchNameContainsIgnoreCaseOrderByCreatedAtDesc(searchName, CommonUtils.getPageableForNative(page, size));
                    break;
            }
        }
        Page<ResSale> pageable = null;
        if (sales != null) {
            pageable = new PageImpl<>(
                    sales.getContent().stream().map(this::getResSale).collect(Collectors.toList()),
                    sales.getPageable(),
                    sales.getTotalElements()
            );
        }
        return new ApiResponseModel(true, "Chegirmalar!", pageable);
    }

    public ResSale getResSale(Sale sale) {
        if (sale != null) {
            return new ResSale(
                    sale.getId(),
                    sale.getTitleUz(),
                    sale.getTitleRu(),
                    sale.getTitleEn(),
                    sale.getStartDate() == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(sale.getStartDate()),
                    sale.getEndDate() == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(sale.getEndDate()),
                    sale.getDescriptionUz(),
                    sale.getDescriptionRu(),
                    sale.getDescriptionEn(),
                    sale.isActive(),
                    sale.getBall(),
                    sale.getSaleHistories() == null ? null : sale.getSaleHistories().stream().map(this::getResSaleHistory).collect(Collectors.toList())
            );
        }
        return null;
    }

    public ApiResponse deleteSale(UUID id) {
        try {
            saleRepository.deleteById(id);
            return new ApiResponse("Muvoffaqiyatli o'chirildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public SaleHistory createSaleHistory(ReqSaleHistory reqSaleHistory) {
        SaleHistory saleHistory = new SaleHistory();
        if (reqSaleHistory.getId() != null) {
            saleHistory = saleHistoryRepository.findById(reqSaleHistory.getId()).orElseThrow(() -> new ResourceNotFoundException("Sale history not found!", "id", reqSaleHistory.getId()));
        }
        saleHistory.setBall(reqSaleHistory.getBall());
        saleHistory.setSale(saleRepository.findById(reqSaleHistory.getSaleId()).orElseThrow(() -> new ResourceNotFoundException("Sale not found!", "id", reqSaleHistory.getSaleId())));
        saleHistory.setSaleUser(userRepository.findById(reqSaleHistory.getSaleUserId()).orElseThrow(() -> new ResourceNotFoundException("User nnot found!", "id", reqSaleHistory.getSaleUserId())));
        return saleHistoryRepository.save(saleHistory);
    }

    public ResSaleHistory getResSaleHistory(SaleHistory saleHistory) {
        return new ResSaleHistory(
                saleHistory.getId(),
                saleHistory.getBall(),
                saleHistory.getSaleUser() == null ? null : userService.getResUser(saleHistory.getSaleUser()),
                saleHistory.getSale() == null ? null : saleHistory.getSale().getId(),
                saleHistory.getCreatedAt() == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(saleHistory.getCreatedAt()));
    }

    public ApiResponse deleteSaleHistory(UUID id) {
        try {
            saleHistoryRepository.deleteById(id);
            return new ApiResponse("Muvoffaqiyatli o'chirildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public ApiResponseModel getSaleHistory(UUID id) {
        SaleHistory saleHistory = saleHistoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sale history", "id", id));
        return new ApiResponseModel(true, "Aksiya tarixi", getResSaleHistory(saleHistory));
    }

    public ApiResponseModel getSaleHistories(int page, int size, String searchName) {
        Page<SaleHistory> salesPage = saleHistoryRepository.findAllBySaleUser_FullNameContainsIgnoreCaseOrSaleUser_PhoneNumberOrSale_TitleUzContainsIgnoreCase(searchName, searchName, searchName, CommonUtils.getPageable(page, size));
        Page<ResSaleHistory> resSaleHistories = new PageImpl<ResSaleHistory>(
                salesPage.getContent().stream().map(this::getResSaleHistory).collect(Collectors.toList()),
                salesPage.getPageable(),
                salesPage.getTotalPages()
        );
        return new ApiResponseModel(true, "Aksiyalar tarixi", resSaleHistories);
    }

    @Transactional
    public ApiResponseModel getSaleHistoryUsers(UUID saleId, int page, int size) {
        Page<Object[]> allByaaa = saleHistoryRepository.findAllByaaa(saleId, CommonUtils.getPageableWithoutSort(page, size));
        PageImpl<ResSaleHistoryUser> saleHistoryUsers = new PageImpl<>(
                allByaaa.getContent().stream().map(saleHistory ->
                        getResSaleHistoryUser(
                                Double.parseDouble(saleHistory[0].toString()),
                                UUID.fromString(saleHistory[1].toString()),
                                saleId
                        )
                ).collect(Collectors.toList()),
                allByaaa.getPageable(),
                allByaaa.getTotalElements()
        );
        return new ApiResponseModel(true, "Aksiya qatnashuvchilari!", saleHistoryUsers);
    }

    public ResSaleHistoryUser getResSaleHistoryUser(Double ball, UUID userId, UUID saleId) {
        return new ResSaleHistoryUser(
                saleId,
                ball,
                userService.getResUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!", "id", userId))));
    }
}
