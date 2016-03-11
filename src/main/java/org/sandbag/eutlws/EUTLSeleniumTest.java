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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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

    public static final String INSTALLATIONS_HEADER = "Country\tType\tAccount Holder Name\tID\tCompany Registration Number\tStatus\tType\tName\tMain Address\tSecondary Address\tPostal Code\tCity\tCountry\tInstallation name\tPermit ID\tPermit Entry Date\tPermit Expiry/Revocation Date\tSubsidiary Company\tParent Company\tE-PRTR identification";
    public static final String AIRCRAFT_OPERATORS_HEADER = "Country\tType\tAccount Holder Name\tID\tCompany Registration Number\tStatus\tType\tName\tMain Address\tSecondary Address\tPostal Code\tCity\tCountry\tUnique Code under Commission Regulation\tMonitoring Plan ID\tMonitoring plan first year of applicability\tMonitoring plan year of expiry\tSubsidiary Company\tParent Company\tE-PRTR identification\tICAO designator";

    public static final String ALLOWANCES_IN_ALLOCATION_TYPE = "Allowances in Allocation";
    public static final String VERIFIED_EMISSIONS_TYPE = "Verified Emissions";
    public static final String UNITS_SURRENDERED_TYPE = "Units Surrendered";
    public static final String CUMULATIVE_UNITS_SURRENDERED_TYPE = "Cumulative Surrendered Emmissions";
    public static final String CUMULATIVE_VERIFIED_EMISSIONS_TYPE = "Cumulative Verified Emissions";
    public static final String COMPLIANCE_CODE_TYPE = "Compliance Code";

    public static final String INSTALLATIONS_COMPLIANCE_DATA_HEADER = "Country\tInstallation ID\tYear\t" +
            ALLOWANCES_IN_ALLOCATION_TYPE + "\t" + VERIFIED_EMISSIONS_TYPE + "\t" + UNITS_SURRENDERED_TYPE + "\t" +
            CUMULATIVE_UNITS_SURRENDERED_TYPE + "\t" + CUMULATIVE_VERIFIED_EMISSIONS_TYPE + "\t" +
            COMPLIANCE_CODE_TYPE + "\tValue";

    public static void main(String[] args) throws Exception {


        if(args.length != 6){
            System.out.println("This program expects the following parameters: " +
                    "1. Output CSV file name period 0 \n" +
                    "2. Output CSV file name period 1 \n" +
                    "3. Output CSV file name period 2 \n " +
                    "4. Output CSV Installations file name \n " +
                    "5. Output CSV Aircraft Operators file name \n" +
                    "6. Output CSV Installations Compliance Data file name");
        }else{

            getOperatorHoldingAccounts(args[3], args[4], args[5]);
            getAllocationsToStationaryInstallations(args[0], args[1], args[2]);

        }

    }

    public static void getOperatorHoldingAccounts(String installationsFileSt,
                                                  String aircraftOpsFileSt,
                                                  String installationsComplianceDataSt) throws Exception{


        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(6);

        for (String countryCode : countriesArray){

            // Lambda Runnable
            Runnable countryRunnable = () -> {

                try {

                    System.out.println(Thread.currentThread().getName() + " is running");
                    System.out.println("countryCode = " + countryCode);

                    File installationsFile = new File(installationsFileSt.split("\\.")[0] + countryCode + ".csv");
                    BufferedWriter installationsOutBuff = new BufferedWriter(new FileWriter(installationsFile));
                    installationsOutBuff.write(INSTALLATIONS_HEADER + "\n");

                    File aircraftOpsFile = new File(aircraftOpsFileSt.split("\\.")[0] + countryCode + ".csv");
                    BufferedWriter aircraftOpsOutBuff = new BufferedWriter(new FileWriter(aircraftOpsFile));
                    aircraftOpsOutBuff.write(AIRCRAFT_OPERATORS_HEADER + "\n");

                    File installationsCompDataFile = new File(aircraftOpsFileSt.split("\\.")[0] + countryCode + ".csv");
                    BufferedWriter installationsCompOutBuff = new BufferedWriter(new FileWriter(installationsComplianceDataSt));
                    installationsCompOutBuff.write(INSTALLATIONS_COMPLIANCE_DATA_HEADER + "\n");

                    // Create a new instance of the Firefox driver
                    WebDriver driver = new FirefoxDriver();

                    driver.get("http://ec.europa.eu/environment/ets/oha.do?form=oha&languageCode=en&account.registryCodes=" + countryCode + "&accountHolder=&installationIdentifier=&installationName=&permitIdentifier=&mainActivityType=-1&search=Search&searchType=oha&currentSortSettings=&resultList.currentPageNumber=1");

                    WebElement detailsAllButton = driver.findElement(By.name("detailsAllAllPeriods"));
                    detailsAllButton.click();

                    WebElement nextButton = driver.findElement(By.name("nextList"));

                    while(nextButton.getAttribute("disabled") == null){

                        //-------------------------------------------------------------------------
                        //------------------------GENERAL INFORMATION------------------------------

                        WebElement tableGeneralInfo = driver.findElement(By.id("tblAccountGeneralInfo"));
                        List<WebElement> tr_collection = tableGeneralInfo.findElements(By.xpath("id('tblAccountGeneralInfo')/tbody/tr"));

                        WebElement validRow = tr_collection.get(2);
                        List<WebElement> td_collection = validRow.findElements(By.xpath("td"));

                        String nationalAdministratorSt = td_collection.get(0).getText();
                        String accountTypeSt = td_collection.get(1).getText();
                        String accountHolderNameSt = td_collection.get(2).getText();
                        String idSt = td_collection.get(3).getText();
                        String companyRegistrationNumberSt = td_collection.get(4).getText();
                        String accountStatus = td_collection.get(5).getText();


                        //====================DETAILS ON CONTACT INFORMATION================================
                        //================================================================================
                        WebElement tableContactInfo = driver.findElement(By.id("tblAccountContactInfo"));
                        tr_collection = tableContactInfo.findElements(By.xpath("id('tblAccountContactInfo')/tbody/tr"));
                        validRow = tr_collection.get(2);
                        td_collection = validRow.findElements(By.xpath("td"));

                        String typeSt = td_collection.get(0).getText();
                        String nameSt = td_collection.get(1).getText();
                        String mainAddressSt = td_collection.get(2).getText();
                        String secondaryAddressSt = td_collection.get(3).getText();
                        String postalCodeSt = td_collection.get(4).getText();
                        String citySt = td_collection.get(5).getText();
                        String countrySt = td_collection.get(6).getText();

                        String contentToBeWrittenSt = nationalAdministratorSt + "\t" + accountTypeSt + "\t" + accountHolderNameSt + "\t" + idSt + "\t" +
                                companyRegistrationNumberSt + "\t" + accountStatus + "\t";
                        contentToBeWrittenSt += typeSt + "\t" + nameSt + "\t" + mainAddressSt + "\t" + secondaryAddressSt + "\t" +
                                postalCodeSt + "\t" + citySt + "\t" + countrySt + "\t";

                        //===============================INFORMATION TABLE==========================
                        //=========================================================================

                        WebElement tableDetails = driver.findElement(By.id("tblChildDetails"));
                        List<WebElement> table_collection = tableDetails.findElements(By.xpath("id('tblChildDetails')/tbody/tr/td/table"));

                        //+++++++++++++++++++GENERAL INFO++++++++++++++++++++++++

                        WebElement generalInformationTable = table_collection.get(0);
                        tr_collection = generalInformationTable.findElements(By.xpath("tbody/tr"));

                        WebElement headerRow = tr_collection.get(1);
                        td_collection = headerRow.findElements(By.xpath("td"));
                        String isAircraftText = td_collection.get(0).getText().trim();

                        boolean isAircraft = isAircraftText.equals("Aircraft Operator ID");



                        //*************************COMPLIANCE INFORMATION++++++++++++++++++++++++

                        List<WebElement> complianceRows = tableDetails.findElements(By.xpath("id('tblChildDetails')/tbody/tr/td/div/table/tbody/tr"));
                        for(int i=2;i<=17;i++){
                            WebElement currentRow = complianceRows.get(i);
                            List<WebElement> columns = currentRow.findElements(By.xpath("td"));

                            String yearSt = columns.get(1).getText();
                            String allowancesInAllocationSt = columns.get(2).getText();
                            String verifiedEmissionsSt = columns.get(3).getText();
                            String unitsSurrenderedSt = columns.get(4).getText();
                            String cumulativeSurrenderedUnitsSt = columns.get(5).getText();
                            String cumulativeVerifiedEmissionsSt = columns.get(6).getText();
                            String complianceCodeSt = columns.get(7).getText();
                            //System.out.println("yearSt = " + yearSt);

                            installationsCompOutBuff.write(countrySt + "\t" + idSt + "\t" + yearSt + "\t" +
                                    allowancesInAllocationSt + "\t" + verifiedEmissionsSt + "\t" + unitsSurrenderedSt +
                                    "\t" + cumulativeSurrenderedUnitsSt + "\t" + cumulativeVerifiedEmissionsSt + "\t" +
                                    complianceCodeSt + "\n");

                        }

                        installationsCompOutBuff.flush();


                        if(isAircraft){

                            aircraftOpsOutBuff.write(contentToBeWrittenSt);

                            WebElement dataRow = tr_collection.get(2);
                            td_collection = dataRow.findElements(By.xpath("td"));
                            String uniqueCodeComissionSt = td_collection.get(1).getText();
                            String monitoringPlanIDst = td_collection.get(2).getText();
                            String monitoringPlanFirstYearSt = td_collection.get(3).getText();
                            String monitoringPlanYearExpirySt = td_collection.get(4).getText();
                            String subsidiaryCompanySt = td_collection.get(5).getText();
                            String parentCompanySt = td_collection.get(6).getText();
                            String eprtrIdSt = td_collection.get(7).getText();
                            String icaoDesignatorSt = td_collection.get(8).getText();

                            aircraftOpsOutBuff.write(uniqueCodeComissionSt + "\t" + monitoringPlanIDst + "\t" + monitoringPlanFirstYearSt +
                                    "\t" + monitoringPlanYearExpirySt + "\t" + subsidiaryCompanySt + "\t" +
                                    parentCompanySt + "\t" + eprtrIdSt + "\t" + icaoDesignatorSt + "\n") ;

                            aircraftOpsOutBuff.flush();



                        }else{

                            installationsOutBuff.write(contentToBeWrittenSt);

                            WebElement dataRow = tr_collection.get(2);
                            td_collection = dataRow.findElements(By.xpath("td"));
                            String installationNameSt = td_collection.get(1).getText();
                            String permitIDSt = td_collection.get(2).getText();
                            String permitEntryDateSt = td_collection.get(3).getText();
                            String permitExpiryDateSt = td_collection.get(4).getText();
                            String subsidiaryCompanySt = td_collection.get(5).getText();
                            String parentCompanySt = td_collection.get(6).getText();
                            String eprtrIdSt = td_collection.get(7).getText();

                            installationsOutBuff.write(installationNameSt + "\t" + permitIDSt + "\t" + permitEntryDateSt +
                                    "\t" + permitExpiryDateSt + "\t" + subsidiaryCompanySt + "\t" + parentCompanySt + "\t"
                                    + eprtrIdSt + "\n") ;

                            installationsOutBuff.flush();

                        }

//            System.out.println("isAircraft = " + isAircraft);
//            System.out.println("nameSt = " + nameSt);

                        nextButton.click();
                        nextButton = driver.findElement(By.name("nextList"));
                    }


                    driver.quit();

                    installationsOutBuff.close();
                    aircraftOpsOutBuff.close();
                    installationsCompOutBuff.close();



                }catch (Exception e){
                    e.printStackTrace();
                }

            };

            executor.execute(countryRunnable);
        }


        System.out.println("Maximum threads inside pool " + executor.getMaximumPoolSize());
        executor.shutdown();


    }

    public static void getAllocationsToStationaryInstallations(
            String filePeriod0St,
            String filePeriod1St,
            String filePeriod2St) throws Exception{

        File outputFilePeriod0 = new File(filePeriod0St);
        File outputFilePeriod1 = new File(filePeriod1St);
        File outputFilePeriod2 = new File(filePeriod2St);

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

                            for(int rowCounter=3; rowCounter<tr_collection.size(); rowCounter++)
                            {

                                outBuffPeriod0.write(country + "\t");

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

                            for(int rowCounter=3; rowCounter<tr_collection.size(); rowCounter++)
                            {

                                outBuffPeriod1.write(country + "\t");

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

                            for(int rowCounter=3; rowCounter<(tr_collection.size()-3); rowCounter++)
                            {

                                outBuffPeriod2.write(country + "\t");

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
