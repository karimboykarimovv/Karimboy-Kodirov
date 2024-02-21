package org.example.entitiy;

import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Bot.BotService;
import org.example.entitiy.enums.TelegramState;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser {

    private Long chatId;
    private String phone;
    private Integer count = 1;
    private UUID selectedProdactId;
    private UUID selectedCategoryId;
    private Integer editingMessageId;
    private TelegramState telegramState = TelegramState.START;
    private List<Integer> deletingMessages = new ArrayList<>();
    private Map<Product , Integer> basket = new  LinkedHashMap<>();
    public void addToDeleting(SendResponse response) {
        addToDeleting(response.message().messageId());
    }
    public void addToDeleting(Integer messageId) {
        deletingMessages.add(messageId);
    }
   public void clearMessages(){
        for (Integer deletingMessage : deletingMessages){
            DeleteMessage deleteMessage = new DeleteMessage(
                    chatId,deletingMessage);
            BotService.telegramBot.execute(deleteMessage);
        }
        deletingMessages.clear();
    }

    public Integer basketPrice() {
        Set<Map.Entry<Product, Integer>> entries = basket.entrySet();
        int sum = 0;
        for (Map.Entry<Product, Integer> entry : entries) {
            Product product = entry.getKey();
            Integer count = entry.getValue();
            sum += product.getPrice() * count;
        }
        return sum;
    }
}
