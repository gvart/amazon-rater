# Amazon rater
Application calculates popularity of a keyword in the Amazon, based on [completion.amazon.com](https://completion.amazon.com/search/complete) api.

By computing data from the API final score is a value between 0 and 100 for input keyword, depending on word popularity. 


### Logic behind the approach
The final user of the application is not so aware of the position in the auto completion list, much more important
is the presence in that list, so we could ignore order of the items returned by Amazon API and assume that popularity 
of each item is approximately the same.   

All the strategies are based on the presence of the keyword in the Amazon Completion API for each passed sequence.

---
General idea *(Concrete implementations are optimized)*: 
0. Having a keyword "ABCD", that is shown in autocomplete API after "ABC" input was inserted.
1. With accuracy level **HIGH**, will divide our initial keyword in 'A', 'B', 'C', 'D' parts.
2. Now, perform a sequential search in API by merging previously created parts (Requests for "A", "AB", "ABC", "ABCD").
3. Checking each result if it contains the keyword "ABCD" and merge the final results (In our example we will have 2 
   positive cases for "ABC", and "ABCD" and 2 negative for "A" and "AB").
4. By having 2 positive and 2 negative results our final score will be 50 out of 100.

*NOTE*:
```
My guess is that this approach is not the optimal one (Due lack of knowledge how SEO works), and we can't evaluate 
significance of the first and last letter, and my algorithm could be improved by adding some predefined value for each letter,
where the first letters are most valuable, while the last one are least valuable.

Actual accuray is aproximatly 75%-80%
```
---
### How to use
* To build an executable jar file run `./gradlew bootJar` command. 
  Then that executable JAR could be found in *build/libs* directory.
* To run the executable JAR, navigate under *build/libs* and use command
  `java -jar amazon-rater-0.0.1-SNAPSHOT.jar` to startup web application. (Note, ensure that port 8080 is free)`
* To access API, perform a [GET] request to http://localhost:8080/api/v1/rate/amazon?keyword=YOUR_KEYWORD_GOES_HERE
---
Example of requests: 
- http://localhost:8080/api/v1/rate/amazon?keyword=macbook%20pro
- http://localhost:8080/api/v1/rate/amazon?keyword=iphone%20charger
- http://localhost:8080/api/v1/rate/amazon?keyword=nokia

 
### Configuration

Applications at this stage allows to configure following properties:

|Property|Description|Admissible values|Default values|
| :---: | :---: | :---: | :---: |
|`amazon.rater.accuracy` |Affects accuracy and computation speed of the result. [Level description](#accuracy-level)|`HIGH`, `MEDIUM`, `LOW`|`HIGH`|
|`amazon.rater.search-strategy-type` |Defines computation strategy|`FULL_PARALLEL`, `SEQUENTIAL_BREAK`, `REVERSE_SEQUENTIAL_BREAK` [Strategy description](#strategy-description)|`FULL_PARALLEL`|
|`amazon.rater.url-template` |Url template to Amazon Completion API that is used in score calculation. Required to have an attribute notated as `{keyword}` within url| - | `https://completion.amazon.com/search/complete?search-alias=aps&mkt=1&q={keyword}`


#### Accuracy level
Each level describes how many parts the keyword will be divided into. By having a higher accuracy level the precision of 
final result will be increased, as a drawback computation takes longer.
* **HIGH** - Split by each character. *Example: For keyword 'ABCD', division is 'A', 'B', 'C', 'D'*
* **MEDIUM** - Split by each second character. *Example: For keyword 'ABCD', division is  'AB', 'CD'*
* **LOW** - Split by each third character. *Example: For keyword 'ABCD', division is 'ABC', 'D'*

#### Strategy description

* **FULL_PARALLEL** - Perform a parallel search for all created parts and finally merge them into final result, most optimal 
                      in terms of speed, drawback is that requires much more resources than other.  
* **SEQUENTIAL_BREAK** - Perform a sequential search from left to right until the first occurrence (there's no reason to continue search, 
                         because it's obvious that each following querying will contain the keyword).
                         **Could be used to search most popular words.**

* **REVERSE_SEQUENTIAL_BREAK** - Perform a sequential search from right to left until the first disappearance occur 
                                (there's no reason to continue search, because it's obvious that each following querying 
                                will miss the keyword). **Could be used to search less popular words.**

### If it would be a real world application without restrictions I'd follow next steps: 
1. Develop application following TDD.
2. Implement REST-Api Integration test
3. Add application level in memory cache to avoid calling web-api multiple times for the same queries.
4. Add web level cache by using Cache-Control header.
5. Use all the SearchStrategies based on following logic: 
```
* Use SEQUENTIAL_BREAK strategy on application startup perfrom a Cache Warming for top search positions.
* Use FULL_PARALLEL strategy when demand is low and for the priority clients.
* Use REVERSE_SEQUENTIAL_BREAK strategy for high demand and less searched keywords.
```