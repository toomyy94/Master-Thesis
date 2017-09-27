package pt.ptinovacao.arqospocket.views.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import pt.ptinovacao.arqospocket.R;

public class ArQoSProgressBar extends RelativeLayout {

	private SeekBar seek_bar;
	private TextView progress_text;


	public ArQoSProgressBar(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.custom_seekbar, this,
				true);

        seek_bar = (SeekBar) findViewById(R.id.radiolog_periodicity);
        progress_text = (TextView) findViewById(R.id.progress_text);

	}

	public void setProgress_text(String text) {
		if(text != null)
            progress_text.setText(text);
	}

	public SeekBar getSeek_bar() {
		return seek_bar;
	}

	public TextView getProgress_text() {
		return progress_text;
	}

}