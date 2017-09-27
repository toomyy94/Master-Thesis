package pt.ptinovacao.arqospocket.core.network.data;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for the radio info data.
 * <p>
 * Created by Emílio Simões on 08-05-2017.
 */
public abstract class RadioInfo {

    protected static final int RADIO_INFO_FIELD_COUNT = 42;

    /**
     * Formats the data in a string representation format (comma separated).
     *
     * @return the formatted string.
     */
    public abstract String format();

    /**
     * Gets the list of values positioned at the right index.
     *
     * @return the list of values.
     */
    protected abstract List<String> getPositionalValues();

    public String mergeAndFormat(RadioInfo other) {
        List<String> values1 = getPositionalValues();
        List<String> values2 = other.getPositionalValues();

        List<String> values = new ArrayList<>();
        for (int i = 0; i < RADIO_INFO_FIELD_COUNT; i++) {
            if (Strings.nullToEmpty(values1.get(i)).length() > 0) {
                values.add(values1.get(i));
            } else if (Strings.nullToEmpty(values2.get(i)).length() > 0) {
                values.add(values2.get(i));
            } else {
                values.add(StringUtils.EMPTY);
            }
        }

        return Joiner.on(',').useForNull("").join(values);
    }
}
