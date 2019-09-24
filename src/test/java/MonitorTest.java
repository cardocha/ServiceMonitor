import org.junit.Test;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class MonitorTest {

    @Test
    public void monitorMustSupportMultipleServiceChecksTest() throws InterruptedException {
        final ServiceCheck serviceCheck1 = new ServiceCheck(new Service("http://www.google.com", 80), 5);
        final ServiceCheck serviceCheck2 = new ServiceCheck(new Service("http://stackoverflow.com/", 80), 6);
        final ServiceCheck serviceCheck3 = new ServiceCheck(new Service("http://gmail.com/", 80), 3);

        Monitor monitor = new Monitor(2);
        monitor.addServiceCheck(serviceCheck1);
        monitor.addServiceCheck(serviceCheck2);
        monitor.addServiceCheck(serviceCheck3);
        monitor.start();

        sleep(10000);

        assertFalse(monitor.getMessagesFromServiceCheck(serviceCheck1).isEmpty());
        assertFalse(monitor.getMessagesFromServiceCheck(serviceCheck2).isEmpty());
        assertFalse(monitor.getMessagesFromServiceCheck(serviceCheck3).isEmpty());
    }


    @Test
    public void serviceCheckMustWaitForMonitorGraceTime() throws InterruptedException {
        final ServiceCheck serviceCheck1 = new ServiceCheck(new Service("http://www.google.com", 80), 2);
        final ServiceCheck serviceCheck2 = new ServiceCheck(new Service("http://incompany.epizy.com/incompany/", 2), 8);

        final ServiceCheck serviceCheck3 = new ServiceCheck(new Service("http://gmail.com/", 801), 1);

        Monitor monitor = new Monitor(10);
        monitor.addServiceCheck(serviceCheck1);
        monitor.addServiceCheck(serviceCheck2);
        monitor.addServiceCheck(serviceCheck3);
        monitor.start();

        sleep(8000);
        assertFalse(monitor.getMessagesFromServiceCheck(serviceCheck1).contains("Waiting for grace time to finish"));
        assertFalse(monitor.getMessagesFromServiceCheck(serviceCheck2).contains("Waiting for grace time to finish"));
        assertTrue(monitor.getMessagesFromServiceCheck(serviceCheck3).contains("Waiting for grace time to finish"));
    }

    @Test
    public void monitorMustEnsureOnlyOneConnectionAttemptPerSecondOnServiceChecksWithTheSameHostAndPort() throws InterruptedException {
        final ServiceCheck serviceCheck1 = new ServiceCheck(new Service("http://www.google.com", 80), 1);
        final ServiceCheck serviceCheck2 = new ServiceCheck(new Service("http://www.google.com", 80), 1);
        final ServiceCheck serviceCheck3 = new ServiceCheck(new Service("http://www.google.com", 80), 1);


        Monitor monitor = new Monitor(5);
        monitor.addServiceCheck(serviceCheck1);
        monitor.addServiceCheck(serviceCheck2);
        monitor.addServiceCheck(serviceCheck3);
        monitor.start();

        sleep(100);

        assertTrue(monitor.getMessagesFromServiceCheck(serviceCheck1).size() > 0);
        assertTrue(monitor.getMessagesFromServiceCheck(serviceCheck1).contains("Service is Up"));

        assertTrue(monitor.getMessagesFromServiceCheck(serviceCheck2).size() > 0);
        assertFalse(monitor.getMessagesFromServiceCheck(serviceCheck2).contains("Service is Up"));

        assertTrue(monitor.getMessagesFromServiceCheck(serviceCheck3).size() > 0);
        assertFalse(monitor.getMessagesFromServiceCheck(serviceCheck3).contains("Service is Up"));
    }


}
