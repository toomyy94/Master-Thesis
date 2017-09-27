package pt.ptinovacao.arqospocket.core;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import pt.ptinovacao.arqospocket.core.location.LocationInfo;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LocationTest {

    private static final String EXPECTED = "4036.18703 N 839.86579 W 1.199000 93.019997";

    @Test
    public void locationLatitudeIsFormattedCorrectly() throws Exception {
        Location location = getSampleLocation();

        String latitude = convert(location.getLatitude());
        assertEquals("40:36.18703", latitude);
    }

    @Test
    public void locationLatitudeIsParsedCorrectly() throws Exception {
        double latitude = round(Location.convert("40:36.18703"));
        assertEquals(40.6031172, latitude, 0.000000001);
    }

    @Test
    public void locationLongitudeIsFormattedCorrectly() throws Exception {
        Location location = getSampleLocation();

        String longitude = convert(location.getLongitude());
        assertEquals("-8:39.86579", longitude);
    }

    @Test
    public void locationLongitudeIsParsedCorrectly() throws Exception {
        double latitude = round(Location.convert("8:39.86579"));
        assertEquals(8.6644298, latitude, 0.000000001);
    }

    @Test
    public void numberIsFormattedCorrectly() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat formatter = new DecimalFormat("0.000000", symbols);
        formatter.setRoundingMode(RoundingMode.FLOOR);
        String result = formatter.format(8.6644298);
        assertEquals("8.664429", result);
    }

    @Test
    public void locationInfoWellFormatted() {
        LocationInfo info = LocationInfo.from(getSampleLocation());
        String formatted = info.format();
        assertEquals(EXPECTED, formatted);
    }

    private double round(double convert) {
        return Math.round(convert * 10000000.0) / 10000000.0;
    }

    private String convert(double value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat formatter = new DecimalFormat("###.#####", symbols);

        String prefix = "";
        if (value < 0) {
            prefix = "-";
            value = -value;
        }
        int degrees = (int) Math.floor(value);
        double minutes = (value - degrees) * 60;
        return prefix + degrees + ":" + formatter.format(minutes);
    }

    @NonNull
    private Location getSampleLocation() {
        Location location = new Location(LocationManager.NETWORK_PROVIDER);
        location.setLatitude(40.6031172);
        location.setLongitude(-8.6644298);
        location.setSpeed((float) (1.199 / 2.23693629));
        location.setBearing((float) 93.02);
        return location;
    }
}
