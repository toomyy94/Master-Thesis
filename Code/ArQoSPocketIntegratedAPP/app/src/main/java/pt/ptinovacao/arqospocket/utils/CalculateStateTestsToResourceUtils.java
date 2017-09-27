package pt.ptinovacao.arqospocket.utils;

import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.network.ConnectionTechnology;
import pt.ptinovacao.arqospocket.views.tests.TestsFragment;

public class CalculateStateTestsToResourceUtils {

    private int intResourceColor;

    private int intResourceImage;

    private int intResourceStringPercentage;

    private CalculateStateTestsToResourceUtils(TestsFragment.StateTest stateTest, int position) {
        switch (stateTest) {
            case COMPLETED:
                testCompleted();
                break;
            case FAILED:
                testFailed(position);
                break;
            case ONGOIONG:
                testOngoing(position);
                break;
            case INACTIVE:
            default:
                testInactive();
                break;
        }
    }

    public CalculateStateTestsToResourceUtils(TestsFragment.StateTest stateTest) {
        this(stateTest, 0);
    }

    private void testOngoing(int percentage) {

        switch (percentage) {

            case 0:
                intResourceImage = R.mipmap.falhado_default;
                intResourceStringPercentage = R.string.percent_0;
                intResourceColor = R.color.ongoing;
                break;

            case 10:

                intResourceImage = R.mipmap.progress_ongoing_10;
                intResourceStringPercentage = R.string.percent_10;
                intResourceColor = R.color.ongoing;
                break;
            case 20:

                intResourceImage = R.mipmap.progress_ongoing_20;
                intResourceStringPercentage = R.string.percent_20;
                intResourceColor = R.color.ongoing;
                break;
            case 30:

                intResourceImage = R.mipmap.progress_ongoing_30;
                intResourceStringPercentage = R.string.percent_30;
                intResourceColor = R.color.ongoing;
                break;
            case 40:

                intResourceImage = R.mipmap.progress_ongoing_40;
                intResourceStringPercentage = R.string.percent_40;
                intResourceColor = R.color.ongoing;
                break;
            case 50:

                intResourceImage = R.mipmap.progress_ongoing_50;
                intResourceStringPercentage = R.string.percent_50;
                intResourceColor = R.color.ongoing;
                break;
            case 60:
                intResourceImage = R.mipmap.progress_ongoing_60;
                intResourceStringPercentage = R.string.percent_60;
                intResourceColor = R.color.ongoing;
                break;
            case 70:
                intResourceImage = R.mipmap.progress_ongoing_70;

                break;
            case 80:
                intResourceImage = R.mipmap.progress_ongoing_80;
                intResourceStringPercentage = R.string.percent_80;
                intResourceColor = R.color.ongoing;
                break;
            case 90:
                intResourceImage = R.mipmap.progress_ongoing_90;
                intResourceStringPercentage = R.string.percent_90;
                intResourceColor = R.color.ongoing;
                break;
            default:
            case 100:
                intResourceImage = R.mipmap.progress_ongoing_100;
                intResourceStringPercentage = R.string.percent_100;
                intResourceColor = R.color.ongoing;
                break;
        }
    }

