package com.comany.KorzinkaBot;

import com.comany.KorzinkaBot.modul.Customer;
import com.comany.KorzinkaBot.modul.CustomerDto;
import com.sun.nio.sctp.SendFailedNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class KorzinkaBot extends TelegramLongPollingBot {

    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;

    private String step;

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage returnMessage = new SendMessage();
        Message message = update.getMessage();
        String text = message.getText();

        if (message.hasText()) {

            Customer customer = customerMapper.toEntity(new CustomerDto());
            if (text.equals("/start")) {
                    customer.setChatId(update.getMessage().getChatId());
                    customer.setUserName(update.getMessage().getChat().getUserName());
                    this.customerMapper.toDto(this.userRepository.save(customer));
                    returnMessage = stageStartUz(message);
            } else if (text.equals("Til: O'zbekcha \uD83C\uDDFA\uD83C\uDDFF/ English\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F") || text.equals("Language : O'zbekcha \uD83C\uDDFA\uD83C\uDDFF/ English\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F")) {
                returnMessage = language_start(message);

            } else if (text.equals("Uzbek\uD83C\uDDFA\uD83C\uDDFF")) {
                returnMessage = stageStartUz(message);

            } else if (text.equals("English\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F")) {
                returnMessage = stageStartEn(message);

            } else if (text.equals("Biz haqimizda")) {
                returnMessage = about_startUz(message);
            } else if (text.equals("About Us")) {
                returnMessage = about_startEn(message);
            } else if (text.equals("Qaynoq vakansiyalar \uD83D\uDD25")) {
                returnMessage = stageVakansUz(message);
            } else if ( text.equals("Hot Vacancies \uD83D\uDD25")) {
                returnMessage=stageVakansEn(message);
            } else if (text.equals("Back")) {
                returnMessage = stageBack(message);
            } else if (text.equals("Yuk tashubvchi") || text.equals("Oshpaz")|| text.equals("Qo'riqchi")|| text.equals("Sotuvchi kassir")) {
                returnMessage=stageIshUz(message);
            } else if (text.equals("Shipper") || text.equals("Chef") || text.equals("Warden")|| text.equals("Cashier")) {
                returnMessage=stageIshEn(message);
            } else if (text.equals("\uD83D\uDED2 Eshonguzar")) {
                returnMessage=stageLokatsiya(message);
            }
            else if (Objects.equals(step, "2") && text!="Back"){
                customer.setChatId(update.getMessage().getChatId());
                customer.setUserName(update.getMessage().getChat().getUserName());
                customer.setFullName(message.getText());
                this.customerMapper.toDto(this.userRepository.save(customer));
                returnMessage=stageData(message);
            }
            else if (Objects.equals(step, "3") && text!="Back") {
                customer.setChatId(update.getMessage().getChatId());
                customer.setUserName(update.getMessage().getChat().getUserName());
                customer.setData(message.getText());
                this.customerMapper.toDto(this.userRepository.save(customer));
                returnMessage=stageHome(message);
            }
            else if (Objects.equals(step, "4") && text!="Back") {
                customer.setChatId(update.getMessage().getChatId());
                customer.setUserName(update.getMessage().getChat().getUserName());
                 customer.setHome(message.getText());
                this.customerMapper.toDto(this.userRepository.save(customer));
                returnMessage=stageContact(message);
            }
            else if (text.equals("Erkak") || text.equals("Ayol") || step.equals("5")) {
                customer.setChatId(update.getMessage().getChatId());
                customer.setUserName(update.getMessage().getChat().getUserName());
               customer.setJinsi(message.getText());
                this.customerMapper.toDto(this.userRepository.save(customer));
                returnMessage=stageEnd(message);
            }

        }
        else if (message.hasContact()) {
            returnMessage =stageJins(message);
        }
        try {
            execute(returnMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
    private SendMessage stageJins(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Iltimos jinisingizni tanlang");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow first = new KeyboardRow();
        KeyboardButton firstButton = new KeyboardButton();
        firstButton.setText("Erkak");
        KeyboardButton secButton = new KeyboardButton();
        secButton.setText("Ayol");
        first.add(firstButton);
        first.add(secButton);
        rowList.add(first);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }
    private SendMessage stageHome(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Yashash manzilingizni kiriting");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        KeyboardRow second = new KeyboardRow();
        KeyboardButton backButton = new KeyboardButton();
        backButton.setText("Back");
        second.add(backButton);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(second);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setResizeKeyboard(true);
        step="4";
        return sendMessage;
    }
    private SendMessage stageData(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Tug'ilgan sanangizni jonating: 11.10.2000");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        KeyboardRow second = new KeyboardRow();
        KeyboardButton backButton = new KeyboardButton();
        backButton.setText("Back");
        second.add(backButton);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(second);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setResizeKeyboard(true);
        step="3";
        return sendMessage;
    }


    public void sendLocationToTelegramBot( long chatId) {
        SendLocation location = new SendLocation();
        location.setChatId(chatId);
        location.setLatitude(41.25210);
        location.setLongitude(69.15298);
        // Telegram botga location obyektini jonatish
        try {
            execute(location);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private SendMessage stageLokatsiya(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("\uD83D\uDCCCManzil: Zangiota tumani, Eshonguzar.\n" +
                "\n" +
                "\uD83D\uDD0EMo'ljal: \"Eshonguzar\" basseyn.");
        sendMessage.setText("Ism Familyangizni otangizni ismini kiriting");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendLocationToTelegramBot(message.getChatId());
        KeyboardRow second = new KeyboardRow();
        KeyboardButton backButton = new KeyboardButton();
        backButton.setText("Back");
        second.add(backButton);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(second);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setResizeKeyboard(true);
        step="2";
        return sendMessage;
    }



    private SendMessage language_start(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Select language pls");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow first = new KeyboardRow();
        KeyboardButton firstButton = new KeyboardButton();
        firstButton.setText("Uzbek\uD83C\uDDFA\uD83C\uDDFF");
        KeyboardButton secButton = new KeyboardButton();
        secButton.setText("English\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F");
        first.add(firstButton);
        first.add(secButton);
        rowList.add(first);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }


    private SendMessage stageStartUz(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Assalomu Aleykum botga Hush kelibsiz \uD83D\uDE42\uD83D\uDC47");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendTextWithImage(message.getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow first = new KeyboardRow();
        KeyboardRow second = new KeyboardRow();
        KeyboardRow third = new KeyboardRow();
        KeyboardRow fourth = new KeyboardRow();
        KeyboardButton firstButton = new KeyboardButton();
        firstButton.setText("Biz haqimizda");
        KeyboardButton secondButton = new KeyboardButton();
        secondButton.setText("Qaynoq vakansiyalar \uD83D\uDD25");
        KeyboardButton thirdButton = new KeyboardButton();
        thirdButton.setText("Bo'sh ish o'rinlari");
        KeyboardButton fourthButton = new KeyboardButton();
        fourthButton.setText("Til: O'zbekcha \uD83C\uDDFA\uD83C\uDDFF/ English\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F");
        first.add(firstButton);
        second.add(secondButton);
        third.add(thirdButton);
        fourth.add(fourthButton);
        rowList.add(first);
        rowList.add(second);
        rowList.add(third);
        rowList.add(fourth);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        step="1";
        return sendMessage;
    }

    private SendMessage stageStartEn(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Welcome to my Bot \uD83D\uDE42\uD83D\uDC47");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow first = new KeyboardRow();
        KeyboardRow second = new KeyboardRow();
        KeyboardRow third = new KeyboardRow();
        KeyboardRow fourth = new KeyboardRow();
        KeyboardButton firstButton = new KeyboardButton();
        firstButton.setText("About Us");
        KeyboardButton secondButton = new KeyboardButton();
        secondButton.setText("Hot Vacancies \uD83D\uDD25");
        KeyboardButton thirdButton = new KeyboardButton();
        thirdButton.setText("Void Vacancies");
        KeyboardButton fourthButton = new KeyboardButton();
        fourthButton.setText("Language : O'zbekcha \uD83C\uDDFA\uD83C\uDDFF/ English\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F");
        first.add(firstButton);
        second.add(secondButton);
        third.add(thirdButton);
        fourth.add(fourthButton);
        rowList.add(first);
        rowList.add(second);
        rowList.add(third);
        rowList.add(fourth);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        step="1";
        return sendMessage;
    }

    private SendMessage about_startEn(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(BotQuery.EnText);
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> first = new ArrayList<>();
        InlineKeyboardButton firstButton = new InlineKeyboardButton();
        firstButton.setText("Our site");
        firstButton.setUrl("https://rabota.korzinka.uz/");
        first.add(firstButton);
        keyboard.add(first);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    private SendMessage about_startUz(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(BotQuery.UzText);
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> first = new ArrayList<>();
        InlineKeyboardButton firstButton = new InlineKeyboardButton();
        firstButton.setText("Bizni sayt");
        firstButton.setUrl("https://rabota.korzinka.uz/");
        first.add(firstButton);
        keyboard.add(first);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }


    private SendMessage stageVakansUz(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Keling Anketangizni yaratamiz ! \n Qaysi lavozimda ishlamoqchisiz");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        KeyboardRow first = new KeyboardRow();
        KeyboardRow second = new KeyboardRow();
        KeyboardRow third = new KeyboardRow();
        KeyboardButton but1 = new KeyboardButton();
        but1.setText("Yuk tashubvchi");
        KeyboardButton but2 = new KeyboardButton();
        but2.setText("Oshpaz");
        KeyboardButton but3 = new KeyboardButton();
        but3.setText("Qo'riqchi");
        KeyboardButton but4 = new KeyboardButton();
        but4.setText("Sotuvchi kassir");
        KeyboardButton backButton = new KeyboardButton();
        backButton.setText("Back");
        first.add(but1);
        first.add(but2);
        second.add(but3);
        second.add(but4);
        third.add(backButton);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(first);
        rowList.add(second);
        rowList.add(third);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }
    private SendMessage stageVakansEn(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("\n" +
                "Let's Create Your Questionnaire! \n In which position do you want to work");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        KeyboardRow first = new KeyboardRow();
        KeyboardRow second = new KeyboardRow();
        KeyboardRow third = new KeyboardRow();
        KeyboardButton but1 = new KeyboardButton();
        but1.setText("Shipper");
        KeyboardButton but2 = new KeyboardButton();
        but2.setText("Chef");
        KeyboardButton but3 = new KeyboardButton();
        but3.setText("Warden");
        KeyboardButton but4 = new KeyboardButton();
        but4.setText("Cashier");
        KeyboardButton backButton = new KeyboardButton();
        backButton.setText("Back");
        first.add(but1);
        first.add(but2);
        second.add(but3);
        second.add(but4);
        third.add(backButton);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(first);
        rowList.add(second);
        rowList.add(third);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    private SendMessage stageIshUz(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(BotQuery.ResumeText);
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        KeyboardRow first = new KeyboardRow();
        KeyboardRow second = new KeyboardRow();
        KeyboardButton first_car = new KeyboardButton();
        first_car.setText("\uD83D\uDED2 Eshonguzar");
        KeyboardButton backButton = new KeyboardButton();
        backButton.setText("Back");
        first.add(first_car);
        second.add(backButton);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(first);
        rowList.add(second);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setResizeKeyboard(true);

        return sendMessage;
    }
    private SendMessage stageIshEn(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(BotQuery.ResumeTextEn);
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        KeyboardRow first = new KeyboardRow();
        KeyboardRow second = new KeyboardRow();
        KeyboardButton first_car = new KeyboardButton();
        first_car.setText("\uD83D\uDED2Eshonguzar");
        KeyboardButton backButton = new KeyboardButton();
        backButton.setText("Back");
        first.add(first_car);
        second.add(backButton);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(first);
        rowList.add(second);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setResizeKeyboard(true);

        return sendMessage;
    }

    private SendMessage satgeLocation(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Please send Locatcion :)");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        KeyboardRow first = new KeyboardRow();
        KeyboardButton locationButton = new KeyboardButton();
        locationButton.setText("Location");
        locationButton.setRequestLocation(true);
        KeyboardButton backButton = new KeyboardButton();
        backButton.setText("Back");
        first.add(locationButton);
        first.add(backButton);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(first);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    private SendMessage stageEnd(Message message) {
        SendMessage sendMessage;
        sendMessage = stageBack(message);
        sendMessage.setText("Sizga Tez orada aloqaga chiqamiz");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        KeyboardRow first = new KeyboardRow();
        KeyboardButton backButton = new KeyboardButton();
        backButton.setText("Back");
        first.add(backButton);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(first);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;

    }

    private SendMessage stageContact(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Iltimos telefon nomeringizni jonating");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        KeyboardRow first = new KeyboardRow();
        KeyboardButton contactButton = new KeyboardButton();
        contactButton.setText("Contact");
        contactButton.setRequestContact(true);
        KeyboardButton backButton = new KeyboardButton();
        backButton.setText("Back");
        first.add(contactButton);
        first.add(backButton);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(first);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        step="5";
        return sendMessage;
    }


    private void sendTextWithImage(Long chatId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId.toString());
        sendPhoto.setPhoto(new InputFile("https://tse1.mm.bing.net/th?id=OIP.HkSN5GUhCDT4u6q-H1zWZAHaE8&pid=Api&P=0&h=220"));
        sendPhoto.setCaption("Приветствую Вас, я HR-бот Корзинки\n\n\uD83E\uDD16Я:\n- расскажу Вам о компании и о преимуществах работы у нас;\n- помогу найти актуальные вакансии и заполнить анкету.\n\nРабота в Корзинке все лучше и лучше\n________________________________________\n\nXush kelibsiz, men HR-bot Korzinka\n\n\uD83E\uDD16Men:\n- sizga kompaniya haqida va biz bilan ishlashning afzalliklari haqida gapirib beraman;\n- mavjud vakansiyalarni topishga va so'rovnomani to'ldirishga yordam beraman.\n\nKorzinkada odatdagidan yaxshi ish");
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage stageBack(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Assalomu Aleykum botga Hush kelibsiz \uD83D\uDE42\uD83D\uDC47");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendTextWithImage(message.getChatId());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow first = new KeyboardRow();
        KeyboardRow second = new KeyboardRow();
        KeyboardRow third = new KeyboardRow();
        KeyboardRow fourth = new KeyboardRow();
        KeyboardButton firstButton = new KeyboardButton();
        firstButton.setText("Biz haqimizda");
        KeyboardButton secondButton = new KeyboardButton();
        secondButton.setText("Qaynoq vakansiyalar \uD83D\uDD25");
        KeyboardButton thirdButton = new KeyboardButton();
        thirdButton.setText("Bo'sh ish o'rinlari");
        KeyboardButton fourthButton = new KeyboardButton();
        fourthButton.setText("Til: O'zbekcha \uD83C\uDDFA\uD83C\uDDFF/ English\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F");
        first.add(firstButton);
        second.add(secondButton);
        third.add(thirdButton);
        fourth.add(fourthButton);
        rowList.add(first);
        rowList.add(second);
        rowList.add(third);
        rowList.add(fourth);
        replyKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        step="1";
        return sendMessage;
    }


    public String getBotUsername() {
        return "@korzinko_bot";
    }

    public String getBotToken() {
        return "6855571978:AAFzWDjGDJU5QOSx_wpLcFD4-SNDtdoXtPM";
    }

}


// Til tanlash uchun ishlattim nu til tanlagandan keyin ichi ishlamotti

    /*private SendMessage language_start(Message message){
    SendMessage sendMessage = new SendMessage();
     sendMessage.setChatId(String.valueOf(message.getChatId()));
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<InlineKeyboardButton> td = new ArrayList<>();

    InlineKeyboardButton inlineKeyboardButtonUz = new InlineKeyboardButton();
    inlineKeyboardButtonUz.setText("En \uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F");
    inlineKeyboardButtonUz.setCallbackData(handleStart(message));

    InlineKeyboardButton inlineKeyboardButtonRU = new InlineKeyboardButton();
    inlineKeyboardButtonRU.setText("Ru \uD83C\uDDF7\uD83C\uDDFA");
    inlineKeyboardButtonRU.setCallbackData(BotQuery.Ru_select);

    td.add(inlineKeyboardButtonUz);
    td.add(inlineKeyboardButtonRU);
    List<List<InlineKeyboardButton>> tr = new ArrayList<>();
    tr.add(td);
    inlineKeyboardMarkup.setKeyboard(tr);
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    return sendMessage;
}*/

// buu til tanlagandan keyin kk edi nu ishlatolmadim shekilli
       /* else if (update.hasCallbackQuery()) {
            String data=update.getCallbackQuery().getData();
           // String chatId=update.getCallbackQuery().getFrom().getId().toString();
            if (data.equals(BotQuery.En_select)){
            returnMessage.setText("pasha");
            } else if (data.equals(BotQuery.Ru_select)) {

            }

        }*/


/*



InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

// Uzbek tili tugmasi
List<InlineKeyboardButton> uzbekButtonRow = new ArrayList<>();
InlineKeyboardButton uzbekButton = new InlineKeyboardButton();
        uzbekButton.setText("O'zbekcha");
        uzbekButton.setCallbackData("uzbek");
        uzbekButtonRow.add(uzbekButton);
        keyboard.add(uzbekButtonRow);

// English tili tugmasi
List<InlineKeyboardButton> englishButtonRow = new ArrayList<>();
InlineKeyboardButton englishButton = new InlineKeyboardButton();
        englishButton.setText("English");
        englishButton.setCallbackData("english");
        englishButtonRow.add(englishButton);
        keyboard.add(englishButtonRow);

        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

*/
