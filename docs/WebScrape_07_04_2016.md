## Tests performed

- [x] **All countries from the European Union (+3) exist**
- [x] **DataQualityChecks program** executed successfully _(only the problematic ID: HR200696 could not be found)_
- [ ] **Total number of installations per country**: 

| Country ID | Number of installations | Number of Aircraft Operators | Installations + Aircraft Operators (Database) | EUTL total  |
|------------|-------------------------|------------------------------|-----------------------------------------------|-------------|
| AT         | 260                     | 28                           | 288                                           |  OK         |
| BE         | 451                     | 33                           | 484                                           | OK          |
| BG         | 168                     | 4                            | 172                                           | OK          |
| CY         | 14                      | 9                            | 23                                            | OK          |
| CZ         | 451                     | 13                           | 464                                           | OK          |
| DE         | 2501                    | 168                          | 2669                                          | OK          |
| DK         | 434                     | 23                           | 457                                           | OK          |
| EE         | 62                      | 3                            | 65                                            | OK          |
| ES         | 1376                    | 75                           | 1451                                          | OK          |
| FI         | 710                     | 12                           | 722                                           | OK          |
| FR         | 1437                    | 210                          | 1647                                          | OK          |
| GB         | 1313                    | 450                          | 1763                                          | **1764**        |
| GR         | 190                     | 20                           | 210                                           | OK          |
| HR         | 58                      | 1                            | 59                                            | OK          |
| HU         | 287                     | 5                            | 292                                           | OK          |
| IE         | 135                     | 85                           | 220                                           | OK          |
| IS         | 6                       | 33                           | 39                                            | OK          |
| IT         | 1495                    | 80                           | 1575                                          | **1576**        |
| LI         | 2                       | 0                            | 2                                             | OK          |
| LT         | 119                     | 5                            | 124                                           | OK          |
| LU         | 24                      | 7                            | 31                                            | OK          |
| LV         | 115                     | 2                            | 117                                           | OK          |
| MT         | 2                       | 5                            | 7                                             | OK          |
| NL         | 614                     | 33                           | 647                                           | OK          |
| NO         | 164                     | 11                           | 175                                           | **176**         |
| PL         | 1027                    | 13                           | 1040                                          | **1041**        |
| PT         | 333                     | 25                           | 358                                           | OK          |
| RO         | 291                     | 6                            | 297                                           | OK          |
| SE         | 911                     | 22                           | 933                                           | OK          |
| SI         | 103                     | 1                            | 104                                           | OK          |
| SK         | 219                     | 2                            | 221                                           | OK          |

There are four installations missing:

* [PL207726](http://ec.europa.eu/environment/ets/ohaDetails.do?returnURL=&languageCode=en&accountID=&registryCode=&buttonAction=all&action=&account.registryCode=&accountType=&identifierInReg=&accountHolder=&primaryAuthRep=&installationIdentifier=&installationName=&accountStatus=&permitIdentifier=&complianceStatus=&mainActivityType=-1&searchType=oha&account.registryCodes=PL&resultList.currentPageNumber=1040&nextList=Next%C2%A0%3E&selectedPeriods=) _No compliance data included in the EUTL website_
* [UK207723](http://ec.europa.eu/environment/ets/ohaDetails.do?returnURL=&languageCode=en&accountID=&registryCode=&buttonAction=all&action=&account.registryCode=&accountType=&identifierInReg=&accountHolder=&primaryAuthRep=&installationIdentifier=&installationName=&accountStatus=&permitIdentifier=&complianceStatus=&mainActivityType=-1&searchType=oha&account.registryCodes=GB&resultList.currentPageNumber=1764&nextList=Next%C2%A0%3E&selectedPeriods=) _No compliance data included in the EUTL website_
* [IT207704](http://ec.europa.eu/environment/ets/ohaDetails.do?returnURL=&languageCode=en&accountID=&registryCode=&buttonAction=all&action=&account.registryCode=&accountType=&identifierInReg=&accountHolder=&primaryAuthRep=&installationIdentifier=&installationName=&accountStatus=&permitIdentifier=&complianceStatus=&mainActivityType=-1&searchType=oha&account.registryCodes=IT&resultList.currentPageNumber=1575&nextList=Next%C2%A0%3E&selectedPeriods=) _Just one value as part of its compliance data (Verified Emissions for 2015), perhaps it has been updated since the moment when the Web Scraper was executed. We should check this installation again whenever the Web Scraper is executed again..._
* [NO207728](http://ec.europa.eu/environment/ets/ohaDetails.do?returnURL=&languageCode=en&accountID=&registryCode=&buttonAction=all&action=&account.registryCode=&accountType=&identifierInReg=&accountHolder=&primaryAuthRep=&installationIdentifier=&installationName=&accountStatus=&permitIdentifier=&complianceStatus=&mainActivityType=-1&searchType=oha&account.registryCodes=NO&resultList.currentPageNumber=175&nextList=Next%C2%A0%3E&selectedPeriods=) _No compliance data included in the EUTL website_

- [x]  All previously included keys included in the new file 1
- [ ]  All previously included keys for offset records and offset entitlement records are covered in the latest scrape files (files 2 & 3)
- [ ]  Free allocation totals per member state for the new file 1 
     * Estonia Phase3 std + 10c doesn't match
     * France Phase3 std doesn't match
     * Germany Phase3 std doesn't match
     * Italy Phase3 std doesn't match
     * Norway Phase3 std doesn't match
     * Romania Phase3 std doesn't match
     * United Kingdom Phase2 std & Phase3 std don't match
  
