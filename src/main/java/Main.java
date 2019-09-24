import org.joda.time.DateTime;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        final Service service = new Service("http://www.google.com.br", 801);
        final DateTime now = new DateTime(new Date());
        final DateTime nowPlus10Seconds = new DateTime(now).plusSeconds(10);
        service.addPlanedOutage(now, nowPlus10Seconds);
        new ServiceCheck(service, 5).start();
    }

}
