package com.dt.invocing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.dt.invocing.model.CurrencyType;
import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InvoicingApplicationIT extends BaseTest {

    private static final String DOCUMENT = "document";

    private static final String TARGET_CURRENCY = "targetCurrency";

    private static final String EXCHANGE_RATES = "exchangeRates";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void sumInvoicesWithInvalidData() {

        ClassLoader classLoader = getClass().getClassLoader();
        FileSystemResource fileResource = new FileSystemResource(
                new File(classLoader.getResource(INVALID_FILE_TXT).getFile()));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = buildMultipartRequestEntity(
                "USD", fileResource);
        ResponseEntity<JsonNode> response = template.postForEntity(
                "http://localhost:" + port + "/sum", requestEntity,
                JsonNode.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
                response.getStatusCode().value(),
                "Status code differnt than 400");
        System.out.println("Response:" + response.toString());

    }

    @Test
    public void sumInvoicesAllCustomersCurrencyUSD() {

        ClassLoader classLoader = getClass().getClassLoader();
        FileSystemResource fileResource = new FileSystemResource(
                new File(classLoader.getResource(DATA_CSV).getFile()));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = buildMultipartRequestEntity(
                "USD", fileResource);
        ResponseEntity<JsonNode> response = template.postForEntity(
                "http://localhost:" + port + "/sum", requestEntity,
                JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK.value(),
                response.getStatusCode().value(),
                "Status code differnt than 200");
        Assertions.assertEquals(4739.16,
                response.getBody().findValue("documentsSum").asDouble());
        System.out.println("Response:" + response.toString());

    }

    @Test
    public void sumInvoicesAllCustomersCurrencyEUR() {

        ClassLoader classLoader = getClass().getClassLoader();
        FileSystemResource fileResource = new FileSystemResource(
                new File(classLoader.getResource(DATA_CSV).getFile()));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = buildMultipartRequestEntity(
                "EUR", fileResource);
        ResponseEntity<JsonNode> response = template.postForEntity(
                "http://localhost:" + port + "/sum", requestEntity,
                JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK.value(),
                response.getStatusCode().value(),
                "Status code differnt than 200");
        Assertions.assertEquals(4308.33,
                response.getBody().findValue("documentsSum").asDouble());
        System.out.println("Response:" + response.toString());
    }

    @Test
    public void sumInvoicesAllCustomersCurrencyGBP() {

        ClassLoader classLoader = getClass().getClassLoader();
        FileSystemResource fileResource = new FileSystemResource(
                new File(classLoader.getResource(DATA_CSV).getFile()));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = buildMultipartRequestEntity(
                "GBP", fileResource);
        ResponseEntity<JsonNode> response = template.postForEntity(
                "http://localhost:" + port + "/sum", requestEntity,
                JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK.value(),
                response.getStatusCode().value(),
                "Status code differnt than 200");
        Assertions.assertEquals(3575.91,
                response.getBody().findValue("documentsSum").asDouble());
        System.out.println("Response:" + response.toString());
    }
    @Test
    public void sumInvoicesSingleCustomerCurrencyGBP() {

        ClassLoader classLoader = getClass().getClassLoader();
        FileSystemResource fileResource = new FileSystemResource(
                new File(classLoader.getResource(DATA_CSV).getFile()));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = buildMultipartRequestEntity(
                "GBP", fileResource);

        ResponseEntity<JsonNode> response = template.postForEntity(
                "http://localhost:" + port + "/sum" + "?customerVat=123456789",
                requestEntity, JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK.value(),
                response.getStatusCode().value(),
                "Status code differnt than 200");
        Assertions.assertEquals(1596.82,
                response.getBody().findValue("documentsSum").asDouble());
        System.out.println("Response:" + response.toString());
    }
    @Test
    public void sumInvoicesSingleCustomerCurrencyEUR() {

        ClassLoader classLoader = getClass().getClassLoader();
        FileSystemResource fileResource = new FileSystemResource(
                new File(classLoader.getResource(DATA_CSV).getFile()));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = buildMultipartRequestEntity(
                "EUR", fileResource);

        ResponseEntity<JsonNode> response = template.postForEntity(
                "http://localhost:" + port + "/sum" + "?customerVat=123456789",
                requestEntity, JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK.value(),
                response.getStatusCode().value(),
                "Status code differnt than 200");
        Assertions.assertEquals(1923.88,
                response.getBody().findValue("documentsSum").asDouble());

        System.out.println("Response:" + response.toString());
    }
    @Test
    public void sumInvoicesSingleCustomerCurrencyUSD() {

        ClassLoader classLoader = getClass().getClassLoader();
        FileSystemResource fileResource = new FileSystemResource(
                new File(classLoader.getResource(DATA_CSV).getFile()));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = buildMultipartRequestEntity(
                "USD", fileResource);

        ResponseEntity<JsonNode> response = template.postForEntity(
                "http://localhost:" + port + "/sum" + "?customerVat=123456789",
                requestEntity, JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK.value(),
                response.getStatusCode().value(),
                "Status code differnt than 200");
        Assertions.assertEquals(2116.27,
                response.getBody().findValue("documentsSum").asDouble());
        System.out.println("Response:" + response.toString());
    }

    private HttpEntity<MultiValueMap<String, Object>> buildMultipartRequestEntity(
            String currency, FileSystemResource fileResouce) {
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpHeaders requestHeadersJSON = new HttpHeaders();
        requestHeadersJSON.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 1.0);
        rates.put("USD", 1.10);
        rates.put("GBP", 0.83);
        HttpEntity<Map<String, Double>> ratesEntity = new HttpEntity<>(rates,
                requestHeadersJSON);
        multipartRequest.set(EXCHANGE_RATES, ratesEntity);

        requestHeadersJSON = new HttpHeaders();
        requestHeadersJSON.setContentType(MediaType.APPLICATION_JSON);

        CurrencyType targetCurency = CurrencyType.valueOf(currency);

        HttpEntity<CurrencyType> targetCurrenctEntity = new HttpEntity<>(
                targetCurency, requestHeadersJSON);

        multipartRequest.set(TARGET_CURRENCY, targetCurrenctEntity);
        multipartRequest.set(DOCUMENT, new HttpEntity<>(fileResouce));
        return new HttpEntity<>(multipartRequest, requestHeaders);
    }

}
