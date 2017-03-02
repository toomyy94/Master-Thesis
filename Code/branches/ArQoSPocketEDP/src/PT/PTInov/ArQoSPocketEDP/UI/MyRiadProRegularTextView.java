package PT.PTInov.ArQoSPocketEDP.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyRiadProRegularTextView extends TextView{
	
	public MyRiadProRegularTextView(Context context) {
		super(context);
		
		// set custom font to text header
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/MyriadPro-Regular.otf");
        this.setTypeface(tf);
	}

	public MyRiadProRegularTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// set custom font to text header
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/MyriadPro-Regular.otf");
        this.setTypeface(tf);
	}

	

}
