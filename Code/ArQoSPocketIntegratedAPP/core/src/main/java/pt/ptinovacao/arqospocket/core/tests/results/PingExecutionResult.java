package pt.ptinovacao.arqospocket.core.tests.results;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import pt.ptinovacao.arqospocket.core.network.PingResult;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;
import pt.ptinovacao.arqospocket.core.tests.data.results.PingTaskResult;

/**
 * Ping call execution result.
 * <p>
 * Created by Emílio Simões on 17-04-2017.
 */
public class PingExecutionResult extends BaseTaskExecutionResult {

    private final PingTaskResult taskResult;

    public PingExecutionResult(TaskResult result) {
        super(result);
        taskResult = (PingTaskResult) result;
    }

    public void updatePingData(PingResult result, String status) {
        DecimalFormat formatter = numberFormatter();
        taskResult.setMaximum(formatter.format(result.getMaximum()));
        taskResult.setMedium(formatter.format(result.getAverage()));
        taskResult.setMinimum(formatter.format(result.getMinimum()));
        taskResult.setSentPackets(formatter.format(result.getSentPackets()));
        taskResult.setReceivedPackets(formatter.format(result.getReceivedPackets()));
        taskResult.setLostPackets(formatter.format(result.getLostPackets()));
        taskResult.setStatus(status);
    }

    private static DecimalFormat numberFormatter() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        return new DecimalFormat("0.000000", symbols);
    }
}
