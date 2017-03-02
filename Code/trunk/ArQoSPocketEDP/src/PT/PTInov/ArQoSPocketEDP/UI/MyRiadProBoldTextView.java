package PT.PTInov.ArQoSPocketEDP.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyRiadProBoldTextView extends TextView{
	
	public MyRiadProBoldTextView(Context context) {
		super(context);
		
		// set custom font to text header
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/MyriadPro-Bold.otf");
        this.setTypeface(tf);
	}

	public MyRiadProBoldTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// set custom font to text header
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/MyriadPro-Bold.otf");
        this.setTypeface(tf);
	}

	

}
