import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

class Monitor {

	private List<ServiceCheck> serviceChecks;
	private int graceTime;

	Monitor(int graceTime) {
		this.serviceChecks = new ArrayList<>();
		this.graceTime = graceTime;
	}

	List<ServiceCheck> getServiceChecks() {
		return serviceChecks;
	}

	void addServiceCheck(ServiceCheck serviceCheck) {
		serviceCheck.setGraceTime(this.graceTime);
		this.serviceChecks.add(serviceCheck);
	}

	List<String> getMessagesFromServiceCheck(ServiceCheck serviceCheckSelected) {
		return Objects.requireNonNull(
				this.serviceChecks.stream()
						.filter(serviceCheck -> serviceCheck == serviceCheckSelected)
						.findAny()
						.orElse(null)
		).getMessages();
	}

	void waitOneSecondToStartIfNeeded(ServiceCheck current, int indice) {
		if (indice > 0) {
			final ServiceCheck previous = this.serviceChecks.get(indice - 1);
			if (current.getService().equals(previous.getService()))
				TimeUtil.waitOneSecond();
		}
	}

	void start() {
		AtomicInteger index = new AtomicInteger();
		this.serviceChecks.forEach(serviceCheck -> {
			final int currentIndex = index.getAndIncrement();
			final ServiceCheck current = this.serviceChecks.get(currentIndex);
			waitOneSecondToStartIfNeeded(current, currentIndex);
			current.start();
		});
	}

}
