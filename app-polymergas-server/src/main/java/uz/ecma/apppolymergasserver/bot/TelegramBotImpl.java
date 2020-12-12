package uz.ecma.apppolymergasserver.bot;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.ecma.apppolymergasserver.entity.*;
import uz.ecma.apppolymergasserver.entity.enums.RoleName;
import uz.ecma.apppolymergasserver.entity.enums.Step;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ReqCart;
import uz.ecma.apppolymergasserver.payload.ReqSelectedProduct;
import uz.ecma.apppolymergasserver.payload.ReqUserOrderHistory;
import uz.ecma.apppolymergasserver.repository.*;
import uz.ecma.apppolymergasserver.service.UserOrderService;
import uz.ecma.apppolymergasserver.service.UserService;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TelegramBotImpl implements TelegramService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @Autowired
    SelectedProductRepository selectedProductRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    UserOrderRepository userOrderRepository;

    @Autowired
    UserOrderHistoryRepository userOrderHistoryRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserOrderService userOrderService;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public SendMessage welcomeToBot(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        String text = update.getMessage().getText();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        User user = optionalUser.get();
        boolean en = text.equals(Constant.LANGUAGE_EN);
        boolean ru = text.equals(Constant.LANGUAGE_RU);
        boolean uzb = text.equals(Constant.LANGUAGE_UZ);

        if (uzb) {
            user.setLanguage(Constant.LANG_UZ);
        } else if (ru) {
            user.setLanguage(Constant.LANG_RU);
        } else {
            user.setLanguage(Constant.LANG_EN);
        }
        userRepository.save(user);
        if (optionalUser.isPresent()) {
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            if (uzbek) {
                sendMessage.setText(Constant.WELCOME_TEXT_UZ);
            } else if (rus) {
                sendMessage.setText(Constant.WELCOME_TEXT_RU);
            } else {
                sendMessage.setText(Constant.WELCOME_TEXT_EN);
            }
        }
        return sendMessage;
    }

    @Override
    public SendMessage chooseLanguage(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        KeyboardButton keyboardButton3 = new KeyboardButton();

        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());

        User user = optionalUser.orElseGet(User::new);
        user.setChatId(update.getMessage().getChatId());
        user.setFullName(" ");
        user.setPhoneNumber(" ");
        user.setState(BotState.CHOOSE_LANGUAGE);
        userRepository.save(user);

        keyboardButton1.setText(Constant.LANGUAGE_UZ);
        keyboardButton2.setText(Constant.LANGUAGE_RU);
        keyboardButton3.setText(Constant.LANGUAGE_EN);
        sendMessage.setText(Constant.CHOOSE_LANGUAGE);
        keyboardRow.add(keyboardButton1);
        keyboardRow.add(keyboardButton2);
        keyboardRow.add(keyboardButton3);
        keyboardRows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    @Override
    public SendMessage shareContact(Update update) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setParseMode(ParseMode.MARKDOWN);

        String text = update.getMessage().getText();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setRequestContact(true);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setState(BotState.SHARE_CONTACT_STATE);
            userRepository.save(user);

            boolean en = text.equals(Constant.LANGUAGE_EN);
            boolean ru = text.equals(Constant.LANGUAGE_RU);
            boolean uzb = text.equals(Constant.LANGUAGE_UZ);

            if (uzb) {
                sendMessage.setText(Constant.SHARE_CONTACT_TEXT_UZ);
                keyboardButton.setText(Constant.SHARE_CONTACT_UZ);
            } else if (ru) {
                sendMessage.setText(Constant.SHARE_CONTACT_TEXT_RU);
                keyboardButton.setText(Constant.SHARE_CONTACT_RU);
            } else {
                sendMessage.setText(Constant.SHARE_CONTACT_TEXT_EN);
                keyboardButton.setText(Constant.SHARE_CONTACT_EN);
            }
            keyboardRow.add(keyboardButton);
            keyboardRows.add(keyboardRow);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    @Override
    public SendMessage mainMenu(Update update, int type) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            if (type == 0) {
                Message message = update.getMessage();
                String phoneNumber;
                String fullName;
                if (message.hasContact()) {
                    Contact contact = message.getContact();
                    phoneNumber = contact.getPhoneNumber();
                    fullName = contact.getFirstName() != null ? contact.getFirstName() : "" + contact.getLastName() != null ? contact.getLastName() : "";
                } else {
                    phoneNumber = update.getMessage().getText();
                    if (!phoneNumber.matches("\\+998[0-9]{9}")) {

                        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                        replyKeyboardMarkup.setResizeKeyboard(true);
                        replyKeyboardMarkup.setSelective(true);
                        List<KeyboardRow> keyboardRows = new ArrayList<>();
                        KeyboardRow keyboardRow = new KeyboardRow();
                        KeyboardButton keyboardButton = new KeyboardButton();
                        keyboardButton.setRequestContact(true);

                        if (uzbek) {
                            sendMessage.setText(Constant.SHARE_CONTACT_TEXT_UZ);
                            keyboardButton.setText(Constant.SHARE_CONTACT_UZ);
                        } else if (rus) {
                            sendMessage.setText(Constant.SHARE_CONTACT_TEXT_RU);
                            keyboardButton.setText(Constant.SHARE_CONTACT_RU);
                        } else {
                            sendMessage.setText(Constant.SHARE_CONTACT_TEXT_EN);
                            keyboardButton.setText(Constant.SHARE_CONTACT_EN);
                        }
                        user.setState(BotState.SHARE_CONTACT_STATE);
                        userRepository.save(user);

                        keyboardRow.add(keyboardButton);
                        keyboardRows.add(keyboardRow);
                        replyKeyboardMarkup.setKeyboard(keyboardRows);
                        sendMessage.setReplyMarkup(replyKeyboardMarkup);

                        return sendMessage;
                    } else {
                        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                        replyKeyboardMarkup.setResizeKeyboard(true);
                        replyKeyboardMarkup.setSelective(true);
                        List<KeyboardRow> keyboardRows = new ArrayList<>();
                        KeyboardRow keyboardRow = new KeyboardRow();
                        KeyboardButton keyboardButton = new KeyboardButton();
                        keyboardButton.setRequestContact(true);

                        if (uzbek) {
                            sendMessage.setText(Constant.SHARE_CONTACT_TEXT_UZ);
                            keyboardButton.setText(Constant.SHARE_CONTACT_UZ);
                        } else if (rus) {
                            sendMessage.setText(Constant.SHARE_CONTACT_TEXT_RU);
                            keyboardButton.setText(Constant.SHARE_CONTACT_RU);
                        } else {
                            sendMessage.setText(Constant.SHARE_CONTACT_TEXT_EN);
                            keyboardButton.setText(Constant.SHARE_CONTACT_EN);
                        }
                        user.setState(BotState.SHARE_CONTACT_STATE);
                        userRepository.save(user);

                        keyboardRow.add(keyboardButton);
                        keyboardRows.add(keyboardRow);
                        replyKeyboardMarkup.setKeyboard(keyboardRows);
                        sendMessage.setReplyMarkup(replyKeyboardMarkup);
                        return sendMessage;
                    }
                }
                phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
                String finalPhoneNumber = phoneNumber;
                user.setPhoneNumber(finalPhoneNumber);
                user.setFullName(fullName);
                user.setState(BotState.MAIN_MENU);
                Optional<Role> byId = roleRepository.findById(1);
                Role role = byId.get();
                List<Role> roles = new ArrayList<>();
                roles.add(role);
                user.setRoles(roles);
                userRepository.save(user);
            } else {
                user.setState(BotState.MAIN_MENU);
                userRepository.save(user);
            }
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            KeyboardButton keyboardButton1 = new KeyboardButton();
            KeyboardButton keyboardButton2 = new KeyboardButton();

            if (uzbek) {
                sendMessage.setText(Constant.LETS_GO_ORDER_UZ);
                keyboardButton.setText(Constant.ORDER_TEXT_UZ);
                keyboardButton1.setText(Constant.SETTINGS_TEXT_UZ);
                keyboardButton2.setText(Constant.MY_ORDER_TEXT_UZ);
            } else if (rus) {
                sendMessage.setText(Constant.LETS_GO_ORDER_RU);
                keyboardButton.setText(Constant.ORDER_TEXT_RU);
                keyboardButton1.setText(Constant.SETTINGS_TEXT_RU);
                keyboardButton2.setText(Constant.MY_ORDER_TEXT_RU);
            } else {
                sendMessage.setText(Constant.LETS_GO_ORDER_EN);
                keyboardButton.setText(Constant.ORDER_TEXT_EN);
                keyboardButton1.setText(Constant.SETTINGS_TEXT_EN);
                keyboardButton2.setText(Constant.MY_ORDER_TEXT_EN);
            }
            keyboardRow.add(keyboardButton);
            keyboardRow.add(keyboardButton1);
            keyboardRow1.add(keyboardButton2);
            keyboardRows.add(keyboardRow);
            keyboardRows.add(keyboardRow1);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    @Override
    public SendMessage myOrders(Update update, int type) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);

            if (type == 0) {
                user.setState(BotState.MY_ORDER);
            } else {
                user.setState(BotState.MY_ORDER);
            }
            userRepository.save(user);

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardButton back = new KeyboardButton();


            List<UserOrder> allByUserId = userOrderRepository.findAllByUserId(user.getId());

            if (allByUserId.size() != 0) {
                for (int i = 0; i < allByUserId.size(); i++) {
                    KeyboardButton keyboardButton = new KeyboardButton();
                    if (i % 2 == 0) {
                        keyboardRows.add(keyboardRow);
                        keyboardRow = new KeyboardRow();
                    }
                    if (uzbek) {
                        sendMessage.setText(Constant.ORDERS_HISTORY_UZ);
                        keyboardButton.setText(Constant.ORDER_NUMBER_UZ + " => " + allByUserId.get(i).getUniqueOrder().getNumber());
                        back.setText(Constant.BACK_UZ);

                    } else if (rus) {
                        sendMessage.setText(Constant.ORDER_HISTORY_TEXT_RU);
                        keyboardButton.setText(Constant.ORDER_NUMBER_RU + " => " + allByUserId.get(i).getUniqueOrder().getNumber());
                        back.setText(Constant.BACK_RU);
                    } else {
                        sendMessage.setText(Constant.ORDER_HISTORY_TEXT_EN);
                        keyboardButton.setText(Constant.ORDER_NUMBER_EN + " => " + allByUserId.get(i).getUniqueOrder().getNumber());
                        back.setText(Constant.BACK_EN);
                    }
                    keyboardRow.add(keyboardButton);
                }
            } else {
                if (uzbek) {
                    sendMessage.setText(Constant.ORDER_HISTORY_EMPTY_UZ);
                    back.setText(Constant.BACK_UZ);
                } else if (rus) {
                    sendMessage.setText(Constant.ORDER_HISTORY_EMPTY_RU);
                    back.setText(Constant.BACK_RU);
                } else {
                    sendMessage.setText(Constant.ORDER_HISTORY_EMPTY_RU);
                    back.setText(Constant.BACK_RU);
                }
            }
            keyboardRow1.add(back);
            keyboardRows.add(keyboardRow);
            keyboardRows.add(keyboardRow1);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    @Override
    public SendMessage myOneOrder(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);
        String text = update.getMessage().getText();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            UserOrder userOrder;
            Integer id;
            user.setState(BotState.ORDER_HISTORY);
            userRepository.save(user);
            int index = text.indexOf(">");
            id = Integer.valueOf(text.substring(index + 2));

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardButton back = new KeyboardButton();

            Optional<UserOrder> byUniqueOrderNumber = userOrderRepository.findByUniqueOrder_Number(id);
            userOrder = byUniqueOrderNumber.get();
            List<SelectedProduct> selectedProducts = userOrder.getSelectedProducts();
            StringBuilder stringBuilder = new StringBuilder();

            double totalProducts = 0;
            stringBuilder.append(uzbek ? Constant.ORDER_HISTORY_TEXT_UZ : rus ? Constant.ORDER_HISTORY_TEXT_RU : Constant.ORDER_HISTORY_TEXT_EN + "\n");
            for (SelectedProduct selectedProduct : selectedProducts) {
                Optional<Product> byId = productRepository.findById(selectedProduct.getProductId());
                Product product = byId.get();
                ProductType productType = product.getProductType();
                Double v = selectedProduct.getCalculatedPrice();
                BigDecimal bigDecimal = new BigDecimal(selectedProduct.getCalculatedPrice());// form to BigDecimal
                String str = bigDecimal.toString();
                stringBuilder
                        .append("\n")
                        .append(uzbek ? Constant.NAME_PRODUCT_UZ + product.getName() : rus ? Constant.NAME_PRODUCT_RU : Constant.NAME_PRODUCT_EN + product.getName())
                        .append("\n")
                        .append(uzbek ? Constant.PRODUCT_TYPE_TEXT_UZ + productType.getNameUz() : rus ? Constant.PRODUCT_TYPE_TEXT_RU + productType.getNameRu() : Constant.PRODUCT_TYPE_TEXT_EN)
                        .append("\n")
                        .append(uzbek ? "Miqdori(kg) : " + selectedProduct.getCount() : rus ? "Количество(кг) : " + selectedProduct.getCount() : "Quantity (kg):" + selectedProduct.getCount())
                        .append("\n")
                        .append(uzbek ? "Narxi : " + (selectedProduct.getCalculatedPrice() / selectedProduct.getCount()) : rus ? "Стоимость : " + (selectedProduct.getCalculatedPrice() / selectedProduct.getCount()) : "Value : " + (selectedProduct.getCalculatedPrice() / selectedProduct.getCount()))
                        .append("\n")
                        .append(uzbek ? "Summa : " + str : rus ? "Сумма : " + str : "Amount: " + str)
                        .append(uzbek ? Constant.SUM_UZ : rus ? Constant.SUM_RU : Constant.SUM_EN)
                        .append("\n")
                        .append(uzbek ? Constant.ORDER_STEP_UZ + (userOrder.getOrderStep().equals(Step.CREATED) ? "tekshirish bosqichida" :
                                userOrder.getOrderStep().equals(Step.IN_PROGRESS) ? "yetkazilishga tayyor" :
                                        userOrder.getOrderStep().equals(Step.CLOSED) ? "yopilgan" :
                                                userOrder.getOrderStep().equals(Step.CANCELLED) ? "bekor qilindi" :
                                                        userOrder.getOrderStep().equals(Step.REJECTED) ? "qaytarildi" : "")
                                : rus ? Constant.ORDER_STEP_RU + (userOrder.getOrderStep().equals(Step.CREATED) ? "на этапе проверки" :
                                userOrder.getOrderStep().equals(Step.IN_PROGRESS) ? "готов для доставки" :
                                        userOrder.getOrderStep().equals(Step.CLOSED) ? "доставленно" :
                                                userOrder.getOrderStep().equals(Step.CANCELLED) ? "отменен" :
                                                        userOrder.getOrderStep().equals(Step.REJECTED) ? "возвращенный" : "") :
                                Constant.ORDER_STEP_EN + (userOrder.getOrderStep().equals(Step.CREATED) ? "at the verification stage" :
                                        userOrder.getOrderStep().equals(Step.IN_PROGRESS) ? "ready for delivery" :
                                                userOrder.getOrderStep().equals(Step.CLOSED) ? "delivered" :
                                                        userOrder.getOrderStep().equals(Step.CANCELLED) ? "canceled" :
                                                                userOrder.getOrderStep().equals(Step.REJECTED) ? "returned" : ""))
                        .append("\n");
                totalProducts += v;

            }
            BigDecimal bigDecimal = new BigDecimal(totalProducts);// form to BigDecimal
            String str = bigDecimal.toString();
            stringBuilder.append("\n").append(uzbek ? "Umumiy Summa " + str + Constant.SUM_UZ : rus ? "Общая сумма " + str + Constant.SUM_RU : "Total amount " + str + Constant.SUM_EN);
            sendMessage.setText(stringBuilder.toString());
            back.setText(uzbek ? Constant.BACK_UZ : rus ? Constant.BACK_RU : Constant.BACK_EN);
            keyboardRow.add(back);
            keyboardRows.add(keyboardRow);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    @Override
    public SendMessage inProgress(UserOrder userOrder) {
        UUID id = userOrder.getId();
        Optional<User> byId = userRepository.findById(userOrder.getUserId());
        User user = byId.get();

        boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
        boolean rus = user.getLanguage().equals(Constant.LANG_RU);

        SendMessage sendMessage = new SendMessage()
                .setChatId(user.getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardRow keyboardRow1 = new KeyboardRow();

        KeyboardButton acceptOrder = new KeyboardButton();
        KeyboardButton back = new KeyboardButton();

        if (uzbek) {
            sendMessage.setText("Buyurtmangiz yetib keldimi? Buyurtmani qabul qilish uchun qabul qildim tugmasini bosing.Agarda buyurtma sizni qoniqtirmasa buyurtmani qaytarish tugmasini bosing va sharh qoldiring biz siz bilan bog'lanamiz!");
            acceptOrder.setText(Constant.ACCEPT_ORDER_UZ);
            back.setText(Constant.REJECTED_ORDER_UZ);
        } else if (rus) {
            sendMessage.setText("Ваш заказ прибыл? Нажмите кнопку Принять, чтобы принять заказ. Если заказ не удовлетворяет вас, нажмите кнопку возврата и оставьте комментарий, мы свяжемся с вами!");
            acceptOrder.setText(Constant.ACCEPT_ORDER_RU);
            back.setText(Constant.REJECTED_ORDER_RU);
        } else {
            sendMessage.setText("Has your order arrived? Click the Accept button to accept the order. If the order does not satisfy you, click the return button and leave a comment, we will contact you!");
            acceptOrder.setText(Constant.ACCEPT_ORDER_EN);
            back.setText(Constant.REJECTED_ORDER_EN);
        }
        user.setOrderId(id);
        user.setState(BotState.IN_PROGRESS);
        userRepository.save(user);

        keyboardRow.add(acceptOrder);
        keyboardRow1.add(back);
        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    @Override
    public SendMessage inProgressAccept(Update update, int type) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        String text = update.getMessage().getText();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            Optional<UserOrder> byId = userOrderRepository.findById(user.getOrderId());
            UserOrder userOrder = byId.get();
            if (type == 0) {
                if (uzbek) {
                    sendMessage.setText("Iltimos o'z izohingizni qoldiring");
                } else if (rus) {
                    sendMessage.setText("Если вам понравился наш сервис, оставьте свой комментарий");
                } else {
                    sendMessage.setText("If you liked our service, give your comment");
                }
            } else {
                if (uzbek) {
                    sendMessage.setText("Iltimos nimadan ko'nglingiz to'lmadi shu haqida to'liq izohingizni qoldiring");
                } else if (rus) {
                    sendMessage.setText("Пожалуйста, оставьте комментарий о том, что вас не устраивает");
                } else {
                    sendMessage.setText("Please give a comment about what you are not happy with");
                }
            }
            user.setCommentType(text);
            user.setState(BotState.IN_PROGRESS_ACCEPT);
            userRepository.save(user);
            userOrderRepository.save(userOrder);

            ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
            sendMessage.setReplyMarkup(replyKeyboardRemove);
        }
        return sendMessage;
    }

    @Override
    public SendMessage inProgressComment(Update update, int type) {

        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        String text = update.getMessage().getText();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            KeyboardButton keyboardButton1 = new KeyboardButton();
            KeyboardButton keyboardButton2 = new KeyboardButton();

            Optional<UserOrder> byId = userOrderRepository.findById(user.getOrderId());
            UserOrder userOrder = byId.get();
            ReqUserOrderHistory reqUserOrderHistory = new ReqUserOrderHistory();
            if (type == 0) {
                reqUserOrderHistory.setCurrentStep(Step.CLOSED);
                reqUserOrderHistory.setMessage(text);
                reqUserOrderHistory.setUserOrderId(userOrder.getId());
                userOrderService.changeStepUserOrder(reqUserOrderHistory);
                if (uzbek) {
                    sendMessage.setText("Buyurtma Qabul qilindi");
                } else if (rus) {
                    sendMessage.setText("Заказ принят");
                } else {
                    sendMessage.setText("Order is accepted");
                }
            } else {
                reqUserOrderHistory.setCurrentStep(Step.REJECTED);
                reqUserOrderHistory.setUserOrderId(userOrder.getId());
                reqUserOrderHistory.setMessage(text);
                userOrderService.changeStepUserOrder(reqUserOrderHistory);
                if (uzbek) {
                    sendMessage.setText("Buyurtmangiz qaytarildi! ");
                } else if (rus) {
                    sendMessage.setText("Заказ возвращен");
                } else {
                    sendMessage.setText("Order returned");
                }
            }
            if (uzbek) {
                keyboardButton.setText(Constant.ORDER_TEXT_UZ);
                keyboardButton1.setText(Constant.SETTINGS_TEXT_UZ);
                keyboardButton2.setText(Constant.MY_ORDER_TEXT_UZ);
            } else if (rus) {
                keyboardButton.setText(Constant.ORDER_TEXT_RU);
                keyboardButton1.setText(Constant.SETTINGS_TEXT_RU);
                keyboardButton2.setText(Constant.MY_ORDER_TEXT_RU);
            } else {
                keyboardButton.setText(Constant.ORDER_TEXT_EN);
                keyboardButton1.setText(Constant.SETTINGS_TEXT_EN);
                keyboardButton2.setText(Constant.MY_ORDER_TEXT_EN);
            }
            user.setState(BotState.MAIN_MENU);
            userRepository.save(user);

            keyboardRow.add(keyboardButton);
            keyboardRow.add(keyboardButton1);
            keyboardRow1.add(keyboardButton2);
            keyboardRows.add(keyboardRow);
            keyboardRows.add(keyboardRow1);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    @Override
    public SendMessage menuCategory(Update update, int type) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            if (type == 0) {
                user.setState(BotState.MENU_CATEGORY);
            } else {
                user.setState(BotState.MENU_CATEGORY);
            }
            userRepository.save(user);

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardRow keyboardRow2 = new KeyboardRow();

            KeyboardButton basket = new KeyboardButton();
            KeyboardButton acceptOrder = new KeyboardButton();
            KeyboardButton back = new KeyboardButton();

            List<Category> categories = categoryRepository.findAll();
            if (categories.size() > 0) {
                for (int i = 0; i < categories.size(); i++) {
                    KeyboardButton keyboardButton = new KeyboardButton();
                    if (i % 2 == 0) {
                        keyboardRows.add(keyboardRow);
                        keyboardRow = new KeyboardRow();
                    }
                    if (uzbek) {
                        sendMessage.setText(Constant.MENU_CATEGORY_TEXT_UZ);
                        keyboardButton.setText(categories.get(i).getNameUz());
                        basket.setText(Constant.TRASH_UZ);
                        acceptOrder.setText(Constant.SET_ORDER_UZ);
                        back.setText(Constant.BACK_UZ);
                    } else if (rus) {
                        sendMessage.setText(Constant.MENU_CATEGORY_TEXT_RU);
                        keyboardButton.setText(categories.get(i).getNameRu());
                        basket.setText(Constant.TRASH_RU);
                        acceptOrder.setText(Constant.SET_ORDER_RU);
                        back.setText(Constant.BACK_RU);
                    } else {
                        sendMessage.setText(Constant.MENU_CATEGORY_TEXT_EN);
                        keyboardButton.setText(categories.get(i).getNameEn());
                        basket.setText(Constant.TRASH_EN);
                        acceptOrder.setText(Constant.SET_ORDER_EN);
                        back.setText(Constant.BACK_EN);
                    }
                    keyboardRow.add(keyboardButton);
                }
            } else {
                if (uzbek) {
                    sendMessage.setText("Kategoriyalar mavjud emas!");
                    basket.setText(Constant.TRASH_UZ);
                    acceptOrder.setText(Constant.SET_ORDER_UZ);
                    back.setText(Constant.BACK_UZ);
                } else if (rus) {
                    sendMessage.setText("Категории недоступны!");
                    basket.setText(Constant.TRASH_RU);
                    acceptOrder.setText(Constant.SET_ORDER_RU);
                    back.setText(Constant.BACK_RU);
                } else {
                    sendMessage.setText("Categories are not available!");
                    basket.setText(Constant.TRASH_EN);
                    acceptOrder.setText(Constant.SET_ORDER_EN);
                    back.setText(Constant.BACK_EN);
                }
            }
            keyboardRow1.add(basket);
            keyboardRow1.add(acceptOrder);
            keyboardRow2.add(back);
            keyboardRows.add(keyboardRow);
            keyboardRows.add(keyboardRow1);
            keyboardRows.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    @Override
    public SendMessage menuProducts(Update update, int type) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);
        String text = update.getMessage().getText();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            Category category;

            if (type == 0) {
                Optional<Category> byName = categoryRepository.findByNameUzOrNameRuOrNameEn(text, text, text);
                category = byName.get();
                user.setCategoryId(category.getId());
                user.setState(BotState.MENU_PRODUCTS);
                userRepository.save(user);

            } else {
                Optional<Category> byId = categoryRepository.findById(user.getCategoryId());
                category = byId.get();
                user.setState(BotState.MENU_PRODUCTS);
                userRepository.save(user);
            }

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardRow keyboardRow2 = new KeyboardRow();

            KeyboardButton basket = new KeyboardButton();
            KeyboardButton back = new KeyboardButton();

            List<Product> products = productRepository.findAllByCategoryId(category.getId());

            if (products.size() != 0) {
                for (int i = 0; i < products.size(); i++) {
                    Product product = products.get(i);
                    KeyboardButton keyboardButton = new KeyboardButton();
                    if (i % 2 == 0) {
                        keyboardRows.add(keyboardRow);
                        keyboardRow = new KeyboardRow();
                    }
                    if (uzbek) {
                        sendMessage.setText("Mahsulotni tanlang");
                        keyboardButton.setText(product.getName());
                        basket.setText(Constant.TRASH_UZ);
                        back.setText(Constant.BACK_UZ);
                        user.setProductId(product.getId());
                        userRepository.save(user);
                    } else if (rus) {
                        sendMessage.setText("Выберите продукт");
                        keyboardButton.setText(product.getName());
                        basket.setText(Constant.TRASH_RU);
                        back.setText(Constant.BACK_RU);
                        user.setProductId(product.getId());
                        userRepository.save(user);
                    } else {
                        sendMessage.setText("Select product");
                        keyboardButton.setText(product.getName());
                        basket.setText(Constant.TRASH_EN);
                        back.setText(Constant.BACK_EN);
                        user.setProductId(product.getId());
                        userRepository.save(user);
                    }
                    keyboardRow.add(keyboardButton);
                }
            } else {
                if (uzbek) {
                    sendMessage.setText(Constant.NOT_PRODUCT_TEXT_UZ);
                    basket.setText(Constant.TRASH_UZ);
                    back.setText(Constant.BACK_UZ);
                } else if (rus) {
                    sendMessage.setText(Constant.NOT_PRODUCT_TEXT_RU);
                    basket.setText(Constant.TRASH_RU);
                    back.setText(Constant.BACK_RU);
                } else {
                    sendMessage.setText(Constant.NOT_PRODUCT_TEXT_EN);
                    basket.setText(Constant.TRASH_EN);
                    back.setText(Constant.BACK_EN);
                }
            }
            keyboardRow1.add(basket);
            keyboardRow2.add(back);
            keyboardRows.add(keyboardRow);
            keyboardRows.add(keyboardRow1);
            keyboardRows.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    @Override
    public SendPhoto menuProductPrices(Update update, int type) {
        SendPhoto sendPhoto = new SendPhoto()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        String text = update.getMessage().getText();
        Optional<User> optionalBotUser = userRepository.findByChatId(update.getMessage().getChatId());

        if (optionalBotUser.isPresent()) {
            User user = optionalBotUser.get();
            Optional<Product> product;

            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);

            if (type == 0) {
                product = productRepository.findByName(text);
                Product oneProduct = product.get();
                user.setProductId(oneProduct.getId());
                userRepository.save(user);
                user.setState(BotState.MENU_PRODUCT_PRICES);
                userRepository.save(user);
            } else {
                product = productRepository.findById(user.getProductId());
                user.setState(BotState.MENU_PRODUCT_PRICES);
                userRepository.save(user);
            }

            Product oneProduct = product.get();
            List<Attachment> photos = oneProduct.getPhotos();
            Attachment attachment = photos.get(0);
            Optional<AttachmentContent> byAttachment = attachmentContentRepository.findByAttachment(attachment);
            AttachmentContent attachmentContent = byAttachment.get();

            ProductType productType = oneProduct.getProductType();
            if (uzbek) {
                sendPhoto.setCaption(Constant.NAME_PRODUCT_UZ + oneProduct.getName() + "\n\n" + Constant.PRODUCT_TYPE_TEXT_UZ + productType.getNameUz() + "\n\n" + Constant.DESCRIPTION_PRODUCT_UZ + oneProduct.getDescriptionUz() + "\n\n" + Constant.SELECT_PRODUCT_PRICE_TEXT_UZ);
            } else if (rus) {
                sendPhoto.setCaption(Constant.NAME_PRODUCT_RU + oneProduct.getName() + "\n\n" + Constant.PRODUCT_TYPE_TEXT_RU + productType.getNameRu() + "\n\n" + Constant.DESCRIPTION_PRODUCT_RU + oneProduct.getDescriptionRu() + "\n\n" + Constant.SELECT_PRODUCT_PRICE_TEXT_RU);
            } else {
                sendPhoto.setCaption(Constant.NAME_PRODUCT_RU + oneProduct.getName() + "\n\n" + Constant.PRODUCT_TYPE_TEXT_RU + productType.getNameRu() + "\n\n" + Constant.DESCRIPTION_PRODUCT_EN + oneProduct.getDescriptionEn() + "\n\n" + Constant.SELECT_PRODUCT_PRICE_TEXT_EN);
            }
            sendPhoto.setPhoto(attachmentContent.getAttachment().getName(), new ByteArrayInputStream(attachmentContent.getContent()));
            List<ProductPrice> productPrices = oneProduct.getProductPrices();
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardRow keyboardRow2 = new KeyboardRow();

            KeyboardButton basket = new KeyboardButton();
            KeyboardButton back = new KeyboardButton();
            int j = 0;
            for (int i = 0; i < productPrices.size(); i++) {
                ProductPrice productPrice = productPrices.get(i);
                if (productPrice.isHaveProduct()) {
                    j++;
                    KeyboardButton keyboardButton = new KeyboardButton();
                    if (i % 2 == 0) {
                        keyboardRows.add(keyboardRow);
                        keyboardRow = new KeyboardRow();
                    }
                    if (uzbek) {
                        back.setText(Constant.BACK_UZ);
                        basket.setText(Constant.TRASH_UZ);
                        keyboardButton.setText(productPrice.getQuantity().getValue() + " : " + productPrice.getQuantity().getQuantityType().name() + " = > " + productPrice.getPrice());

                    } else if (rus) {
                        back.setText(Constant.BACK_RU);
                        basket.setText(Constant.TRASH_RU);
                        keyboardButton.setText(productPrice.getQuantity().getValue() + " : " + productPrice.getQuantity().getQuantityType().name() + " = > " + productPrice.getPrice());
                    } else {
                        back.setText(Constant.BACK_EN);
                        basket.setText(Constant.TRASH_EN);
                        keyboardButton.setText(productPrice.getQuantity().getValue() + " : " + productPrice.getQuantity().getQuantityType().name() + " = > " + productPrice.getPrice());
                    }
                    keyboardRow.add(keyboardButton);
                } else {
                    if (j == 0) {
                        if (uzbek) {
                            back.setText(Constant.BACK_UZ);
                            basket.setText(Constant.TRASH_UZ);
                            sendPhoto.setCaption(Constant.PRODUCT_NOT_UZ);
                        } else if (rus) {
                            back.setText(Constant.BACK_RU);
                            basket.setText(Constant.TRASH_RU);
                            sendPhoto.setCaption(Constant.PRODUCT_NOT_RU);
                        } else {
                            back.setText(Constant.BACK_EN);
                            basket.setText(Constant.TRASH_EN);
                            sendPhoto.setCaption(Constant.PRODUCT_NOT_EN);
                        }
                    }
                }
            }
            keyboardRow1.add(basket);
            keyboardRow2.add(back);
            keyboardRows.add(keyboardRow);
            keyboardRows.add(keyboardRow1);
            keyboardRows.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendPhoto.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendPhoto;
    }

    @Override
    public SendPhoto menuProductCount(Update update, int type) {
        SendPhoto sendPhoto = new SendPhoto()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        String text = update.getMessage().getText();
        Optional<User> optionalBotUser = userRepository.findByChatId(update.getMessage().getChatId());

        if (optionalBotUser.isPresent()) {
            User user = optionalBotUser.get();
            Optional<Product> product;

            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            if (type == 0) {
                product = productRepository.findByName(text);
                Product oneProduct = product.get();
                user.setProductId(oneProduct.getId());
                userRepository.save(user);
                user.setState(BotState.MENU_PRODUCT_PRICES);
                userRepository.save(user);
            } else {
                product = productRepository.findById(user.getProductId());
                user.setState(BotState.MENU_PRODUCT_PRICES);
                userRepository.save(user);
            }

            Product oneProduct = product.get();
            List<Attachment> photos = oneProduct.getPhotos();
            Attachment attachment = photos.get(0);
            Optional<AttachmentContent> byAttachment = attachmentContentRepository.findByAttachment(attachment);
            AttachmentContent attachmentContent = byAttachment.get();

            ProductType productType = oneProduct.getProductType();
            List<ProductPrice> productPrices = oneProduct.getProductPrices();
            ProductPrice productPrice = productPrices.get(0);
            productPrice.isHaveProduct();

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardRow keyboardRow2 = new KeyboardRow();

            KeyboardButton basket = new KeyboardButton();
            KeyboardButton back = new KeyboardButton();

            if (!productPrice.isHaveProduct()) {
                if (uzbek) {
                    sendPhoto.setCaption(Constant.PRODUCT_NOT_UZ);
                    back.setText(Constant.BACK_UZ);
                    basket.setText(Constant.TRASH_UZ);
                } else if (rus) {
                    sendPhoto.setCaption(Constant.PRODUCT_NOT_RU);
                    back.setText(Constant.BACK_RU);
                    basket.setText(Constant.TRASH_RU);
                } else {
                    sendPhoto.setCaption(Constant.PRODUCT_NOT_EN);
                    back.setText(Constant.BACK_EN);
                    basket.setText(Constant.TRASH_EN);
                }
                sendPhoto.setPhoto(attachmentContent.getAttachment().getName(), new ByteArrayInputStream(attachmentContent.getContent()));
            } else {
                if (uzbek) {
                    sendPhoto.setCaption(Constant.NAME_PRODUCT_UZ + oneProduct.getName() + "\n\n" + Constant.PRODUCT_TYPE_TEXT_UZ + productType.getNameUz() + "\n\n" + Constant.DESCRIPTION_PRODUCT_UZ + oneProduct.getDescriptionUz() + "\n\n" + Constant.SELECT_PRODUCT_COUNT_TEXT_UZ);
                    back.setText(Constant.BACK_UZ);
                    basket.setText(Constant.TRASH_UZ);
                } else if (rus) {
                    sendPhoto.setCaption(Constant.NAME_PRODUCT_RU + oneProduct.getName() + "\n\n" + Constant.PRODUCT_TYPE_TEXT_RU + productType.getNameRu() + "\n\n" + Constant.DESCRIPTION_PRODUCT_RU + oneProduct.getDescriptionRu() + "\n\n" + Constant.SELECT_PRODUCT_COUNT_TEXT_RU);
                    back.setText(Constant.BACK_RU);
                    basket.setText(Constant.TRASH_RU);
                } else {
                    sendPhoto.setCaption(Constant.NAME_PRODUCT_EN + oneProduct.getName() + "\n\n" + Constant.PRODUCT_TYPE_TEXT_EN + productType.getNameEn() + "\n\n" + Constant.DESCRIPTION_PRODUCT_EN + oneProduct.getDescriptionEn() + "\n\n" + Constant.SELECT_PRODUCT_COUNT_TEXT_EN);
                    back.setText(Constant.BACK_EN);
                    basket.setText(Constant.TRASH_EN);
                }
                sendPhoto.setPhoto(attachmentContent.getAttachment().getName(), new ByteArrayInputStream(attachmentContent.getContent()));
            }
            keyboardRow1.add(basket);
            keyboardRow2.add(back);
            keyboardRows.add(keyboardRow1);
            keyboardRows.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendPhoto.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendPhoto;
    }

    @Override
    public SendMessage numberProduct(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        String text = update.getMessage().getText();

        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            int index = text.indexOf(">");
            double productP = Double.parseDouble(text.substring(index + 2));

            Optional<ProductPrice> byPrice = productPriceRepository.findByPriceAndProducts_Id(productP, user.getProductId());
            ProductPrice productPrice = byPrice.get();
            user.setProductPriceId(productPrice.getId());
            user.setState(BotState.NUMBER_PRODUCTS_STATE);
            userRepository.save(user);

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();

            KeyboardRow keyboardRow1 = new KeyboardRow();
            for (int i = 0; i < 9; i++) {
                KeyboardButton keyboardButton = new KeyboardButton();
                if (i % 3 == 0) {
                    keyboardRows.add(keyboardRow1);
                    keyboardRow1 = new KeyboardRow();
                }
                keyboardButton.setText("" + (i + 1));
                keyboardRow1.add(keyboardButton);
            }
            keyboardRows.add(keyboardRow1);
            KeyboardRow keyboardRow2 = new KeyboardRow();
            KeyboardRow keyboardRow3 = new KeyboardRow();
            KeyboardButton keyboardButton2 = new KeyboardButton();
            if (uzbek) {
                keyboardButton2.setText(Constant.TRASH_UZ);
                sendMessage.setText(Constant.NUMBER_PRODUCTS_TEXT_UZ);
                keyboardRow3.add(Constant.BACK_UZ);
            } else if (rus) {
                keyboardButton2.setText(Constant.TRASH_RU);
                sendMessage.setText(Constant.NUMBER_PRODUCTS_TEXT_RU);
                keyboardRow3.add(Constant.BACK_RU);
            } else {
                keyboardButton2.setText(Constant.TRASH_EN);
                sendMessage.setText(Constant.NUMBER_PRODUCTS_TEXT_EN);
                keyboardRow3.add(Constant.BACK_EN);
            }
            keyboardRow2.add(keyboardButton2);
            keyboardRows.add(keyboardRow2);
            keyboardRows.add(keyboardRow3);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    @Override
    public SendMessage saveBascetAndMainMenuCategory(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        String text = update.getMessage().getText();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());

        User user1 = optionalUser.get();

        boolean uzbek = user1.getLanguage().equals(Constant.LANG_UZ);
        boolean rus = user1.getLanguage().equals(Constant.LANG_RU);
        if (!text.matches("[0-9]{1,}")) {
            user1.setState(BotState.MENU_PRODUCT_PRICES);
            userRepository.save(user1);
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardRow keyboardRow2 = new KeyboardRow();

            KeyboardButton basket = new KeyboardButton();
            KeyboardButton back = new KeyboardButton();

            if (uzbek) {
                sendMessage.setText("Buyurtma miqdorini faqat son ko'rinishida kiriting (100)");
                back.setText(Constant.BACK_UZ);
                basket.setText(Constant.TRASH_UZ);
            } else if (rus) {
                sendMessage.setText("Введите количество заказа только в числовой форме (100)");
                back.setText(Constant.BACK_RU);
                basket.setText(Constant.TRASH_RU);
            } else {
                sendMessage.setText("Please enter order quantity in numerical form only (100)");
                back.setText(Constant.BACK_EN);
                basket.setText(Constant.TRASH_EN);
            }
            keyboardRow1.add(basket);
            keyboardRow2.add(back);
            keyboardRows.add(keyboardRow1);
            keyboardRows.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);

        } else {
            List<Category> categories = categoryRepository.findAll();

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardButton keyboardButton1 = new KeyboardButton();
            KeyboardButton keyboardButton2 = new KeyboardButton();
            double amount = Double.parseDouble(update.getMessage().getText());

            optionalUser.ifPresent(user -> {
                KeyboardRow keyboardRow2 = new KeyboardRow();
                KeyboardRow keyboardRow3 = new KeyboardRow();
                if (uzbek) {
                    sendMessage.setText(Constant.CONTINUE_BEGIN_DELIVERY_TEXT_UZ);
                    keyboardButton1.setText(Constant.TRASH_UZ);
                    keyboardButton2.setText(Constant.SET_ORDER_UZ);
                    keyboardRow3.add(Constant.BACK_UZ);
                } else if (rus) {
                    sendMessage.setText(Constant.CONTINUE_BEGIN_DELIVERY_TEXT_RU);
                    keyboardButton1.setText(Constant.TRASH_RU);
                    keyboardButton2.setText(Constant.SET_ORDER_RU);
                    keyboardRow3.add(Constant.BACK_RU);
                } else {
                    sendMessage.setText(Constant.CONTINUE_BEGIN_DELIVERY_TEXT_EN);
                    keyboardButton1.setText(Constant.TRASH_EN);
                    keyboardButton2.setText(Constant.SET_ORDER_EN);
                    keyboardRow3.add(Constant.BACK_EN);
                }
                Optional<Product> byId = productRepository.findById(user.getProductId());
                Product product = byId.get();
                ProductPrice productPrice = product.getProductPrices().get(0);
//            Optional<ProductPrice> byId = productPriceRepository.findById(user.getProductPriceId());
//            ProductPrice productPrice = byId.get();

                SelectedProduct selectedProduct = new SelectedProduct();
                selectedProduct.setProductId(user.getProductId());
                selectedProduct.setCount(amount);
                selectedProduct.setProductPriceId(productPrice.getId());
                selectedProduct.setChatId(user.getChatId());
                selectedProduct.setCalculatedPrice(amount * productPrice.getPrice());
                selectedProductRepository.save(selectedProduct);

                user.setState(BotState.MENU_CATEGORY);
                userRepository.save(user);

                keyboardRow1.add(keyboardButton1);
                keyboardRow1.add(keyboardButton2);
                keyboardRows.add(keyboardRow1);

                for (int i = 0; i < categories.size(); i++) {
                    KeyboardButton keyboardButton = new KeyboardButton();
                    if (i % 2 == 0) {
                        keyboardRows.add(keyboardRow2);
                        keyboardRow2 = new KeyboardRow();
                    }
                    if (uzbek) {
                        keyboardButton.setText(categories.get(i).getNameUz());
                    } else if (rus) {
                        keyboardButton.setText(categories.get(i).getNameRu());
                    } else {
                        keyboardButton.setText(categories.get(i).getNameEn());
                    }
                    keyboardRow2.add(keyboardButton);
                }

                keyboardRows.add(keyboardRow2);
                keyboardRows.add(keyboardRow3);
                replyKeyboardMarkup.setKeyboard(keyboardRows);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
            });
        }
        return sendMessage;
    }

    @Override
    public SendMessage getProductsFromBasket(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            List<SelectedProduct> allByChatId = selectedProductRepository.findAllByChatId(user.getChatId());
            KeyboardRow keyboardRow2 = new KeyboardRow();

            if (allByChatId.size() != 0) {
                double totalProducts = 0;
                stringBuilder.append(uzbek ? Constant.TRASH_TEXT_UZ : rus ? Constant.TRASH_TEXT_RU : Constant.TRASH_TEXT_EN + "\n");
                for (SelectedProduct selectedProduct : allByChatId) {
                    Optional<Product> byId = productRepository.findById(selectedProduct.getProductId());
                    Product product = byId.get();

                    Double v = selectedProduct.getCalculatedPrice();
                    stringBuilder
                            .append("\n")
                            .append(uzbek ? product.getName() : rus ? product.getName() : product.getName())
                            .append("\n")
                            .append(uzbek ? selectedProduct.getCount() + " kg " : rus ? selectedProduct.getCount() + " кг " : selectedProduct.getCount() + " kg ")
                            .append("x ").append(selectedProduct.getCalculatedPrice() / selectedProduct.getCount())
                            .append(" = ")
                            .append(v)
                            .append(uzbek ? Constant.SUM_UZ : rus ? Constant.SUM_RU : Constant.SUM_EN)
                            .append("\n");
                    totalProducts += v;
                    KeyboardRow keyboardRow1 = new KeyboardRow();
                    KeyboardButton keyboardButton = new KeyboardButton();
                    keyboardButton.setText("❌ " + (uzbek ? product.getName() : rus ? product.getName() : product.getName()));
                    keyboardRow1.add(keyboardButton);
                    keyboardRows.add(keyboardRow1);
                }
                stringBuilder.append("\n").append(uzbek ? " Umumiy Summa " + totalProducts + Constant.SUM_UZ : rus ? " Общая сумма " + totalProducts + Constant.SUM_RU : " Total amount " + totalProducts + Constant.SUM_EN);
                sendMessage.setText(stringBuilder.toString());
                KeyboardButton keyboardButton2 = new KeyboardButton();
                keyboardButton2.setText(uzbek ? Constant.CLEAR_BASKET_UZ : rus ? Constant.CLEAR_BASKET_RU : Constant.CLEAR_BASKET_EN);
                keyboardRow2.add(keyboardButton2);
            } else {
                if (uzbek) {
                    sendMessage.setText("Savatcha bo'sh.");
                } else if (rus) {
                    sendMessage.setText("Корзина пуста.");
                } else {
                    sendMessage.setText("Your shopping cart is empty.");
                }
            }
            KeyboardButton keyboardButton1 = new KeyboardButton();
            KeyboardButton keyboardButton = new KeyboardButton();
            if (uzbek) {
                keyboardButton1.setText(Constant.BACK_UZ);
                keyboardButton.setText(Constant.SET_ORDER_UZ);
            } else if (rus) {
                keyboardButton1.setText(Constant.BACK_RU);
                keyboardButton.setText(Constant.SET_ORDER_RU);
            } else {
                keyboardButton1.setText(Constant.BACK_EN);
                keyboardButton.setText(Constant.SET_ORDER_EN);
            }
            keyboardRow2.add(keyboardButton1);
            KeyboardRow keyboardRow3 = new KeyboardRow();

            keyboardRow3.add(keyboardButton);
            keyboardRows.add(keyboardRow3);
            keyboardRows.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboardRows);

            user.setState(BotState.TRASH_STATE);
            userRepository.save(user);
        }
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    @Override
    public SendMessage deleteProductAndUpdateTrash(Update update) {
        String text = update.getMessage().getText();
        String productName = text.substring(2);
        Optional<Product> byProductName = productRepository.findByName(productName);
        Product product = byProductName.get();
        UUID id = product.getId();
        selectedProductRepository.deleteAllByChatIdAndProductId(update.getMessage().getChatId(), id);
        return getProductsFromBasket(update);
    }

    @Override
    public SendMessage clearAllProducts(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        optionalUser.ifPresent(user -> {

            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            if (uzbek) {
                sendMessage.setText(Constant.CLOSE_CLEAR_BASKET_TEXT_UZ);
            } else if (rus) {
                sendMessage.setText(Constant.CLOSE_CLEAR_BASKET_TEXT_RU);
            } else {
                sendMessage.setText(Constant.CLOSE_CLEAR_BASKET_TEXT_EN);
            }
            user.setState(BotState.MENU_CATEGORY);
            userRepository.save(user);
        });
        selectedProductRepository.deleteAllByChatId(update.getMessage().getChatId());
        return sendMessage;
    }

    @Override
    public SendMessage setOrder(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();


        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setState(BotState.SET_ORDER);
            userRepository.save(user);

            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            List<SelectedProduct> allByChatId = selectedProductRepository.findAllByChatId(user.getChatId());
            StringBuilder stringBuilder = new StringBuilder();

            if (allByChatId.size() > 0) {

                double totalProducts = 0;
                stringBuilder
                        .append(uzbek ? Constant.TRASH_TEXT_UZ : rus ? Constant.TRASH_TEXT_RU : Constant.TRASH_TEXT_EN + "\n");
                for (SelectedProduct selectedProduct : allByChatId) {
                    Optional<Product> byId = productRepository.findById(selectedProduct.getProductId());
                    Product product = byId.get();

                    Double v = selectedProduct.getCalculatedPrice();
                    BigDecimal bigDecimal = new BigDecimal(selectedProduct.getCalculatedPrice());// form to BigDecimal
                    String str = bigDecimal.toString();
                    stringBuilder
                            .append("\n")
                            .append(uzbek ? product.getName() : rus ? product.getName() : product.getName())
                            .append("\n")
                            .append(selectedProduct.getCount())
                            .append("x").append(selectedProduct.getCalculatedPrice() / selectedProduct.getCount())
                            .append("=")
                            .append(str)
                            .append(uzbek ? Constant.SUM_UZ : rus ? Constant.SUM_RU : Constant.SUM_EN)
                            .append("\n");
                    totalProducts += v;
                }
                BigDecimal bigDecimal = new BigDecimal(totalProducts);// form to BigDecimal
                String str = bigDecimal.toString();
                stringBuilder.append("\n").append(uzbek ? " Umumiy Summa " + str + Constant.SUM_UZ : rus ? " Общая сумма " + str + Constant.SUM_RU : " Total amount " + str + Constant.SUM_EN);
                sendMessage.setText(stringBuilder.toString());

                if (uzbek) {
                    keyboardButton1.setText(Constant.ORDERED_UZ);
                    keyboardButton2.setText(Constant.BACK_UZ);
                } else if (rus) {
                    keyboardButton1.setText(Constant.ORDERED_RU);
                    keyboardButton2.setText(Constant.BACK_RU);
                } else {
                    keyboardButton1.setText(Constant.ORDERED_EN);
                    keyboardButton2.setText(Constant.BACK_EN);
                }
                keyboardRow1.add(keyboardButton1);
                keyboardRow2.add(keyboardButton2);
                keyboardRows.add(keyboardRow1);
                keyboardRows.add(keyboardRow2);
                replyKeyboardMarkup.setKeyboard(keyboardRows);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
            } else {
                if (uzbek) {
                    sendMessage.setText(Constant.NOTHING_BOUGHT_UZ);
                } else if (rus) {
                    sendMessage.setText(Constant.NOTHING_BOUGHT_RU);
                } else {
                    sendMessage.setText(Constant.NOTHING_BOUGHT_EN);
                }
                user.setState(BotState.MENU_CATEGORY);
                userRepository.save(user);
            }
        }
        return sendMessage;
    }

    @Override
    public SendMessage setAdditionalPhone(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        KeyboardButton keyboardButton1 = new KeyboardButton();

        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setState(BotState.ADDITIONAL_PHONE);
            userRepository.save(user);

            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            if (uzbek) {
                keyboardButton.setText(Constant.BACK_UZ);
                keyboardButton1.setText(Constant.NO_UZ);
                sendMessage.setText("Buyurtmani qabul qilib Siz bilan boglanishimiz uchun qo'shmcha telefon raqam qoldirishni xoxlayszmi? \n Agar imkoni bo'lsa qoldiring,bo'lmasa yo'q tugmasini bosib davom eting!");
            } else if (rus) {
                keyboardButton.setText(Constant.BACK_RU);
                keyboardButton1.setText(Constant.NO_RU);
                sendMessage.setText("Хотите оставить дополнительный номер телефона, чтобы мы могли связаться с вами после получения вашего заказа? \n Оставьте, если можете, в противном случае продолжите, нажав кнопку «нет»!");
            } else {
                keyboardButton.setText(Constant.BACK_EN);
                keyboardButton1.setText(Constant.NO_EN);
                sendMessage.setText("Do you want to give an additional phone number so that we can contact you after receiving your order?\n" +
                        " Leave if you can, otherwise continue by clicking no!");
            }
            keyboardRow.add(keyboardButton);
            keyboardRow1.add(keyboardButton1);
            keyboardRows.add(keyboardRow1);
            keyboardRows.add(keyboardRow);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    @Override
    public SendMessage ordered(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        String text = update.getMessage().getText();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());

        if (!text.matches("\\+998[0-9]{9}") && !text.equals(Constant.NO_UZ) && !text.equals(Constant.NO_RU) && !text.equals(Constant.ORDERED_RU) && !text.equals(Constant.ORDERED_UZ)) {
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setState(BotState.ADDITIONAL_PHONE);
                userRepository.save(user);

                boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
                boolean rus = user.getLanguage().equals(Constant.LANG_RU);

                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setSelective(true);
                List<KeyboardRow> keyboardRows = new ArrayList<>();
                KeyboardRow keyboardRow = new KeyboardRow();
                KeyboardButton keyboardButton = new KeyboardButton();

                if (uzbek) {
                    keyboardButton.setText(Constant.BACK_UZ);
                    sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_UZ);
                } else if (rus) {
                    keyboardButton.setText(Constant.BACK_RU);
                    sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_RU);
                } else {
                    keyboardButton.setText(Constant.BACK_EN);
                    sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_EN);
                }
                keyboardRow.add(keyboardButton);
                keyboardRows.add(keyboardRow);
                replyKeyboardMarkup.setKeyboard(keyboardRows);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
            }
            return sendMessage;
        } else {
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setState(BotState.MAIN_MENU);
                userRepository.save(user);

                boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
                boolean rus = user.getLanguage().equals(Constant.LANG_RU);

                if (!text.equals(Constant.NO_UZ) && !text.equals(Constant.NO_RU) && !text.equals(Constant.NO_EN) && !text.equals(Constant.ORDERED_RU) && !text.equals(Constant.ORDERED_UZ) && !text.equals(Constant.ORDERED_EN)) {
                    userService.addAdditionalPhone(text, user.getId());
                }

                List<SelectedProduct> allByChatId = selectedProductRepository.findAllByChatId(user.getChatId());
                List<ReqSelectedProduct> reqSelectedProductList = new ArrayList<>();
                for (SelectedProduct selectedProduct : allByChatId) {
                    ReqSelectedProduct reqSelectedProduct = new ReqSelectedProduct();
                    reqSelectedProduct.setCount(selectedProduct.getCount());
                    reqSelectedProduct.setCalculatedPrice(selectedProduct.getCalculatedPrice());
                    reqSelectedProduct.setProductId(selectedProduct.getProductId());
                    reqSelectedProduct.setProductPriceId(selectedProduct.getProductPriceId());
                    reqSelectedProductList.add(reqSelectedProduct);
                }
                ReqCart reqCart = new ReqCart();
                reqCart.setReqSelectedProducts(reqSelectedProductList);
                reqCart.setStep(Step.CREATED);
                reqCart.setUserId(user.getId());
                userOrderService.createUserOrder(reqCart);

                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setSelective(true);
                List<KeyboardRow> keyboardRows = new ArrayList<>();
                KeyboardRow keyboardRow = new KeyboardRow();
                KeyboardRow keyboardRow1 = new KeyboardRow();
                KeyboardButton keyboardButton = new KeyboardButton();
                KeyboardButton keyboardButton1 = new KeyboardButton();
                KeyboardButton keyboardButton2 = new KeyboardButton();

                if (uzbek) {
                    keyboardButton.setText(Constant.ORDER_TEXT_UZ);
                    keyboardButton1.setText(Constant.SETTINGS_TEXT_UZ);
                    keyboardButton2.setText(Constant.MY_ORDER_TEXT_UZ);
                    sendMessage.setText("Muvaffaqiyatli buyurtma berildi!");
                    selectedProductRepository.deleteAllByChatId(update.getMessage().getChatId());
                } else if (rus) {
                    keyboardButton.setText(Constant.ORDER_TEXT_RU);
                    keyboardButton1.setText(Constant.SETTINGS_TEXT_RU);
                    keyboardButton2.setText(Constant.MY_ORDER_TEXT_RU);
                    sendMessage.setText("Успешный заказ размещен!");
                    selectedProductRepository.deleteAllByChatId(update.getMessage().getChatId());
                } else {
                    keyboardButton.setText(Constant.ORDER_TEXT_EN);
                    keyboardButton1.setText(Constant.SETTINGS_TEXT_EN);
                    keyboardButton2.setText(Constant.MY_ORDER_TEXT_EN);
                    sendMessage.setText("Successful order placed!");
                    selectedProductRepository.deleteAllByChatId(update.getMessage().getChatId());
                }
                keyboardRow.add(keyboardButton);
                keyboardRow.add(keyboardButton1);
                keyboardRow1.add(keyboardButton2);
                keyboardRows.add(keyboardRow);
                keyboardRows.add(keyboardRow1);
                replyKeyboardMarkup.setKeyboard(keyboardRows);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
            }
            return sendMessage;
        }
    }

    //settings
    @Override
    public SendMessage settings(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setState(BotState.SETTINGS);
            userRepository.save(user);
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardButton editFIO = new KeyboardButton();
            KeyboardButton editPhone = new KeyboardButton();
            KeyboardButton editLang = new KeyboardButton();
            KeyboardButton back = new KeyboardButton();

            if (uzbek) {
                editFIO.setText(Constant.EDIT_NAME_SETTINGS_UZ);
                editPhone.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_UZ);
                editLang.setText(Constant.EDIT_LANGUAGE_SETTINGS_UZ);
                back.setText(Constant.BACK_UZ);
                sendMessage.setText(Constant.SETTINGS_TEXT_UZ);
            } else if (rus) {
                editFIO.setText(Constant.EDIT_NAME_SETTINGS_RU);
                editPhone.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_RU);
                editLang.setText(Constant.EDIT_LANGUAGE_SETTINGS_RU);
                back.setText(Constant.BACK_RU);
                sendMessage.setText(Constant.SETTINGS_TEXT_RU);
            } else {
                editFIO.setText(Constant.EDIT_NAME_SETTINGS_EN);
                editPhone.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_EN);
                editLang.setText(Constant.EDIT_LANGUAGE_SETTINGS_EN);
                back.setText(Constant.BACK_EN);
                sendMessage.setText(Constant.SETTINGS_TEXT_EN);
            }
            keyboardRow.add(editFIO);
            keyboardRow.add(editPhone);
            keyboardRow1.add(editLang);
            keyboardRow1.add(back);
            keyboardRows.add(keyboardRow);
            keyboardRows.add(keyboardRow1);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    @Override
    public SendMessage editName(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        optionalUser.ifPresent(user -> {
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);

            if (uzbek) {
                sendMessage.setText(Constant.EDIT_NAME_IN_SETTINGS_UZ);
                keyboardRow1.add(Constant.BACK_UZ);
            } else if (rus) {
                sendMessage.setText(Constant.EDIT_NAME_IN_SETTINGS_RU);
                keyboardRow1.add(Constant.BACK_RU);
            } else {
                sendMessage.setText(Constant.EDIT_NAME_IN_SETTINGS_EN);
                keyboardRow1.add(Constant.BACK_EN);
            }
            user.setState(BotState.EDIT_NAME_IN_SETTINGS_STATE);
            userRepository.save(user);
        });
        keyboardRows.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    @Override
    public SendMessage setName(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        KeyboardButton keyboardButton3 = new KeyboardButton();
        KeyboardButton keyboardButton4 = new KeyboardButton();
        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        optionalUser.ifPresent(user -> {
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);
            if (uzbek) {
                sendMessage.setText("Ism Familiya o'zgartirildi!");
                keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_UZ);
                keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_UZ);
                keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_UZ);
                keyboardButton4.setText(Constant.BACK_UZ);
            } else if (rus) {
                sendMessage.setText("Имя Фамилия изменена!");
                keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_RU);
                keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_RU);
                keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_RU);
                keyboardButton4.setText(Constant.BACK_RU);
            } else {
                sendMessage.setText("First name Last name changed!");
                keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_EN);
                keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_EN);
                keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_EN);
                keyboardButton4.setText(Constant.BACK_EN);
            }
            user.setFullName(update.getMessage().getText());
            user.setState(BotState.SETTINGS);
            userRepository.save(user);
        });
        keyboardRow1.add(keyboardButton1);
        keyboardRow1.add(keyboardButton2);
        keyboardRow2.add(keyboardButton3);
        keyboardRow2.add(keyboardButton4);
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    @Override
    public SendMessage editLanguage(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        KeyboardButton keyboardButton3 = new KeyboardButton();

        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        User user = optionalUser.get();
        user.setState(BotState.EDIT_LANG_SETTINGS_STATE);
        userRepository.save(user);

        boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
        boolean rus = user.getLanguage().equals(Constant.LANG_RU);

        keyboardButton1.setText(Constant.LANGUAGE_UZ);
        keyboardButton2.setText(Constant.LANGUAGE_RU);
        keyboardButton3.setText(Constant.LANGUAGE_EN);
        if (uzbek) {
            sendMessage.setText(Constant.EDIT_LANGUAGE_SETTINGS_UZ);
        } else if (rus) {
            sendMessage.setText(Constant.EDIT_LANGUAGE_SETTINGS_RU);
        } else {
            sendMessage.setText(Constant.EDIT_LANGUAGE_SETTINGS_EN);
        }
        keyboardRow.add(keyboardButton1);
        keyboardRow.add(keyboardButton2);
        keyboardRow.add(keyboardButton3);
        keyboardRows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    @Override
    public SendMessage setLanguage(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        KeyboardButton keyboardButton3 = new KeyboardButton();
        KeyboardButton keyboardButton4 = new KeyboardButton();
        Optional<User> optionalCustomer = userRepository.findByChatId(update.getMessage().getChatId());
        optionalCustomer.ifPresent(user -> {
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);

            String text = update.getMessage().getText();
            if (text.equals(Constant.LANGUAGE_UZ)) {
                user.setLanguage(Constant.LANG_UZ);
            } else if (text.equals(Constant.LANGUAGE_RU)) {
                user.setLanguage(Constant.LANG_RU);
            } else {
                user.setLanguage(Constant.LANG_EN);
            }
            if (text.equals(Constant.LANGUAGE_UZ)) {
                sendMessage.setText(Constant.SETTINGS_TEXT_UZ);
                keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_UZ);
                keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_UZ);
                keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_UZ);
                keyboardButton4.setText(Constant.BACK_UZ);
            } else if (text.equals(Constant.LANGUAGE_RU)) {
                sendMessage.setText(Constant.SETTINGS_TEXT_RU);
                keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_RU);
                keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_RU);
                keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_RU);
                keyboardButton4.setText(Constant.BACK_RU);
            } else {
                sendMessage.setText(Constant.SETTINGS_TEXT_EN);
                keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_EN);
                keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_EN);
                keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_EN);
                keyboardButton4.setText(Constant.BACK_EN);
            }
            user.setState(BotState.SETTINGS);
            userRepository.save(user);
        });
        keyboardRow1.add(keyboardButton1);
        keyboardRow1.add(keyboardButton2);
        keyboardRow2.add(keyboardButton3);
        keyboardRow2.add(keyboardButton4);
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    @Override
    public SendMessage editAdditionalPhone(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.HTML);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
