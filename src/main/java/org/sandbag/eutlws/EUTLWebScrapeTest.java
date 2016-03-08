package org.sandbag.eutlws;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by root on 07/03/16.
 */
public class EUTLWebScrapeTest {

    public static void main(String[] args){
        Document doc = null;
        try {
            doc = Jsoup.connect("http://ec.europa.eu/environment/ets/napMgt.do").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String title = doc.title();
        System.out.println("title = " + title);
    }
}
