package org.sandbag.eutlws;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 07/03/16.
 */
public class EUTLSeleniumTest {

    public static String[] countriesArray = {"AT","BE","HR","CY","CZ","DK","EE","FI","FR","DE","GR","HU",
                                            "IS","IE","IT","LV","LI","LT","LU","MT","NL","NO","PL","PT","RO",
                                            "SK","SI","ES","SE","GB"};
    //public static String[] countriesArray = {"EU"};

    public static String PERIOD_0_HEADER = "Country\tInstallation ID\tLatest Update\t2005\t2006\t2007";
    public static String PERIOD_1_HEADER = "Country\tInstallation ID\tLatest Update\t2008\t2009\t2010\t2011\t2012";
    public static String PERIOD_2_HEADER = "Country\tInstallation ID\tLatest Update\t2013\t2014\t2015\t2016\t2017\t2018\t2019\t2020";

    public static final String INSTALLATIONS_HEADER = "Country\tAccount Type\tAccount Holder Name\t" +
            "Company Registration Number\tAccount Status\tType\tCompany Name\tCompany Main Address\tCompany Secondary Address\t" +
            "Postal Code\tCompany City\tInstallation ID\tInstallation name\tPermit ID\tPermit Entry Date\t" +
            "Permit Expiry/Revocation Date\tSubsidiary Company\tParent Company\tE-PRTR identification\t" +
            "Installation Main Address\tInstallation Secondary Address\tInstallation Postal Code\tInstallation City\t" +
            "Country ID\tLatitude\tLongitude\tMain Activity";

    public static final String AIRCRAFT_OPERATORS_HEADER = "Country\tAccount Type\tAccount Holder Name\t" +
            "Company Registration Number\tAccount Status\tType\tCompany Name\tCompany Main Address\tCompany Secondary Address\t" +
            "Postal Code\tCompany City\tAircraft Operator ID\tUnique Code under Commission Regulation\tMonitoring Plan ID\t" +
            "Monitoring plan first year of applicability\tMonitoring plan year of expiry\tSubsidiary Company\t" +
            "Parent Company\tE-PRTR identification\tICAO designator\tAircraft Operator Main address\tAircraft Operator Secondary Address\t" +
            "Aircraft Operator Postal Code\tAircraft Operator City\tCountry ID\tLatitude\tLongitude\tMain Activity";

    public static final String ALLOWANCES_IN_ALLOCATION_TYPE = "Allowances in Allocation";
    public static final String VERIFIED_EMISSIONS_TYPE = "Verified Emissions";
    public static final String UNITS_SURRENDERED_TYPE = "Units Surrendered";
    public static final String COMPLIANCE_CODE_TYPE = "Compliance Code";

    public static final String NEW_ENTRANT_RESERVE_CODE = "*****";
    public static final String NEW_ENTRANT_RESERVE_CODE_REGEXP = "\\*\\*\\*\\*\\*";

    public static final String ARTICLE_10C_CODE = "****";
    public static final String ARTICLE_10C_CODE_REGEXP = "\\*\\*\\*\\*";

    public static final String INSTALLATIONS_COMPLIANCE_DATA_HEADER = "Country\tInstallation ID\tYear\t" +
            ALLOWANCES_IN_ALLOCATION_TYPE + "\t" + VERIFIED_EMISSIONS_TYPE + "\t" + UNITS_SURRENDERED_TYPE + "\t" +
            COMPLIANCE_CODE_TYPE;

    public static final String NER_ALLOCATION_DATA_HEADER = "Country\tInstallation ID\tYear\tNER allocation";
    public static final String ARTICLE_10C_ALLOCATION_DATA_HEADER = "Country\tInstallation ID\tYear\tArticle 10c allocation";

