# Background

The services implementation is based on the provided RAML specification. Out of curiosity, I decided to examine the current state of the art regarding:

* RAML code generation
* Declaritive validation in JAX-RS (e.g. Bean Validation)

as part of the impelementation.

After a bit of research, I settled on [RAML for JAX-RS](https://github.com/mulesoft-labs/raml-for-jax-rs) from MuleSoft Labs as it appeared to have the feature set I was lookings for as well as being associated with the company behind RAML itself. Typical of open source project, the documentation is lacking. However, there are example configurations that can be used as a starting point and with a bit experimentation, can be made to work. In particular, the modular schema took some time to figure out.

This tool uses another tool under the hood to generate Java from JSON schema that appears to be from [these guys](http://www.jsonschema2pojo.org/). The code it generates is generally fine, but the class names generated for types was thoroughly abhorrant (e.g. Json_1233535453.java or similar); just a complete non-starter. The solution they provide to customize type naming is the ability to annotate the JSON schema with name value. This means adding the following to each type of interest:
```
"javaType" : "my.path.MyCustomType"
```
I took the liberty of modifying the schema with a few of these. (I also need to fix one typo in the definitions: String -> string.)

This practice also helped a bit with another issue regarding generated code: **it's not DRY!** Request and response models differ in the fact that the response adds a couple of addtional properties however the generated code duplicates all the common properties in each generated type. Same with asset allocations, which are generated twice (I tricked the generator into overwriting the first with the second rather than generating separate types). This criticism is likey misplaced; the generator is simply following the JSON schema definitions to the letter; duplication in the schema leads to duplication in the code.

Things are now reasonable regarding the generated model types but the resource type code generated is difficult to explain. The claim is supprt for JSR-303 Validation and various various annotation are present in the generated code but I'm not able to understand from the generated resource interface how that is actually supposed happen. Annotatings either a request or response type with `@Valid` automatically causes the instance to be run through the validation framework but the generated interface does not use this annotation. Adding it to the implementation would then require that **all** of the annotations from the interface be duplicated in the implementation. Even if one follows that course, the JAX-RS fromework performs the validation **prior** to calling the implementation code, in which case, how is the implementation suppsed to make use of all those fancy (fugly) response types is generated? Additionally, I could find no way to specify custom JSR-303 annotations to be applied to a complex type on generated code (on models to validate allocation percentage, for example). This is where I drew the line in following this path and decided to cut my losses.

Both the code generation and the beginnings of an attempt to implement the generated interface are left in the project for posterity. Code generation is performed by the `trizic-api` model in the `api/specification` directory. Interface implementation is in the `trizic-service` in the `api/service` directory along the final implemenation. It is included in the `com.trizic.api.service.junked` package.

I ended up using some of the generated types as the basis for the implementation going forward, simply to save some typing. Since code is no longer generated, I was free to name types and add annotations as I saw fit.

In conclusion, the code generator I choose to experiment with left much to be desired, at least on first glance. The resulting code reminded me much of the SOAP code generators back in the old days and that's not a complement. There may ways to do the things I felt were missing upon further investigation, things may improve with RAML 1.0 support (none today) or I may wake up tomorrow and decide I've become a masochist.

# Implemenation

The technology stack is comprised of:

* Java
* JAX-RS
* Bean Validation (JSR-303)
* JWT
* Jetty
* TestNG
* Maven

I took a couple of liberties in terms of validation. Percentage values are a range [0 - 100] and are tested for validity in this implemenation, except for asset allocation percentages, which were specified as ```number``` and is coverted to ```Double``` in Java. This is a remnant of the code generator and I really should have changed this to use ```BigDecimal``` as doubles are notoriously prone to rounding errors and shouldn't be used anywhere near anything having to do with money.

The model paging reponse was decorated with the ```Link``` response header as an example of an alternative, standard based way of providing the equivalent paging metadata currently presented in the response.

One liberty I did not take was to expand the error type to allow for returning multiple errors. One thing that frustrates me as a service client is the need for a invoke, rinse, repeat cycle because a service only returns one error at at time. You really never know how close you are to compliance until you actually get there. Allowing for multiple errors let's one know how close (or not) they are. This implementation is not only capable, but geared toward responding with all known error conditions. To satisfy the single error requirment, one error of many was picked at random.

# Layout

The source code is organized as a Maven multi-module, hierarchical layout. The following Maven modules are present:

* api-docs the RAML service specification and documentation
* api root directory of the service implementation; this is the root for the Maven project
* api/specification code generation from RAML to Java (no artifacts from this module are actually used) 
* api/service the service implementation.

# Running the service

First, the service must be built.

```
# cd api
# mvn clean verify
```
This command will compile and package the service as well as running the unit and functional tests.

Next, the service must be started.

```
# cd service
mvn exec:java [--port=#] [-Dtrizic.api.data=<data persistence directory>] [-Dcom.trizic.auth.disable=<true|false>]
```

This command will lauch a Jetty web server containing the service implemenation. All command line arguments are optional. The following agruments may be specified:

* --port listen on a custom port; the default is 8080.
* -Dtrizic.api.data specify a directory for service data to be persisted. This value must be a directory that you have write access to and will be created if it does not exist. By default, the service creates a temporary directory for this purpose which is deleted when the server is stopped. To retain data across server restarts, this property **must** be used. 
* -Dcom.trizic.auth.disable setting to ```true``` disables service authorization checking. Any other value, as well as the default, is to require client to present an API key to assert any privileges.

Now, you can access the service using your HTTP client of choice (mine appears to be ```curl```). 

The first step a client must perform is to register as an advisor.
```
# curl -v -X POST -H accept:application/json http://localhost:8080/v1/registration/advisors
```
**201 Created**
```json
{
  "advisorId":"6523384a-4c5a-43b3-81bf-5caf2bca9cf6",
  "apiKey":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NTIzMzg0YS00YzVhLTQzYjMtODFiZi01Y2FmMmJjYTljZjYiLCJpYXQiOjE0ODk4MDQ0NzMsInN1YiI6IjY1MjMzODRhLTRjNWEtNDNiMy04MWJmLTVjYWYyYmNhOWNmNiIsImlzcyI6ImNvbS50cml6aWMuYXBpLnYxIn0.jsf2A37-0QggzYXye_30leHgRJznCNQU7GJj9d9gkRg"
}
```
The response is a JSON Web Token (JWT). It contains an advisor ID along with an API key. The ID is used to satisfy the path parameter in service calls. The API key is used to authorize the client and **must** be passed as a Bearer token in the Authorization header with all subsequest requests unless access control was disabled.

The next step is to create some model data. Make sure to use the advisor and api key from registration.
```
# curl -v -d @src/test/resources/example-model-1.json -X PUT -H "authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NTIzMzg0YS00YzVhLTQzYjMtODFiZi01Y2FmMmJjYTljZjYiLCJpYXQiOjE0ODk4MDQ0NzMsInN1YiI6IjY1MjMzODRhLTRjNWEtNDNiMy04MWJmLTVjYWYyYmNhOWNmNiIsImlzcyI6ImNvbS50cml6aWMuYXBpLnYxIn0.jsf2A37-0QggzYXye_30leHgRJznCNQU7GJj9d9gkRg" -H content-type:application/json -H accept:application/json http://localhost:8080/v1/advisor/6523384a-4c5a-43b3-81bf-5caf2bca9cf6/model
```
**200 OK**
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
Once some models have been created, they can be fetched from the service. Fetched results represent one page of data rather than the entrire dataset. The caller specifies a page size and page number to specify the actual page of data they required. The implementation uses the defaults of pageNumber = 1, pageSize = 20 when not specified by the client.
```
# curl -v -H "authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NTIzMzg0YS00YzVhLTQzYjMtODFiZi01Y2FmMmJjYTljZjYiLCJpYXQiOjE0ODk4MDQ0NzMsInN1YiI6IjY1MjMzODRhLTRjNWEtNDNiMy04MWJmLTVjYWYyYmNhOWNmNiIsImlzcyI6ImNvbS50cml6aWMuYXBpLnYxIn0.jsf2A37-0QggzYXye_30leHgRJznCNQU7GJj9d9gkRg" -H content-type:application/json -H accept:application/json http://localhost:8080/v1/advisor/6523384a-4c5a-43b3-81bf-5caf2bca9cf6/model
```

**200 OK**
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

