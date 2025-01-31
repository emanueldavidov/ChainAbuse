
package server;

import java.net.http.HttpClient;

/**
 * Default factory for creating a simple HttpClient.
 * It is used when no extra settings (timeouts, headers, authentication) are needed.
 */
public class DefaultHttpClientFactory implements HttpClientFactory {
    @Override
    public HttpClient createClient() {
        return HttpClient.newHttpClient(); // Plain default client

    }
}
