# REST API Solution

The implemented solution can be found [here](https://github.com/rnott/rest-project/tree/master/api).

# Create a REST API from a RAML spec

Your task is to create a REST API the implements the end points available in the
provided RAML specification.

Please complete **any** number of the steps below within **5 days** of us
sending you this document. You may put in as many hours as you wish, but we
recommend a maximum of around 5 hours. Use any framework and languages you
would like (Java encouraged) to complete the task.  Update
your forked repository with your solution and a README.md with any steps needed to run it.

We are looking for code quality and innovation/thoughtfulness.

### Part I: Get it

Fork this repository.

Follow the instructions in the `api-docs` directory to view the RAML specification.  The
RAML specification defines an endpoint /v1/advisor/{advisorId}/model and the json schema
for a portfolio model.  A portfolio model is a way for an advisor to specify a group of
asset allocations and rules to manage a portfolio,
see <http://www.investopedia.com/terms/p/portfoliomanagement.asp>.

Create a REST API application that allows a user to add portfolio models for an advisor
and retrieve a list of portfolio models for an advisor.

There should be some persistent store backing the REST API to keep all the models. The
choice is yours in what to use.

The PUT call to add a model should be idempotent based on the model name.  Meaning that
multiple calls with the same model name will not add additional models to the advisor but
overwrite the existing model with that name.
Models with a different name should be added to the list of models for that advisor.

Add validation to make sure the sum of the cash allocation percentage and all of the
individual asset allocations percentage equals 100%.

The GET call should return all models for the specified advisorId as well as support
paging based on query parameters.

Please include unit/integration tests.

**Bonuses/for fun**:
- Validate the request and response from your implementation match the RAML specification.
- Add authentication/authorization so that a logged in advisor has access to only their
own models.
