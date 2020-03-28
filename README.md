# Transactions
The main use case for our API is
to calculate real time statistic from the last 60 seconds. There will be three APIs, one of
them is called every time a transaction is made. It is also the sole input of this rest
API. Second  one returns the statistic based of the transactions of the last 60.
Third API to delete all transactions.

## Specs

### Transactions

Every Time a new transaction happened, this endpoint will be called.

Where:
* amount is BigDecimal specifying transaction amount
* timestamp is in UTC time zone specifying transaction time

#### Request sample
```http
POST /api/transactions HTTP/1.1
Content-Type: application/json

{
  "amount": "6.5",
  "timestamp": "2018-10-21T11:13:23.594373900Z"
}

```
#### Success response sample
```http
HTTP/1.1 201 Created
```

#### Error response sample - when any field is not parsable
```http
HTTP/1.1 422
```
#### Error response sample - json is invalid
```http
HTTP/1.1 400
```

### Statistics
This is the main endpoint of this task. It returns the statistic based on the transactions which happened
in the last 60 seconds.

#### Request sample
```http
GET /api/statistics HTTP/1.1
Accept: application/json
```

#### Response sample
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "sum": 1000,
    "avg": 100,
    "max": 200,
    "min": 50,
    "count": 10
}
```

Where:
* sum is a BigDecimal specifying the total sum of transaction value in the last 60
seconds
* avg is a BigDecimal specifying the average amount of transaction value in the last
60 seconds
* max is a BigDecimal specifying single highest transaction value in the last 60
seconds
* min is a BigDecimal specifying single lowest transaction value in the last 60
seconds
* count is a long specifying the total number of transactions happened in the last
60 seconds

## Requirements

Other requirements, which are obvious, but also listed here explicitly:
* The API have to be threadsafe with concurrent requests
* The application should delete transactions which become older than 60 seconds
* The API have to function properly, with proper result
* The project should be buildable, and tests should also complete successfully. e.g. If maven is used, then mvn clean install should complete successfully.
* The API should be able to deal with time discrepancy, which means, at any point of time, we could receive a transaction which have a timestamp of the past
* Make sure to send the case in memory solution without database (including in-memory database)
* Done
