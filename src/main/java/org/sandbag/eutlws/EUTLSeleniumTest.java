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

            BufferedWriter outBuff = new BufferedWriter(new FileWriter(outputFile));

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

                    // Now submit the form. WebDriver will find the form for us from the element
                    elementToBeClicked.click();

                    Thread.sleep(1000);

                    WebElement table_element = driver.findElement(By.id("tblNapList"));

                    //-----------------------TABLE ELEMENT-------------------------------------------------------------

                    while(table_element != null){
                        List<WebElement> tr_collection=table_element.findElements(By.xpath("id('tblNapList')/tbody/tr"));

                        System.out.println("NUMBER OF ROWS IN THIS TABLE = "+tr_collection.size());

                        int row_num,col_num;
                        row_num=1;

                        for(WebElement trElement : tr_collection)
                        {
                            List<WebElement> td_collection=trElement.findElements(By.xpath("td"));
                            System.out.println("NUMBER OF COLUMNS="+td_collection.size());
                            col_num=1;
                            for(int counter=0; counter< td_collection.size()-1;counter++)
                            {

                                WebElement tdElement = td_collection.get(counter);

                                if(period == 0){

                                }else if(period == 1){

                                }else if(period == 2){

                                }


                                //System.out.println("row # "+row_num+", col # "+col_num+ "text="+tdElement.getText());
                                col_num++;
                            }
                            row_num++;
                        }


                        //----------------NEXT PAGE OF RESULTS BUTTON-----------------------
                        WebElement nextButton = driver.findElement(By.name("nextList"));
                        if(nextButton != null){
                            nextButton.click();
                            table_element = driver.findElement(By.id("tblNapList"));
                        }

                    }

                    //-----------------------------------------------------------------------------------------

                }
            }

            //Close the browser
            driver.quit();

            //Close output file
            outBuff.close();
        }





    }
}
