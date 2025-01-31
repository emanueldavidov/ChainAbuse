package server;

import java.net.http.HttpClient;

/**
 * Interface for HttpClient factory.
 */
public interface HttpClientFactory {
    HttpClient createClient();
}
