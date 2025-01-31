package server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JsonHandler {

    private static ArrayList<String> response;

    public JsonHandler() {
        response = new ArrayList<>();
    }

    /**
     * Reads the JSON file (e.g. api_json.json) from "./txt/[json_name].json"
     * and returns an ArrayList with two elements:
     *  [0] = url  (like "https://api.chainabuse.com/v0/reports")
     *  [1] = api_token (like "Basic Y2FfUm1WeVMxQmlk...")
     */
    public ArrayList<String> readJson(String json_name) {
        ArrayList<String> ret = null;
        String json_path = "./txt/" + json_name + ".json";
        File file = new File(json_path);
        if (file != null) {
            try {
                ret = new ArrayList<>();
                String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
                JSONObject jsonContent = new JSONObject(content);

                String url = jsonContent.getString("url");
                String api_token = jsonContent.getString("api_token");

                ret.add(url);
                ret.add(api_token);
            } catch (IOException e) {
                System.err.println("Error opening file: " + json_path + "\n Please check if the file exists.");
            }
        }
        return ret;
    }

    /**
     * The response from chainabuse.com/v0 is an array of objects, e.g.:
     * [
     *   {
     *     "id": "...",
     *     "scamCategory": "...",
     *     "addresses": [ { "address": "...", "chain": "BTC", ... } ]
     *   },
     *   ...
     * ]
     *
     * We'll just take the FIRST item in that array, grab the first address
     * in its "addresses" array, and count how many total objects we have.
     */
    public static void parse(String response_body) {
        System.out.println("HttpResponse: " + response_body);
        response.clear();

        try {
            // Check if the response is an object or an array
            if (response_body.trim().startsWith("{")) {
                JSONObject jsonObject = new JSONObject(response_body);

                // Check for "Too many requests" message
                if (jsonObject.has("message") && "Too many requests.".equals(jsonObject.getString("message"))) {
                    response.add("ERROR_TOO_MANY_REQUESTS"); // Add an error marker
                    return; // Exit early
                }
                
                // Check for "Invalid credentials" message
                if (jsonObject.has("message") && "Invalid credentials".equals(jsonObject.getString("message"))) {
                    response.add("ERROR_INVALID_API_TOKEN"); // Add an error marker
                    return; // Exit early
                }

                // Handle other object-based responses here if needed
            } else if (response_body.trim().startsWith("[")) {
                JSONArray jsonArray = new JSONArray(response_body);

                int totalReports = jsonArray.length();
                if (totalReports > 0) {
                    JSONObject firstObject = jsonArray.getJSONObject(0);

                    JSONArray addresses = firstObject.optJSONArray("addresses");
                    if (addresses != null && addresses.length() > 0) {
                        JSONObject addrObj = addresses.getJSONObject(0);
                        String address = addrObj.optString("address", "unknown");

                        String reportCount = String.valueOf(totalReports);
                        String link = "https://www.chainabuse.com/address/" + address;

                        response.add(address);       // index 0
                        response.add(reportCount);   // index 1
                        response.add(link);          // index 2
                    }
                }
            } else {
                // Unexpected response format
                System.err.println("Unexpected response format: " + response_body);
            }
        } catch (Exception e) {
            System.err.println("Error while parsing JSON response:");
            e.printStackTrace();
        }
    }



    public static ArrayList<String> getResponse() {
        return response;
    }

    public static void resetResponse() {
        response.clear();
    }
}
