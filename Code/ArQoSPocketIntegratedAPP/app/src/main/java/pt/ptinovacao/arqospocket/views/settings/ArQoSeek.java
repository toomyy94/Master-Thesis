package pt.ptinovacao.arqospocket.views.settings;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import pt.ptinovacao.arqospocket.R;

public class ArQoSeek extends RelativeLayout {

    private TextView tvState;

    private SeekBar seekBar;

    public ArQoSeek(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_image_seek, this, true);

        tvState = (TextView) findViewById(R.id.tv_state_text);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    public void setLabel(String labelText) {
        if (seekBar != null) {
            tvState.setText(labelText);
        }
    }

    public void setLabel(@StringRes int labelText) {
        if (seekBar != null) {
            tvState.setText(labelText);
        }
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }
}