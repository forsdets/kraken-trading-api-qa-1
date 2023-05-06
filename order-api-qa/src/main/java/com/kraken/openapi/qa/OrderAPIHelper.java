package com.kraken.openapi.qa;

import com.kraken.reusable.lib.Helper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;

import static com.kraken.openapi.qa.OrderAPIConstants.PROPERTY_FILE_PATH;
import static com.kraken.openapi.qa.StepDefinitions.queryParam;

public class OrderAPIHelper {

    static Logger logger = Logger.getLogger(OrderAPIHelper.class);

    public static Response getApiResponse(RequestSpecification requestSpecification) {
        int noOfQueryParam = queryParam.size();
        String staticURI = Helper.getPropertyValue(PROPERTY_FILE_PATH, "static_uri");
        String getOrderBookEndpoint = Helper.getPropertyValue(PROPERTY_FILE_PATH, "book_endpoint");
        String getOrderBookEndpointWithCount = Helper.getPropertyValue(PROPERTY_FILE_PATH, "book_endpoint_with_count");
        if (noOfQueryParam == 1 && queryParam.containsKey("pair")) {
            logger.info("URI to route::: " + staticURI + getOrderBookEndpoint);
            requestSpecification.baseUri(staticURI);
            return requestSpecification.get(getOrderBookEndpoint, queryParam.get("pair"));
        } else {
            logger.info("URI to route::: " + staticURI + getOrderBookEndpoint + getOrderBookEndpointWithCount);
            requestSpecification.baseUri(staticURI);
            return requestSpecification.get(getOrderBookEndpoint + getOrderBookEndpointWithCount, queryParam.get("pair"), queryParam.get("count"));
        }
    }

    public static Response postApiResponse(RequestSpecification requestSpecification) {
        String staticURI = Helper.getPropertyValue(PROPERTY_FILE_PATH, "static_uri");
        String getOrderBookEndpoint = Helper.getPropertyValue(PROPERTY_FILE_PATH, "book_endpoint");
        logger.info("URI to route::: " + staticURI + getOrderBookEndpoint);
        requestSpecification.baseUri(staticURI);
        return requestSpecification.post(getOrderBookEndpoint);
    }

    public static Response putApiResponse(RequestSpecification requestSpecification) {
        String staticURI = Helper.getPropertyValue(PROPERTY_FILE_PATH, "static_uri");
        String getOrderBookEndpoint = Helper.getPropertyValue(PROPERTY_FILE_PATH, "book_endpoint");
        logger.info("URI to route::: " + staticURI + getOrderBookEndpoint);
        requestSpecification.baseUri(staticURI);
        return requestSpecification.put(getOrderBookEndpoint);
    }

    public static Response patchApiResponse(RequestSpecification requestSpecification) {
        String staticURI = Helper.getPropertyValue(PROPERTY_FILE_PATH, "static_uri");
        String getOrderBookEndpoint = Helper.getPropertyValue(PROPERTY_FILE_PATH, "book_endpoint");
        logger.info("URI to route::: " + staticURI + getOrderBookEndpoint);
        requestSpecification.baseUri(staticURI);
        return requestSpecification.patch(getOrderBookEndpoint);
    }
}