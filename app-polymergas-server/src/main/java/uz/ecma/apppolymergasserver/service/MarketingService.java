package uz.ecma.apppolymergasserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.ecma.apppolymergasserver.bot.BotState;
import uz.ecma.apppolymergasserver.bot.Constant;
import uz.ecma.apppolymergasserver.bot.PolymerBot;
import uz.ecma.apppolymergasserver.entity.Marketing;
import uz.ecma.apppolymergasserver.entity.User;
import uz.ecma.apppolymergasserver.exception.ResourceNotFoundException;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ReqMarketing;
import uz.ecma.apppolymergasserver.payload.ResPageable;
import uz.ecma.apppolymergasserver.repository.AttachmentContentRepository;
import uz.ecma.apppolymergasserver.repository.AttachmentRepository;
import uz.ecma.apppolymergasserver.repository.MarketingRepository;
import uz.ecma.apppolymergasserver.repository.UserRepository;
import uz.ecma.apppolymergasserver.utils.CommonUtils;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MarketingService {

    @Autowired
    MarketingRepository marketingRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PolymerBot polymerBot;

    public ApiResponse saveMarketing(@RequestBody ReqMarketing reqMarketing) {
        Marketing marketing = marketingRepository.save(new Marketing(
                reqMarketing.getTitleUz(),
                reqMarketing.getTitleRu(),
                reqMarketing.getTitleEn(),
                reqMarketing.getTextUz(),
                reqMarketing.getTextRu(),
                reqMarketing.getTextEn(),
                attachmentRepository.findById(reqMarketing.getPhotoId()).orElseThrow(() -> new ResourceNotFoundException("getPhoto", "photoId", reqMarketing.getPhotoId()))
        ));
        new Thread(() -> sendMarketingToTelegram(marketing)).start();
        return new ApiResponse("Yuborildi", true);
    }

    private void sendMarketingToTelegram(Marketing marketing) {
        byte[] bytes = attachmentContentRepository.getByte(marketing.getAttachment().getId());
        int page = 0;
        boolean isHave = true;
        while (isHave) {
            Page<User> users = userRepository.findAllByChatIdIsNotNull(CommonUtils.getPageable(page, 20));
            isHave = users.getTotalPages() > page + 1;
            for (User user : users.getContent()) {
                try {
                    polymerBot.execute(sendPhoto(user.getChatId(), user.getLanguage().equals(Constant.LANG_UZ), user.getLanguage().equals(Constant.LANG_RU), marketing.getTitleUz(), marketing.getTitleRu(), marketing.getTitleEn(), marketing.getTextUz(), marketing.getTextRu(), marketing.getTextEn(), bytes));
                    polymerBot.execute(mainMenu(user.getId(), user.getChatId(), user.getLanguage().equals(Constant.LANG_UZ), user.getLanguage().equals(Constant.LANG_RU)));

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            page += 1;
        }
    }

    private SendPhoto sendPhoto(Long chatId, boolean uzb, boolean ru, String titleUz, String titleRu, String titleEn, String messageUz, String messageRu, String messageEn, byte[] bytes) {
        SendPhoto sendPhoto = new SendPhoto().setParseMode(ParseMode.HTML).setChatId(chatId);
        sendPhoto.setPhoto("polymer", new ByteArrayInputStream(bytes));
        String stringBuilder =
                "\n" +
                        (uzb ? titleUz : ru ? titleRu : titleEn) +
                        "\n" +
                        (uzb ? messageUz : ru ? messageRu : messageEn) +
                        "\n";
        sendPhoto.setCaption(stringBuilder);
        return sendPhoto;
    }

    private SendMessage mainMenu(UUID userId, Long chatId, boolean uzb, boolean ru) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setParseMode(ParseMode.MARKDOWN);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton keyboardButton1 = new KeyboardButton();
        KeyboardButton keyboardButton2 = new KeyboardButton();
        KeyboardButton keyboardButton3 = new KeyboardButton();
        keyboardButton1.setText(uzb ? Constant.ORDER_TEXT_UZ : ru ? Constant.ORDER_TEXT_RU : Constant.ORDER_TEXT_EN);
        keyboardButton2.setText(uzb ? Constant.SETTINGS_TEXT_UZ : ru ? Constant.SETTINGS_TEXT_RU : Constant.SETTINGS_TEXT_EN);
        keyboardButton3.setText(uzb ? Constant.MY_ORDER_TEXT_UZ : ru ? Constant.MY_ORDER_TEXT_RU : Constant.MY_ORDER_TEXT_EN);
//        userRepository.setState(userId, BotState.MAIN_MENU);
        Optional<User> byId = userRepository.findById(userId);
        User user = byId.get();
        user.setState(BotState.MAIN_MENU);
        userRepository.save(user);
        sendMessage.setText(uzb ? Constant.LETS_GO_ORDER_UZ : ru ? Constant.LETS_GO_ORDER_RU : Constant.LETS_GO_ORDER_EN);
        keyboardRow.add(keyboardButton1);
        keyboardRow.add(keyboardButton2);
        keyboardRows.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }


    public ResPageable getMarketings(int page, int size) {
        Page<Marketing> marketingPage = marketingRepository.findAll(CommonUtils.getPageable(page, size));
        return new ResPageable(
                marketingPage.getContent().stream().map(this::getMarketing).collect(Collectors.toList()),
                marketingPage.getTotalElements(),
                page
        );
    }

    public ReqMarketing getMarketing(Marketing marketing) {
        return new ReqMarketing(
                marketing.getId(),
                marketing.getTitleUz(),
                marketing.getTitleRu(),
                marketing.getTitleEn(),
                marketing.getTextUz(),
                marketing.getTextRu(),
                marketing.getTextEn(),
                marketing.getAttachment().getId(),
                new SimpleDateFormat("dd.MM.yyyy HH:mm").format(marketing.getCreatedAt()),
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/").path(marketing.getAttachment().getId().toString()).toUriString()
        );
    }

    public ApiResponse deleteMarketing(UUID id) {
        try {
            marketingRepository.deleteById(id);
            return new ApiResponse("Muvaffaqiyatli o'chirildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }
}
