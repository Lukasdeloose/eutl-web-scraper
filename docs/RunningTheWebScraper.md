## How to run the Web Scraper

Here's a step-by-step guide on how to run the Web Scraper:

### 1. Get the following files

* **eutl-web-scraper.jar** _(located [here](/dist/eutl-web-scraper.jar))_
* **EUTLWebScraperConfig.json** _(located [here](/EUTLWebScraperConfig.json))_
  * Check the following [section](/docs/ConfigurationParameters.md) to see the different configuration parameters 
 
### 2. Launch an AWS instance

  Preferably a **m3.medium** or more powerful instance type 
    
    m3.medium specs: vCPU: 1 	Mem(GiB): 3.75	SSD Storage (GB):	1 x 4 
 

### 3. Install java 8

``` bash
sudo yum remove java-1.7.0-openjdk  (optional)
sudo yum install java-1.8.0
```

### 4. Create a few folders:
  
  Create the following directory structure:
  
```.
+-- installations
+-- aircraft_operators
+-- offsets
+-- compliance
```

### 4. Run the following command

``` bash
java -d64 -Xmx2G -jar eutl-web-scraper.jar EUTLWebScraperConfig.json
```

### 5. Possible problems during scrape

There is chance that some installations will not have all the data required by the scraper. Such case was with the installation FR431 where Details on Contact Information were missing (and as of April 2nd 2018, the problem is not fixed on the EUTL website). For such cases the scraper will leave the fields in the output file empty, and those have to be filled in manually, before starting the database generator. If new empty fields cause the scraper to crash, a fix has to be implemented in the scraper class _(located [here](/src/main/java/org/sandbag/eutlws/EUTLWebScraper.java))_ similar to one starting in the line 471.

