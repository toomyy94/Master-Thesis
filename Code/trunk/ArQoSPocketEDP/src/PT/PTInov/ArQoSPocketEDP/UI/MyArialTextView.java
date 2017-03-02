package PT.PTInov.ArQoSPocketEDP.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyArialTextView extends TextView{
	
	public MyArialTextView(Context context) {
		super(context);
		
		// set custom font to text header
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/arial.ttf");
        this.setTypeface(tf);
	}

	public MyArialTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// set custom font to text header
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/arial.ttf");
        this.setTypeface(tf);
	}

	

}
