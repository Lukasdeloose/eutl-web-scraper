package org.sandbag.eutlws;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by root on 07/03/16.
 */
public class EUTLSeleniumTest {

    public static String[] countriesArray = {"AT","BE","HR","CY","CZ","DK","EE","EU","FI","FR","DE","GR","HU",
                                            "IS","IE","IT","LV","LI","LT","LU","MT","NL","NO","PL","PT","RO",
                                            "SK","SI","ES","SE","GB"};
    public static String PERIOD_0_HEADER = "Country\tInstallation ID\tInstallation Name\tAddress City\tAccount Holder Name\tAccount Status\tPermit ID\tLatest Update\t2005\t2006\t2007\tStatus";
    public static String PERIOD_1_HEADER = "Country\tInstallation ID\tInstallation Name\tAddress City\tAccount Holder Name\tAccount Status\tPermit ID\tLatest Update\t2008\t2009\t2010\t2011\t2012\tStatus";
    public static String PERIOD_2_HEADER = "Country\tInstallation ID\tInstallation Name\tAddress City\tAccount Holder Name\tAccount Status\tPermit ID\tLatest Update\t2013\t2014\t2015\t2016\t2017\t2018\t2019\t2020\tStatus";




    public static void main(String[] args) throws Exception {


        if(args.length != 3){
            System.out.println("This program expects the following parameters: " +
                    "1. Output CSV file name period 0 \n" +
                    "2. Output CSV file name period 1 \n" +
                    "3. Output CSV file name period 2");
        }else{


            File outputFilePeriod0 = new File(args[0]);
            File outputFilePeriod1 = new File(args[1]);
            File outputFilePeriod2 = new File(args[2]);

            // Create a new instance of the Firefox driver
            // Notice that the remainder of the code relies on the interface,
            // not the implementation.
            WebDriver driver = new FirefoxDriver();

            BufferedWriter outBuffPeriod0 = new BufferedWriter(new FileWriter(outputFilePeriod0));
            outBuffPeriod0.write(PERIOD_0_HEADER + "\n");
            BufferedWriter outBuffPeriod1 = new BufferedWriter(new FileWriter(outputFilePeriod1));
            outBuffPeriod1.write(PERIOD_1_HEADER + "\n");
            BufferedWriter outBuffPeriod2 = new BufferedWriter(new FileWriter(outputFilePeriod2));
            outBuffPeriod2.write(PERIOD_2_HEADER + "\n");


            for(String country : countriesArray){
                for(int period=0; period<=2; period++){
                    // And now use this to visit Google
                    driver.get("http://ec.europa.eu/environment/ets/nap.do?languageCode=en&nap.registryCodeArray="+
                            country +"&periodCode=" + period + "&search=Search&currentSortSettings=");


                    List<WebElement> tempElements = driver.findElements(By.id("lnkNapInformation"));
                    WebElement elementToBeClicked = null;

                    for(WebElement element : tempElements){
                        //System.out.println("element = " + element);
                        String tempHrefl = element.getAttribute("href");
                        System.out.println("tempHrefl = " + tempHrefl);

                        WebElement spanElement = element.findElement(By.tagName("span"));
                        String spanText = spanElement.getText();
                        System.out.println("spanText = " + spanText);
                        if(spanText.equals("Installations linked to this Allocation Table")){
                            elementToBeClicked = element;
                        }
                    }

                    if(elementToBeClicked != null){
                        // Now submit the form. WebDriver will find the form for us from the element
                        elementToBeClicked.click();

                        Thread.sleep(1000);

                        WebElement table_element = driver.findElement(By.id("tblNapList"));

                        //-----------------------TABLE ELEMENT-------------------------------------------------------------

                        int pageNumber = 0;

                        while(table_element != null){

                            System.out.println("Country: " + country + " Period: " + period + " Page: " + pageNumber);

                            List<WebElement> tr_collection=table_element.findElements(By.xpath("id('tblNapList')/tbody/tr"));

                            if(period == 0){
                                outBuffPeriod0.write(country + "\t");

                                for(int rowCounter=3; rowCounter<tr_collection.size(); rowCounter++)
                                {

                                    WebElement trElement = tr_collection.get(rowCounter);

                                    List<WebElement> td_collection=trElement.findElements(By.xpath("td"));

                                    for(int columnCounter=0; columnCounter< td_collection.size()-1;columnCounter++)
                                    {

                                        WebElement tdElement = td_collection.get(columnCounter);

                                        outBuffPeriod0.write(tdElement.getText().replaceAll("\n"," "));

                                        if(columnCounter == 10){
                                            outBuffPeriod0.write("\n");
                                        }else{
                                            outBuffPeriod0.write("\t");
                                        }

                                    }

                                    System.out.println("Row: " + rowCounter + " completed");
                                }

                            }else if(period == 1){
                                outBuffPeriod1.write(country + "\t");

                                for(int rowCounter=3; rowCounter<tr_collection.size(); rowCounter++)
                                {

                                    WebElement trElement = tr_collection.get(rowCounter);

                                    List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
                                    //System.out.println("NUMBER OF COLUMNS="+td_collection.size());



                                    for(int columnCounter=0; columnCounter< td_collection.size()-1;columnCounter++)
                                    {

                                        WebElement tdElement = td_collection.get(columnCounter);

                                        outBuffPeriod1.write(tdElement.getText().replaceAll("\n"," "));
                                        if(columnCounter == 12){
                                            outBuffPeriod1.write("\n");
                                        }else{
                                            outBuffPeriod1.write("\t");
                                        }

                                    }

                                    System.out.println("Row: " + rowCounter + " completed");
                                }
                            }else if(period == 2){
                                outBuffPeriod2.write(country + "\t");

                                for(int rowCounter=3; rowCounter<(tr_collection.size()-3); rowCounter++)
                                {

                                    WebElement trElement = tr_collection.get(rowCounter);

                                    List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
                                    //System.out.println("NUMBER OF COLUMNS="+td_collection.size());

                                    for(int columnCounter=0; columnCounter< td_collection.size()-1;columnCounter++)
                                    {

                                        WebElement tdElement = td_collection.get(columnCounter);

                                        outBuffPeriod2.write(tdElement.getText().replaceAll("\n"," "));
                                        if(columnCounter == 15){
                                            outBuffPeriod2.write("\n");
                                        }else{
                                            outBuffPeriod2.write("\t");
                                        }

                                    }

                                    System.out.println("Row: " + rowCounter + " completed");
                                }
                            }




                            //----------------NEXT PAGE OF RESULTS BUTTON-----------------------
                            WebElement nextButton = driver.findElement(By.name("nextList"));
                            if(nextButton.getAttribute("disabled") == null){
                                nextButton.click();
                                table_element = driver.findElement(By.id("tblNapList"));
                            }else{
                                table_element = null;
                            }

                            pageNumber++;

                        }

                        outBuffPeriod0.flush();
                        outBuffPeriod1.flush();
                        outBuffPeriod2.flush();

                        //-----------------------------------------------------------------------------------------
                    }

                }
            }

            //Close the browser
            driver.quit();

            //Close output files
            outBuffPeriod0.close();
            outBuffPeriod1.close();
            outBuffPeriod2.close();
        }





    }

}
