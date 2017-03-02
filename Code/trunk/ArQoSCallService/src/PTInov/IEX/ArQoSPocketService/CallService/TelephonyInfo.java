package PTInov.IEX.ArQoSPocketService.CallService;
/*
 * Developed by Ricardo Silva at PT Inovacao (ricardo-s-silva@ptinovacao.pt)
 *
 * banzap@gmail.com
 */
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import android.os.INetStatService;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.AsyncResult;
import com.android.internal.telephony.ITelephony;
import android.telephony.SignalStrength;
import com.android.internal.telephony.Call;
import android.os.SystemProperties;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.IccUtils;

public class TelephonyInfo {

	private TelephonyManager manager;
	private Phone phone;

	public static final int EVENT_RAW_GSM_PAGE = 400;
	public static final int EVENT_RAW = 401;
	public static final int EVENT_SET_PREFERRED_TYPE_DONE_TO_2G = 402;
	public static final int EVENT_SET_PREFERRED_TYPE_DONE_TO_3G = 403;

	public static final int PHASE_SET2G = 1;
	public static final int PHASE_SET3G = 3;
	public static final int PHASE_CALLING = 5;
	public static final int PHASE_END = 6;
	public static final int RECEIVE_LOG_INFO = 1020;

	private static final int INITIAL_DELAY = 8000;
	private static final int BETWEEN_CALLS_DELAY = 1000;

	private Handler handlerreceiver;
	private int callnumber = 0;
	private int lognumber = 0;
	private Timer timer_logs;
	private Timer timer_calls;
	private Timer timer_calls_cancel;
	private boolean running = false;

	public TelephonyInfo(TelephonyManager mng, Phone phone) {
		this.manager = mng;
		this.phone = phone;
	}
	
	public TelephonyInfo(TelephonyManager mng, Handler handler) {
		this.manager = mng;
		this.handlerreceiver = handler;
	}

	public TelephonyInfo(TelephonyManager mng, Handler handler, Phone phone) {
		this.manager = mng;
		this.handlerreceiver = handler;
		this.phone = phone;
	}