//        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
//        KeyboardButton keyboardButton1 = new KeyboardButton();
//        keyboardButton1.setRequestContact(true);
        KeyboardButton keyboardButton2 = new KeyboardButton();

        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        optionalUser.ifPresent(user -> {
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);

            if (uzbek) {
                sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_UZ);
//                keyboardButton1.setText(Constant.SHARE_CONTACT_UZ);
                keyboardButton2.setText(Constant.BACK_UZ);
            } else if (rus) {
                sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_RU);
//                keyboardButton1.setText(Constant.SHARE_CONTACT_RU);
                keyboardButton2.setText(Constant.BACK_RU);
            } else {
                sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_EN);
//                keyboardButton1.setText(Constant.SHARE_CONTACT_RU);
                keyboardButton2.setText(Constant.BACK_EN);
            }
            user.setState(BotState.EDIT_PHONE_NUMBER_IN_SETTINGS_STATE);
            userRepository.save(user);
        });
//        keyboardRow1.add(keyboardButton1);
        keyboardRow2.add(keyboardButton2);
//        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    @Override
    public SendMessage setEditAdditionalPhone(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Message message = update.getMessage();
            String phoneNumber;
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);

            phoneNumber = update.getMessage().getText();
            if (!phoneNumber.matches("\\+998[0-9]{9}")) {
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setSelective(true);
                List<KeyboardRow> keyboardRows = new ArrayList<>();
//                    KeyboardRow keyboardRow1 = new KeyboardRow();
                KeyboardRow keyboardRow2 = new KeyboardRow();
//                    KeyboardButton keyboardButton1 = new KeyboardButton();
//                    keyboardButton1.setRequestContact(true);
                KeyboardButton keyboardButton2 = new KeyboardButton();

                if (uzbek) {
                    sendMessage.setText(Constant.VALIDATION_TEXT_UZ);
//                        keyboardButton1.setText(Constant.SHARE_CONTACT_UZ);
                    keyboardButton2.setText(Constant.BACK_UZ);
                } else if (rus) {
                    sendMessage.setText(Constant.VALIDATION_TEXT_RU);
//                        keyboardButton1.setText(Constant.SHARE_CONTACT_RU);
                    keyboardButton2.setText(Constant.BACK_RU);
                } else {
                    sendMessage.setText(Constant.VALIDATION_TEXT_EN);
//                        keyboardButton1.setText(Constant.SHARE_CONTACT_RU);
                    keyboardButton2.setText(Constant.BACK_EN);
                }
                user.setState(BotState.EDIT_PHONE_NUMBER_IN_SETTINGS_STATE);
                userRepository.save(user);

//                    keyboardRow1.add(keyboardButton1);
                keyboardRow2.add(keyboardButton2);
//                    keyboardRows.add(keyboardRow1);
                keyboardRows.add(keyboardRow2);
                replyKeyboardMarkup.setKeyboard(keyboardRows);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                return sendMessage;
            } else {

                String finalPhoneNumber = phoneNumber;
//                ApiResponse apiResponse = userService.addAdditionalPhone(finalPhoneNumber, user.getId());
                user.setAdditionalPhone(finalPhoneNumber);
//                if (!apiResponse.isSuccess()) {
//                    user.setState(BotState.EDIT_PHONE_NUMBER_IN_SETTINGS_STATE);
//                    userRepository.save(user);
//
//                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//                    replyKeyboardMarkup.setResizeKeyboard(true);
//                    replyKeyboardMarkup.setSelective(true);
//                    List<KeyboardRow> keyboardRows = new ArrayList<>();
//                    KeyboardRow keyboardRow2 = new KeyboardRow();
//                    KeyboardButton keyboardButton2 = new KeyboardButton();
//
//                    if (user.getLanguage().equals(Constant.LANG_UZ)) {
//                        sendMessage.setText(Constant.VALIDATION_ADDITIONAL_TEXT_UZ);
//                        keyboardButton2.setText(Constant.BACK_UZ);
//                    } else {
//                        sendMessage.setText(Constant.VALIDATION_ADDITIONAL_TEXT_RU);
//                        keyboardButton2.setText(Constant.BACK_RU);
//                    }
//                    keyboardRow2.add(keyboardButton2);
//                    keyboardRows.add(keyboardRow2);
//                    replyKeyboardMarkup.setKeyboard(keyboardRows);
//                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
//                    return sendMessage;
//                } else {
                user.setState(BotState.SETTINGS);
                userRepository.save(user);

                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setSelective(true);
                List<KeyboardRow> keyboardRows = new ArrayList<>();
                KeyboardRow keyboardRow1 = new KeyboardRow();
                KeyboardRow keyboardRow2 = new KeyboardRow();
                KeyboardButton keyboardButton1 = new KeyboardButton();
                KeyboardButton keyboardButton2 = new KeyboardButton();
                KeyboardButton keyboardButton3 = new KeyboardButton();
                KeyboardButton keyboardButton4 = new KeyboardButton();


                if (uzbek) {
                    sendMessage.setText("Qo'shimcha raqam o'zgartirildi!");
                    keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_UZ);
                    keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_UZ);
                    keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_UZ);
                    keyboardButton4.setText(Constant.BACK_UZ);
                } else if (rus) {
                    sendMessage.setText("Дополнительный номер изменен!");
                    keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_RU);
                    keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_RU);
                    keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_RU);
                    keyboardButton4.setText(Constant.BACK_RU);
                } else {
                    sendMessage.setText("Additional number changed!");
                    keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_EN);
                    keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_EN);
                    keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_EN);
                    keyboardButton4.setText(Constant.BACK_EN);
                }

                keyboardRow1.add(keyboardButton1);
                keyboardRow1.add(keyboardButton2);
                keyboardRow2.add(keyboardButton3);
                keyboardRow2.add(keyboardButton4);
                keyboardRows.add(keyboardRow1);
                keyboardRows.add(keyboardRow2);
                replyKeyboardMarkup.setKeyboard(keyboardRows);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
