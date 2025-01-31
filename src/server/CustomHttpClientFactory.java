package server;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Custom factory for creating an advanced HttpClient with additional settings.
 * 
 * ⚠️ **Note:** This class is **not currently in use**, as the project is using the `DefaultHttpClientFactory`.
 * However, it remains available for future use when we need:
 *  - **Custom timeouts** to prevent long waits for responses.
 *  - **Automatic redirect handling** for API requests.
 *  - **Future enhancements** like authentication headers or proxy support.
 * 
 * To use this, replace `DefaultHttpClientFactory` with `CustomHttpClientFactory` in `HttpHandler`.
 */

public class CustomHttpClientFactory implements HttpClientFactory {
    private Duration timeout;
    private boolean followRedirects;

    public CustomHttpClientFactory(Duration timeout, boolean followRedirects) {
        this.timeout = timeout;
        this.followRedirects = followRedirects;
    }

    @Override
    public HttpClient createClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2) // Supports HTTP/2
                .connectTimeout(timeout) // Sets timeout duration
                .followRedirects(followRedirects ? HttpClient.Redirect.ALWAYS : HttpClient.Redirect.NEVER) // Handles redirects
                .build();
    }
}