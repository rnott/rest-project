# Implemention

# Running

1. Build
```
# cd api
# mvn clean verify
```

2. Run the service
```
# cd service
mvn exec:java [-Dtrizic.api.data=<data persistence directory>] [-Dcom.trizic.auth.disable=<true|false>]
```

3. Using your HTTP client of choice:

  a. Register an advisor
```
# curl -v -X POST -H accept:application/json http://localhost:8080/v1/registration/advisors
```

*201 Created*
```json
{
  "advisorId":"6523384a-4c5a-43b3-81bf-5caf2bca9cf6",
  "apiKey":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NTIzMzg0YS00YzVhLTQzYjMtODFiZi01Y2FmMmJjYTljZjYiLCJpYXQiOjE0ODk4MDQ0NzMsInN1YiI6IjY1MjMzODRhLTRjNWEtNDNiMy04MWJmLTVjYWYyYmNhOWNmNiIsImlzcyI6ImNvbS50cml6aWMuYXBpLnYxIn0.jsf2A37-0QggzYXye_30leHgRJznCNQU7GJj9d9gkRg"
}
```

An advisor ID is created along with an API key. The ID is used to satisfy the path parameter in service calls. The API key is used to authorize the client and is passed as a Bearer token in the Authorization header.

  b. Create a model
```
# curl -v -d @src/test/resources/example-model-1.json -X PUT -H "authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NTIzMzg0YS00YzVhLTQzYjMtODFiZi01Y2FmMmJjYTljZjYiLCJpYXQiOjE0ODk4MDQ0NzMsInN1YiI6IjY1MjMzODRhLTRjNWEtNDNiMy04MWJmLTVjYWYyYmNhOWNmNiIsImlzcyI6ImNvbS50cml6aWMuYXBpLnYxIn0.jsf2A37-0QggzYXye_30leHgRJznCNQU7GJj9d9gkRg" -H content-type:application/json -H accept:application/json http://localhost:8080/v1/advisor/6523384a-4c5a-43b3-81bf-5caf2bca9cf6/model
```
*200 OK*
```json
{
  "guid":"63b4aa75-800d-460f-bc98-e3310d67578b",
  "createdOn":"2017-03-17T19:47:24-0700",
  "advisorId":"6523384a-4c5a-43b3-81bf-5caf2bca9cf6",
  "name":"example model 1",
  "description":"example model with tech stocks",
  "cashHoldingPercentage":10,
  "driftPercentage":5,
  "modelType":"TAXABLE",
  "rebalanceFrequency":"QUARTERLY",
  "assetAllocations":[
    {
      "symbol":"AAPL",
      "percentage":30.0
    },{
      "symbol":"GOOG",
      "percentage":20.0
    },{
      "symbol":"IBM",
      "percentage":15.0
    },{
      "symbol":"FB","percentage":25.0
    }
  ]
}
```
  c. Read existing model (defaults to pageNumber = 1, pageSize = 20)
```
# curl -v -H "authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NTIzMzg0YS00YzVhLTQzYjMtODFiZi01Y2FmMmJjYTljZjYiLCJpYXQiOjE0ODk4MDQ0NzMsInN1YiI6IjY1MjMzODRhLTRjNWEtNDNiMy04MWJmLTVjYWYyYmNhOWNmNiIsImlzcyI6ImNvbS50cml6aWMuYXBpLnYxIn0.jsf2A37-0QggzYXye_30leHgRJznCNQU7GJj9d9gkRg" -H content-type:application/json -H accept:application/json http://localhost:8080/v1/advisor/6523384a-4c5a-43b3-81bf-5caf2bca9cf6/model
```

*200 OK*
```json
{
  "pageNumber":1,
  "pageSize":20,
  "numberOfPages":1,
  "totalNumberOfElements":1,
  "page":[
    {
      "guid":"63b4aa75-800d-460f-bc98-e3310d67578b",
      "createdOn":"2017-03-17T19:47:24-0700",
      "advisorId":"6523384a-4c5a-43b3-81bf-5caf2bca9cf6",
      "name":"example model 1",
      "description":"example model with tech stocks",
      "cashHoldingPercentage":10,
      "driftPercentage":5,
      "modelType":"TAXABLE",
      "rebalanceFrequency":"QUARTERLY",
      "assetAllocations":[
        {
          "symbol":"AAPL",
          "percentage":30.0
        }, {
          "symbol":"GOOG",
          "percentage":20.0
        }, {
          "symbol":"IBM",
          "percentage":15.0
        }, {
          "symbol":"FB",
          "percentage":25.0
        }
      ]
    }
  ]
}
```

# Adendum

