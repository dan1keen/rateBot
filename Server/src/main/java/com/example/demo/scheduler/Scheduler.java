package com.example.demo.scheduler;

import com.example.demo.DemoApplication;
import com.example.demo.model.Item;
import com.example.demo.service.ItemServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;


@Component
public class Scheduler {
    private final ItemServiceImpl itemService;

    @Autowired
    public Scheduler(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }
    @PostConstruct
    public void onStart() {

        readFromXmlFile();

    }
    //Метод запускается каждые 10 минут
    @Scheduled(cron = "0 0/10 * * * *")
    public void loadEveryTenMinutes() {

        readFromXmlFile();

    }

    public void readFromXmlFile(){
        ObjectMapper mapper = new ObjectMapper();
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ctx.init(new KeyManager[0], new TrustManager[] {new DemoApplication.DefaultTrustManager()}, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLContext.setDefault(ctx);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = db.parse(new URL("https://nationalbank.kz/rss/rates.xml").openStream());

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            Item item = new Item();

            NodeList nodes = doc.getElementsByTagName("channel");
            Element element = (Element) nodes.item(0);
            NodeList nodeList = element.getElementsByTagName("item");
            Element nodeElement = (Element) nodeList.item(2);
            NodeList description = nodeElement.getElementsByTagName("description");
            NodeList name = nodeElement.getElementsByTagName("title");
            NodeList change = nodeElement.getElementsByTagName("change");
            NodeList pubDate = nodeElement.getElementsByTagName("pubDate");
//            System.out.println(description.item(0).getFirstChild().getTextContent());

            item.setDescr(description.item(0).getFirstChild().getTextContent());
            item.setName(name.item(0).getFirstChild().getTextContent());
            item.setChange(change.item(0).getFirstChild().getTextContent());
            item.setPubDate(pubDate.item(0).getFirstChild().getTextContent());
            Item last = itemService.getLastByName(item.getName());


        //----------------------------------------------------------------------------------------------------------
            Item itemRUR = new Item();
            Element nodeElementRUR = (Element) nodeList.item(0);
            NodeList descriptionRUR = nodeElementRUR.getElementsByTagName("description");
            NodeList nameRUR = nodeElementRUR.getElementsByTagName("title");
            NodeList changeRUR = nodeElementRUR.getElementsByTagName("change");
            NodeList pubDateRUR = nodeElementRUR.getElementsByTagName("pubDate");
    //            System.out.println(description.item(0).getFirstChild().getTextContent());

            itemRUR.setDescr(descriptionRUR.item(0).getFirstChild().getTextContent());
            itemRUR.setName(nameRUR.item(0).getFirstChild().getTextContent());
            itemRUR.setChange(changeRUR.item(0).getFirstChild().getTextContent());
            itemRUR.setPubDate(pubDateRUR.item(0).getFirstChild().getTextContent());
            Item lastRUR = itemService.getLastByName(itemRUR.getName());

        //----------------------------------------------------------------------------------------------------------
            Item itemEUR = new Item();
            Element nodeElementEUR = (Element) nodeList.item(1);
            NodeList descriptionEUR = nodeElementEUR.getElementsByTagName("description");
            NodeList nameEUR = nodeElementEUR.getElementsByTagName("title");
            NodeList changeEUR = nodeElementEUR.getElementsByTagName("change");
            NodeList pubDateEUR = nodeElementEUR.getElementsByTagName("pubDate");
            //            System.out.println(description.item(0).getFirstChild().getTextContent());

            itemEUR.setDescr(descriptionEUR.item(0).getFirstChild().getTextContent());
            itemEUR.setName(nameEUR.item(0).getFirstChild().getTextContent());
            itemEUR.setChange(changeEUR.item(0).getFirstChild().getTextContent());
            itemEUR.setPubDate(pubDateEUR.item(0).getFirstChild().getTextContent());
            System.out.println("TESTIM "+itemEUR.getDescr());
            Item lastEUR = itemService.getLastByName(itemEUR.getName());



            if (itemService.exists() > 0) {
                if (!itemEUR.getDescr().equals(lastEUR.getDescr())) {
                    itemService.save(itemEUR);
                    System.out.println("EURO saved Value is: " + itemEUR.getDescr());
                    System.out.println("Published date is: " + itemEUR.getPubDate());
                    //Иначе просто говорим что последнее изменение такое же что мы вытащили из json файла
                } else {
                    System.out.println("EURO value is the same! Value is: " + itemEUR.getDescr());
                    System.out.println(itemEUR.getDescr());
                }
                if (!itemRUR.getDescr().equals(lastRUR.getDescr())) {
                    itemService.save(itemRUR);
                    System.out.println("RUBLE saved Value is: " + itemRUR.getDescr());
                    System.out.println("Published date is: " + itemRUR.getPubDate());
                    //Иначе просто говорим что последнее изменение такое же что мы вытащили из json файла
                } else {
                    System.out.println("RUBLE value is the same! Value is: " + itemRUR.getDescr());
                    System.out.println(itemRUR.getDescr());
                }
                if (!item.getDescr().equals(last.getDescr())) {
                    itemService.save(item);
                    System.out.println("DOLLAR saved Value is: " + item.getDescr());
                    System.out.println("Published date is: " + item.getPubDate());
                    //Иначе просто говорим что последнее изменение такое же что мы вытащили из json файла
                } else {
                    System.out.println("DOLLAR value is the same! Value is: " + item.getDescr());
                    System.out.println(item.getDescr());
                }
            }  else {
                itemService.save(itemEUR);
                itemService.save(itemRUR);
                itemService.save(item);
                System.out.println("EURO saved Value is: " + itemEUR.getDescr());
                System.out.println("Published date is: " + itemEUR.getPubDate());
                System.out.println("RUBLE saved Value is: " + itemRUR.getDescr());
                System.out.println("Published date is: " + itemRUR.getPubDate());
                System.out.println("DOLLAR saved Value is: " + item.getDescr());
                System.out.println("Published date is: " + item.getPubDate());
            }



    }

}
