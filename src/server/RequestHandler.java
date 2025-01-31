package server;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

public class RequestHandler {

    public RequestHandler() {}

    /**
     * Build the GET URL for the v0 endpoint, including
     * ?address=...&includePrivate=false&page=1&perPage=50
     */
    public String parseGetRequest(String request_url, String address, String api_token) {
        String requestString = null;
        if (!(request_url.isEmpty() || address.isEmpty() || api_token.isEmpty())) {
            // For v0, the base endpoint might be something like:
            // https://api.chainabuse.com/v0/reports
            // We append the query params:
            requestString = request_url
                + "?address=" + address
                + "&includePrivate=false"
                + "&page=1"
                + "&perPage=50";

            System.out.println("HttpRequest: " + requestString);
        }
        return requestString;
    }

    /**
     * Build the HttpRequest with the "authorization: Basic ..."
     * header from the api_token field in api_json.json.
     *
     * If your api_token ALREADY starts with "Basic ", then you can use it as-is.
     * Otherwise, you'd do something like:
     *    "Basic " + base64EncodedUsernameColonPassword
     */
    public HttpRequest buildHttpRequest(String requestString, String api_token) {
        HttpRequest request = null;
        try {
            URI myuri = new URI(requestString);

            // If your api_token field already includes "Basic " at the start,
            // e.g. "Basic Y2FfU1V3M1U..."
            // then you can use it directly:
            String authorizationValue = api_token.trim();
            request = HttpRequest.newBuilder()
                    .uri(myuri)
                    .header("accept", "application/json")
                    .header("authorization", authorizationValue)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            System.err.println("An error occurred while building the HTTP Request.");
        }
        return request;
    }
}
