package pt.ptinovacao.arqospocket.map;

import pt.ptinovacao.arqospocket.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PinLegenda_Layout extends LinearLayout {

	private TextView tv_legenda;
	
	public PinLegenda_Layout(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.pin_legenda_layout, this,
				true);

		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.pinLegenda, 0, 0);

		ImageView myIcon = (ImageView) findViewById(R.id.img_type);
		Drawable drawable = array.getDrawable(R.styleable.pinLegenda_img_type);
		if (drawable != null) {
			myIcon.setImageDrawable(drawable);
		}

		TextView label = (TextView) findViewById(R.id.tv_label_type);
		String labelText = array.getString(R.styleable.pinLegenda_label_type);
		if (labelText != null) {
			label.setText(labelText);
		}
		
		tv_legenda = (TextView) findViewById(R.id.tv_legenda);

		array.recycle();
	}
	
	public void setLabel(String labelText) {
		if(tv_legenda != null)
			tv_legenda.setText(labelText);
	}

}