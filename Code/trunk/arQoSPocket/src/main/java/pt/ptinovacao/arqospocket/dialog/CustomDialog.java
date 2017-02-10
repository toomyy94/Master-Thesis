package pt.ptinovacao.arqospocket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context, int view) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;

	}

}
