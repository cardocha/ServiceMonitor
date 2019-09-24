import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServiceCheck extends CheckJob {

    private Service service;
    private int frequency;
    private List<String> messages;
    private Integer graceTime;
    private DateTime graceTimeInit;

    ServiceCheck(Service service, int frequency) {
        super();
        this.graceTimeInit = null;
        this.messages = new ArrayList<>();
        this.service = service;
        this.frequency = frequency;
        this.graceTime = null;
    }

    Service getService() {
        return service;
    }

    int getFrequency() {
        return frequency;
    }

    private boolean isServiceOn() {
        try {
            return new Connection(service).test();
        } catch (IOException e) {
            return false;
        }
    }

    public void setGraceTime(Integer graceTime) {
        this.graceTime = graceTime;
    }

    List<String> getMessages() {
        return messages;
    }

    void addMessage(String message) {
        messages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceCheck that = (ServiceCheck) o;
        return frequency == that.frequency &&
                Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, frequency);
    }

    @Override
    ServiceCheck getServiceCheck() {
        return this;
    }

    @Override
    boolean pauseJobIfNeeded() {
        return this.service.isInAPlanedOutage() || graceTimeInit != null;
    }

    @Override
    void performCheck() {
        boolean serviceStatus = this.isServiceOn();

        if (!serviceStatus && graceTime != null) {
            graceTimeInit = DateTime.now();
        }

        if (graceTimeInit != null && graceTimeInit.plusSeconds(graceTime).isBeforeNow()) {
            graceTimeInit = null;
        }

        final String message = graceTimeInit == null ? ActionMessages.getServiceStatusMessage(serviceStatus)
                : ActionMessages.getGraceTimeMessage();
        messages.add(message);
    }
}
