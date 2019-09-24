import static java.lang.Thread.sleep;
import static org.joda.time.DateTimeConstants.MILLIS_PER_SECOND;

abstract class TimeUtil {

    public static void waitOneSecond() {
        try {
            sleep(MILLIS_PER_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    abstract void waitAction(int seconds);

    void waitAndExecuteFor(int seconds) {
        int secondsTemp = seconds;
        while (secondsTemp > 0) {
            waitAction(secondsTemp);
            secondsTemp--;
            waitOneSecond();
        }
    }

}

