public abstract class CheckJob extends Thread {

    private boolean stop;
    private boolean pauseCheck;

    public CheckJob() {
        this.pauseCheck = false;
        this.stop = false;
    }

    abstract ServiceCheck getServiceCheck();

    abstract void performCheck();

    abstract boolean pauseJobIfNeeded();

    @Override
    public void run() {

        TimeUtil timeUtil = new TimeUtil() {
            @Override
            void waitAction(int seconds) {
                getServiceCheck().addMessage(ActionMessages.getWaitingMessage(seconds));
            }
        };

        while (!this.stop) {
            pauseCheck = pauseJobIfNeeded();
            if (!pauseCheck) {
                timeUtil.waitAndExecuteFor(getServiceCheck().getFrequency());
                final String connectionMessage = ActionMessages.getConnectionMessage(getServiceCheck().getService());
                getServiceCheck().addMessage(connectionMessage);
                performCheck();
            }
        }
    }


}
