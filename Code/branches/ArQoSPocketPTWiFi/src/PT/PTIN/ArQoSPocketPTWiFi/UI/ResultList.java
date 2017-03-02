package PT.PTIN.ArQoSPocketPTWiFi.UI;

import java.util.ArrayList;
import java.util.List;


import PT.PTIN.ArQoSPocketPTWiFi.R;
import PT.PTIN.ArQoSPocketPTWiFi.Store.StoreInformation;
import PT.PTIN.ArQoSPocketPTWiFi.Utils.RowDataTwoLines;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ResultList extends Activity {

	private final static String tag = "ResultList";

	private LayoutInflater mInflater = null;
	private ListAdapter adapter = null;
	private List<RowDataTwoLines> data = null;

	public ResultList myRef = null;

	public static int indexHistory = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultlist);

		// Set my reference
		myRef = this;
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		// Set back event
		ImageView backButton = (ImageView) findViewById(R.id.backImg);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// Start the interactive tests activity
				myRef.finish();
			}
		});

		data = convertTaskStoreStructToRowData(ArQoSPocketPTWiFiActivity.getAllStoreInformation());

		adapter = new ResultTestListView(this,
				R.layout.rowlistwithtwolinesandtwoimgs, data);
		ListView listView = (ListView) findViewById(R.id.resutlTestlist);
		listView.setAdapter(adapter);

		// Set the click event
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (indexHistory != -1) {
					indexHistory = position;
					Log.v(tag, "You selected: " + position + " item");

					startActivity(new Intent(myRef, Result.class));
				}
			}

		});

	}

	private List<RowDataTwoLines> convertTaskStoreStructToRowData(
			List<StoreInformation> sfList) {

		ArrayList<RowDataTwoLines> myRowData = new ArrayList<RowDataTwoLines>();

		if (sfList.size() == 0) {
			myRowData.add(new RowDataTwoLines("Sem Resultados", " ", true));
			indexHistory = -1;
		}

		for (StoreInformation sf : sfList) {
			myRowData.add(new RowDataTwoLines(sf.getuserLocationInfo(), sf
					.getRegistryDateFormated(), sf.getSuccess()));
			indexHistory = 0;
		}

		return myRowData;
	}
}
