
package com.kraken.reusable.lib;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class Helper {

    static Logger logger = Logger.getLogger(Helper.class);

    public static String getProperty(String name) {
        return System.getProperty(name);
    }

    private static final int NOT_FOUND = -1;
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOW_SEPARATOR = '\\';

    public static String getPropertyValue(String filePath, String property) {

        String propertyValue = "";
        try (InputStream input = new FileInputStream(filePath)) {
            Properties prop = new Properties();
            prop.load(input);
            propertyValue = prop.getProperty(property);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return propertyValue;
    }

    public static Map<String, String> makeRequestHeaders(String expectedHeaderFiles) throws IOException {
        Map<String, String> headerFields = new HashMap<>();
        JSONObject jsonHeaders = new JSONObject(expectedHeaderFiles);
        String str = jsonHeaders.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            headerFields = objectMapper.readValue(str, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            logger.error("File input exception", e);
        }
        return headerFields;
    }

    public static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    public static void updatedRequestHeaders(Map<String, String> requestHeaderFields, RequestSpecification requestSpecification) {
        for (Map.Entry<String, String> entry : requestHeaderFields.entrySet()) {
            requestSpecification.header(new Header(entry.getKey(), entry.getValue()));
        }
    }

    public static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }


    public static void assertAPIResponse(JSONObject expectedJson, JSONObject actualJson) {
        JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
        logger.info("Expected Response: " + expectedJson);
        logger.info("Actual Response: " + actualJson);
    }

    public static String getName(final String fileName) {
        if (fileName == null) {
            return null;
        }
        failIfNullbytePresent(fileName);
        final int index = indexOfLineSeparator(fileName);
        return fileName.substring(index + 1);
    }

    private static int indexOfLineSeparator(String fileName) {
        if (fileName == null) {
            return NOT_FOUND;
        }
        final int lastUnixPos = fileName.lastIndexOf(UNIX_SEPARATOR);
        final int lastWindowPos = fileName.lastIndexOf(WINDOW_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowPos);

    }

    private static void failIfNullbytePresent(final String path) {
        final int len = path.length();
        for (int i = 0; i < len; i++) {
            if (path.charAt(i) == 0) {
                throw new IllegalArgumentException("Null byte present in the file/path name. There are no " +
                        "known legitimate use cases for such data, but several injection attacks may use it");
            }
        }
    }
}


