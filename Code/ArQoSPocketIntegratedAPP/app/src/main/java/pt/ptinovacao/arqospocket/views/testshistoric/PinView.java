package pt.ptinovacao.arqospocket.views.testshistoric;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pt.ptinovacao.arqospocket.R;

public class PinView extends LinearLayout {

    private TextView tvLegend;

    public PinView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pin_legend_layout, this,
                true);

        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.styleablePinLegend, 0, 0);

        ImageView myIcon = (ImageView) findViewById(R.id.img_type);
        Drawable drawable = array.getDrawable(R.styleable.styleablePinLegend_img_type);
        if (drawable != null) {
            myIcon.setImageDrawable(drawable);
        }

        TextView tvLabelType = (TextView) findViewById(R.id.tv_label_type);
        String labelText = array.getString(R.styleable.styleablePinLegend_label_type);
        if (labelText != null) {
            tvLabelType.setText(labelText);
        }

        tvLegend = (TextView) findViewById(R.id.tv_legenda);

        array.recycle();
    }

    public void setLabel(String labelText) {
        if (tvLegend != null)
            tvLegend.setText(labelText);
    }

}