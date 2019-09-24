# Service Monitor
A simple web service monitor mader with JAVA.

This project is a possible solution for this Technical Assignment

Problem:
Design and implement (in java) a service monitoring class. This monitor will be
used to monitor the status of multiple services.

A service is defined as a host/port combination. To check if a service is up, the
monitor will establish a TCP connection to the host on the specified port.
If a connection is established, the service is up. If the connection is refused, the
service is not up.

The monitor will allow callers to register interest in a service, and a polling
frequency. The callers will be notified when the service goes up and down.
The monitor should detect multiple callers registering interest in the same service,
and should not poll any service more frequently than once a second.
The monitor should allow callers to register a planned service outage. The caller
will specify a start and end time for which no notifications for that service will be
delivered.

The monitor should allow callers to define a grace time that applies to all services
being monitored. If a service is not responding, the monitor will wait for the grace
time to expire before notifying any clients. If the service goes back on line during
this grace time, no notification will be sent. If the grace time is less than the
polling frequency, the monitor should schedule extra checks of the service.

```java
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


Class Diagram used in this solution.
![alt text](https://raw.githubusercontent.com/cardocha/ServiceMonitor/master/src/main/resources/classDiagram.png)


User Interface Prototype implemented for future improvements.
![alt text](https://raw.githubusercontent.com/cardocha/ServiceMonitor/master/src/main/resources/ui_screenshot.png)


## Getting Started
The source is a Maven project.  

### Prerequisites

JAVA 8+ / Maven 3.6.1+

### Dependencies
https://github.com/JodaOrg/joda-time

https://github.com/google/guava

https://github.com/mabe02/lanterna

## Authors

Luciano Cardoso https://github.com/cardocha

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

