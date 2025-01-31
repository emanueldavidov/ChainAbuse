package server;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import common.AddressReport;

/**
 * Handles HTTP operations using a factory to create HttpClient instances.
 */
public class HttpHandler {
    private HttpClient client;

    /**
     * Default constructor that uses DefaultHttpClientFactory.
     */
    public HttpHandler() {
        this(new DefaultHttpClientFactory()); // Use DefaultHttpClientFactory by default
    }

    /**
     * Constructor that allows specifying a custom factory.
     *
     * @param httpClientFactory The factory to use.
     */
    public HttpHandler(HttpClientFactory httpClientFactory) {
        this.client = httpClientFactory.createClient();
    }

    /**
     * Sends an HTTP request asynchronously.
     *
     * @param request The HttpRequest to send.
     */
    public void sendRequest(HttpRequest request) {
        try {
            client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
                  .thenApply(java.net.http.HttpResponse::body)
                  .thenAccept(JsonHandler::parse)
                  .join();
        } catch (Exception e) {
            System.err.println("Error sending request: " + e.getMessage());
        }
    }


    /**
     * Creates a ReportEntry based on the HTTP response.
     *
     * @param response The response data.
     * @param url The URL of the request.
     * @param requestedAddress The requested address.
     * @return A new ReportEntry instance.
     */
    public AddressReport createReportEntry(ArrayList<String> response, String url, String requestedAddress) {
        if (response.size() == 3) {
            String address = response.get(0);
            String reportCount = response.get(1);
            String link = response.get(2);
            return new AddressReport(address, reportCount, link);
        } else {
            String reportCount = "0";
            String link = "https://www.chainabuse.com/address/" + requestedAddress;
            return new AddressReport(requestedAddress, reportCount, link);
        }
    }
}