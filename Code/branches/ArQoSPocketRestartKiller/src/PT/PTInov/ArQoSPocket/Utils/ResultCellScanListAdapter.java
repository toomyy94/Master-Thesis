package PT.PTInov.ArQoSPocket.Utils;

import java.util.List;

import PT.PTInov.ArQoSPocket.R;
import PT.PTInov.ArQoSPocket.Service.CellInformation;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultCellScanListAdapter extends ArrayAdapter<NeighboringCellInfo> {

	private final String tag = "ResultCellScanListAdapter";

	private Context context;

	public ResultCellScanListAdapter(Context pcontext, int textViewResourceId,
			List<NeighboringCellInfo> objects) {
		super(pcontext, textViewResourceId, objects);

		context = pcontext;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		// widgets displayed by each item in your list
		TextView cid = null;
		TextView lac = null;
		TextView NetWorkType = null;
		TextView Psc = null;
		TextView Rssi = null;

		String tagMethod = "onCreate";

		// we want to reuse already constructed row views...
		if (null == convertView) {

			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.rowcellscandata, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}

		try {
			
			// data from your adapter
			NeighboringCellInfo rowData = getItem(position);

			holder = (ViewHolder) convertView.getTag();

			cid = holder.getCid();
			lac = holder.getLac();
			NetWorkType = holder.getNetWorkType();
			Psc = holder.getPsc();
			Rssi = holder.getRssi();

			//cid.setText(rowData.getCid()+"");
			cid.setText(getPaddedHex(rowData.getCid(),is2G(rowData.getNetworkType())?4:8));
			lac.setText(rowData.getLac()+"");
			NetWorkType.setText( getNetworkType(rowData.getNetworkType()));
			Psc.setText(rowData.getPsc()+"");
			Rssi.setText(convertToDBM(rowData.getRssi(), rowData.getNetworkType())+" ");
			
		} catch (Exception ex) {

		}

		return convertView;
	}
	
	public int convertToDBM(int signalStrengthSystem, int networkType) {
		
		if (networkType == TelephonyManager.NETWORK_TYPE_GPRS || networkType ==TelephonyManager.NETWORK_TYPE_EDGE) {
			return (-113 + (2 * signalStrengthSystem));
		} else {
			return (-116 + (1 * signalStrengthSystem));
		}
	}

	/**
	 * Convert an int to an hex String and pad with 0's up to minLen.
	 */
	private String getPaddedHex(int nr, int minLen) {
		
		if (minLen == 4) {
			
			return nr+"";
			
		} else {
			String str = Integer.toHexString(nr);
			str = str.substring(2, str.length());
			
			return Integer.parseInt(str, 16)+"";
		}
	}
	
	
	private boolean is2G(int networkType) {
		
		switch (networkType) {
		case (TelephonyManager.NETWORK_TYPE_GPRS): // 2G
			return true;
		case (TelephonyManager.NETWORK_TYPE_EDGE): // 2G
			return  true;
		case (TelephonyManager.NETWORK_TYPE_HSDPA): // 3G
			return false;
		case (TelephonyManager.NETWORK_TYPE_HSPA): // 3G
			return false;
		case (TelephonyManager.NETWORK_TYPE_HSUPA): // 3G
			return false;
		case (TelephonyManager.NETWORK_TYPE_UMTS): // 3G
			return false;
		case (TelephonyManager.NETWORK_TYPE_UNKNOWN): // 3G
			return false;
		default:
			break;
		}
		
		return false;
	}
	
	private String getNetworkType(int networkType) {
		
		switch (networkType) {
		case (TelephonyManager.NETWORK_TYPE_GPRS): // 2G
			return "GPRS";
		case (TelephonyManager.NETWORK_TYPE_EDGE): // 2G
			return  "EDGE";
		case (TelephonyManager.NETWORK_TYPE_HSDPA): // 3G
			return "HSDPA";
		case (TelephonyManager.NETWORK_TYPE_HSPA): // 3G
			return "HSPA";
		case (TelephonyManager.NETWORK_TYPE_HSUPA): // 3G
			return "HSUPA";
		case (TelephonyManager.NETWORK_TYPE_UMTS): // 3G
			return "UMTS";
		case (TelephonyManager.NETWORK_TYPE_UNKNOWN): // 3G
			return "UNKNOWN";
		default:
			break;
		}
		
		return  "UNKNOWN";
	}

	/**
	 * Wrapper for row data.
	 * 
	 */
	private class ViewHolder {
		private View mRow;
		private TextView cid = null;
		private TextView lac = null;
		private TextView NetWorkType = null;
		private TextView Psc = null;
		private TextView Rssi = null;

		public ViewHolder(View row) {
			mRow = row;
		}

		public TextView getCid() {

			if (null == cid) {
				cid = (TextView) mRow.findViewById(R.id.resultCid);
			}

			return cid;
		}

		public TextView getLac() {

			if (null == lac) {
				lac = (TextView) mRow.findViewById(R.id.resultLac);
			}

			return lac;
		}

		public TextView getNetWorkType() {

			if (null == NetWorkType) {
				NetWorkType = (TextView) mRow
						.findViewById(R.id.resultNetWorkType);
			}

			return NetWorkType;
		}

		public TextView getPsc() {

			if (null == Psc) {
				Psc = (TextView) mRow.findViewById(R.id.resultPsc);
			}

			return Psc;
		}

		public TextView getRssi() {

			if (null == Rssi) {
				Rssi = (TextView) mRow.findViewById(R.id.resultRssi);
			}

			return Rssi;
		}
	}
}
