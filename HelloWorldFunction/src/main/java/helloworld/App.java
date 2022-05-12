package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        responseHeaders.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(responseHeaders);

        try {
            final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
            String cognitoIdentityId = "anonymous";
            if (context != null && context.getIdentity() != null) {
                cognitoIdentityId = context.getIdentity().getIdentityId();
            }
            String requestIdentityUser = "anonymous";
            if (input != null && input.getRequestContext() != null && input.getRequestContext().getIdentity() != null) {
                requestIdentityUser = input.getRequestContext().getIdentity().getUser();
            }
            String requestHeaders = null;
            if (input != null && input.getHeaders() != null) {
                requestHeaders = input.getHeaders().entrySet().stream().map(e -> { return "\"" + e.getKey() + "\":\"" + e.getValue() + "\""; } ).collect(Collectors.joining(","));
            }
            String rawAuthorizationValue = null;
            String rawJwtToken = null;
            String jwtToken = null;
            if (input != null && input.getHeaders() != null) {
                rawAuthorizationValue = input.getHeaders().get("authorization");
                if (rawAuthorizationValue != null && rawAuthorizationValue.startsWith("Bearer ")) {
                    rawJwtToken = rawAuthorizationValue.substring(8);
                    jwtToken = extractTokenPayload(rawJwtToken);
                }
            }
            StringBuilder output = new StringBuilder();
            output.append("{ ");
            output.append("\"message\":").append("\"hello world\"");
            output.append(",\"location\":").append("\"").append(pageContents).append("\"");
            output.append(",\"cognitoIdentityId\":").append("\"").append(cognitoIdentityId).append("\"");
            output.append(",\"requestIdentityUser\":").append("\"").append(requestIdentityUser).append("\"");
            if (requestHeaders != null) {
                output.append(",\"headers\":{").append(requestHeaders).append("}");
            }
            if (rawJwtToken != null) {
                output.append(",\"rawJwtToken\":").append("\"").append(rawJwtToken).append("\"");
            }
            if (jwtToken != null) {
                output.append(",\"jwtToken\":").append(jwtToken);
            }
            output.append(" }");

            return response
                    .withStatusCode(200)
                    .withBody(output.toString());
        } catch (IOException e) {
            return response
                    .withBody("{}")
                    .withStatusCode(500);
        }
    }

    private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private String extractTokenPayload(String encodedJwt) {
        String[] encodedJwtChunks = encodedJwt.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String result = new String(decoder.decode(encodedJwtChunks[1]));
        return result;
    }
}