//                    return sendMessage;
            }
        }
//        }
        return sendMessage;
    }

    @Override
    public SendMessage editPhoneNumber(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.HTML);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        keyboardButton1.setRequestContact(true);
        KeyboardButton keyboardButton2 = new KeyboardButton();

        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        optionalUser.ifPresent(user -> {
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);

            if (uzbek) {
                sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_UZ);
                keyboardButton1.setText(Constant.SHARE_CONTACT_UZ);
                keyboardButton2.setText(Constant.BACK_UZ);
            } else if (rus) {
                sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_RU);
                keyboardButton1.setText(Constant.SHARE_CONTACT_RU);
                keyboardButton2.setText(Constant.BACK_RU);
            } else {
                sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_EN);
                keyboardButton1.setText(Constant.SHARE_CONTACT_EN);
                keyboardButton2.setText(Constant.BACK_EN);
            }
            user.setState(BotState.EDIT_PHONE_NUMBER_IN_SETTINGS_STATE);
            userRepository.save(user);
        });
        keyboardRow1.add(keyboardButton1);
        keyboardRow2.add(keyboardButton2);
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    @Override
    public SendMessage setEditPhoneNumber(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setParseMode(ParseMode.MARKDOWN);

        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Message message = update.getMessage();
            String phoneNumber;
            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
            boolean rus = user.getLanguage().equals(Constant.LANG_RU);

            if (message.hasContact()) {
                Contact contact = message.getContact();
                phoneNumber = contact.getPhoneNumber();
            } else {
                phoneNumber = update.getMessage().getText();
                if (!phoneNumber.matches("\\+998[0-9]{9}")) {
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    replyKeyboardMarkup.setSelective(true);
                    List<KeyboardRow> keyboardRows = new ArrayList<>();
                    KeyboardRow keyboardRow1 = new KeyboardRow();
                    KeyboardRow keyboardRow2 = new KeyboardRow();
                    KeyboardButton keyboardButton1 = new KeyboardButton();
                    keyboardButton1.setRequestContact(true);
                    KeyboardButton keyboardButton2 = new KeyboardButton();

                    if (uzbek) {
                        sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_UZ);
                        keyboardButton1.setText(Constant.SHARE_CONTACT_UZ);
                        keyboardButton2.setText(Constant.BACK_UZ);
                    } else if (rus) {
                        sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_RU);
                        keyboardButton1.setText(Constant.SHARE_CONTACT_RU);
                        keyboardButton2.setText(Constant.BACK_RU);
                    } else {
                        sendMessage.setText(Constant.EDIT_PHONE_NUMBER_INFO_SETTINGS_EN);
                        keyboardButton1.setText(Constant.SHARE_CONTACT_EN);
                        keyboardButton2.setText(Constant.BACK_EN);
                    }
                    user.setState(BotState.EDIT_PHONE_NUMBER_IN_SETTINGS_STATE);
                    userRepository.save(user);

                    keyboardRow1.add(keyboardButton1);
                    keyboardRow2.add(keyboardButton2);
                    keyboardRows.add(keyboardRow1);
                    keyboardRows.add(keyboardRow2);
                    replyKeyboardMarkup.setKeyboard(keyboardRows);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
                    return sendMessage;
                } else {
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    replyKeyboardMarkup.setSelective(true);
                    List<KeyboardRow> keyboardRows = new ArrayList<>();
                    KeyboardRow keyboardRow = new KeyboardRow();
                    KeyboardButton keyboardButton = new KeyboardButton();
                    keyboardButton.setRequestContact(true);

                    if (uzbek) {
                        sendMessage.setText(Constant.SHARE_CONTACT_TEXT_UZ);
                        keyboardButton.setText(Constant.SHARE_CONTACT_UZ);
                    } else if (rus) {
                        sendMessage.setText(Constant.SHARE_CONTACT_TEXT_RU);
                        keyboardButton.setText(Constant.SHARE_CONTACT_RU);
                    } else {
                        sendMessage.setText(Constant.SHARE_CONTACT_TEXT_EN);
                        keyboardButton.setText(Constant.SHARE_CONTACT_EN);
                    }
                    user.setState(BotState.EDIT_PHONE_NUMBER_IN_SETTINGS_STATE);
                    userRepository.save(user);

                    keyboardRow.add(keyboardButton);
                    keyboardRows.add(keyboardRow);
                    replyKeyboardMarkup.setKeyboard(keyboardRows);
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
                    return sendMessage;
                }
            }

            phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
            String finalPhoneNumber = phoneNumber;
            user.setPhoneNumber(finalPhoneNumber);
            user.setState(BotState.SETTINGS);
            userRepository.save(user);

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setSelective(true);
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardRow keyboardRow2 = new KeyboardRow();
            KeyboardButton keyboardButton1 = new KeyboardButton();
            KeyboardButton keyboardButton2 = new KeyboardButton();
            KeyboardButton keyboardButton3 = new KeyboardButton();
            KeyboardButton keyboardButton4 = new KeyboardButton();


            if (uzbek) {
                sendMessage.setText(Constant.SETTINGS_TEXT_UZ);
                keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_UZ);
                keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_UZ);
                keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_UZ);
                keyboardButton4.setText(Constant.BACK_UZ);
            } else if (rus) {
                sendMessage.setText(Constant.SETTINGS_TEXT_RU);
                keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_RU);
                keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_RU);
                keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_RU);
                keyboardButton4.setText(Constant.BACK_RU);
            } else {
                sendMessage.setText(Constant.SETTINGS_TEXT_EN);
                keyboardButton1.setText(Constant.EDIT_NAME_SETTINGS_EN);
                keyboardButton2.setText(Constant.EDIT_PHONE_NUMBER_SETTINGS_EN);
                keyboardButton3.setText(Constant.EDIT_LANGUAGE_SETTINGS_EN);
                keyboardButton4.setText(Constant.BACK_EN);
            }

            keyboardRow1.add(keyboardButton1);
            keyboardRow1.add(keyboardButton2);
            keyboardRow2.add(keyboardButton3);
            keyboardRow2.add(keyboardButton4);
            keyboardRows.add(keyboardRow1);
            keyboardRows.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }
}