	public int makeCall(String number) {
		try {
			this.phone.dial(number);
			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;

	}

	public int endCall() {
		try {
			Call ringing = phone.getRingingCall();
			Call fg = phone.getForegroundCall();
			Call bg = phone.getBackgroundCall();
			if (!ringing.isIdle()) {
				ringing.hangup();
			} else if (!fg.isIdle()) {
				fg.hangup();
			} else if (!bg.isIdle()) {
				bg.hangup();
			}

			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;

	}

	public int disableDataConnectivity() {
		try {
			this.phone.disableDataConnectivity();
			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

	public int enableDataConnectivity() {
		try {
			this.phone.enableDataConnectivity();
			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

	public int SetTo2G() {
		// TODO Auto-generated method stub
		System.out.println("SetTo2G");
		Message m = new Message();
		m.what = PHASE_SET2G;
		handlerreceiver.sendMessage(m);
		this.disableDataConnectivity();
		phone.setPreferredNetworkType(1, mHandler
				.obtainMessage(EVENT_SET_PREFERRED_TYPE_DONE_TO_2G));
		return 0;
	}

	public int SetTo3G() {
		// TODO Auto-generated method stub
		System.out.println("SetTo3G");
		Message m = new Message();
		m.what = PHASE_SET3G;
		handlerreceiver.sendMessage(m);

		phone.setPreferredNetworkType(0, mHandler
				.obtainMessage(EVENT_SET_PREFERRED_TYPE_DONE_TO_3G));

		enableDataConnectivity();
		return 0;
	}

	private void obtainRaw() {

		byte[] init1;
		byte[] init2;
		byte[] gsm_page;
		byte[] finish;

		init1 = IccUtils.hexStringToBytes("0000000008000000010000005f000000");
		init2 = IccUtils.hexStringToBytes("0100000000000000");
		gsm_page = IccUtils.hexStringToBytes("0200000000000000");
		finish = IccUtils.hexStringToBytes("0000000008000000000000005f000000");

		phone.invokeOemRilRequestRaw(init1, mHandler.obtainMessage(EVENT_RAW));
		phone.invokeOemRilRequestRaw(init2, mHandler.obtainMessage(EVENT_RAW));
		phone.invokeOemRilRequestRaw(gsm_page, mHandler
				.obtainMessage(EVENT_RAW_GSM_PAGE));
		phone.invokeOemRilRequestRaw(finish, mHandler.obtainMessage(EVENT_RAW));

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			AsyncResult ar;
			switch (msg.what) {
			case EVENT_SET_PREFERRED_TYPE_DONE_TO_2G:
				//disableDataConnectivity();
				//runTask();
				break;
			case EVENT_SET_PREFERRED_TYPE_DONE_TO_3G:
				// System.out.println("EVENT_SET_PREFERRED_TYPE_DONE_TO_3G");
				//Message m = new Message();
				//m.what = PHASE_END;
				//handlerreceiver.sendMessage(m);
				break;
			case EVENT_RAW_GSM_PAGE:
				/*ar = (AsyncResult) msg.obj;
				if (ar.exception == null) {
					byte[] bytes = (byte[]) ar.result;
					updateRawGsmPage(bytes);
				} else {
					System.out.println("ERROR");
				}
*/
				break;

			default:
				break;

			}

		}
	};

	private final void updateRawGsmPage(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);

		int arfcn = bb.getInt();
		String lac = getStringFromBytes(bb, 20);
		String rac = getStringFromBytes(bb, 20);
		String mnc = getStringFromBytes(bb, 20);
		int rssi = bb.getInt();
		String cellinfo1 = getStringFromBytes(bb, 20);
		String cellinfo2 = getStringFromBytes(bb, 20);
		String cellinfo3 = getStringFromBytes(bb, 20);
		String cellinfo4 = getStringFromBytes(bb, 20);
		String cellinfo5 = getStringFromBytes(bb, 20);
		String cellinfo6 = getStringFromBytes(bb, 20);
		int rxquality = bb.getInt();
		int frequenthopping = bb.getInt();
		String lastregisterednetwork = getStringFromBytes(bb, 20);
		String tmsi = getStringFromBytes(bb, 20);
		int periodiclocationupdatevalue = bb.getInt();
		int band = bb.getInt();
		int channelinuse = bb.getInt();
		String rssi1 = getStringFromBytes(bb, 20);
		int lastcellreleasecause = bb.getInt();

		// tmsi = tmsi.replaceAll("\n","");
		// tmsi = tmsi.replaceAll("\t","");
		tmsi = RemoveSpecialCharacters(tmsi);
/*
		ServiceState servs = this.phone.getServiceState();
		GsmCellLocation celllocation = (GsmCellLocation) this.phone
				.getCellLocation();

		SignalStrength ss = this.phone.getSignalStrength();
		int signal = ss.getGsmSignalStrength();
		LogInfo info = new LogInfo(tmsi, celllocation.getCid() + "",
				(-113 + 2 * signal) + "", channelinuse, (new Date())
						.toLocaleString(), Settings.getNumber(), phone
						.getLine1Number(), lognumber);
		Message msg = new Message();
		msg.obj = info;
		msg.what = RECEIVE_LOG_INFO;
		this.handlerreceiver.sendMessage(msg);
*/
	}

	private static String getStringFromBytes(ByteBuffer buffer, int length) {
		byte[] bytes = new byte[length];
		buffer.get(bytes, buffer.arrayOffset(), 20);
		return new String(bytes);
	}

	public void initiateProcess() {
		callnumber = 0;
		lognumber = 0;
		timer_logs = new Timer();
		timer_calls = new Timer();
		timer_calls_cancel = new Timer();
		this.SetTo2G();
	}

	private void sendTestLogInfo() {
		/*LogInfo info = new LogInfo("TMSIXX", 12345 + "", (-113 + 2 * -70) + "",
				4, (new Date()).toLocaleString(), "Number", Settings
						.getNumber(), lognumber);
		Message msg = new Message();
		msg.obj = info;
		msg.what = RECEIVE_LOG_INFO;
		this.handlerreceiver.sendMessage(msg);*/
		// System.out.println("Send Log "+lognumber);
	}

	private void runTask() {
		if (!this.running) {
			try {
				this.runTaskLogs();
				this.runTaskCalls();
				this.running = true;
			} catch (java.lang.IllegalStateException e) {
				System.out.println("Timer was cancelled");
			}
		}
	}

	private void runTaskLogs() {

		System.out.println("Get Logs");
		TimerTask task = new TimerTask() {

			public void run() {
				// TODO Auto-generated method stub

				lognumber++;
				// sendTestLogInfo();
				obtainRaw();
			}

		};
		//timer_logs.schedule(task, 8000, 1 * Settings.getFrequencyoflogs());
	}

	private void runTaskCalls() {
		/*
		System.out.println("Make calls");
		TimerTask calltask = new TimerTask() {

			public void run() {
				// TODO Auto-generated method stub

				makeCall(Settings.getNumber());
				callnumber++;
				System.out.println("Make call " + callnumber);
				Message msg = new Message();
				msg.arg1 = callnumber;
				msg.what = PHASE_CALLING;
				handlerreceiver.sendMessage(msg);
				if (callnumber == Settings.getNumberofcalls()) {
					this.cancel();
				}
				
			}
		};
		
		timer_calls.schedule(calltask, INITIAL_DELAY, Settings
				.getTimeofthecall()
				* 1000 + BETWEEN_CALLS_DELAY);

		System.out.println("Cancel calls");
		TimerTask canceltask = new TimerTask() {

			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Cancel call " + callnumber);
				endCall();
				if (callnumber == Settings.getNumberofcalls()) {
					this.cancel();
					timer_logs.cancel();
					SetTo3G();
					running = false;
				}
			}
		};
		timer_calls_cancel.schedule(canceltask, INITIAL_DELAY
				+ Settings.getTimeofthecall() * 1000, Settings
				.getTimeofthecall()
				* 1000 + BETWEEN_CALLS_DELAY);
				* */
	}

	public void stopProcess() {
		timer_calls.cancel();
		timer_calls_cancel.cancel();
		timer_logs.cancel();
		this.SetTo3G();
		this.running = false;
	}

	public static String RemoveSpecialCharacters(String str) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if ((str.charAt(i) >= '0' && str.charAt(i) <= '9')
					|| (str.charAt(i) >= 'A' && str.charAt(i) <= 'z' || (str.charAt(i) == '.' || str.charAt(i) == '_')))
				sb.append(str.charAt(i));
		}

		return sb.toString();
	}

}