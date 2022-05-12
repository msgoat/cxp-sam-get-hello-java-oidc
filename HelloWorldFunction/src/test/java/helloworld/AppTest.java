package helloworld;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class AppTest {
    @Test
    public void successfulResponse() {
        App app = new App();
        Map<String, String> requestHeaders = new LinkedHashMap<>();
        requestHeaders.put("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJNcWpMWTJ0X0prWDZaWUd0U1NxLXFHa3o5QnNqZVpGaHRCUTNVY3hkbEVJIn0.eyJleHAiOjE2NTIyODY0MjgsImlhdCI6MTY1MjI4NjEyOCwianRpIjoiZDg4YzdlOTItNTExNS00NDkzLTk5ZmUtZTE4ZDM0OTVkZTMyIiwiaXNzIjoiaHR0cHM6Ly9vaWRjLmNsb3VkdHJhaW4uYXdzLm1zZ29hdC5ldS9hdXRoL3JlYWxtcy9DbG91ZEV4cGVydFByb2dyYW0iLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMjcyYmIwNzItZGM1ZC00NDc5LWE4MWEtNTVhNDQzNjk5ZjI1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY3hwLXNlcnZlcmxlc3MtdGVzdGVyIiwic2Vzc2lvbl9zdGF0ZSI6IjMyMzA2MjBmLTBhMTMtNDQzZi05YTk2LTMzOTBhMDU2MjVmZiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9zZXJ2ZXJsZXNzLmN4cC5hd3MubXNnb2F0LmV1Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJDWFBfVVNFUiIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBtaWNyb3Byb2ZpbGUtand0IHByb2ZpbGUgZW1haWwiLCJ1cG4iOiJjeHAtdGVzdGVyIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiQ1hQIFRlc3RlciIsImdyb3VwcyI6WyJDWFBfVVNFUiIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXSwicHJlZmVycmVkX3VzZXJuYW1lIjoiY3hwLXRlc3RlciIsImdpdmVuX25hbWUiOiJDWFAiLCJmYW1pbHlfbmFtZSI6IlRlc3RlciIsImVtYWlsIjoiY3hwLXRlc3RlckBtc2cuZ3JvdXAifQ.WrtYK0SavoGw78Yb-ieKk1ZE89kz0ANuPDuvVBvT-FbUEu8KOcxrh1dUo6Uju80NxotCNy3ZPDNFCUl2BLSL3T-oOh5f3eOldjbLUUKMO2vf_xkE-7FUcW0CUWQkpTpo4mSscQxmfyHRALf0_hcZQTjTUak7S6Dd5OXMAOpUuKws44tqGVfK12RCqHMi3AN_3Sz8Gw6vazZ82lTm1B4MZsoE0x2MxgdDuHZYMM17iuuggiNG2ye506lEmphoy1L7vuaUttUSuSzcPp7M9mqA-kCCOaYwLnadPNek7yohWNgBwEmf2Vx7bc6Qktt5NR7dQbTMJpHFM08wxhAtPMV2sQ");
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withHeaders(requestHeaders);
        APIGatewayProxyResponseEvent result = app.handleRequest(request, null);
        assertEquals(200, result.getStatusCode().intValue());
        assertEquals("application/json", result.getHeaders().get("Content-Type"));
        String content = result.getBody();
        assertNotNull(content);
        assertTrue(content.contains("\"message\""));
        assertTrue(content.contains("\"hello world\""));
        assertTrue(content.contains("\"location\""));
        assertTrue(content.contains("\"headers\""));
    }
}
