class ActionMessages {

    private static final String waitingMessage = "Waiting ...";
    private static final String connectingMessage = "Connecting to ";
    private static final String serviceMessage = "Service is ";
    private static final String graceTimeMessage = "Waiting for grace time to finish";

    static String getWaitingMessage(int seconds) {
        return waitingMessage.concat(String.valueOf(seconds));
    }

    static String getConnectionMessage(Service service) {
        return connectingMessage
                .concat(service.getHost())
                .concat(" On ")
                .concat(String.valueOf(service.getPort()));
    }

    static String getServiceStatusMessage(boolean serviceStatus) {
        return serviceMessage.concat(serviceStatus ? "Up" : "Down");
    }

    static String getGraceTimeMessage() {
        return graceTimeMessage;
    }
}
