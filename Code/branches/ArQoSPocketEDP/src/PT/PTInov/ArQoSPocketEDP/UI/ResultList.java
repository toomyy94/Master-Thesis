package PT.PTInov.ArQoSPocketEDP.UI;

import java.util.ArrayList;
import java.util.List;

import PT.PTInov.ArQoSPocketEDP.R;
import PT.PTInov.ArQoSPocketEDP.Culture.CultureAdapter;
import PT.PTInov.ArQoSPocketEDP.Culture.Dictionary;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowBase;
import PT.PTInov.ArQoSPocketEDP.Utils.LogType;
import PT.PTInov.ArQoSPocketEDP.Utils.Logger;
import PT.PTInov.ArQoSPocketEDP.Utils.ResultTestListView;
import PT.PTInov.ArQoSPocketEDP.Utils.RowDataTwoLines;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
		final ImageView backButton = (ImageView) findViewById(R.id.backImg);
		backButton.setOnTouchListener( new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				
				Logger.v(tag, "backButton.setOnTouchListener", LogType.Debug, "event :" + event);

				switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						backButton.setImageDrawable(myRef.getResources().getDrawable(R.drawable.voltarpressed));
						break;
					case MotionEvent.ACTION_UP:
						backButton.setImageDrawable(myRef.getResources().getDrawable(R.drawable.back));
						myRef.finish();
						break;
				}
				
				return true;
			}
			
		});

		data = convertTaskStoreStructToRowData(MainActivity
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
			List<WorkFlowBase> sfList) {

		ArrayList<RowDataTwoLines> myRowData = new ArrayList<RowDataTwoLines>();

		if (sfList.size() == 0) {
			myRowData.add(new RowDataTwoLines(Dictionary.getNoResults(myRef), " ", true));
			indexHistory = -1;
		}

		for (WorkFlowBase sf : sfList) {
			// TODO: alterar isto.....................................................................................
			/*
			myRowData.add(new RowDataTwoLines(sf.getuserLocationInfo(), sf
					.getRegistryDateFormated(), sf.getSuccess()));
					*/
			indexHistory = 0;
		}

		return myRowData;
	}
}
