import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.getLast;

public class Monitor {

    private List<ServiceCheck> serviceChecks;
    private int graceTime;

    public Monitor(int graceTime) {
        this.serviceChecks = new ArrayList<>();
        this.graceTime = graceTime;
    }

    public List<ServiceCheck> getServiceChecks() {
        return serviceChecks;
    }

    public void addServiceCheck(ServiceCheck serviceCheck) {
        serviceCheck.setGraceTime(this.graceTime);
        this.serviceChecks.add(serviceCheck);
    }

    public List<String> getMessagesFromServiceCheck(ServiceCheck serviceCheckSelected) {
        for (ServiceCheck serviceCheck : this.serviceChecks) {
            if (serviceCheck == serviceCheckSelected)
                return serviceCheck.getMessages();
        }

        return null;
    }

    public List<String> getServiceCheckLastMessages() {
        final List<String> lastMessages = new ArrayList<>();

        for (ServiceCheck serviceCheck : this.getServiceChecks()) {
            lastMessages.add(getLast(serviceCheck.getMessages()));
        }

        return lastMessages;
    }

    public void start() {
        for (int i = 0; i < this.serviceChecks.size(); i++) {
            final ServiceCheck current = this.serviceChecks.get(i);
            if (i > 0) {
                final ServiceCheck previous = this.serviceChecks.get(i - 1);
                if (current.getService().equals(previous.getService()))
                    TimeUtil.waitOneSecond();
            }

            current.start();
        }
    }

}
