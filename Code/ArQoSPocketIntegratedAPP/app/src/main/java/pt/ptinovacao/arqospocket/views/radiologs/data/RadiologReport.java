package pt.ptinovacao.arqospocket.views.radiologs.data;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.google.common.base.Strings;

import org.apache.commons.lang3.ArrayUtils;

import pt.ptinovacao.arqospocket.R;

/**
 * Maps a radiolog category into an rad
 * Created by Tomás Rodrigues on 02-05-2017.
 */
public enum RadiologReport {
    ALL(R.mipmap.icon_menu_internet, R.string.all_radiologs, R.mipmap.pin_radiolog, R.mipmap.pin_radiologs_ok),
    RADIOLOG(R.mipmap.icon_radiologs, R.string.radiolog, R.mipmap.pin_radiolog, R.mipmap.pin_radiolog_ok),
    EVENT(R.mipmap.icon_radiologs, R.string.radiolog_event, R.mipmap.pin_event, R.mipmap.pin_event_ok),
    SNAPSHOT(R.mipmap.icon_snapshot, R.string.snapshot, R.mipmap.pin_snapshot, R.mipmap.pin_snapshot_ok),
    SCANLOG(R.mipmap.icon_wifi_menu, R.string.scanlog, R.mipmap.pin_scanlog, R.mipmap.pin_scanlog_ok);

    private final int resourceIcon;

    private final int resourceText;

    private final int resourceImage;

    private final int pinResourceImage;

    RadiologReport(@DrawableRes int resourceIcon, @StringRes int resourceText, int resourceImage, int pinResourceImage) {
        this.resourceIcon = resourceIcon;
        this.resourceText = resourceText;
        this.resourceImage = resourceImage;
        this.pinResourceImage = pinResourceImage;
    }

    public static RadiologReport[] getListTypeRadiolog() {
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

    private static RadiologReport[] getElementWithAllOrNot(boolean isActiveAll) {
        RadiologReport[] radiologsReports;
        if (!isActiveAll) {
            radiologsReports = ArrayUtils.remove(values(), 0);
        } else {
            radiologsReports = values();
        }

        return radiologsReports;
    }

    public static int geImageOfText(Context context, String text) {
        for (RadiologReport radiologReport : getElementWithAllOrNot(false)) {
            if (Strings.nullToEmpty(text).equals(context.getResources().getString(radiologReport.getResourceText()))) {
                return radiologReport.getResourceIcon();
            }
        }
        return 0;
    }

    public static int getResourceImage(Context context, String text) {
        for (RadiologReport radiologReport : getElementWithAllOrNot(false)) {
            if (Strings.nullToEmpty(text).equals(context.getResources().getString(radiologReport.getResourceText()))) {
                return radiologReport.getResourceImage();
            }
        }
        return 0;
    }

    public static int getPinResourceImage(Context context, String text) {
        for (RadiologReport radiologReport : getElementWithAllOrNot(false)) {
            if (Strings.nullToEmpty(text).equals(context.getResources().getString(radiologReport.getResourceText()))) {
                return radiologReport.getPinResourceImage();
            }
        }
        return 0;
    }

    public static RadiologReport getRadiologReportOfText(Context context, String text) {
        for (RadiologReport radiologReport : getElementWithAllOrNot(false)) {
            if (Strings.nullToEmpty(text).equals(context.getResources().getString(radiologReport.getResourceText()))) {
                return radiologReport;
            }
        }
        return null;
    }



    @Override
    public String toString() {
        return "RadiologReport{" + "resourceIcon=" + resourceIcon + ", resourceText=" + resourceText + '}';
    }
}
