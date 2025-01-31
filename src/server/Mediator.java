package server;

import java.net.http.HttpRequest;
import java.util.ArrayList;

import common.AddressReport;

/**
 * Mediator to coordinate between handlers.
 */
class Mediator {
    private HttpHandler httpHandler;
    private RequestHandler requestHandler;
    public EventDispatcher event_manager;

    /**
     * Constructor for Mediator.
     *
     * @param httpHandler The HttpHandler.
     * @param requestHandler The RequestHandler.
     * @param jsonHandler The JsonHandler.
     */
    public Mediator(HttpHandler httpHandler, RequestHandler requestHandler, EventDispatcher eventManager) {
        this.httpHandler = httpHandler;
        this.requestHandler = requestHandler;
        this.event_manager = eventManager; // Initialize event_manager
    }


    /**
     * Handles a request by orchestrating between the handlers.
     *
     * @param domainUrl The domain URL.
     * @param address The address to check.
     * @param apiToken The API token for authentication.
     * @return A ReportEntry with the results.
     * @throws Exception If an error occurs.
     */
    public AddressReport handleRequest(String domainUrl, String address, String apiToken) throws Exception {
        String request = requestHandler.parseGetRequest(domainUrl, address, apiToken);
        HttpRequest httpRequest = requestHandler.buildHttpRequest(request, apiToken);
        JsonHandler.resetResponse();
        
        httpHandler.sendRequest(httpRequest);

        ArrayList<String> response = JsonHandler.getResponse();

        if (response.size() == 1 && "ERROR_TOO_MANY_REQUESTS".equals(response.get(0))) {
            event_manager.notify("Error", "Too many requests. Please wait or use a new API token.");
            return null;
        }
        
        if (response.size() == 1 && "ERROR_INVALID_API_TOKEN".equals(response.get(0))) {
            event_manager.notify("Error", "Invalid Api Token. Check your token");
            return null;
        }

        AddressReport report = httpHandler.createReportEntry(response, domainUrl, address);
        return report;
    }


}