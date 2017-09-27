package pt.ptinovacao.arqospocket.core.anomalies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.persistence.AnomalyDao;
import pt.ptinovacao.arqospocket.persistence.models.Anomaly;

/**
 * Manager for the anomalies.
 * <p>
 * Created by Emílio Simões on 28-04-2017.
 */
public class AnomaliesManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnomaliesManager.class);

    private static AnomaliesManager instance;

    private final CoreApplication application;

    private final AnomalyDao anomalyDao;

    private AnomaliesManager(CoreApplication application) {
        this.application = application;
        anomalyDao = application.getDatabaseHelper().createAnomalyDao();
    }

    public static AnomaliesManager getInstance(CoreApplication application) {
        if (instance == null) {
            instance = new AnomaliesManager(application);
        }
        return instance;
    }

    public void reportAnomaly(Anomaly anomaly) {
        anomaly.setReportDate(Calendar.getInstance().getTime());
        anomaly.setReported(false);
        anomalyDao.saveAnomaly(anomaly).subscribe(new Consumer<Anomaly>() {
            @Override
            public void accept(@NonNull Anomaly anomaly) throws Exception {
                LOGGER.debug("Created anomaly");
            }
        });
    }
}
