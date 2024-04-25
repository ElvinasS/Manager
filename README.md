# Manager
 Bank Account Manager system

    Requires Docker 
    Postman to submit new csv files

 To run solution:
* Copy repository to local storage
* Open project in intellij
* * either run command "mvn clean package" 
* * or
* * from Maven tab select Lifecycle > package
* Open terminal in project folder
* * run command:
* * "docker build -t bank-management-system ." 
* * run command:
* * "docker-compose up"

 At this point solution should be running with both application and postgresql server on docker.

 Highlights:
    
 Available api request can be seen at:
 * http://localhost:8080/swagger-ui/index.html 

 Overview:
 * GET request
 * http://localhost:8080/api/v1/Statement
 * * Will return all existing statements in database
 *
 * GET request
 * http://localhost:8080/api/v1/Statement/exportCsv
 * * Will export csv files with all statements
 * * using optional dateFrom and dateTo parameter allows to export statements from selected timeframe
 * * * example of request with parameters:
 * * * * http://localhost:8080/api/v1/Statement/exportCsv?dateFrom=2014-01-01T00:00:00&dateTo=2024-01-06T00:00:00
 *
 * POST request
 * http://localhost:8080/api/v1/Statement/uploadCsv
 * * Used to import new statements, can be either single or multiple files, with multiple statements in single file.
 * * Using Postman, in request body select "form-data", key name = files, value = csv file/s with statements.
 *
 * GET request
 * http://localhost:8080/api/v1/Statement/balance
 * * Will return balance for specified account
 * * Requires parameter "accountNumber", example: 
 * * * http://localhost:8080/api/v1/Statement/balance?accountNumber=12345678901
 * * Optionaly allows to select timeframe for balance calculation with parameters dateFrom and dateTo, example:
 * * * http://localhost:8080/api/v1/Statement/balance?accountNumber=12345678901&dateFrom=2014-01-05T00:00:00&dateTo=2015-01-06T00:00:00

 Notes:
 * Test data csv files included in project folder.
 * After running solution for the first time, database should be empty, and GET request should not return anything.
 * Balance calculation only takes in to consideration statements, and always counts from 0.
 * Currency format is not taken in to consideration.

