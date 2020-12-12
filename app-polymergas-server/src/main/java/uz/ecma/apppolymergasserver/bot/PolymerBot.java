package uz.ecma.apppolymergasserver.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.ecma.apppolymergasserver.entity.User;
import uz.ecma.apppolymergasserver.repository.UserRepository;

import javax.xml.bind.SchemaOutputResolver;
import java.util.Optional;


@Component
public class PolymerBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    String botToken;

    @Value("${bot.username}")
    String username;

    @Autowired
    TelegramService telegramService;

    @Autowired
    UserRepository userRepository;

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                String text = message.getText();
                if (message.hasText()) {
                    if (text.equals("/start")) {
                        execute(telegramService.chooseLanguage(update));
                    } else {
                        Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
                        if (optionalUser.isPresent()) {
                            User user = optionalUser.get();
//                            boolean uzbek = user.getLanguage().equals(Constant.LANG_UZ);
//                            boolean rus = user.getLanguage().equals(Constant.LANG_RU);

                            String state = user.getState();
                            switch (state) {
                                case BotState.CHOOSE_LANGUAGE:
                                    execute(telegramService.welcomeToBot(update));
                                    execute(telegramService.shareContact(update));
                                    break;
                                case BotState.SHARE_CONTACT_STATE:
                                    execute(telegramService.mainMenu(update, 0));
                                    break;
                                case BotState.MAIN_MENU:
                                    switch (text) {
                                        case Constant.ORDER_TEXT_RU:
                                        case Constant.ORDER_TEXT_UZ:
                                        case Constant.ORDER_TEXT_EN:
                                            execute(telegramService.menuCategory(update, 0));
                                            break;
                                        case Constant.SETTINGS_TEXT_RU:
                                        case Constant.SETTINGS_TEXT_UZ:
                                        case Constant.SETTINGS_TEXT_EN:
                                            execute(telegramService.settings(update));
                                            break;
                                        case Constant.MY_ORDER_TEXT_UZ:
                                        case Constant.MY_ORDER_TEXT_RU:
                                        case Constant.MY_ORDER_TEXT_EN:
                                            execute(telegramService.myOrders(update, 0));
                                            break;
                                        default:
                                            execute(telegramService.mainMenu(update, 0));
                                    }
                                    break;
                                case BotState.MY_ORDER:
                                    switch (text) {
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.mainMenu(update, 1));
                                            break;
                                        default:
                                            execute(telegramService.myOneOrder(update));
                                    }
                                    break;
                                case BotState.ORDER_HISTORY:
                                    switch (text) {
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.myOrders(update, 1));
                                            break;
                                    }
                                    break;

                                case BotState.IN_PROGRESS:
                                    switch (text) {
                                        case Constant.ACCEPT_ORDER_UZ:
                                        case Constant.ACCEPT_ORDER_RU:
                                        case Constant.ACCEPT_ORDER_EN:
                                            execute(telegramService.inProgressAccept(update, 0));
                                            break;
                                        case Constant.REJECTED_ORDER_UZ:
                                        case Constant.REJECTED_ORDER_RU:
                                        case Constant.REJECTED_ORDER_EN:
                                            execute(telegramService.inProgressAccept(update, 1));
                                            break;
                                    }
                                    break;
                                case BotState.IN_PROGRESS_ACCEPT:
                                    if (user.getCommentType().equals(Constant.ACCEPT_ORDER_UZ) || user.getCommentType().equals(Constant.ACCEPT_ORDER_RU) || user.getCommentType().equals(Constant.ACCEPT_ORDER_EN)) {
                                        execute(telegramService.inProgressComment(update, 0));
                                    } else {
                                        execute(telegramService.inProgressComment(update, 1));
                                    }
                                    break;
                                case BotState.MENU_CATEGORY:
                                    switch (text) {
                                        case Constant.TRASH_RU:
                                        case Constant.TRASH_UZ:
                                        case Constant.TRASH_EN:
                                            execute(telegramService.getProductsFromBasket(update));
                                            break;
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.mainMenu(update, 1));
                                            break;
                                        case Constant.SET_ORDER_RU:
                                        case Constant.SET_ORDER_UZ:
                                        case Constant.SET_ORDER_EN:
                                            execute(telegramService.setOrder(update));
                                            break;
                                        default:
                                            execute(telegramService.menuProducts(update, 0));
                                    }
                                    break;
                                case BotState.SETTINGS:
                                    System.out.print("Text" + text);
                                    switch (text) {
                                        case Constant.EDIT_NAME_SETTINGS_RU:
                                        case Constant.EDIT_NAME_SETTINGS_UZ:
                                        case Constant.EDIT_NAME_SETTINGS_EN:
                                            execute(telegramService.editName(update));
                                            break;
                                        case Constant.EDIT_PHONE_NUMBER_SETTINGS_RU:
                                        case Constant.EDIT_PHONE_NUMBER_SETTINGS_UZ:
                                        case Constant.EDIT_PHONE_NUMBER_SETTINGS_EN:
                                            execute(telegramService.editAdditionalPhone(update));
                                            break;
                                        case Constant.EDIT_LANGUAGE_SETTINGS_RU:
                                        case Constant.EDIT_LANGUAGE_SETTINGS_UZ:
                                        case Constant.EDIT_LANGUAGE_SETTINGS_EN:
                                            execute(telegramService.editLanguage(update));
                                            break;
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.mainMenu(update, 1));
                                            break;
                                    }
                                    break;
                                case BotState.EDIT_LANG_SETTINGS_STATE:
                                    switch (text) {
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.settings(update));
                                            break;
                                        default:
                                            execute(telegramService.setLanguage(update));
                                            break;
                                    }
                                    break;
                                case BotState.EDIT_NAME_IN_SETTINGS_STATE:
                                    switch (text) {
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.settings(update));
                                            break;
                                        default:
                                            execute(telegramService.setName(update));
                                            break;
                                    }
                                    break;
                                case BotState.EDIT_PHONE_NUMBER_IN_SETTINGS_STATE:
                                    switch (text) {
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.settings(update));
                                            break;
                                        default:
                                            execute(telegramService.setEditAdditionalPhone(update));
                                            break;
                                    }
                                    break;
                                case BotState.MENU_PRODUCTS:
                                    switch (text) {
                                        case Constant.TRASH_RU:
                                        case Constant.TRASH_UZ:
                                        case Constant.TRASH_EN:
                                            execute(telegramService.getProductsFromBasket(update));
                                            break;
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.menuCategory(update, 1));
                                            break;
                                        default:
                                            execute(telegramService.menuProductCount(update, 0));
                                    }
                                    break;
                                case BotState.MENU_PRODUCT_PRICES:
                                    switch (text) {
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.menuProducts(update, 1));
                                            break;
                                        case Constant.TRASH_RU:
                                        case Constant.TRASH_UZ:
                                        case Constant.TRASH_EN:
                                            execute(telegramService.getProductsFromBasket(update));
                                            break;
                                        default:
                                            execute(telegramService.saveBascetAndMainMenuCategory(update));
                                    }
                                    break;
                                case BotState.NUMBER_PRODUCTS_STATE:
                                    switch (text) {
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.menuProductPrices(update, 1));
                                            break;
                                        default:
                                            execute(telegramService.saveBascetAndMainMenuCategory(update));
                                    }
                                    break;
                                case BotState.TRASH_STATE:
                                    switch (text) {
                                        case Constant.CLEAR_BASKET_RU:
                                        case Constant.CLEAR_BASKET_UZ:
                                        case Constant.CLEAR_BASKET_EN:
                                            execute(telegramService.clearAllProducts(update));
                                            break;
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.menuCategory(update, 1));
                                            break;
                                        case Constant.SET_ORDER_RU:
                                        case Constant.SET_ORDER_UZ:
                                        case Constant.SET_ORDER_EN:
                                            execute(telegramService.setOrder(update));
                                            break;
                                        default:
                                            execute(telegramService.deleteProductAndUpdateTrash(update));
                                    }
                                    break;

                                case BotState.SET_ORDER:
                                    switch (text) {
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.mainMenu(update, 1));
                                            break;
                                        case Constant.ORDERED_RU:
                                        case Constant.ORDERED_UZ:
                                        case Constant.ORDERED_EN:
                                            if (user.getAdditionalPhone() == null) {
                                                execute(telegramService.setAdditionalPhone(update));
                                                break;
                                            } else {
                                                execute(telegramService.ordered(update));
                                                break;
                                            }
                                    }
                                    break;
                                case BotState.ADDITIONAL_PHONE:
                                    switch (text) {
                                        case Constant.BACK_RU:
                                        case Constant.BACK_UZ:
                                        case Constant.BACK_EN:
                                            execute(telegramService.setAdditionalPhone(update));
                                            break;
                                        case Constant.NO_RU:
                                        case Constant.NO_UZ:
                                            execute(telegramService.ordered(update));
                                            break;
                                        default:
                                            execute(telegramService.ordered(update));
                                            break;
                                    }
                                    break;
                            }
                        }
                    }
                } else if (message.hasContact()) {
                    Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        String state = user.getState();
                        switch (state) {
                            case BotState.SHARE_CONTACT_STATE:
                                execute(telegramService.mainMenu(update, 0));
                                break;
                            case BotState.MAIN_MENU:
//                                execute(telegramService)
                        }
                    }
                } else if (message.hasLocation()) {
                    Optional<User> optionalUser = userRepository.findByChatId(update.getMessage().getChatId());
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        String state = user.getState();
                        switch (state) {
//                            case BotState.SEND_LOCATION_STATE:
//                                execute(telegramService.sendPassword(update));
//                                break;
                        }
                    }
                }
            } else if (update.hasCallbackQuery()) {
                String data = update.getCallbackQuery().getData();
//                if (data.equals("langUz") || data.equals("langRu")) {
//                    execute(telegramService.deleteTopMessage(update));
//                    execute(telegramService.shareContact(update));
//                } else if (data.equals("selectMainColor")) {
//                    execute(telegramService.deleteTopMessage(update));
//                    execute(telegramService.selectMainColor(update));
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
