import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import javax.xml.parsers.DocumentBuilderFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class CurrencyBot extends TelegramLongPollingBot {
    SSLContext ctx = SSLContext.getInstance("TLS");
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    public CurrencyBot() throws NoSuchAlgorithmException, KeyManagementException {
        ctx.init(new KeyManager[0], new TrustManager[] {new MainClass.DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(ctx);
    }
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private String description ="";
    private String title ="";
    private String change ="";
    private String pubDate ="";
    public void onUpdateReceived(Update update) {


        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/rates?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
                stmt = conn.createStatement();
                // Set variables
                String message_text = update.getMessage().getText();
                long chat_id = update.getMessage().getChatId();
                if (message_text.equals("menu")) {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText("Выберите курс валют");

                    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                    List<KeyboardRow> keyboard = new ArrayList<>();
                    KeyboardRow row = new KeyboardRow();


                    // Set each button, you can also use KeyboardButton objects if you need something else than text
                    row.add("USD");
                    row.add("EUR");
                    row.add("RUR");


                    row = new KeyboardRow();
                    row.add("hide");
                    keyboard.add(row);
                    keyboardMarkup.setKeyboard(keyboard);

                    message.setReplyMarkup(keyboardMarkup);

                    try {
                        execute(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (message_text.equals("USD")) {

                        rs = stmt.executeQuery("SELECT * FROM item WHERE item_name = 'USD' ORDER BY item_id DESC LIMIT 1");
                        while (rs.next()) {
                            title = rs.getString("item_name");
                            description = rs.getString("item_descr");
                            change = rs.getString("item_change");
                            pubDate = rs.getString("item_published");
                        }
                        SendMessage message = new SendMessage() // Create a message object object
                                .setChatId(chat_id)
                                .setText("1 Доллар (" +
                                        title + ") равeн " +
                                        description + " Тенге (KZT) \n" +
                                        "Изменения: " + change + "\n" +
                                        "Национальный Банк Казахстана " + "\n" +
                                        pubDate);


                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }



                } else if (message_text.equals("EUR")) {
                    rs = stmt.executeQuery("SELECT * FROM item WHERE item_name = 'EUR' ORDER BY item_id DESC LIMIT 1");
                    while (rs.next()) {
                        title = rs.getString("item_name");
                        description = rs.getString("item_descr");
                        change = rs.getString("item_change");
                        pubDate = rs.getString("item_published");
                    }
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText("1 евро (" +
                                    title + ") равeн " +
                                    description + " Тенге (KZT) \n" +
                                    "Изменения: " + change + "\n" +
                                    "Национальный Банк Казахстана " + "\n" +
                                    pubDate);


                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                } else if (message_text.equals("RUR")) {
                     rs =  stmt.executeQuery("SELECT * FROM item WHERE item_name = 'RUR' ORDER BY item_id DESC LIMIT 1");
                    while (rs.next()) {
                        title = rs.getString("item_name");
                        description = rs.getString("item_descr");
                        change = rs.getString("item_change");
                        pubDate = rs.getString("item_published");
                    }
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText("1 рубль (" +
                                    title + ") равeн " +
                                    description + " Тенге (KZT) \n" +
                                    "Изменения: " + change + "\n" +
                                    "Национальный Банк Казахстана " + "\n" +
                                    pubDate);
//                    ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
//                    message.setReplyMarkup(keyboardMarkup);

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                } else if(message_text.equals("hide")){
                    SendMessage message = new SendMessage().setChatId(chat_id).setText("Меню Закрылось");
                    ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                    message.setReplyMarkup(keyboardMarkup);
                    try {
                        execute(message); // Call method to send the photo
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }


                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }





    public String getBotUsername() {
        return "currency_bot";
    }

    public String getBotToken() {
        return "773777009:AAGMOpAw93kRr1IgwPM0hFdWDZOAItxBtj8";
    }


}
