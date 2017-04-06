package pt.ptinovacao.arqospocket.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import pt.ptinovacao.arqospocket.R;

public class ImageSwitch_Layout extends RelativeLayout {

	private ImageView icon;
	private Switch s_connection;

	public ImageSwitch_Layout(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.custom_image_switch, this,
				true);

        icon = (ImageView) findViewById(R.id.icon);
		s_connection = (Switch) findViewById(R.id.radio_connection_switch);
	}

	public void setLabel(String labelText) {
        Log.d("s_connection",""+s_connection);
        Log.d("labelText",""+labelText);

        if(s_connection != null)
			s_connection.setText(labelText);
	}

	public void setIcon(Drawable drawable) {
		if(icon != null)
            icon.setImageDrawable(drawable);
	}

	public void setIconVisibility(Integer visibility) {
            icon.setVisibility(visibility);
	}

	public Switch getSwitch() {
		return s_connection;
	}

	public ImageView getIcon() {
		return icon;
	}

}