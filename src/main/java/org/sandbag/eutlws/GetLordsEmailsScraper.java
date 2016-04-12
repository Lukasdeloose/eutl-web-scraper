package org.sandbag.eutlws;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by root on 11/04/16.
 */
public class GetLordsEmailsScraper {

    public static void main(String[] args) throws Exception {

        File file = new File("LordsEmails.csv");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        writer.write("Name,Email\n");

        // Create a new instance of the Firefox driver
        WebDriver driver = new HtmlUnitDriver();



        driver.close();

        writer.close();


    }
}
