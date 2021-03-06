# Revolut Tech Challenge

## Prerequisites
* java 

## Running the application locally

* Maven wrapper is being used in the app and hence the running environment need not install/contain maven.

#### Running tests From Terminal:

```sh
$ ./mvnw test
```
#### Running the application:

```sh
$ ./mvnw clean install 
$ ./mvnw exec:java --> runs the applictaion on port 8080 on default
```

## Technologies

* Used `jetty-jesrsey` for server and REST framework, as it is suitable for light-weight rest applications.
* Used `ConcurrentHashMap` for in memory storage as it handles concurrency, synchronization and thread-safe functionality while processing multiple requests on same account.(all data will be erased after the application is terminated).
* git for version control.

## API design

The app exposes different features under two main controllers (all example requests are given below separately)

* To handle requests regarding transactions in a Transaction Controller
 
1. POST api for Creating account, with endpoint `api/account/create`. It takes no parameter and returns an account object.

2. GET api for getting balance with endpoint `/api/account/balance/{accountNumber}` . It takes account number as query param and returns an account object with balance 

3. POST api for deposit/credit amount into an account with endpoint `/api/account/deposit` . It takes an json with `accountNumber` and `amount` in the post request body and returns an account object with updated balance for the account number.

4. POST api for withdraw/debit amount from an account with endpoint `/api/account/withdraw` . It takes an json with `accountNumber` and `amount` in the post request body and returns an account object with updated balance for the account number.

* To handle transfer request in a Transfer Controller

1. POST request to transfer amount from a source account to a target account.  with endpoint `/api/transfer` . It takes an json with `sourceAccount` ,`targetAccount` and `amount` in the post request body and returns 200 as status code if the request is successful with no response body.

## Testing

* Unit tests for all service methods in both transfer and transaction sevrices are given 
* Integration tests for both controllers are also specified(the transfer controller integration test gives complete flow of all services used).
* A unit test for complete flow testing is also specified in addition to above tests.



## Example Tests and Results

#### 1. Create two accounts:

* Test url:`curl -X POST http://localhost:8080/api/account/create `

** runnig the same service twice for two accounts which will be used for tetsing the below requets

* Example Output1:
`{
    "accountNumber": "dfc58e1c-79fd-4ce5-9437-1a8175b58b9a",
    "balance": 0
}`
* Example Output2:
`{
    "accountNumber": "6c54b0ed-79d8-491d-8cac-2844dab4a221",
    "balance": 0
}`

** please use the same account numbers in the tested example outputs for all the below requests

#### 2. Get Balance: 

* Test url:`curl -X GET  http://localhost:8080/api/account/balance/dfc58e1c-79fd-4ce5-9437-1a8175b58b9a `

* Output: 
`{
    "accountNumber": "dfc58e1c-79fd-4ce5-9437-1a8175b58b9a",
    "balance": 0
}`
#### 3. Deposit in the first Account: 

* Test url:
`curl -X POST   http://localhost:8080/api/account/deposit   -H 'content-type: application/json' -d '{"accountNumber":"dfc58e1c-79fd-4ce5-9437-1a8175b58b9a","amount":"1000"}'`

* Output: 
`{
    "accountNumber": "dfc58e1c-79fd-4ce5-9437-1a8175b58b9a",
    "balance": 1000
}`

#### 4. Withdraw from the first Account: 

* Test url:
`curl -X POST  http://localhost:8080/api/account/withdraw  -H 'content-type: application/json'  -d '{"accountNumber":"dfc58e1c-79fd-4ce5-9437-1a8175b58b9a","amount":"100"}'	`

* Output: 
`{
    "accountNumber": "dfc58e1c-79fd-4ce5-9437-1a8175b58b9a",
    "balance": 900
}`

#### 5. Transfer amount from account 1 to account 2 (created above):

* Test url:
`curl -XPOST -H "Content-type: application/json" -d '{"sourceAccount":"dfc58e1c-79fd-4ce5-9437-1a8175b58b9a","targetAccount":"6c54b0ed-79d8-491d-8cac-2844dab4a221","amount":"100"}' 'localhost:8080/api/transfer'`

* Output: status 200.

#### 6. Check balance in source and target accounts for verification :

##### balance in source:

* Test url:`curl -X GET  http://localhost:8080/api/account/balance/dfc58e1c-79fd-4ce5-9437-1a8175b58b9a `

* output:
`{
    "accountNumber": "dfc58e1c-79fd-4ce5-9437-1a8175b58b9a",
    "balance": 800
}`

##### balance in target:

* Test url: `curl -X GET  http://localhost:8080/api/account/balance/6c54b0ed-79d8-491d-8cac-2844dab4a221 `

* output:
`{
    "accountNumber": "dfc58e1c-79fd-4ce5-9437-1a8175b58b9a",
    "balance": 100
}`


## Improvements
* In memory databases like H2 can be used for the applications of this kind for improved entity relationships.
* Annotations like lombok can be used to avoid overriding of methods(equals , hashcode)  in reponse/model Objects.



