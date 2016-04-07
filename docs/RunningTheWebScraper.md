## How to run the Web Scraper

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


