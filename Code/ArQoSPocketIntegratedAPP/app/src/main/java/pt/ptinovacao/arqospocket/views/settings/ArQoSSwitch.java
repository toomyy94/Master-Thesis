package pt.ptinovacao.arqospocket.views.settings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import pt.ptinovacao.arqospocket.R;

public class ArQoSSwitch extends RelativeLayout {

    private ImageView icon;

    private TextView tvState;

    private Switch sConnection;

    public ArQoSSwitch(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_image_switch, this, true);

        icon = (ImageView) findViewById(R.id.icon);
        tvState = (TextView) findViewById(R.id.tv_state_text);
        sConnection = (Switch) findViewById(R.id.radio_connection_switch);
    }

    public void setLabel(String labelText) {
        if (sConnection != null) {
            tvState.setText(labelText);
        }
    }
    public void setLabel(@StringRes int labelText) {
        if (sConnection != null) {
            tvState.setText(labelText);
        }
    }
    public void setIcon(Drawable drawable) {
        if (icon != null) {
            icon.setImageDrawable(drawable);
        }
    }
    public void setIcon(@DrawableRes int drawable) {
        if (icon != null) {
            icon.setImageResource(drawable);
        }
    }

    public void setIconVisibility(Integer visibility) {
        if (icon == null) {
            return;
        }

        icon.setVisibility(visibility);
    }

    public Switch getSwitch() {
        return sConnection;
    }

    public ImageView getIcon() {
        return icon;
    }

}