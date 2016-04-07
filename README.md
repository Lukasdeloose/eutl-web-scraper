# eutl-web-scraper
New version of the EUTL web scraper

## Running the Web Scraper

Here is a detailed description of the different steps to follow in order to run the Web Scraper.

### 1. Get the following files

* **eutl-web-scraper.jar** _(located [here](/dist/eutl-web-scraper.jar))_
* **EUTLWebScraperConfig.json** _(located [here](/EUTLWebScraperConfig.json))_
  * Check the following [section](/docs/ConfigurationParameters.md) to see the different configuration parameters 
 
### 2. Launch an AWS instance

  Preferably a **m3.medium** or more powerful instance type 
    
    m3.medium specs: vCPU: 1 	Mem(GiB): 3.75	SSD Storage (GB):	1 x 4 

### 3. Create a few folders:
  
  Create the following directory structure:
  
```.
+-- installations
+-- aircraft_operators
+-- offsets
+-- compliance
```

## [Data quality checks](docs/QualityChecks.md)


