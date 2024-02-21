package org.example.Bot;

import com.pengrad.telegrambot.model.request.*;
import org.example.entitiy.TelegramUser;


public class BotUtils {
    public static Keyboard generateContactButton() {

        return new ReplyKeyboardMarkup(new KeyboardButton("Kontakt yuborish").requestContact(true)).resizeKeyboard(true);

    }

    public static InlineKeyboardMarkup generateCounterButtons(TelegramUser telegramUser) {
           return new InlineKeyboardMarkup(
            new InlineKeyboardButton("+").callbackData(BotConstant.PLUS),
            new InlineKeyboardButton(telegramUser.getCount().toString()).callbackData(BotConstant.NUMBER),
            new InlineKeyboardButton("-").callbackData(BotConstant.MINUS)).addRow(
                    new InlineKeyboardButton("Savat").callbackData(BotConstant.BASKET),
                    new InlineKeyboardButton("Orqaga").callbackData(BotConstant.BACK));
    }


    public static Keyboard generateBascketButton() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton(BotConstant.BACK),
                new KeyboardButton(BotConstant.GENERATE_ORDER),
                new KeyboardButton(BotConstant.CLEAR_BASKET)
        ).resizeKeyboard(true);

    }
}