    public static void main(String[] args) throws Exception {


        if(args.length != 6){
            System.out.println("This program expects the following parameters: " +
                    "1. Installations folder \n " +
                    "2. Aircraft operators folder \n" +
                    "3. Compliance data folder\n" +
                    "4. NER allocation data file name\n" +
                    "5. Article 10c data file name\n" +
                    "6. Number of concurrent browsers (int)");
        }else{

            String installationsFolderSt = args[0];
            String aircraftOperatorsFolderSt = args[1];
            String complianceDataFolderSt = args[2];
            String nerAllocationFileSt = args[3];
            String article10cFileSt = args[4];
            int numberOfConcurrentBrowsers = Integer.parseInt(args[5]);

            getOperatorHoldingAccounts(installationsFolderSt,
                                        aircraftOperatorsFolderSt,
                                        complianceDataFolderSt,
                                        nerAllocationFileSt,
                                        article10cFileSt,
                                        numberOfConcurrentBrowsers);

            //getAllocationsToStationaryInstallations(args[0], args[1], args[2],numberOfConcurrentBrowsers);

        }

    }

    public static void getOperatorHoldingAccounts(String installationsFolderSt,
                                                  String aircraftOpsFolderSt,
                                                  String complianceFolderSt,
                                                  String nerAllocationFileSt,
                                                  String article10cFileSt,
                                                  int numberOfConcurrentBrowsers) throws Exception{


        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfConcurrentBrowsers);

        File nerAllocationFile = new File(nerAllocationFileSt);
        BufferedWriter nerAllocOutBuff = new BufferedWriter(new FileWriter(nerAllocationFile));
        nerAllocOutBuff.write(NER_ALLOCATION_DATA_HEADER + "\n");

        File article10cFile = new File(article10cFileSt);
        BufferedWriter article10cOutBuff = new BufferedWriter(new FileWriter(article10cFile));
        article10cOutBuff.write(ARTICLE_10C_ALLOCATION_DATA_HEADER + "\n");

