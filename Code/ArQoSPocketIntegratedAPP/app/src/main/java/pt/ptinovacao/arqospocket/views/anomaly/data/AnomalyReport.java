package pt.ptinovacao.arqospocket.views.anomaly.data;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.google.common.base.Strings;

import org.apache.commons.lang3.ArrayUtils;

import pt.ptinovacao.arqospocket.R;

/**
 * Maps a anomaly category into an ano
 * Created by Emílio Simões on 02-05-2017.
 */
public enum AnomalyReport {
    ALL(R.drawable.todas_anomalias, R.string.all_anomalies, R.mipmap.icon_litle_anomalias_bot_finalizar,
            R.mipmap.pin_mapa_outra),
    VOICE(R.drawable.selector_voz, R.string.anomaly_category_voice, R.mipmap.icon_menu_voz, R.mipmap.pin_mapa_voz),
    INTERNET(R.drawable.selector_internet, R.string.anomaly_category_internet, R.mipmap.icon_menu_internet,
            R.mipmap.pin_mapa_internet),
    MESSAGING(R.drawable.selector_messaging, R.string.anomaly_category_messaging, R.mipmap.icon_menu_messaging,
            R.mipmap.pin_mapa_messaging),
    COVERAGE(R.drawable.selector_cobertura, R.string.anomaly_category_network_coverage, R.mipmap.icon_menu_cobertura,
            R.mipmap.pin_mapa_cobertura),
    OTHER(R.drawable.selector_outra, R.string.anomaly_category_other, R.mipmap.icon_menu_outra,
            R.mipmap.pin_mapa_outra);

    private final int resourceIcon;

    private final int resourceText;

    private final int resourceImage;

    private final int pinResourceImage;

    AnomalyReport(@DrawableRes int resourceIcon, @StringRes int resourceText, int resourceImage, int pinResourceImage) {
        this.resourceIcon = resourceIcon;
        this.resourceText = resourceText;
        this.resourceImage = resourceImage;
        this.pinResourceImage = pinResourceImage;
    }

    public static AnomalyReport[] getListTypeAnomaly() {
        return getElementWithAllOrNot(false);
    }

    public int getResourceIcon() {
        return resourceIcon;
    }

    public int getResourceText() {
        return resourceText;
    }

    public int getResourceImage() {
        return resourceImage;
    }

    public int getPinResourceImage() {
        return pinResourceImage;
    }

    private static AnomalyReport[] getElementWithAllOrNot(boolean isActiveAll) {
        AnomalyReport[] anomalyReports;
        if (!isActiveAll) {
            anomalyReports = ArrayUtils.remove(values(), 0);
        } else {
            anomalyReports = values();
        }

        return anomalyReports;
    }

    public static int geImageOfText(Context context, String text) {
        for (AnomalyReport anomalyReport : getElementWithAllOrNot(false)) {
            if (Strings.nullToEmpty(text).equals(context.getResources().getString(anomalyReport.getResourceText()))) {
                return anomalyReport.getResourceIcon();
            }
        }
        return 0;
    }

    public static int getResourceImage(Context context, String text) {
        for (AnomalyReport anomalyReport : getElementWithAllOrNot(false)) {
            if (Strings.nullToEmpty(text).equals(context.getResources().getString(anomalyReport.getResourceText()))) {
                return anomalyReport.getResourceImage();
            }
        }
        return 0;
    }

    public static int getPinResourceImage(Context context, String text) {
        for (AnomalyReport anomalyReport : getElementWithAllOrNot(false)) {
            if (Strings.nullToEmpty(text).equals(context.getResources().getString(anomalyReport.getResourceText()))) {
                return anomalyReport.getPinResourceImage();
            }
        }
        return 0;
    }

    public static AnomalyReport geAnomalyReportOfText(Context context, String text) {
        for (AnomalyReport anomalyReport : getElementWithAllOrNot(false)) {
            if (Strings.nullToEmpty(text).equals(context.getResources().getString(anomalyReport.getResourceText()))) {
                return anomalyReport;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "AnomalyReport{" + "resourceIcon=" + resourceIcon + ", resourceText=" + resourceText + '}';
    }
}
