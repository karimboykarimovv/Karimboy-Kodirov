package org.example;
import org.example.Bot.MyBot;
import org.example.db.DB;

public class Main {
    static {
        DB.generate();
    }
    public static void main(String[] args) {
        MyBot myBot = new MyBot();
        myBot.start();
    }
}