        for (String countryCode : countriesArray){

            // Lambda Runnable
            Runnable countryRunnable = () -> {

                try {

                    System.out.println(Thread.currentThread().getName() + " is running");
                    System.out.println("countryCode = " + countryCode);

                    File installationsFile = new File(installationsFolderSt + "/" + countryCode + ".csv");
                    BufferedWriter installationsOutBuff = new BufferedWriter(new FileWriter(installationsFile));
                    installationsOutBuff.write(INSTALLATIONS_HEADER + "\n");

                    File aircraftOpsFile = new File(aircraftOpsFolderSt + "/" + countryCode + ".csv");
                    BufferedWriter aircraftOpsOutBuff = new BufferedWriter(new FileWriter(aircraftOpsFile));
                    aircraftOpsOutBuff.write(AIRCRAFT_OPERATORS_HEADER + "\n");

                    File installationsCompDataFile = new File(complianceFolderSt + "/" + countryCode + ".csv");
                    BufferedWriter installationsCompOutBuff = new BufferedWriter(new FileWriter(installationsCompDataFile));
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
                        List<WebElement> tr_collection_account_general_info = tableGeneralInfo.findElements(By.xpath("id('tblAccountGeneralInfo')/tbody/tr"));

                        WebElement validRow = tr_collection_account_general_info.get(2);
                        List<WebElement> td_collection = validRow.findElements(By.xpath("td"));

                        String nationalAdministratorSt = td_collection.get(0).getText();
                        String accountTypeSt = td_collection.get(1).getText();
                        String accountHolderNameSt = td_collection.get(2).getText();
                        String installationIdSt = td_collection.get(3).getText();
                        String companyRegistrationNumberSt = td_collection.get(4).getText();
                        String accountStatus = td_collection.get(5).getText();


                        //====================DETAILS ON CONTACT INFORMATION================================
                        //================================================================================
                        WebElement tableContactInfo = driver.findElement(By.id("tblAccountContactInfo"));
                        List<WebElement> tr_collection_account_contact_info = tableContactInfo.findElements(By.xpath("id('tblAccountContactInfo')/tbody/tr"));
                        validRow = tr_collection_account_contact_info.get(2);
                        td_collection = validRow.findElements(By.xpath("td"));

                        String companyTypeSt = td_collection.get(0).getText();
                        String companyNameSt = td_collection.get(1).getText();
                        String companyMainAddressSt = td_collection.get(2).getText();
                        String companySecondaryAddressSt = td_collection.get(3).getText();
                        String companyPostalCodeSt = td_collection.get(4).getText();
                        String companyCitySt = td_collection.get(5).getText();
                        //String countryNameSt = td_collection.get(6).getText();


                        String contentToBeWrittenSt = nationalAdministratorSt + "\t" + accountTypeSt + "\t" + accountHolderNameSt + "\t" +
                                companyRegistrationNumberSt + "\t" + accountStatus + "\t";
                        contentToBeWrittenSt += companyTypeSt + "\t" + companyNameSt + "\t" + companyMainAddressSt + "\t"
                                + companySecondaryAddressSt + "\t" + companyPostalCodeSt + "\t" + companyCitySt + "\t";

                        //===============================INFORMATION TABLE==========================
                        //=========================================================================

                        WebElement tableDetails = driver.findElement(By.id("tblChildDetails"));
                        List<WebElement> table_collection = tableDetails.findElements(By.xpath("id('tblChildDetails')/tbody/tr/td/table"));

                        //+++++++++++++++++++GENERAL INFO++++++++++++++++++++++++

                        WebElement generalInformationTable = table_collection.get(0);
                        List<WebElement> tr_collection_general_info = generalInformationTable.findElements(By.xpath("tbody/tr"));

                        WebElement headerRow = tr_collection_general_info.get(1);
                        td_collection = headerRow.findElements(By.xpath("td"));
                        String isAircraftText = td_collection.get(0).getText().trim();

                        boolean isAircraft = isAircraftText.equals("Aircraft Operator ID");

                        //+++++++++++++++++++ADDRESS INFO++++++++++++++++++++++++

                        WebElement addressInformationTable = table_collection.get(1);
                        List<WebElement> tr_collection_address = addressInformationTable.findElements(By.xpath("tbody/tr"));

                        WebElement dataRow = tr_collection_address.get(2);
                        td_collection = dataRow.findElements(By.xpath("td"));

                        String installationMainAddressSt = td_collection.get(0).getText();
                        String installationSecondaryAddressSt = td_collection.get(1).getText();
                        String installationPostalCodeSt = td_collection.get(2).getText();
                        String installationCitySt = td_collection.get(3).getText();
                        String installationCountryId = td_collection.get(4).getText();
                        String installationLatitudeSt = td_collection.get(5).getText();
                        String installationLongitudeSt = td_collection.get(6).getText();
                        String installationMainActivitySt = td_collection.get(7).getText();

                        String addressInfoSt = installationMainAddressSt + "\t" + installationSecondaryAddressSt + "\t" +
                                installationPostalCodeSt + "\t" + installationCitySt + "\t" + installationCountryId + "\t" +
                                installationLatitudeSt + "\t" + installationLongitudeSt + "\t" + installationMainActivitySt + "\n";

                        if(isAircraft){

                            aircraftOpsOutBuff.write(contentToBeWrittenSt);

                            dataRow = tr_collection_general_info.get(2);
                            td_collection = dataRow.findElements(By.xpath("td"));

                            String aircraftOpId = td_collection.get(0).getText();
                            String uniqueCodeComissionSt = td_collection.get(1).getText();
                            String monitoringPlanIDst = td_collection.get(2).getText();
                            String monitoringPlanFirstYearSt = td_collection.get(3).getText();
                            String monitoringPlanYearExpirySt = td_collection.get(4).getText();
                            String subsidiaryCompanySt = td_collection.get(5).getText();
                            String parentCompanySt = td_collection.get(6).getText();
                            String eprtrIdSt = td_collection.get(7).getText();
                            String icaoDesignatorSt = td_collection.get(8).getText();

                            aircraftOpsOutBuff.write(aircraftOpId + "\t" + uniqueCodeComissionSt + "\t" + monitoringPlanIDst +
                                    "\t" + monitoringPlanFirstYearSt + "\t" + monitoringPlanYearExpirySt + "\t" +
                                    subsidiaryCompanySt + "\t" + parentCompanySt + "\t" + eprtrIdSt + "\t" + icaoDesignatorSt + "\t") ;

                            aircraftOpsOutBuff.write(addressInfoSt);

                            aircraftOpsOutBuff.flush();



                        }else{

                            installationsOutBuff.write(contentToBeWrittenSt);

                            dataRow = tr_collection_general_info.get(2);
                            td_collection = dataRow.findElements(By.xpath("td"));

                            installationIdSt = td_collection.get(0).getText().trim();
                            String installationNameSt = td_collection.get(1).getText();
                            String permitIDSt = td_collection.get(2).getText();
                            String permitEntryDateSt = td_collection.get(3).getText();
                            String permitExpiryDateSt = td_collection.get(4).getText();
                            String subsidiaryCompanySt = td_collection.get(5).getText();
                            String parentCompanySt = td_collection.get(6).getText();
                            String eprtrIdSt = td_collection.get(7).getText();

                            installationsOutBuff.write(installationIdSt + "\t" + installationNameSt + "\t" + permitIDSt +
                                    "\t" + permitEntryDateSt + "\t" + permitExpiryDateSt + "\t" + subsidiaryCompanySt +
                                    "\t" + parentCompanySt + "\t" + eprtrIdSt + "\t") ;

                            installationsOutBuff.write(addressInfoSt);

                            installationsOutBuff.flush();

                        }


                        //*************************COMPLIANCE INFORMATION++++++++++++++++++++++++

                        List<WebElement> complianceRows = tableDetails.findElements(By.xpath("id('tblChildDetails')/tbody/tr/td/div/table/tbody/tr"));
                        for(int i=2;i<=17;i++){
                            //System.out.println("i = " + i);
                            WebElement currentRow = complianceRows.get(i);
                            List<WebElement> columns = currentRow.findElements(By.xpath("td"));

                            String yearSt = columns.get(1).getText();
                            String allowancesInAllocationSt = columns.get(2).getText();
                            String verifiedEmissionsSt = columns.get(3).getText().replaceAll("\n"," ");
                            String unitsSurrenderedSt = columns.get(4).getText().replaceAll("\n", " ");
                            //String cumulativeSurrenderedUnitsSt = columns.get(5).getText();
                            //String cumulativeVerifiedEmissionsSt = columns.get(6).getText();
                            String complianceCodeSt = columns.get(7).getText().replaceAll("\n"," ");
                            //System.out.println("yearSt = " + yearSt);

//                            System.out.println("=====================");
//                            System.out.println("allowancesInAllocationSt = " + allowancesInAllocationSt);
//                            System.out.println(allowancesInAllocationSt.indexOf(ARTICLE_10C_CODE) );
//                            System.out.println(allowancesInAllocationSt.indexOf(NEW_ENTRANT_RESERVE_CODE) );
//                            System.out.println(allowancesInAllocationSt.indexOf("****") );
//                            System.out.println(allowancesInAllocationSt.indexOf("*****") );
//                            System.out.println("---------------------");

                            String[] newLineSplit = allowancesInAllocationSt.split("\n");

                            if(newLineSplit.length == 3){

                                //-------------------NER ALLOCATIONS----------------------
                                String nerValue = newLineSplit[2].split(NEW_ENTRANT_RESERVE_CODE_REGEXP)[0].trim();
                                nerAllocOutBuff.write(countryCode + "\t" + installationIdSt + "\t" + yearSt + "\t" +
                                        nerValue + "\n");

                                //-------------------ARTICLE 10C ALLOCATIONS----------------------
                                String article10cValue = newLineSplit[1].split(ARTICLE_10C_CODE_REGEXP)[0].trim();
                                article10cOutBuff.write(countryCode + "\t" + installationIdSt + "\t" + yearSt + "\t" +
                                        article10cValue + "\n");

                                allowancesInAllocationSt = newLineSplit[0].trim();


                            }else if(newLineSplit.length == 2){

                                String otherValue = newLineSplit[1];

                                if(otherValue.indexOf(NEW_ENTRANT_RESERVE_CODE) > 0){
                                    //-------------------NER ALLOCATIONS----------------------
                                    String nerValue = otherValue.split(NEW_ENTRANT_RESERVE_CODE_REGEXP)[0].trim();
                                    nerAllocOutBuff.write(countryCode + "\t" + installationIdSt + "\t" + yearSt + "\t" +
                                            nerValue + "\n");

                                }else if(otherValue.indexOf(ARTICLE_10C_CODE) > 0){
                                    //-------------------ARTICLE 10C ALLOCATIONS----------------------
                                    String article10cValue = otherValue.split(ARTICLE_10C_CODE_REGEXP)[0].trim();
                                    article10cOutBuff.write(countryCode + "\t" + installationIdSt + "\t" + yearSt + "\t" +
                                            article10cValue + "\n");
                                }

                                allowancesInAllocationSt = newLineSplit[0].trim();
                            }

                            //------------------STANDARD ALLOCATIONS---------------------
                            installationsCompOutBuff.write(countryCode + "\t" + installationIdSt + "\t" + yearSt + "\t" +
                                    allowancesInAllocationSt + "\t" + verifiedEmissionsSt + "\t" + unitsSurrenderedSt + "\t" +
                                    complianceCodeSt + "\n");


                        }


                        installationsCompOutBuff.flush();


                        nextButton.click();
                        nextButton = driver.findElement(By.name("nextList"));
                    }


                    driver.quit();

                    installationsOutBuff.close();
                    aircraftOpsOutBuff.close();
                    installationsCompOutBuff.close();
                    nerAllocOutBuff.flush();
                    article10cOutBuff.flush();



                }catch (Exception e){
                    e.printStackTrace();
                }

            };

