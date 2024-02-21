package org.example.Bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.example.db.DB;
import org.example.entitiy.TelegramUser;
import org.example.entitiy.enums.TelegramState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyBot {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    public void start(){
        BotService.telegramBot.setUpdatesListener(updates->{
            for (Update update : updates){
                executorService.execute(()->{
                    try {
                        handleUpdate(update);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleUpdate(Update update) {
      if (update.message() != null) {
          Message message = update.message();
          TelegramUser telegramUser = getUser(message.chat().id());
          if (message.text() != null) {
              if (message.text().equals("/start")) {
                  System.out.println(telegramUser.getPhone());
                  BotService.acceptStartAskContact(telegramUser);
              } else {
                  if (message.text().equals(BotConstant.BASKET)){
                      BotService.showBasket(telegramUser);
                  }
                  if (telegramUser.getTelegramState().equals(TelegramState.SELECT_CATEGORY)) {
                      if (message.text().equals(BotConstant.BACK)){
                          BotService.back(telegramUser);
                      }else {
                          BotService.acceptCategoryChildren(telegramUser, message);
                      }
                  } else if (telegramUser.getTelegramState().equals(TelegramState.SELECT_PRODACT)) {
                      if (message.text().equals(BotConstant.BACK)){
                          BotService.back(telegramUser);
                      }else {
                          BotService.acceptProdactSendProdactInfo(telegramUser, message);
                      }
                  }else if (telegramUser.getTelegramState().equals(TelegramState.BASKET_INSIDE)){
                      if (message.text().equals(BotConstant.BACK)){
                          BotService.back(telegramUser);
                      }else if (message.text().equals(BotConstant.CLEAR_BASKET)){
                          BotService.clearBasket(telegramUser);
                      }else if(message.text().equals(BotConstant.GENERATE_ORDER)){
                          BotService.generateOrder(telegramUser );
                      }
                  }
              }
              }else if (message.contact() != null) {
              if (telegramUser.getTelegramState().equals(TelegramState.SHERE_CONTACT)) {
                  BotService.acceptContactShowCatigories(telegramUser, message);
              }
          }
      }else if(update.callbackQuery() != null) {
          CallbackQuery callbackQuery = update.callbackQuery();
           TelegramUser telegramUser = getUser(callbackQuery.from().id());
           if (telegramUser.getTelegramState().equals(TelegramState.ADD_TO_BACKED)){
               if (callbackQuery.data().equals(BotConstant.PLUS)){
                   BotService.acceptPlus(telegramUser,callbackQuery);
               } else if (callbackQuery.data().equals(BotConstant.MINUS)) {
                   BotService.acceptMinus(telegramUser,callbackQuery);
               } else if (callbackQuery.data().equals(BotConstant.BACK)) {
                   BotService.goBack(telegramUser,callbackQuery);
               }else if (callbackQuery.data().equals(BotConstant.BASKET)) {
                   BotService.addBasket(telegramUser,callbackQuery);
               }
           }
      }else if(update.inlineQuery() != null) {

      }

    }

    private TelegramUser getUser(Long id) {
        for (TelegramUser telegramUser : DB.TELEGRAM_USERS) {
            if (telegramUser.getChatId().equals(id)) {
                return telegramUser;
            }
        }
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(id);
        DB.TELEGRAM_USERS.add(telegramUser);
        return telegramUser;
    }

}
