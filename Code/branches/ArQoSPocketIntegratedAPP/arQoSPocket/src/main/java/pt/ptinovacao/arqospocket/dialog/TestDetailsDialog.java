package pt.ptinovacao.arqospocket.dialog;

import pt.ptinovacao.arqospocket.DetailItem;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.RowItemTestesDetalhes;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TestDetailsDialog extends CustomListDialog {
	private static final String TAG = "TestDetailsDialog";

	private final static int resId = R.layout.dialog_list_testes_detalhes;
	
	private ImageView image_Percent_deta, imgState;
	private TextView txtPerc, txtName;
	
	public TestDetailsDialog(Context context, ArrayAdapter<?> adapter, 
			RowItemTestesDetalhes rowItemTestesDetalhes) {
		this(context, resId, adapter, rowItemTestesDetalhes);
	}
	
	public TestDetailsDialog(Context context, int view, final ArrayAdapter<?> adapter,
							 RowItemTestesDetalhes rowItemTestesDetalhes) {
		super(context, view, adapter);
		
		image_Percent_deta = (ImageView) findViewById(R.id.image_Percent_deta);		
		txtPerc = (TextView) findViewById(R.id.text_percent_deta);
		imgState = (ImageView) findViewById(R.id.imageState_deta);
		txtName = (TextView) findViewById(R.id.nome_teste_deta);

		image_Percent_deta.setImageResource(rowItemTestesDetalhes.getPercImg());
		txtPerc.setText(rowItemTestesDetalhes.getTextPerc());
		txtPerc.setTextColor(context.getResources().getColor(rowItemTestesDetalhes.getColor()));
		txtName.setText(rowItemTestesDetalhes.getTextName());
		txtName.setTextColor(context.getResources().getColor(rowItemTestesDetalhes.getColor()));
	
		imgState.setImageResource(rowItemTestesDetalhes.getState());

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DetailItem item = (DetailItem) adapter.getItem(position);

				Log.d(TAG, "Audio File: " + item.getFieldValue());
			}
		});
	}

}
