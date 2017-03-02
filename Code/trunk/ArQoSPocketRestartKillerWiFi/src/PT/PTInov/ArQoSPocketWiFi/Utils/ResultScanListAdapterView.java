package PT.PTInov.ArQoSPocketWiFi.Utils;

import java.util.List;

import PT.PTInov.ArQoSPocketWiFi.R;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultScanListAdapterView extends ArrayAdapter<ScanResult> {

	private final String tag = "ResultScanListAdapterView";

	private Context context;

	public ResultScanListAdapterView(Context pcontext, int textViewResourceId,
			List<ScanResult> objects) {
		super(pcontext, textViewResourceId, objects);

		context = pcontext;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		// widgets displayed by each item in your list
		TextView bssid = null;
		TextView ssid = null;
		TextView capabilites = null;
		TextView frequency = null;
		TextView level = null;
		TextView channel = null;

		String tagMethod = "onCreate";

		// we want to reuse already constructed row views...
		if (null == convertView) {

			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.rowwifiscandata, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}

		try {
			
			// data from your adapter
			ScanResult rowData = getItem(position);

			holder = (ViewHolder) convertView.getTag();

			bssid = holder.getBSSID();
			ssid = holder.getSSID();
			capabilites = holder.getCapabilites();
			frequency = holder.getFrequency();
			level = holder.getLevel();
			channel = holder.getChannel();

			bssid.setText(rowData.BSSID);
			ssid.setText(rowData.SSID);
			capabilites.setText(rowData.capabilities);
			/*
			if (capabilites.getText().length()>24) {
				TextView headerfrequency = (TextView) convertView.findViewById(R.id.headerfrequency);
				headerfrequency.setPadding(38, 20, 0, 0);
			}*/
			frequency.setText(rowData.frequency + "");
			level.setText(rowData.level + "");
			channel.setText(FrequencyToChannel.convertChannel(rowData.frequency) + "");
			
		} catch (Exception ex) {

		}

		return convertView;
	}

	/**
	 * Wrapper for row data.
	 * 
	 */
	private class ViewHolder {
		private View mRow;
		private TextView bssid = null;
		private TextView ssid = null;
		private TextView capabilites = null;
		private TextView frequency = null;
		private TextView level = null;
		private TextView channel = null;

		public ViewHolder(View row) {
			mRow = row;
		}
		
		public TextView getChannel() {

			if (null == channel) {
				channel = (TextView) mRow.findViewById(R.id.resultChannel);
			}

			return channel;
		}

		public TextView getBSSID() {

			if (null == bssid) {
				bssid = (TextView) mRow.findViewById(R.id.resultBSSID);
			}

			return bssid;
		}

		public TextView getSSID() {

			if (null == ssid) {
				ssid = (TextView) mRow.findViewById(R.id.NetworkName);
			}

			return ssid;
		}

		public TextView getCapabilites() {

			if (null == capabilites) {
				capabilites = (TextView) mRow
						.findViewById(R.id.resultcapabilities);
			}

			return capabilites;
		}

		public TextView getFrequency() {

			if (null == frequency) {
				frequency = (TextView) mRow.findViewById(R.id.resultfrequency);
			}

			return frequency;
		}

		public TextView getLevel() {

			if (null == level) {
				level = (TextView) mRow.findViewById(R.id.resultSignalLevel);
			}

			return level;
		}
	}
}