            threadPoolExecutor.submit(countryRunnable);
        }



        System.out.println("Maximum threads inside pool " + threadPoolExecutor.getMaximumPoolSize());
        while(threadPoolExecutor.getActiveCount() > 0){
            TimeUnit.SECONDS.sleep(30);
            System.out.println("Just woke up! ");
            System.out.println("threadPoolExecutor.getActiveCount() = " + threadPoolExecutor.getActiveCount());
        }
        threadPoolExecutor.shutdown();
        nerAllocOutBuff.close();
        article10cOutBuff.close();


    }

    public static void getAllocationsToStationaryInstallations(
            String filePeriod0St,
            String filePeriod1St,
            String filePeriod2St,
            int numberOfConcurrentBrowsers) throws Exception{


        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfConcurrentBrowsers);


        for(String country : countriesArray){

            // Lambda Runnable
            Runnable countryRunnable = () -> {
                try {

                    File outputFilePeriod0 = new File(filePeriod0St.split("\\.")[0] + country + ".csv");
                    File outputFilePeriod1 = new File(filePeriod1St.split("\\.")[0] + country + ".csv");
                    File outputFilePeriod2 = new File(filePeriod2St.split("\\.")[0] + country + ".csv");

                    BufferedWriter outBuffPeriod0 = new BufferedWriter(new FileWriter(outputFilePeriod0));
                    outBuffPeriod0.write(PERIOD_0_HEADER + "\n");
                    BufferedWriter outBuffPeriod1 = new BufferedWriter(new FileWriter(outputFilePeriod1));
                    outBuffPeriod1.write(PERIOD_1_HEADER + "\n");
                    BufferedWriter outBuffPeriod2 = new BufferedWriter(new FileWriter(outputFilePeriod2));
                    outBuffPeriod2.write(PERIOD_2_HEADER + "\n");

                    // Create a new instance of the Firefox driver
                    // Notice that the remainder of the code relies on the interface,
                    // not the implementation.
                    WebDriver driver = new FirefoxDriver();

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

                                        WebElement trElement = tr_collection.get(rowCounter);

                                        List<WebElement> td_collection=trElement.findElements(By.xpath("td"));

                                        String installationIDst = td_collection.get(0).getText();
                                        String latestUpdateSt = td_collection.get(6).getText();
                                        String year2005St = td_collection.get(7).getText();
                                        String year2006St = td_collection.get(8).getText();
                                        String year2007St = td_collection.get(9).getText();

                                        outBuffPeriod0.write(country + "\t" + installationIDst + "\t" + latestUpdateSt +
                                                "\t" + year2005St + "\t" + year2006St + "\t" + year2007St + "\n");

                                        //System.out.println("Country: " + country + " Row: " + rowCounter + " completed");
                                    }

                                }else if(period == 1){

                                    for(int rowCounter=3; rowCounter<tr_collection.size(); rowCounter++)
                                    {

                                        WebElement trElement = tr_collection.get(rowCounter);

                                        List<WebElement> td_collection=trElement.findElements(By.xpath("td"));

                                        String installationIDst = td_collection.get(0).getText();
                                        String latestUpdateSt = td_collection.get(6).getText();
                                        String year2008St = td_collection.get(7).getText();
                                        String year2009St = td_collection.get(8).getText();
                                        String year2010St = td_collection.get(9).getText();
                                        String year2011St = td_collection.get(10).getText();
                                        String year2012St = td_collection.get(11).getText();

                                        outBuffPeriod1.write(country + "\t" + installationIDst + "\t" + latestUpdateSt +
                                                "\t" + year2008St + "\t" + year2009St + "\t" + year2010St + "\t" +
                                                year2011St + "\t" + year2012St + "\n");


                                        //System.out.println("Country: " + country + " Row: " + rowCounter + " completed");
                                    }
                                }else if(period == 2){

                                    for(int rowCounter=3; rowCounter<(tr_collection.size()-3); rowCounter++)
                                    {

                                        WebElement trElement = tr_collection.get(rowCounter);

                                        List<WebElement> td_collection=trElement.findElements(By.xpath("td"));

                                        String installationIDst = td_collection.get(0).getText();
                                        String latestUpdateSt = td_collection.get(6).getText();
                                        String year2013St = td_collection.get(7).getText();
                                        String year2014St = td_collection.get(8).getText();
                                        String year2015St = td_collection.get(9).getText();
                                        String year2016St = td_collection.get(10).getText();
                                        String year2017St = td_collection.get(11).getText();
                                        String year2018St = td_collection.get(11).getText();
                                        String year2019St = td_collection.get(11).getText();
                                        String year2020St = td_collection.get(11).getText();

                                        outBuffPeriod2.write(country + "\t" + installationIDst + "\t" + latestUpdateSt +
                                                "\t" + year2013St + "\t" + year2014St + "\t" + year2015St + "\t" +
                                                year2016St + "\t" + year2017St + "\t" + year2017St + "\t"
                                                + year2018St + "\t"  + year2019St + "\t"  + year2020St + "\t");


                                        //System.out.println("Country: " + country + " Row: " + rowCounter + " completed");
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

                    //close WebDriver
                    driver.quit();

                    //Close output files
                    outBuffPeriod0.close();
                    outBuffPeriod1.close();
                    outBuffPeriod2.close();

                }catch (Exception e){
                    e.printStackTrace();
                }

            };

            threadPoolExecutor.submit(countryRunnable);

        }

        System.out.println("Maximum threads inside pool " + threadPoolExecutor.getMaximumPoolSize());

        while(threadPoolExecutor.getActiveCount() > 0){
            TimeUnit.SECONDS.sleep(30);
            System.out.println("Just woke up! ");
            System.out.println("threadPoolExecutor.getActiveCount() = " + threadPoolExecutor.getActiveCount());
        }
        threadPoolExecutor.shutdown();




    }

}
