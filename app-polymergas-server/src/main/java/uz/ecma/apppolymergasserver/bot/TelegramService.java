package uz.ecma.apppolymergasserver.bot;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.ecma.apppolymergasserver.entity.UserOrder;

public interface TelegramService {

    SendMessage welcomeToBot(Update update);

    SendMessage chooseLanguage(Update update);

    SendMessage shareContact(Update update);

    SendMessage mainMenu(Update update, int type);

    SendMessage myOrders(Update update, int type);

    SendMessage myOneOrder(Update update);

    SendMessage inProgress(UserOrder userOrder);

    SendMessage inProgressAccept(Update update, int type);

    SendMessage inProgressComment(Update update, int type);

    SendMessage menuCategory(Update update, int type);

    SendMessage menuProducts(Update update, int type);

    SendPhoto menuProductPrices(Update update, int type);

    SendPhoto menuProductCount(Update update, int type);

    SendMessage numberProduct(Update update);

    SendMessage saveBascetAndMainMenuCategory(Update update);

    SendMessage getProductsFromBasket(Update update);

    SendMessage deleteProductAndUpdateTrash(Update update);

    SendMessage clearAllProducts(Update update);

    SendMessage setOrder(Update update);

    SendMessage setAdditionalPhone(Update update);

    SendMessage ordered(Update update);

    SendMessage settings(Update update);

    SendMessage editName(Update update);

    SendMessage setName(Update update);

    SendMessage editLanguage(Update update);

    SendMessage setLanguage(Update update);

    SendMessage editAdditionalPhone(Update update);

    SendMessage setEditAdditionalPhone(Update update);

    SendMessage editPhoneNumber(Update update);

    SendMessage setEditPhoneNumber(Update update);

}
