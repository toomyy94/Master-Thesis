package PT.PTInov.ArQoSPocket.UI;

import java.util.ArrayList;
import java.util.List;

import PT.PTInov.ArQoSPocket.R;
import PT.PTInov.ArQoSPocket.Culture.CultureAdapter;
import PT.PTInov.ArQoSPocket.Culture.Dictionary;
import PT.PTInov.ArQoSPocket.Utils.ResultTestListView;
import PT.PTInov.ArQoSPocket.Utils.RowDataTwoLines;
import PT.PTInov.ArQoSPocket.Utils.StoreAllTestInformation;
import PT.PTInov.ArQoSPocket.Utils.StoreInformation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
		
		CultureAdapter.loadListResults(this, this);

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

		data = convertTaskStoreStructToRowData(MainArQoSPocketRestartKillerActivity
				.getAllResult());

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
			List<StoreAllTestInformation> sfList) {

		ArrayList<RowDataTwoLines> myRowData = new ArrayList<RowDataTwoLines>();

		if (sfList.size() == 0) {
			myRowData.add(new RowDataTwoLines(Dictionary.getNoResults(myRef), " ", true));
			indexHistory = -1;
		}

		for (StoreAllTestInformation sf : sfList) {
			myRowData.add(new RowDataTwoLines(sf.getStoreInformation().getFormatedLocationInfo(), sf.getStoreInformation().getRegistryDateFormated(), sf.allTestState()));
			indexHistory = 0;
		}

		return myRowData;
	}
}
