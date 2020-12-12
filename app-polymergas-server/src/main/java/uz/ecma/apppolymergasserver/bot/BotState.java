package uz.ecma.apppolymergasserver.bot;

public interface BotState {
    String CHOOSE_LANGUAGE = "choose_language";

    String SHARE_CONTACT_STATE = "share_contact";

    String MAIN_MENU = "main_menu";

    String MENU_CATEGORY = "menu_category";

    String MENU_PRODUCTS = "menu_products";

    String MENU_PRODUCT_PRICES = "menu_productPrices";

    String MENU_PRODUCT_COUNT = "menu_productCount";

    String SETTINGS = "settings";

    String EDIT_NAME_IN_SETTINGS_STATE = "edit_name";

    String EDIT_PHONE_NUMBER_IN_SETTINGS_STATE = "edit_phone";

    String EDIT_LANG_SETTINGS_STATE = "edit_lang";

    String NUMBER_PRODUCTS_STATE = "number_products";

    String TRASH_STATE = "trash_state";

    String SET_ORDER = "set_order";

    String ADDITIONAL_PHONE = "set_additional_info";

    String MY_ORDER = "my_orders";

    String ORDER_HISTORY = "order_history";

    String IN_PROGRESS = "in_progress";

    String IN_PROGRESS_ACCEPT = "in_progress_accept";

}
