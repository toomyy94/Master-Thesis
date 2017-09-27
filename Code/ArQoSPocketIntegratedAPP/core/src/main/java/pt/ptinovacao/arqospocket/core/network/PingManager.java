package pt.ptinovacao.arqospocket.core.network;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to execute ping requests.
 * <p>
 * Created by Emílio Simões on 26-04-2017.
 */
public class PingManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingManager.class);

    public static PingResult ping(String address, int packetSize, int packetNumber, int interval, int timeout) {
        LOGGER.debug("Starting ping to {} with {} packets sized {} bytes, interval {}, timeout {}", address,
                packetNumber, packetSize, interval, timeout);

        PingResult result = new PingResult();
        Runtime runtime = Runtime.getRuntime();
        try {
            String command =
                    new CommandBuilder().setAddress(address).setInterval(interval).setPacketNumber(packetNumber)
                            .setPacketSize(packetSize).setTimeout(timeout).build();
            LOGGER.debug("Built ping command: {}", command);
            Process process = runtime.exec(command);
            int exitValue = process.waitFor();

            LOGGER.debug("ExitValue: [{}]", exitValue);
            result.setResult(exitValue);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    LOGGER.debug("Ping command output: '{}'", line);
                    if (line.contains("rtt min/avg/max/mdev")) {
                        processTimes(result, line);
                    } else if (line.contains("packets transmitted")) {
                        processCounters(result, line);
                    }
                }
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            LOGGER.error("Error executing ping command", e);
            return PingResult.EMPTY;
        }

        return result;
    }

    private static void processTimes(PingResult result, String line) {
        Pattern pattern =
                Pattern.compile("rtt min/avg/max/mdev = (\\d+\\.\\d+)/(\\d+\\.\\d+)/(\\d+\\.\\d+)/(\\d+\\.\\d+) ms");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            DecimalFormat formatter = numberFormatter();
            try {
                result.setMinimum(formatter.parse(matcher.group(1)).doubleValue());
                result.setAverage(formatter.parse(matcher.group(2)).doubleValue());
                result.setMaximum(formatter.parse(matcher.group(3)).doubleValue());
                LOGGER.debug("Parsed times: {}", result);
            } catch (ParseException e) {
                LOGGER.error("Could not parse ping times", e);
            }
        } else {
            LOGGER.debug("Line is not a match to times pattern: '{}'", line);
        }
    }

    private static void processCounters(PingResult result, String line) {
        Pattern pattern = Pattern.compile("(.+) packets transmitted, (.+) received, (.+)% packet loss, time (.+)ms");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            DecimalFormat formatter = numberFormatter();
            try {
                result.setSentPackets(formatter.parse(matcher.group(1)).intValue());
                result.setReceivedPackets(formatter.parse(matcher.group(2)).intValue());
                result.setLostPackets(formatter.parse(matcher.group(3)).intValue());
                LOGGER.debug("Parsed counters: {}", result);
            } catch (ParseException e) {
                LOGGER.error("Could not parse ping counters", e);
            }
        } else {
            LOGGER.debug("Line is not a match to counters pattern: '{}'", line);
        }
    }

    private static DecimalFormat numberFormatter() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        return new DecimalFormat("0.000000", symbols);
    }

    private static class CommandBuilder {

        private String address;

        private int packetSize;

        private int packetNumber;

        private int interval;

        private int timeout;

        CommandBuilder setAddress(String address) {
            this.address = Strings.nullToEmpty(address).trim();
            return this;
        }

        CommandBuilder setPacketSize(int packetSize) {
            this.packetSize = packetSize <= 0 ? 56 : packetSize;
            return this;
        }

        CommandBuilder setPacketNumber(int packetNumber) {
            this.packetNumber = packetNumber <= 0 ? 4 : packetNumber;
            return this;
        }

        CommandBuilder setInterval(int interval) {
            this.interval = interval <= 0 ? 1 : interval;
            return this;
        }

        CommandBuilder setTimeout(int timeout) {
            this.timeout = timeout < 0 ? 0 : timeout;
            return this;
        }

        String build() {
            StringBuilder builder =
                    new StringBuilder("ping -c ").append(packetNumber).append(" -i ").append(interval).append(" -s ")
                            .append(packetSize).append(" -w ");

            if (timeout == 0) {
                builder.append(packetNumber * interval + 10);
            } else {
                builder.append(timeout);
            }

            builder.append(" ").append(address);
            return builder.toString();
        }
    }
}