    private void testFailed(int percentage) {
        switch (percentage) {
            case 10:
                intResourceImage = R.mipmap.falhado_10;
                intResourceStringPercentage = R.string.percent_10;
                intResourceColor = R.color.failed;
                break;
            case 20:
                intResourceImage = R.mipmap.falhado_20;
                intResourceStringPercentage = R.string.percent_20;
                intResourceColor = R.color.failed;
                break;
            case 30:
                intResourceImage = R.mipmap.falhado_30;
                intResourceStringPercentage = R.string.percent_30;
                intResourceColor = R.color.failed;
                break;
            case 40:
                intResourceImage = R.mipmap.falhado_40;
                intResourceStringPercentage = R.string.percent_40;
                intResourceColor = R.color.failed;
                break;
            case 50:
                intResourceImage = R.mipmap.falhado_50;
                intResourceStringPercentage = R.string.percent_50;
                intResourceColor = R.color.failed;
                break;
            case 60:
                intResourceImage = R.mipmap.falhado_60;
                intResourceStringPercentage = R.string.percent_60;
                intResourceColor = R.color.failed;
                break;
            case 70:
                intResourceImage = R.mipmap.falhado_70;
                intResourceStringPercentage = R.string.percent_70;
                intResourceColor = R.color.failed;
                break;
            case 80:
                intResourceImage = R.mipmap.falhado_80;
                intResourceStringPercentage = R.string.percent_80;
                intResourceColor = R.color.failed;
                break;
            case 90:
                intResourceImage = R.mipmap.falhado_90;
                intResourceStringPercentage = R.string.percent_90;
                intResourceColor = R.color.failed;
                break;
            case 100:
                intResourceImage = R.mipmap.falhado_100;
                intResourceStringPercentage = R.string.percent_100;
                intResourceColor = R.color.failed;
                break;
            default:
                intResourceImage = R.mipmap.falhado_100;
                intResourceStringPercentage = R.string.percent_0;
                intResourceColor = R.color.failed;
                break;
        }
    }

    private void testCompleted() {

        intResourceImage = R.mipmap.concluido_100;
        intResourceStringPercentage = R.string.percent_100;
        intResourceColor = R.color.completed;
    }

    private void testInactive() {
        intResourceImage = R.mipmap.falhado_default;
        intResourceStringPercentage = R.string.percent_0;
        intResourceColor = R.color.inactive;
    }

    public int getIntResourceColor() {
        return intResourceColor;
    }

    public int getIntResourceStringPercentage() {
        return intResourceStringPercentage;
    }

    public int getIntResourceImage() {
        return intResourceImage;
    }

    public static int getTestBigRowIconResource(ConnectionTechnology testTechnology, boolean state) {
        int resource = -1;

        if (testTechnology == null) {
            testTechnology = ConnectionTechnology.NA;
        }

        switch (testTechnology) {
            case WIFI:
                resource = (state ? R.mipmap.icon_wi_fi_sucesso_big : R.mipmap.icon_wi_fi_erro_big);
                break;
            case MOBILE:
                resource = (state ? R.mipmap.icon_rede_movel_sucesso_big : R.mipmap.icon_rede_movel_erro_big);
                break;
            case MIXED:
                resource = (state ? R.mipmap.icon_misto_sucesso_big : R.mipmap.icon_misto_erro_big);
                break;
            case NA:
            case NOT_CONNECTED:
                resource = (state ? R.mipmap.icon_misto_sucesso_big : R.mipmap.icon_misto_erro_big);
                break;
        }
        return resource;
    }

    public static int getTestPinResource(ConnectionTechnology testTechnology, boolean isMarkerStateOn, boolean isOK) {
        int resource = -1;
        if (testTechnology != null) {
            switch (testTechnology) {
                case WIFI:
                    resource =
                            (isMarkerStateOn ? (isOK ? R.mipmap.pin_mapa_wifi_sucesso : R.mipmap.pin_mapa_wifi_erro) :
                                    R.mipmap.pin_mapa_wi_fi_off);
                    break;
                case MOBILE:
                    resource = (isMarkerStateOn ?
                            (isOK ? R.mipmap.pin_mapa_rede_movel_sucesso : R.mipmap.pin_mapa_rede_movel_erro) :
                            R.mipmap.pin_mapa_rede_movel_off);
                    break;
                case MIXED:
                default:
                    resource =
                            (isMarkerStateOn ? (isOK ? R.mipmap.pin_mapa_misto_sucesso : R.mipmap.pin_mapa_misto_erro) :
                                    R.mipmap.pin_mapa_misto_off);
                    break;
            }
        }
        return resource;
    }

}
