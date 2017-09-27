package pt.ptinovacao.arqospocket.core.http.client.response;

import pt.ptinovacao.arqospocket.core.http.client.HttpResponse;
import pt.ptinovacao.arqospocket.core.serialization.entities.ProbeNotificationResult;

/**
 * Response for an anomaly request.
 * <p>
 * Created by Emílio Simões on 11-05-2017.
 */
public class ProbeNotificationResponse extends HttpResponse<ProbeNotificationResult> {

    public ProbeNotificationResponse(ProbeNotificationResult entity, int code) {
        super(entity, code);
    }

    /**
     * Builder for the {@link ProbeNotificationResponse}.
     */
    public static class Builder {

        private int code;

        private ProbeNotificationResult result;

        /**
         * Constructor for the builder.
         */
        public Builder() {
            this.code = 200;
        }

        /**
         * Sets the result object.
         *
         * @param result the result object.
         * @return the builder instance.
         */
        public Builder result(ProbeNotificationResult result) {
            this.result = result;
            return this;
        }

        /**
         * Sets the result code.
         *
         * @param code the result code.
         * @return the builder instance.
         */
        public Builder code(int code) {
            this.code = code;
            return this;
        }

        /**
         * Sets a success reponse code.
         *
         * @return the builder instance.
         */
        public Builder success() {
            return code(200);
        }

        /**
         * Sets an internal server error response code.
         *
         * @return the builder instance.
         */
        public Builder internalServerError() {
            return code(500);
        }

        public ProbeNotificationResponse build() {
            return new ProbeNotificationResponse(this.result, this.code);
        }

        @Override
        public String toString() {
            return "Builder{" + "code=" + code + ", result=" + result + '}';
        }
    }
}
