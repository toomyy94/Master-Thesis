package PT.PTInov.ArQoSPocketEDP.Utils;

import java.util.List;

import PT.PTInov.ArQoSPocketEDP.R;
import PT.PTInov.ArQoSPocketEDP.DataStructs.StoreInformation;
import PT.PTInov.ArQoSPocketEDP.Service.CellInformation;
import PT.PTInov.ArQoSPocketEDP.UI.Result;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreDetailsListAdapter extends ArrayAdapter<StoreInformation> {

	private final String tag = "ResultCellScanListAdapter";

	private Context context;

	public MoreDetailsListAdapter(Context pcontext, int textViewResourceId,
			List<StoreInformation> objects) {
		super(pcontext, textViewResourceId, objects);

		context = pcontext;
	}

	@SuppressLint({ "ParserError", "NewApi", "NewApi" })
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		// widgets displayed by each item in your list
		TextView LocationLatitudeResult = null;
		TextView LocationLongitudeResult = null;
		TextView MobileConnectionCELLIDResult = null;
		TextView MobileConnectionLacResult = null;
		TextView MobileConnectionPscResult = null;
		TextView MobileConnectionRSSIResult = null;
		TextView MobileConnectionBERResult = null;
		TextView MobileConnectionNetworkTypeResult = null;
		TextView MobileConnectionIMSIResult = null;
		TextView MobileConnectionRoamingResult = null;
		TextView NeighboringCellResult = null;

		String tagMethod = "getView";

		// we want to reuse already constructed row views...
		if (null == convertView) {

			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.rowdatamoreinfo, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}

		try {
			
			holder = (ViewHolder) convertView.getTag();
			
			LocationLatitudeResult = holder.getLocationLatitudeResult();
			LocationLongitudeResult = holder.getLocationLongitudeResult();
			
			MobileConnectionCELLIDResult = holder.getMobileConnectionCELLIDResult();
			MobileConnectionLacResult = holder.getMobileConnectionLacResult();
			MobileConnectionPscResult = holder.getMobileConnectionPscResult();
			MobileConnectionRSSIResult = holder.getMobileConnectionRSSIResult();
			MobileConnectionBERResult = holder.getMobileConnectionBERResult();
			MobileConnectionNetworkTypeResult = holder.getMobileConnectionNetworkTypeResult();
			MobileConnectionIMSIResult = holder.getMobileConnectionIMSIResult();
			MobileConnectionRoamingResult = holder.getMobileConnectionRoamingResult();
			NeighboringCellResult = holder.getNeighboringCellResult();
			
			StoreInformation si = getItem(position);
			
			LocationLatitudeResult.setText(si.getLatitude());
			LocationLongitudeResult.setText(si.getLongitude());

			MobileConnectionCELLIDResult.setText(si.getGsmCellID() + "");
			MobileConnectionLacResult.setText(si.getGsmLac() + "");
			MobileConnectionPscResult.setText(si.getGsmPsc()+"");
			MobileConnectionRSSIResult.setText(si.getGsmSignalStrength()+" dBm");
			MobileConnectionBERResult.setText(si.getGsmBitErrorRate()+"");
			MobileConnectionNetworkTypeResult.setText(si.getNetworkType());
			MobileConnectionIMSIResult.setText(si.getIMSI());
			MobileConnectionRoamingResult.setText(si.getRoaming());
			
			// set the Neighboring list Cells
			StringBuilder neighboringCellString = new StringBuilder();
			
			if (si.getNeighboringCellList().isEmpty()) {
				neighboringCellString.append("Nenhuma célula vizinha encontrada.");
			}
			
			for (NeighboringCellInfo nsf :si.getNeighboringCellList()) {
				
				if (is2G(nsf.getNetworkType())) {
					neighboringCellString.append("CiD: "+nsf.getCid()+", ");
					neighboringCellString.append("RSSI: "+convertToDBM(nsf.getRssi(), nsf.getNetworkType())+", ");
					neighboringCellString.append("Lac: "+nsf.getLac()+", ");
				} else {
					neighboringCellString.append("RSSI: "+convertToDBM(nsf.getRssi(), nsf.getNetworkType())+", ");
					neighboringCellString.append("Psc: "+nsf.getPsc()+", ");
				}
				
				neighboringCellString.append(getNetworkType(nsf.getNetworkType())+" ");
				neighboringCellString.append("\n");
			}
			
			NeighboringCellResult.setText(neighboringCellString.toString());
			
			//define animation for ok button
			final ImageView okButton = holder.getOkButton();
			okButton.setOnTouchListener( new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					
					Logger.v(tag, "okButton.setOnTouchListener", LogType.Debug, "event :" + event);

					switch(event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							okButton.setImageDrawable(((Activity) context).getResources().getDrawable(R.drawable.bok2));
							break;
						case MotionEvent.ACTION_UP:
							okButton.setImageDrawable(((Activity) context).getResources().getDrawable(R.drawable.bok));
							if (Result.menuAddDialog != null)
								Result.menuAddDialog.cancel();
							break;
					}
					
					return true;
				}
				
			});
			
		} catch (Exception ex) {
			Logger.v(tag, tagMethod, LogType.Error, ex.toString());
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
	@SuppressLint("ParserError")
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
		
		
		private ImageView okButton = null;
		private TextView LocationLatitudeResult = null;
		private TextView LocationLongitudeResult = null;
		private TextView MobileConnectionCELLIDResult = null;
		private TextView MobileConnectionLacResult = null;
		private TextView MobileConnectionPscResult = null;
		private TextView MobileConnectionRSSIResult = null;
		private TextView MobileConnectionBERResult = null;
		private TextView MobileConnectionNetworkTypeResult = null;
		private TextView MobileConnectionIMSIResult = null;
		private TextView MobileConnectionRoamingResult = null;
		private TextView NeighboringCellResult = null;
		

		public ViewHolder(View row) {
			mRow = row;
		}
		
		public ImageView getOkButton() {
			if (null == okButton) {
				okButton = (ImageView) mRow.findViewById(R.id.OKButton);
			}

			return okButton;
		}
		
		public TextView getLocationLatitudeResult() {
			if (null == LocationLatitudeResult) {
				LocationLatitudeResult = (TextView) mRow.findViewById(R.id.LocationLatitudeResult);
			}

			return LocationLatitudeResult;
		}
		
		public TextView getLocationLongitudeResult() {
			if (null == LocationLongitudeResult) {
				LocationLongitudeResult = (TextView) mRow.findViewById(R.id.LocationLongitudeResult);
			}

			return LocationLongitudeResult;
		}
		
		public TextView getMobileConnectionCELLIDResult() {
			if (null == MobileConnectionCELLIDResult) {
				MobileConnectionCELLIDResult = (TextView) mRow.findViewById(R.id.MobileConnectionCELLIDResult);
			}

			return MobileConnectionCELLIDResult;
		}
		
		public TextView getMobileConnectionLacResult() {
			if (null == MobileConnectionLacResult) {
				MobileConnectionLacResult = (TextView) mRow.findViewById(R.id.MobileConnectionLacResult);
			}

			return MobileConnectionLacResult;
		}

		public TextView getMobileConnectionPscResult() {
			if (null == MobileConnectionPscResult) {
				MobileConnectionPscResult = (TextView) mRow.findViewById(R.id.MobileConnectionPscResult);
			}

			return MobileConnectionPscResult;
		}
		
		public TextView getMobileConnectionRSSIResult() {
			if (null == MobileConnectionRSSIResult) {
				MobileConnectionRSSIResult = (TextView) mRow.findViewById(R.id.MobileConnectionRSSIResult);
			}

			return MobileConnectionRSSIResult;
		}
		
		public TextView getMobileConnectionBERResult() {
			if (null == MobileConnectionBERResult) {
				MobileConnectionBERResult = (TextView) mRow.findViewById(R.id.MobileConnectionBERResult);
			}

			return MobileConnectionBERResult;
		}
		
		public TextView getMobileConnectionNetworkTypeResult() {
			if (null == MobileConnectionNetworkTypeResult) {
				MobileConnectionNetworkTypeResult = (TextView) mRow.findViewById(R.id.MobileConnectionNetworkTypeResult);
			}

			return MobileConnectionNetworkTypeResult;
		}
		
		public TextView getMobileConnectionIMSIResult() {
			if (null == MobileConnectionIMSIResult) {
				MobileConnectionIMSIResult = (TextView) mRow.findViewById(R.id.MobileConnectionIMSIResult);
			}

			return MobileConnectionIMSIResult;
		}
		
		public TextView getMobileConnectionRoamingResult() {
			if (null == MobileConnectionRoamingResult) {
				MobileConnectionRoamingResult = (TextView) mRow.findViewById(R.id.MobileConnectionRoamingResult);
			}

			return MobileConnectionRoamingResult;
		}
		
		public TextView getNeighboringCellResult() {
			if (null == NeighboringCellResult) {
				NeighboringCellResult = (TextView) mRow.findViewById(R.id.NeighboringCellResult);
			}

			return NeighboringCellResult;
		}
		
		
	}
}
