
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        final ServiceCheck serviceCheck1 = new ServiceCheck(new Service("http://www.google.com", 80), 5);
        final ServiceCheck serviceCheck2 = new ServiceCheck(new Service("http://stackoverflow.com/", 80), 6);
        final ServiceCheck serviceCheck3 = new ServiceCheck(new Service("http://gmail.com/", 80), 3);

        Monitor monitor = new Monitor(2);
        monitor.addServiceCheck(serviceCheck1);
        monitor.addServiceCheck(serviceCheck2);
        monitor.addServiceCheck(serviceCheck3);
        monitor.start();

        MainWindow mainWindow = new MainWindow();
        mainWindow.initialize(monitor);

    }



}
