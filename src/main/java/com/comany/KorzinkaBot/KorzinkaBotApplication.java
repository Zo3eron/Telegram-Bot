package com.comany.KorzinkaBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class KorzinkaBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(KorzinkaBotApplication.class, args);
    }
}
