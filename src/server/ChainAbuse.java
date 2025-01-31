package server;

import common.AddressDetail;
import common.AddressReport;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;

// Behavioral Patterns: Mediator
public final class ChainAbuse implements EventSubscriber {

    public EventDispatcher event_manager;
    private Mediator mediator;
    private String domain_url;  // e.g. "https://api.chainabuse.com/v0/reports"
    private String api_token;   // e.g. "Basic Y2FfUm1WeVMxQmlk..."

    public ChainAbuse(String json_name) {
        event_manager = new EventDispatcher("Scan Results", "Error");

        JsonHandler jsonHandler = new JsonHandler();

        // e.g. api_json.json might have:
        // {
        //   "url": "https://api.chainabuse.com/v0/reports",
        //   "api_token": "Basic Y2FfUm1WeV..."
        // }
        ArrayList<String> params = jsonHandler.readJson(json_name); // index 0 => url, index 1 => api_token
        this.domain_url = params.get(0);
        this.api_token = params.get(1);

        // Use DefaultHttpClientFactory to create HttpHandler
        HttpHandler httpHandler = new HttpHandler(new DefaultHttpClientFactory());
        
        // Initialize the mediator with all required handlers
        mediator = new Mediator(httpHandler, new RequestHandler(), event_manager);
    }

    /**
     * checkAddress() coordinates through the Mediator:
     *  1) parse the full GET request string
     *  2) build the HttpRequest with Basic Auth header
     *  3) send the request
     *  4) parse JSON and create a ReportEntry (could be null if no reports)
     */
    private AddressReport checkAddress(String address) {
        try {
            return mediator.handleRequest(domain_url, address, api_token);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // or keep as null
        }
    }

    @Override
    public void onEvent(String eventType, Object obj) {
        if (eventType.equals("Scan Addresses")) {
            try {
                ArrayList<AddressDetail> address_list = (ArrayList<AddressDetail>) obj;
                ArrayList<AddressReport> report_entry_list = new ArrayList<>();

                for (AddressDetail address_entry : address_list) {
                    AddressReport ret = checkAddress(address_entry.getAddress());

                    // Check for "Too many requests" error in the response
                    if (ret == null && JsonHandler.getResponse().size() == 1 &&
                            "ERROR_TOO_MANY_REQUESTS".equals(JsonHandler.getResponse().get(0))) {
                        System.out.println("Scan aborted due to too many requests.");
                        return; // Exit the method immediately, as "Error" is already notified
                    }
                    
                    // Check for "Too many requests" error in the response
                    if (ret == null && JsonHandler.getResponse().size() == 1 &&
                            "ERROR_INVALID_API_TOKEN".equals(JsonHandler.getResponse().get(0))) {
                        System.out.println("Scan aborted due to Invalid API token key.");
                        return; // Exit the method immediately, as "Error" is already notified
                    }

                    if (ret != null) {
                        report_entry_list.add(ret);
                    } else {
                        System.out.println("No reports found for address: " + address_entry.getAddress());
                    }
                }

                // Notify results if no errors occurred
                if (!report_entry_list.isEmpty()) {
                    event_manager.notify("Scan Results", report_entry_list);
                } else {
                    System.out.println("No reports found for all addresses.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                event_manager.notify("Error", "Error occurred during Scan Addresses.");
            }
        }
    }



}

