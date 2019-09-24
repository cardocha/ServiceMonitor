import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connection {

    private Service service;

    public Connection(Service service) {
        this.service = service;
    }

    public boolean test() throws IOException {
        URL url = new URL(service.getHost() + ":" + service.getPort());
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setConnectTimeout(2000);
        urlConn.connect();
        return HttpURLConnection.HTTP_OK == urlConn.getResponseCode();
    }
}
