package uz.ecma.apppolymergasserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.ecma.apppolymergasserver.bot.Constant;
import uz.ecma.apppolymergasserver.bot.PolymerBot;
import uz.ecma.apppolymergasserver.bot.TelegramService;
import uz.ecma.apppolymergasserver.entity.*;
import uz.ecma.apppolymergasserver.entity.enums.Step;
import uz.ecma.apppolymergasserver.exception.ResourceNotFoundException;
import uz.ecma.apppolymergasserver.payload.*;
import uz.ecma.apppolymergasserver.repository.*;
import uz.ecma.apppolymergasserver.utils.CommonUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserOrderService {


    @Autowired
    UserOrderRepository userOrderRepository;
    @Autowired
    GeneratedRepository generatedRepository;

    @Autowired
    SelectedProductRepository selectedProductRepository;

    @Autowired
    UserOrderHistoryRepository userOrderHistoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    PolymerBot polymerBot;

    @Autowired
    TelegramService telegramService;

    @Autowired
    SaleService saleService;

    @Autowired
    SaleRepository saleRepository;

    @Autowired
    SaleHistoryRepository saleHistoryRepository;

    @Transactional
    public ApiResponseModel createUserOrder(ReqCart reqCart) {
        try {
            if (reqCart.getReqSelectedProducts().stream().filter(reqSelectedProduct -> reqSelectedProduct.getCount() < 1).count() > 0) {
                return new ApiResponseModel(false, "Buyurtmada mahsulotlar soni kamida 1 ta bo'lishi lozim!");
            } else {
                UserOrder userOrder = new UserOrder();
                if (reqCart.getUserOrderId() != null) {
                    userOrder = userOrderRepository.findById(reqCart.getUserOrderId()).orElseThrow(() -> new ResourceNotFoundException("Buyurtma topilmadi!", "id", reqCart.getUserOrderId()));
                    if (userOrder.getOrderStep() == Step.REJECTED || userOrder.getOrderStep() == Step.CLOSED || userOrder.getOrderStep() == Step.CANCELLED) {
                        return new ApiResponseModel(false, "Ushbu buyurtmani o'zgartirish imkoniyati mavjud emas!");
                    }
                }
                userOrder.setUserId(reqCart.getUserId());
                userOrder.setPayed(false);
                userOrder.setUniqueOrder(generatedRepository.save(new Generated()));
                if (reqCart.getUserOrderId() != null) {
                    userOrder.setOrderStep(reqCart.getStep());
                } else {
                    userOrder.setOrderStep(Step.CREATED);
                }
                userOrder.setSelectedProducts(new ArrayList<>());
                userOrder.setCreatedBy(reqCart.getCurrentUserId());
                userOrder.setUserOrderHistory(new ArrayList<>());
                UserOrder save = userOrderRepository.save(userOrder);
                save.getUserOrderHistory().addAll(Collections.singletonList(createUserOrderHistory(save)));
                if (reqCart.getReqSelectedProducts() != null) {
                    ArrayList<SelectedProduct> selectedProducts = new ArrayList<>();
                    reqCart.getReqSelectedProducts().forEach(reqSelectedProduct -> {
                        selectedProducts.add(selectedProductRepository.save(createSelectedProduct(reqSelectedProduct, save)));
                    });
                    save.getSelectedProducts().addAll(selectedProducts);
                }
                notificationService.createNotification("Yangi buyurtma!", userService.getAdmins().stream().map(user -> userService.getResUser(user).getId()).collect(Collectors.toSet()), save.getId(), Step.CREATED);
                return new ApiResponseModel(true, "Muvoffaqiyatli yaratildi!", getResUserOrder(userOrderRepository.save(save)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponseModel(false, "Xatolik", null);
        }
    }

    public SelectedProduct createSelectedProduct(ReqSelectedProduct reqSelectedProduct, UserOrder userOrder) {
        Product product = productRepository.findById(reqSelectedProduct.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found!", "id", reqSelectedProduct.getProductId()));
        ResProduct resProduct = productService.getResProduct(product);
        SelectedProduct selectedProduct = new SelectedProduct();
        selectedProduct.setProductId(reqSelectedProduct.getProductId());
        selectedProduct.setProductPriceId(reqSelectedProduct.getProductPriceId());
        selectedProduct.setCount(reqSelectedProduct.getCount());
        if ((reqSelectedProduct.getCount() * resProduct.getResProductPrices().stream().filter(resProductPrice -> resProductPrice.getProductPriceId().equals(selectedProduct.getProductPriceId())).collect(Collectors.toList()).get(0).getPrice()) == reqSelectedProduct.getCalculatedPrice())
            selectedProduct.setCalculatedPrice(reqSelectedProduct.getCount() * (resProduct.getResProductPrices().stream().filter(resProductPrice -> resProductPrice.getProductPriceId().equals(reqSelectedProduct.getProductPriceId())).collect(Collectors.toList()).get(0).getPrice()));
        else throw new IncorrectResultSetColumnCountException("Error count price", 1, 1);
        selectedProduct.setUserOrder(userOrder);
        selectedProduct.setCreatedBy(userOrder.getCreatedBy());
        return selectedProduct;
    }

    public UserOrderHistory createUserOrderHistory(UserOrder userOrder) {
        UserOrderHistory userOrderHistory = new UserOrderHistory(userOrder, Step.CREATED, "Buyurma yaratildi!", null);
        return userOrderHistoryRepository.save(userOrderHistory);
    }

    public ApiResponse changeStepUserOrder(ReqUserOrderHistory reqUserOrderHistory) {
        try {
            UserOrder userOrder = userOrderRepository.findById(reqUserOrderHistory.getUserOrderId()).orElseThrow(() -> new ResourceNotFoundException("Buyrutma topilmadi!", "id", reqUserOrderHistory.getUserOrderId()));
            userOrder.setOrderStep(reqUserOrderHistory.getCurrentStep());
            sendNotificationTelegramAboutOrder(userOrder);
            notificationService.createNotification(reqUserOrderHistory.getMessage(), userOrder.getUserId(), reqUserOrderHistory.getUserOrderId(), reqUserOrderHistory.getCurrentStep());
            notificationService.createNotification(reqUserOrderHistory.getMessage(), userService.getAdmins().stream().map(user -> userService.getResUser(user).getId()).collect(Collectors.toSet()), reqUserOrderHistory.getUserOrderId(), reqUserOrderHistory.getCurrentStep());
            userOrder.getUserOrderHistory().add(createUserOrderHistory(reqUserOrderHistory));
            userOrderRepository.save(userOrder);
            return new ApiResponse("Muvaffaqiyatli bajarildi!", true);

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik yuz berdi!", false);
        }
    }

    public UserOrderHistory createUserOrderHistory(ReqUserOrderHistory reqUserOrderHistory) {
        UserOrderHistory userOrderHistory = new UserOrderHistory();
        userOrderHistory.setCurrentStep(reqUserOrderHistory.getCurrentStep());
        userOrderHistory.setMessage(reqUserOrderHistory.getMessage());
        userOrderHistory.setUserOrder(userOrderRepository.findById(reqUserOrderHistory.getUserOrderId()).get());
        userOrderHistory.setChangedAdminId(reqUserOrderHistory.getAdminId());
        return userOrderHistoryRepository.save(userOrderHistory);
    }

    public ApiResponse deleteUserOrder(ReqUserOrderHistory reqUserOrderHistory) {
        try {
            UserOrder userOrder = userOrderRepository.findById(reqUserOrderHistory.getUserOrderId()).orElseThrow(() -> new ResourceNotFoundException("Buyurtma topilmadi!", "id", reqUserOrderHistory.getUserOrderId()));
            userOrderRepository.deleteById(reqUserOrderHistory.getUserOrderId());
            createUserOrderHistory(reqUserOrderHistory);
            notificationService.createNotification("Sizning " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(userOrder.getCreatedAt()) + " vaqtda yaratilgan buyurtmangiz adminlar tomonidan o`chirildi!", userOrder.getUserId(), null, Step.DELETED);
            if (reqUserOrderHistory.getAdminId() != null) {
                notificationService.createNotification("Mijozning buyurtmasi " + userService.getUser(reqUserOrderHistory.getAdminId()).getFullName() + " tomonidan o`chirildi!", userService.getAdmins().stream().map(user -> userService.getResUser(user).getId()).collect(Collectors.toSet()), null, Step.DELETED);
            }
            return new ApiResponse("Muvoffaqiyatli bajarildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik yuz berdi!", false);
        }
    }

    public ApiResponseModel getUserOrder(UUID id) {
        UserOrder userOrder = userOrderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Buyurtma topilmadi!", "id", id));
        return new ApiResponseModel(true, "Buyurtma", getResUserOrder(userOrder));
    }

    public ApiResponse changeToPayed(ReqChangeToPay reqChangeToPay) {
        try {
            if (reqChangeToPay.getCurrentStep().equals(Step.CREATED) || reqChangeToPay.getCurrentStep().equals(Step.IN_PROGRESS)) {
                UserOrderHistory userOrderHistory = new UserOrderHistory();
                userOrderHistory.setCurrentStep(reqChangeToPay.getCurrentStep());
                userOrderHistory.setMessage(reqChangeToPay.getMessage());
                userOrderHistory.setUserOrder(userOrderRepository.findById(reqChangeToPay.getUserOrderId()).get());
                userOrderHistory.setChangedAdminId(reqChangeToPay.getAdminId());
                userOrderHistoryRepository.save(userOrderHistory);

                UserOrder userOrder = userOrderRepository.findById(reqChangeToPay.getUserOrderId()).orElseThrow(() -> new ResourceNotFoundException("Buyurtma topilmadi!", "id", reqChangeToPay.getUserOrderId()));
                notificationService.createNotification(reqChangeToPay.getMessage(), userOrder.getUserId(), reqChangeToPay.getUserOrderId(), reqChangeToPay.getCurrentStep());
                notificationService.createNotification(reqChangeToPay.getMessage(), userService.getAdmins().stream().map(user -> userService.getResUser(user).getId()).collect(Collectors.toSet()), reqChangeToPay.getUserOrderId(), reqChangeToPay.getCurrentStep());

                Optional<Sale> optionalSale = saleRepository.findSaleByPayedDateBetweenAndEndAndActiveTrue(new Timestamp(System.currentTimeMillis()));
                if (optionalSale.isPresent()) {
                    Sale sale = optionalSale.get();
                    if (reqChangeToPay.isPayed()) {
                        sale.getSaleHistories().add(saleService.createSaleHistory(new ReqSaleHistory(sale.getBall()*userOrder.getSelectedProducts().stream().map(SelectedProduct::getCount).reduce(Double::sum).get(), userOrder.getUserId(), sale.getId())));
                        saleRepository.save(sale);
                    } else {
                        if (saleHistoryRepository.findAllBySaleUser_IdOrderByCreatedAtDesc(userOrder.getUserId()).size() > 0) {
                            SaleHistory saleHistory = saleHistoryRepository.findAllBySaleUser_IdOrderByCreatedAtDesc(userOrder.getUserId()).get(0);
                            sale.getSaleHistories().remove(saleHistory);
                            saleHistoryRepository.deleteById(saleHistory.getId());
                            saleRepository.save(sale);
                        }
                    }


                }
                userOrder.setPayed(reqChangeToPay.isPayed());
                userOrderRepository.save(userOrder);
                return new ApiResponse("Muvaffaqiyatli amalga oshirildi!", true);

            } else {
                return new ApiResponse("Ushbu holatda to'lovni amalga oshirib bo`lmaydi!", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public ApiResponseModel getUserOrderHistory(UUID userOrderHistoryId) {
        UserOrderHistory userOrderHistory = userOrderHistoryRepository.findById(userOrderHistoryId).orElseThrow(() -> new ResourceNotFoundException("Tarix topilmadi!", "id", userOrderHistoryId));
        return new ApiResponseModel(true, "Buyurtma amaliyoti tarixi", getResUserOrderHistory(userOrderHistory));
    }

    public ApiResponseModel getUserOrderHistories(String sort, String search, int page, int size, String step) {
        List<UserOrder> all;
        switch (sort) {
            case "createdAtDesc":
                all = userOrderRepository.findAllByStatusCreatedAtDesc(search, step, page, size);
                break;
            case "userNameAsc":
                all = userOrderRepository.findAllByStatusUserNameAsc(search, step, page, size);
                break;
            case "userNameDesc":
                all = userOrderRepository.findAllByStatusUserNameDesc(search, step, page, size);
                break;
            case "priceAsc":
                all = userOrderRepository.findAllByStatusPriceAsc(search, step, page, size);
                break;
            case "priceDesc":
                all = userOrderRepository.findAllByStatusPriceDesc(search, step, page, size);
                break;
            case "countAsc":
                all = userOrderRepository.findAllByStatusCountAsc(search, step, page, size);
                break;
            case "countDesc":
                all = userOrderRepository.findAllByStatusCountDesc(search, step, page, size);
                break;
            default:
                all = userOrderRepository.findAllByStatusCreatedAtAsc(search, step, page, size);
                break;
        }

        Page<ResUserOrder> pageable = new PageImpl<>(
                all.stream().map(this::getResUserOrder).collect(Collectors.toList()),
                CommonUtils.getPageable(page, size),
                userOrderRepository.findAllByStatusCount(step)
        );
        return new ApiResponseModel(true, pageable);
    }

    public ResUserOrder getResUserOrder(UserOrder userOrder) {
        if (userOrder.getSelectedProducts() != null) {
            userOrder.setChangedPrice(!(userOrder.getSelectedProducts().stream().filter(selectedProduct -> productPriceRepository.findById(selectedProduct.getProductPriceId()).get().getPrice() * selectedProduct.getCount() == selectedProduct.getCalculatedPrice()).count() == userOrder.getSelectedProducts().size()));
            userOrderRepository.save(userOrder);
        }
        return new ResUserOrder(
                userOrder.getId(),
                userOrder.getUniqueOrder().getNumber(),
                userService.getUser(userOrder.getUserId()),
                userOrder.getSelectedProducts() == null ? null : userOrder.getSelectedProducts().stream().map(this::getResSelectedProduct).collect(Collectors.toList()),
                userOrder.getUserOrderHistory() == null ? null : userOrder.getUserOrderHistory().stream().map(this::getResUserOrderHistory).collect(Collectors.toList()),
                userOrder.getOrderStep(),
                userOrder.isPayed(),
                userOrder.isChangedPrice(),
                userOrder.getSelectedProducts().stream().map(selectedProduct -> productPriceRepository.findById(selectedProduct.getProductPriceId()).get().getPrice() * selectedProduct.getCount()).reduce(0.0, Double::sum)
        );
    }

    public ResUserOrderHistory getResUserOrderHistory(UserOrderHistory userOrderHistory) {
        return new ResUserOrderHistory(
                userOrderHistory.getId(),
                userOrderHistory.getUserOrder() == null ? null : userOrderHistory.getUserOrder().getId(),
                userOrderHistory.getCurrentStep(),
                userOrderHistory.getMessage(),
                userOrderHistory.getCreatedAt() == null ? null : getSimpleDateFormat(userOrderHistory.getCreatedAt()),
                userOrderHistory.getChangedAdminId() == null ? null : userService.getUser(userOrderHistory.getChangedAdminId())
        );
    }

    public ResSelectedProduct getResSelectedProduct(SelectedProduct selectedProduct) {
        return new ResSelectedProduct(
                selectedProduct.getId(),
                selectedProduct.getProductId() == null ? null : productService.getProduct(selectedProduct.getProductId()),
                selectedProduct.getProductPriceId() == null ? null : productService.getResProductPrice(productPriceRepository.findById(selectedProduct.getProductPriceId()).orElseThrow(() -> new ResourceNotFoundException("Bunday maxsulot turi topilmadi!", "id", selectedProduct.getProductPriceId()))),
                selectedProduct.getProductPriceId(),
                selectedProduct.getCount(),
                selectedProduct.getCalculatedPrice() != null && selectedProduct.getProductId() != null ? selectedProduct.getCalculatedPrice() : selectedProduct.getCount() * productService.getProduct(selectedProduct.getProductId()).getResProductPrices().stream().filter(resProductPrice -> resProductPrice.getProductPriceId().equals(selectedProduct.getProductPriceId())).collect(Collectors.toList()).get(0).getPrice(),
                selectedProduct.getUserOrder() == null ? null : selectedProduct.getUserOrder().getId()
        );
    }

    public String getSimpleDateFormat(Timestamp date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public ResDashboard getDashboardUserOrder(ReqDashboard reqDashboard) {
        Timestamp from = Timestamp.valueOf(reqDashboard.getFrom());
        Timestamp to = Timestamp.valueOf(reqDashboard.getTo());
        Integer usersCount = userOrderRepository.getUserOrderCount(from, to, "CLOSED");
        Double aborotSum = userOrderRepository.getUserOrderSum(from, to, "CLOSED");

        Map<Integer, Integer> userStatistic = new HashMap<>();
        if (reqDashboard.getType().equals("month")) {
            do {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(from.getTime()));
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                userStatistic.put(calendar.get(Calendar.MONTH), userOrderRepository.getUserOrderCount(from, new Timestamp(calendar.getTimeInMillis()), "CLOSED"));
                from.setTime(calendar.getTimeInMillis());
            }
            while (!from.after(to));
        } else if (reqDashboard.getType().equals("day")) {
            do {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(from.getTime()));
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                userStatistic.put(calendar.get(Calendar.DAY_OF_MONTH), userOrderRepository.getUserOrderCount(from, new Timestamp(calendar.getTimeInMillis()), "CLOSED"));
                from.setTime(calendar.getTimeInMillis());
            }
            while (!from.after(to));
        } else {
            do {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(from.getTime()));
                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
                userStatistic.put(calendar.get(Calendar.YEAR), userOrderRepository.getUserOrderCount(from, new Timestamp(calendar.getTimeInMillis()), "CLOSED"));

                from.setTime(calendar.getTimeInMillis());
            }
            while (from.getYear() < to.getYear());
        }

        return new ResDashboard(usersCount, userStatistic, aborotSum);
    }

    public ApiResponseModel getUserOrdersByPhoneNumber(String searchName, int page, int size) {
        Page<UserOrder> allByUserName = userOrderRepository.findAllByPhoneNumber(searchName, CommonUtils.getPageableForNative(page, size));
        PageImpl<ResUserOrder> userOrders = new PageImpl<>(
                allByUserName.getContent().stream().map(this::getResUserOrder).collect(Collectors.toList()),
                allByUserName.getPageable(),
                allByUserName.getTotalElements()
        );
        return new ApiResponseModel(true, "Foydalanuvchi telefon raqami bo'yicha buyurtmalar", userOrders);
    }

    public void sendNotificationTelegramAboutOrder(UserOrder userOrder) {
        Optional<User> byId = userRepository.findById(userOrder.getUserId());
        User user = byId.get();
        boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
        boolean rus = user.getLanguage().equals(Constant.LANG_RU);

        Long chatId = user.getChatId();
        if (chatId != null) {
            try {
                SendMessage sendMessage = new SendMessage();
                String text = "";
                if (uzbek) {
                    text += "Sizning №" + userOrder.getUniqueOrder().getNumber() + " buyurtmangiz " +
                            (userOrder.getOrderStep().equals(Step.CREATED) ? "qabul qilindi" :
                                    userOrder.getOrderStep().equals(Step.IN_PROGRESS) ? "yetkazilishga tayyor" :
                                            userOrder.getOrderStep().equals(Step.CLOSED) ? "yetkazib berildi" :
                                                    userOrder.getOrderStep().equals(Step.CANCELLED) ? "bekor qilindi" :
                                                            userOrder.getOrderStep().equals(Step.REJECTED) ? "qaytarildi" : "") + "\n";
                } else if (rus) {
                    text += "Ваш №" + userOrder.getUniqueOrder().getNumber() + " заказ " +
                            (userOrder.getOrderStep().equals(Step.CREATED) ? "принят" :
                                    userOrder.getOrderStep().equals(Step.IN_PROGRESS) ? "готов для доставки" :
                                            userOrder.getOrderStep().equals(Step.CLOSED) ? "доставленно" :
                                                    userOrder.getOrderStep().equals(Step.CANCELLED) ? "отменен" :
                                                            userOrder.getOrderStep().equals(Step.REJECTED) ? "возвращенный" : "") + "\n";

                }else{
                    text += "Your №" + userOrder.getUniqueOrder().getNumber() + " order " +
                            (userOrder.getOrderStep().equals(Step.CREATED) ? "accepted" :
                                    userOrder.getOrderStep().equals(Step.IN_PROGRESS) ? "ready for delivery" :
                                            userOrder.getOrderStep().equals(Step.CLOSED) ? "delivered" :
                                                    userOrder.getOrderStep().equals(Step.CANCELLED) ? "canceled" :
                                                            userOrder.getOrderStep().equals(Step.REJECTED) ? "returned" : "") + "\n";


                }
                sendMessage.setText(text);
                sendMessage.setChatId(chatId);
                polymerBot.execute(sendMessage);
                if (userOrder.getOrderStep().equals(Step.IN_PROGRESS)) {
                    polymerBot.execute(telegramService.inProgress(userOrder));
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public ApiResponse changeOrderSum(ReqChangeOrderSum reqChangeOrderSum) {
        try {
            UserOrder order = userOrderRepository.findById(reqChangeOrderSum.getUserOrderId()).orElseThrow(() -> new ResourceNotFoundException("Order not found!", "id", reqChangeOrderSum.getUserOrderId()));
            UserOrderHistory userOrderHistory = new UserOrderHistory();
            if (order.getOrderStep().equals(Step.CREATED)) {
                if (reqChangeOrderSum.isPriceAdd()) {
                    order.getSelectedProducts().forEach(selectedProduct -> {
                                selectedProduct.setCalculatedPrice(selectedProduct.getCalculatedPrice() + (reqChangeOrderSum.getPrice() / order.getSelectedProducts().size()));
                            }
                    );
                    userOrderHistory.setCurrentStep(reqChangeOrderSum.getCurrentStep());
                    userOrderHistory.setMessage(reqChangeOrderSum.getMessage());
                    userOrderHistory.setUserOrder(userOrderRepository.findById(reqChangeOrderSum.getUserOrderId()).get());
                    userOrderHistory.setChangedAdminId(reqChangeOrderSum.getAdminId());
                    userOrderHistoryRepository.save(userOrderHistory);
                    return new ApiResponse("Muvoffaqiyatli o'zgartirildi!", true);

                } else {
                    if (!(reqChangeOrderSum.getPrice() >= order.getSelectedProducts().stream().map(SelectedProduct::getCalculatedPrice).reduce(0.0, Double::sum))) {
                        order.getSelectedProducts().forEach(selectedProduct -> {
                            selectedProduct.setCalculatedPrice(selectedProduct.getCalculatedPrice() - (reqChangeOrderSum.getPrice() / order.getSelectedProducts().size()));
                        });
                        userOrderHistory.setCurrentStep(reqChangeOrderSum.getCurrentStep());
                        userOrderHistory.setMessage(reqChangeOrderSum.getMessage());
                        userOrderHistory.setUserOrder(userOrderRepository.findById(reqChangeOrderSum.getUserOrderId()).get());
                        userOrderHistory.setChangedAdminId(reqChangeOrderSum.getAdminId());
                        userOrderHistoryRepository.save(userOrderHistory);
                        return new ApiResponse("Muvoffaqiyatli o'zgartirildi!", true);

                    } else {
                        return new ApiResponse("Ayiriladigan summa buyurtmaning umumiy narxidan katta bo'lmasligi yoki teng bo'lmasligi kerak!", false);
                    }
                }
            } else {
                return new ApiResponse("Bu bosqichda narxni o'zgartirib bo`lmaydi!", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

}
