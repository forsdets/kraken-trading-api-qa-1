package com.kraken.openapi.qa;

import com.kraken.reusable.lib.Helper;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.kraken.openapi.qa.OrderAPIConstants.ORDER_API_EXPECTED_RESPONSE_FILE_PATH;
import static com.kraken.openapi.qa.OrderAPIConstants.ORDER_API_HEADER_FILE_PATH;
import static com.kraken.openapi.qa.OrderAPIHelper.*;
import static com.kraken.reusable.lib.Helper.readFileAsString;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class StepDefinitions {
    public static String scenarioName;
    public static String featureName;

    static Logger logger = Logger.getLogger(StepDefinitions.class);

    RequestSpecification requestSpecification = RestAssured.given();
    Map<String, String> requestHeaderFields = null;
    public static Map<String, String> queryParam = new HashMap<>();
    public Response response = null;

    public JSONObject actualResponse = null;
    public JSONObject expectedResponseTexts = null;
    public JSONObject expectedResponse = null;

    @Before
    public void updateScenario(Scenario scenario) {
        featureName = Helper.getName(scenario.getUri().toString());
        scenarioName = scenario.getName();
        logger.info("Feature Name" + featureName);
        logger.info("Scenario Name" + scenarioName);
        StepDefinitions.queryParam.clear();
    }

    @Given("^the book api service and the headers are available$")
    public void makeHeaders() throws Exception {
        requestHeaderFields = new HashMap<>();
        String expectedHeaderFiles = readFileAsString(ORDER_API_HEADER_FILE_PATH);
        requestHeaderFields = Helper.makeRequestHeaders(expectedHeaderFiles);
    }

    @And("^the header is missing with the field (.*)$")
    public void removeHeader(String headerToRemove) {
        requestHeaderFields.remove(headerToRemove);
    }

    @And("^the header (.*) with value (.*) is available in the request$")
    public void updateHeader(String headerTag, String headerValue) {
        if (headerValue.equalsIgnoreCase("empty_value")) {
            requestHeaderFields.put(headerTag, "");
        } else {
            requestHeaderFields.put(headerTag, headerValue);
        }
    }

    @And("the request is ready with the headers")
    public void theRequestIsReadyWithTheHeaders() {
        Helper.updatedRequestHeaders(requestHeaderFields, requestSpecification);
    }

    @And("^the endpoint path (.*) as (.*)$")
    public void theEndpointPathBookidAs(String key, String value) {
        queryParam.put(key, value);
    }

    @When("^the (.*) order book details are retrieved from the service$")
    public void getRequest(String requestType) {
        switch (requestType.toLowerCase(Locale.ROOT)) {
            case "post":
                response = postApiResponse(requestSpecification);
                break;
            case "put":
                response = putApiResponse(requestSpecification);
                break;
            case "patch":
                response = patchApiResponse(requestSpecification);
                break;
            case "get":
                response = getApiResponse(requestSpecification);
                break;
        }
    }

    @Then("the expected response fields are available to validate")
    public void theExpectedResponseFieldsAreAvailableToValidate() throws Exception {
        actualResponse = new JSONObject(response.body().prettyPeek().asString());

        String expectedResponseFilePath = Helper.readFileAsString(ORDER_API_EXPECTED_RESPONSE_FILE_PATH);
        expectedResponseTexts = new JSONObject(expectedResponseFilePath);
        String expectedScenarioTag = expectedResponseTexts.getJSONObject(featureName).get(scenarioName).toString();
        expectedResponse = new JSONObject(expectedScenarioTag);
    }

    @And("the HTTP code should be {int}")
    public void theCodeShouldBe(int value) {
        Assert.assertEquals(value, response.getStatusCode());
    }

    @And("^API (.*) (.*) contains the correct schema structure$")
    public void validateSchema(final String requestType, final String operationType) {
        if (operationType.toLowerCase(Locale.ROOT).equalsIgnoreCase("request") && requestType.toLowerCase(Locale.ROOT).equalsIgnoreCase("post")) {
            requestSpecification.then().body(matchesJsonSchemaInClasspath("schemaFiles/order_book_request_schema.json"));
        } else if (operationType.toLowerCase(Locale.ROOT).equalsIgnoreCase("response") && requestType.toLowerCase(Locale.ROOT).equalsIgnoreCase("post")) {
            response.then().body(matchesJsonSchemaInClasspath("schemaFiles/order_book_response_schema.json"));
        }
//        repeat the same code for put/patch/get
    }

    @And("the API actual response should match with the expected response")
    public void theAPIActualResponseShouldMatchWithTheExpectedResponse() {
        Helper.assertAPIResponse(expectedResponse, actualResponse);
    }
}
