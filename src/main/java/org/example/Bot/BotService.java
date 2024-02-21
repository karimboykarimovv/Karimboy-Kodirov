package org.example.Bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.SneakyThrows;
import org.example.db.DB;
import org.example.entitiy.Category;
import org.example.entitiy.Product;
import org.example.entitiy.TelegramUser;
import org.example.entitiy.enums.TelegramState;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class BotService {
   public static TelegramBot telegramBot = new TelegramBot("6431851457:AAFw3RfZecRAemS7a6SSG7JONFgsjnFizl4");

    public static void acceptStartAskContact(TelegramUser telegramUser) {
        SendMessage sendMessage = new SendMessage(
                telegramUser.getChatId(),
                "Assalamu aleykum Botga xush kelipsiz. Botdan to'liq foydalanish uchun kontakt yuborish tugmasini bosing ");
                 telegramUser.setTelegramState(TelegramState.SHERE_CONTACT);
                sendMessage.replyMarkup(BotUtils.generateContactButton());
                telegramBot.execute(sendMessage);
    }

    public static void acceptContactShowCatigories(TelegramUser telegramUser, Message message) {
        Contact contact = message.contact();
        telegramUser.setPhone(contact.phoneNumber());
        telegramUser.setTelegramState(TelegramState.SELECT_CATEGORY);
        showCatigories(telegramUser,null);
    }
    private static void showCatigories(TelegramUser telegramUser, UUID perentId) {
        List<Category> foundCategories = new ArrayList<>();
     for (Category category : DB.CATEGORIES){
         if (Objects.equals(category.getParentId(),perentId)){
                foundCategories.add(category);
         }
     }
     if (foundCategories.isEmpty()) {
           showProdacts(telegramUser,perentId);
     }else {
         ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup("Savat");
         int count = foundCategories.size();
         int rowCount = (count+1)/2;
         int j = 0;
         for (int i = 0; i <rowCount; i++) {
             if (i == rowCount - 1 && count % 2 != 0) {
                 replyKeyboardMarkup.addRow(foundCategories.get(j).getName()).resizeKeyboard(true);
             }else {
                 replyKeyboardMarkup.addRow(foundCategories.get(j).getName(),foundCategories.get(j+1).getName()).resizeKeyboard(true);

             }
             j += 2;
         }
         if (perentId != null) {
             replyKeyboardMarkup.addRow(BotConstant.BACK);
         }
         SendMessage sendMessage = new SendMessage(
                 telegramUser.getChatId(),
                 "Category tanlang");
         sendMessage.replyMarkup(replyKeyboardMarkup);
     telegramUser.setTelegramState(TelegramState.SELECT_CATEGORY);
         telegramBot.execute(sendMessage);
     }
    }

    private static void showProdacts(TelegramUser telegramUser, UUID categoryId) {
        List<Product> foundProdacts = new ArrayList<>();
        for (Product product : DB.PRODUCTS) {
            if (product.getCategoryId() == categoryId) {
                foundProdacts.add(product);
            }
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(BotConstant.BASKET);
        int count = foundProdacts.size();
        int rowCount = (count+1)/2;
        int j = 0;
        for (int i = 0; i <rowCount; i++) {
            if (i == rowCount - 1 && count % 2 != 0) {
                replyKeyboardMarkup.addRow(foundProdacts.get(j).getName()).resizeKeyboard(true);
            }else {
                replyKeyboardMarkup.addRow(foundProdacts.get(j).getName(),foundProdacts.get(j+1).getName()).resizeKeyboard(true);

            }
            j += 2;
        }
        replyKeyboardMarkup.addRow(BotConstant.BACK);
        SendMessage sendMessage = new SendMessage(
                telegramUser.getChatId(),
                "Prodact tanlang");
        sendMessage.replyMarkup(replyKeyboardMarkup);
        telegramUser.setTelegramState(TelegramState.SELECT_PRODACT);
        SendResponse response = telegramBot.execute(sendMessage);
        telegramUser.addToDeleting(response);
    }
    public static void acceptCategoryChildren(TelegramUser telegramUser, Message message) {
        String text = message.text();
        for (Category category : DB.CATEGORIES) {
            if (category.getName().equals(text)) {
                telegramUser.setSelectedCategoryId(category.getId());
                showCatigories(telegramUser,category.getId());
                return;
            }
        }
        SendMessage sendMessage = new SendMessage(
                telegramUser.getChatId(),
                "Noto'g'ri buyuruq kiritildi");
        telegramBot.execute(sendMessage);
    }

    public static void acceptProdactSendProdactInfo(TelegramUser telegramUser, Message message) {
        String text = message.text();
        for (Product product : DB.PRODUCTS) {
            if (product.getName().equals(text)) {
                showProdactsInfo(telegramUser,product);
                return;
            }
        }
        SendMessage sendMessage = new SendMessage(
                telegramUser.getChatId(),
                "Noto'g'ri buyuruq kiritildi");
        telegramBot.execute(sendMessage);

    }

    @SneakyThrows
    private static void showProdactsInfo(TelegramUser telegramUser, Product product) {
        telegramUser.clearMessages();
        SendPhoto sendPhoto = new SendPhoto(
                telegramUser.getChatId(),
                Files.readAllBytes(Path.of(product.getPath())));
        sendPhoto.caption(product.getName().toString()+" "+product.getPrice()+" so'm");
        telegramUser.setSelectedProdactId(product.getId());
        sendPhoto.replyMarkup(BotUtils.generateCounterButtons(telegramUser));
        telegramUser.setTelegramState(TelegramState.ADD_TO_BACKED);
        SendResponse response = telegramBot.execute(sendPhoto);
        telegramUser.setEditingMessageId(response.message().messageId());
        telegramUser.addToDeleting(response);

    }

    public static void acceptPlus(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        telegramUser.setCount(telegramUser.getCount()+1);
      var e = new EditMessageReplyMarkup(telegramUser.getChatId(),telegramUser.getEditingMessageId());
      e.replyMarkup(BotUtils.generateCounterButtons(telegramUser));
      telegramBot.execute(e);
    }

    public static void acceptMinus(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        if (telegramUser.getCount() >= 1){
            telegramUser.setCount(telegramUser.getCount()-1);
            var e = new EditMessageReplyMarkup(telegramUser.getChatId(),telegramUser.getEditingMessageId());
            e.replyMarkup(BotUtils.generateCounterButtons(telegramUser));
            telegramBot.execute(e);
        }
    }
    public static void goBack(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        telegramUser.clearMessages();
        telegramUser.setTelegramState(TelegramState.SELECT_PRODACT);
        telegramUser.setCount(1);
        Product product = DB.findProductById(telegramUser.getSelectedProdactId());
            showProdacts(telegramUser,product.getCategoryId());
    }

    public static void addBasket(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        telegramUser.clearMessages();
     telegramUser.getBasket().put(
       DB.findProductById(telegramUser.getSelectedProdactId()),
             telegramUser.getCount());
     SendMessage sendMessage = new SendMessage(
             telegramUser.getChatId(),
             "Savatga  qo'shildi"
     );
     telegramBot.execute(sendMessage);
     back(telegramUser);
    }
    public static void back(TelegramUser telegramUser) {
        Category category = DB.findCategoryById(telegramUser.getSelectedCategoryId());
            showCatigories(telegramUser,category.getParentId());
            telegramUser.setSelectedCategoryId(category.getParentId());
            telegramUser.setTelegramState(TelegramState.SELECT_CATEGORY);
    }

    public static void showBasket(TelegramUser telegramUser) {
        Map<Product, Integer> basket = telegramUser.getBasket();
        StringJoiner stringJoiner = new StringJoiner("\n ","Mahsulotlar: \n ","\n Umumiy summa:"+telegramUser.basketPrice());
        basket.forEach((product,value)->{
          stringJoiner.add(product.getName()+" "+product.getPrice()+" so'mdan "+ value+" ta");
        });
      SendMessage sendMessage = new SendMessage(
              telegramUser.getChatId(),
              stringJoiner.toString());
      sendMessage.replyMarkup(BotUtils.generateBascketButton());
      telegramUser.setTelegramState(TelegramState.BASKET_INSIDE);
      telegramBot.execute(sendMessage);
    }

    public static void clearBasket(TelegramUser telegramUser) {
        telegramUser.getBasket().clear();
        back(telegramUser);
    }

    public static void generateOrder(TelegramUser telegramUser) {
        telegramUser.getBasket().clear();
        SendMessage sendMessage = new SendMessage(
                telegramUser.getChatId(),
                "Buyurmangiz qobul qilindi"
        );
        telegramBot.execute(sendMessage);
        telegramUser.setTelegramState(TelegramState.SELECT_CATEGORY);
        showCatigories(telegramUser,null);
    }
}
