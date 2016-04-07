## Web Scraper Configuration Parameters

Here is a sample content for the file [EUTLWebScraperConfig.json](/EUTLWebScraperConfig.json)

``` json
{
	"number_of_threads":"3",
	"country_codes":["AT", "BE", "BG", "CY", "CZ", "DE", "DK", "EE", "ES", "FI", "FR", "GB",
      "GR", "HR", "HU", "IE", "IS", "IT", "LI", "LT", "LU", "LV", "MT", "NL", "NO", "PL", "PT", "RO", "SE",
      "SI", "SK"],
    "intallations_folder":"web_scrape/installations",
    "aircraft_operators_folder":"web_scrape/aircraft_operators",
    "offsets_folder":"web_scrape/offsets",
    "compliance_folder":"web_scrape/compliance",
    "ner_file":"web_scrape/NER.tsv",
    "article10c_file":"web_scrape/article10c.tsv",
    "installations_offset_entitlements_file":"web_scrape/InstallationsEntitlements.tsv",
    "aircraft_operators_offset_entitlements_file":"web_scrape/AircraftOperatorsEntitlements.tsv"
}
```

### Parameters

* **number_of_threads**: Integer indicating the maximum total number of parallel threads running at any time.

> Be careful when setting values higher than 3/4 threads since the European Commission site tends to cut off the connection when many threads are performing requests at once.

* **country_codes**: Array of String values including the _(ISO 3166-2)_ country codes for which information will be retrieved.
* **installations_folder**: Folder for installations files.
* **aircraft_operators_folder**: Folder for aircraft operators files.
* **offsets_folder**: Folder for offsets files.
* **compliance_folder**: Folder for compliance information files.
* **ner_file**: TSV file where allocations of type _NER_ will be stored.
* **article10c_file**: TSV file where allocations of type _Article 10c_ will be stored. 
* **installations_offset_entitlements_file**: TSV file for information pertaining to installations' offset entitlements.
* **aircraft_operators_offset_entitlements_file**: TSV file for information pertaining to aircraft operators' offset entitlements.

