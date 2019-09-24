import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;

public class ServiceCheckTest {

    @Test
    public void pollFrequencyTimerTest() throws InterruptedException {
        final Service service = new Service("http://www.uol.com.br", 80);
        final ServiceCheck serviceCheck = new ServiceCheck(service, 5);
        final List<String> expectedMessages = new ArrayList<String>() {{
            add("Waiting ...5");
            add("Waiting ...4");
            add("Waiting ...3");
            add("Waiting ...2");
            add("Waiting ...1");
        }};

        serviceCheck.start();
        sleep(5000);
        assertTrue(serviceCheck.getMessages().containsAll(expectedMessages));
    }

    @Test
    public void connectingAttemptAfterWaitTest() throws InterruptedException {
        final Service service = new Service("http://www.uol.com.br", 80);
        final ServiceCheck serviceCheck = new ServiceCheck(service, 3);
        final List<String> expectedMessages = new ArrayList<String>() {{
            add("Waiting ...3");
            add("Waiting ...2");
            add("Waiting ...1");
            add("Connecting to http://www.uol.com.br On 80");
        }};

        serviceCheck.start();
        sleep(3200); // 0,2 seconds more second to service connect job.
        assertTrue(serviceCheck.getMessages().containsAll(expectedMessages));
    }

    @Test
    public void performConnectionAttemptSuccessTest() throws InterruptedException {
        final Service service = new Service("http://www.google.com", 80);
        final ServiceCheck serviceCheck = new ServiceCheck(service, 3);
        final List<String> expectedMessages = new ArrayList<String>() {{
            add("Waiting ...3");
            add("Waiting ...2");
            add("Waiting ...1");
            add("Connecting to http://www.google.com On 80");
            add("Service is Up");
        }};

        serviceCheck.start();
        sleep(3300); // 0,3 seconds more to wait the connection attempt.
        assertTrue(serviceCheck.getMessages().containsAll(expectedMessages));
    }

    @Test
    public void performConnectionAttemptFailTest() throws InterruptedException {
        final Service service = new Service("http://www.google.com", 801);
        final ServiceCheck serviceCheck = new ServiceCheck(service, 1);
        final List<String> expectedMessages = new ArrayList<String>() {{
            add("Waiting ...1");
            add("Connecting to http://www.google.com On 801");
            add("Service is Down");
        }};

        serviceCheck.start();
        sleep(4000); // The connection attempt takes almost 1 second and Timeout is set to 2 seconds before expire
        assertTrue(serviceCheck.getMessages().containsAll(expectedMessages));
    }

    @Test
    public void performConnectionAttemptWhileAPlannedOutageTest() throws InterruptedException {
        final Service service = new Service("http://www.google.com", 80);
        service.addPlanedOutage(new PlanedOutage(DateTime.now(), DateTime.now().plusSeconds(10)));

        final ServiceCheck serviceCheck = new ServiceCheck(service, 1);

        serviceCheck.start();
        sleep(2000); // The connection attempt takes almost 1 second and Timeout is set to 2 seconds before expire
        assertTrue(serviceCheck.getMessages().isEmpty());
    }

    @Test
    public void performConnectionAttemptAfterAPlannedOutageTest() throws InterruptedException {
        final Service service = new Service("http://www.google.com", 80);
        service.addPlanedOutage(new PlanedOutage(DateTime.now(), DateTime.now().plusSeconds(10)));

        final ServiceCheck serviceCheck = new ServiceCheck(service, 1);
        final List<String> expectedMessages = new ArrayList<String>() {{
            add("Waiting ...1");
            add("Connecting to http://www.google.com On 80");
            add("Service is Up");
        }};

        serviceCheck.start();
        sleep(13000); // The connection attempt takes almost 1 second and Timeout is set to 2 seconds before expire
        assertTrue(serviceCheck.getMessages().containsAll(expectedMessages));
    }

}
