package pt.ptinovacao.arqospocket.core.serialization.entities;

/**
 * Received result after a probe notification has been sent to the management server.
 * <p>
 * Created by Emílio Simões on 12-05-2017.
 */
public class ProbeNotificationResult {

    private final String errorMessage;

    private ProbeNotificationResult(Builder builder) {
        this.errorMessage = builder.errorMessage;
    }

    /**
     * Gets the result error message, when one exists.
     *
     * @return the result error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Builder for the {@link ProbeNotificationResult}.
     */
    public static class Builder {

        private String errorMessage;

        /**
         * Adds an error errorMessage to the result.
         *
         * @param errorMessage the error errorMessage.
         * @return the builder instance.
         */
        public Builder withError(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        /**
         * Creates a new {@link ProbeNotificationResult} instance.
         *
         * @return the created instance.
         */
        public ProbeNotificationResult build() {
            return new ProbeNotificationResult(this);
        }
    }

    @Override
    public String toString() {
        return "ProbeNotificationResult{" + "errorMessage='" + errorMessage + '\'' + '}';
    }
}
