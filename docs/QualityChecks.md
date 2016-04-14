### Basic checks
 
 * All countries from the European Union exist (make sure all included in the scrape files and data export files)
 * Number of _**total installations and aircraft operators per country**_ is consistent with that shown in the EUTL website
 * Make sure all columns in data export files are populated as expected (do quick totals check compared to previous scrapes)
 * Check scraped free allocation totals to stationary installations per member state match those in the 'Allocations to Stationary Installations' area of the EUTL
 * Check all active keys (member state ISO2 code plus installation number) from previous scrapes are covered in the new scrape
 * Run the test programs included in the class: [DataQualityChecks](https://github.com/sandbag-climate/eutldb/blob/master/src/test/java/org/sandbag/eutldb/tests/DataQualityChecks.java)
 

### [Web Scrape 07/04/2016](/docs/WebScrape_07_04_2016.md)
