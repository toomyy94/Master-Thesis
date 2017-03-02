package pt.ptinovacao.arqospocket.service.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.alarm.IAlarm;
import pt.ptinovacao.arqospocket.service.alarm.MyAlarmManager;
import pt.ptinovacao.arqospocket.service.interfaces.IRunTaskWorker;
import pt.ptinovacao.arqospocket.service.interfaces.IRunTaskWorkerCallback;
import pt.ptinovacao.arqospocket.service.interfaces.IServiceNetworksInfo;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.AnswerVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.AssociateWiFiTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.DeassociateWiFiTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.HTTPDownloadTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.HTTPUploadTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.HangUpVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.MakeVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.PingTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.ReassociateWiFiTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.RecordAudioTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.ScanWiFiNetworksTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.SendMMSTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.SendMailTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.SendSMSTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.TurnOFFMobileTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.TurnOFFWiFiTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.TurnONMobileTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.TurnONWiFiTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.WiFiAuthLoginTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.WiFiAuthLogoutTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonresult.AssociateWiFiTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.DeassociateWiFiTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.HTTPDownloadTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.HTTPUploadTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.PingTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.ReassociateWiFITaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.ScanWiFiTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.TaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.TaskJsonResultError;
import pt.ptinovacao.arqospocket.service.jsonresult.TurnOFFWiFiTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.TurnONWiFiTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.WiFiAuthLoginTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.WiFiNetworkJsonInformation;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.service.tasks.AnswerVoiceCall;
import pt.ptinovacao.arqospocket.service.tasks.HangUpVoiceCall;
import pt.ptinovacao.arqospocket.service.tasks.HttpService;
import pt.ptinovacao.arqospocket.service.tasks.MobileONOFFAdapterTask;
import pt.ptinovacao.arqospocket.service.tasks.Ping;
import pt.ptinovacao.arqospocket.service.tasks.RecordAudio;
import pt.ptinovacao.arqospocket.service.tasks.MakeVoiceCall;
import pt.ptinovacao.arqospocket.service.tasks.WiFiService;
import pt.ptinovacao.arqospocket.service.tasks.WisprAuth;
import pt.ptinovacao.arqospocket.service.tasks.enums.WiFiModuleState;
import pt.ptinovacao.arqospocket.service.tasks.results.HttpServiceResponse;
import pt.ptinovacao.arqospocket.service.tasks.results.PingResponse;
import pt.ptinovacao.arqospocket.service.tasks.structs.AssociationResultState;
import pt.ptinovacao.arqospocket.service.tasks.structs.LoginTaskResult;
import pt.ptinovacao.arqospocket.service.tasks.structs.LogoffTaskResult;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileAdvancedInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileBasicInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.WiFiAdvancedInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.structs.WifiBasicInfoStruct;
import pt.ptinovacao.arqospocket.service.utils.ConnectionInfo;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Pair;

public class RunTaskWorker extends AsyncTask<URL, Pair<TaskJsonResult,String>, TaskJsonResult> implements IAlarm, IRunTaskWorker {
	
	private final static Logger logger = LoggerFactory.getLogger(RunTaskWorker.class);
	private final long delayBeforeStartThread = 100;
	
	private RunTaskWorker myRef = null;
	private AsyncTask<URL, Pair<TaskJsonResult,String>, TaskJsonResult> myInternalRef = null;
	private MyAlarmManager myAlarmManager = null;
	
	private Context appContext = null;
	private IRunTaskWorkerCallback iCallback = null;
	private IServiceNetworksInfo iServiceNetworksInfo = null;
	
	private boolean stopTest = false;
	private TaskStruct taskToRun = null;
	
	private String id_test = null;
	private String task_name = null;
	private String task_id = null;
	
	private Date startTaskDate = null;
	private Date endTaskDate = null;
	
	private GPSInformation gps_information = null;
	private boolean returned = false;
	
	
	public RunTaskWorker(TaskStruct taskToRun, String id_test, Context appContext, IServiceNetworksInfo iServiceNetworksInfo, IRunTaskWorkerCallback iCallback, GPSInformation gps_information) {
		final String method = "RunTaskWorker";
		
		try {
			
			myInternalRef = this;
			myRef = this;
			
			this.gps_information = gps_information;
			this.taskToRun = taskToRun;
			this.id_test = id_test;
			this.appContext = appContext;
			this.iCallback = iCallback;
			this.iServiceNetworksInfo = iServiceNetworksInfo;
			
			myAlarmManager = new MyAlarmManager(appContext);
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public void startThread() {
		final String method = "startThread";
		
		try {
			Handler myHandler = new Handler();
			Runnable r = new Runnable()
			{
				public void run() 
				{
					try {
		    		
						if (myInternalRef != null)
							myInternalRef.execute(null, null, null);
		    		
					} catch(Exception ex) {
						MyLogger.error(logger, method, ex);
					}
				}
			};
		
			long timeout = Long.parseLong(taskToRun.get_timeout());
			timeout *= 1000;
			Calendar calendar = Calendar.getInstance();
			
			long endTimoutAlarm = System.currentTimeMillis()+timeout+delayBeforeStartThread;
			MyLogger.trace(logger, method, "endTimoutAlarm :"+endTimoutAlarm);
			
			calendar.setTimeInMillis(endTimoutAlarm);
			myAlarmManager.setAlarm(calendar, id_test, myRef);
			myHandler.postDelayed(r, delayBeforeStartThread);
		
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	@Override
	protected TaskJsonResult doInBackground(URL... arg0) { 
		final String method = "doInBackground";
		
		// decompem o teste e executa o respectivo teste
		
		try {
			
			if (taskToRun == null)
				return new TaskJsonResultError(new Date(System.currentTimeMillis()),
						new Date(System.currentTimeMillis()), new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
			
			if (taskToRun instanceof AssociateWiFiTaskStruct) {
			
				MyLogger.trace(logger, method, "AssociateWiFiTaskStruct");
				AssociateWiFiTaskStruct associateWiFiTaskStruct = (AssociateWiFiTaskStruct) taskToRun;
				WiFiService wiFiService = new WiFiService(appContext);
				
				startTaskDate = new Date(System.currentTimeMillis());
				AssociationResultState associationResultState = wiFiService.Association(associateWiFiTaskStruct.get_essid());
				endTaskDate = new Date(System.currentTimeMillis());
				
				
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				MobileAdvancedInfoStruct mobileAdvancedInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileAdvancedInfoStruct();
				
				WifiBasicInfoStruct wifiBasicInfoStruct = iServiceNetworksInfo.get_wifi_ref().get_WifiBasicInfoStruct();
				WiFiAdvancedInfoStruct wiFiAdvancedInfoStruct = iServiceNetworksInfo.get_wifi_ref().get_WiFiAdvancedInfoStruct();
				
				String task_id = associateWiFiTaskStruct.get_task_id();
				String task_name = associateWiFiTaskStruct.get_task_name();
				String macro_id = associateWiFiTaskStruct.get_macro_id();
				String task_number = associateWiFiTaskStruct.get_task_number();
				String iccid = associateWiFiTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				String status = (associationResultState.getAssociatePTWiFiState() == AssociationResultState.ActionState.OK)?"OK":"NOK";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				String ssid = iServiceNetworksInfo.get_wifi_ref().getSsid();
				String mac = wiFiAdvancedInfoStruct.get_mac_address();
				String frequency = "";
				String channel = wifiBasicInfoStruct.get_channel();
				String mode = "";
				
				String protocol = "";
				String bitrate = wifiBasicInfoStruct.get_link_speed();
				String encryption = "";
				String association_time_in_sec = associationResultState.getAssociateTime()+"";
				String signal_level = wifiBasicInfoStruct.get_rx_level();
				String noise_level = "";
				String ratio_signal_noise = "";
				String address = wiFiAdvancedInfoStruct.get_ip_address();
				String mask = wiFiAdvancedInfoStruct.get_netmask();
				String gateway = wiFiAdvancedInfoStruct.get_gateway();
				String dns = wiFiAdvancedInfoStruct.get_dns1();
				String domain = wiFiAdvancedInfoStruct.get_server_address();
				String lease_in_sec = wiFiAdvancedInfoStruct.get_lease_duration();
				
				AssociateWiFiTaskJsonResult associateWiFiTaskJsonResult = new AssociateWiFiTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id,
						loc_gps, status, dateini, datefim, ssid,
						mac, frequency, channel, mode, protocol,
						bitrate, encryption, association_time_in_sec, signal_level,
						noise_level, ratio_signal_noise, address, mask, gateway,
						dns, domain, lease_in_sec, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
			
				return associateWiFiTaskJsonResult;
				
				
			} else if (taskToRun instanceof DeassociateWiFiTaskStruct) {
			
				MyLogger.trace(logger, method, "DeassociateWiFiTaskStruct");
				DeassociateWiFiTaskStruct deassociateWiFiTaskStruct = (DeassociateWiFiTaskStruct) taskToRun;
				WiFiService wiFiService = new WiFiService(appContext);
				
				startTaskDate = new Date(System.currentTimeMillis());
				boolean result = wiFiService.Disconnect();
				endTaskDate = new Date(System.currentTimeMillis());
				
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				
				String task_id = deassociateWiFiTaskStruct.get_task_id();
				String task_name = deassociateWiFiTaskStruct.get_task_name();
				String macro_id = deassociateWiFiTaskStruct.get_macro_id();
				String task_number = deassociateWiFiTaskStruct.get_task_number();
				String iccid = deassociateWiFiTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				String status = result?"OK":"NOK";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				DeassociateWiFiTaskJsonResult deassociateWiFiTaskJsonResult = new DeassociateWiFiTaskJsonResult(task_id, task_name,macro_id, task_number, iccid, cell_id,
						loc_gps, status, dateini, datefim, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				
				return deassociateWiFiTaskJsonResult;
			
				
			} else if (taskToRun instanceof HTTPDownloadTaskStruct) {
			
				MyLogger.trace(logger, method, "HTTPDownloadTaskStruct");
				HTTPDownloadTaskStruct hTTPDownloadTaskStruct = (HTTPDownloadTaskStruct) taskToRun;
				HttpService httpService = new HttpService(appContext);
				
				startTaskDate = new Date(System.currentTimeMillis());
				HttpServiceResponse httpServiceResponse = httpService.DoHTTPDownload(hTTPDownloadTaskStruct.get_url(), hTTPDownloadTaskStruct.get_proxy(), hTTPDownloadTaskStruct.get_user_agent());
				endTaskDate = new Date(System.currentTimeMillis());
				
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				
				String task_id = hTTPDownloadTaskStruct.get_task_id();
				String task_name = hTTPDownloadTaskStruct.get_task_name();
				String macro_id = hTTPDownloadTaskStruct.get_macro_id();
				String task_number = hTTPDownloadTaskStruct.get_task_number();
				String iccid = hTTPDownloadTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				String status = (httpServiceResponse.getExecState() == HttpServiceResponse.ResponseHttpServiceEnum.OK)?"OK":"NOK";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				String received_data = httpServiceResponse.getReceived_data();
				String received_data_size = httpServiceResponse.getTotalbytes()+"";
				String download_time_in_sec = httpServiceResponse.getTotalTime()+"";
				String access_time_in_sec = httpServiceResponse.getAccessTime()+"";
				String throughput = httpServiceResponse.getDebito()+"";
				
				HTTPDownloadTaskJsonResult hTTPDownloadTaskJsonResult = new HTTPDownloadTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id, loc_gps, status, dateini, datefim, received_data,
						received_data_size, download_time_in_sec, access_time_in_sec, throughput, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				
				return hTTPDownloadTaskJsonResult;
			
				
			} else if (taskToRun instanceof HTTPUploadTaskStruct) {
			
				MyLogger.trace(logger, method, "HTTPUploadTaskStruct");
				HTTPUploadTaskStruct hTTPUploadTaskStruct = (HTTPUploadTaskStruct) taskToRun;
				HttpService httpService = new HttpService(appContext);
				
				startTaskDate = new Date(System.currentTimeMillis());
				HttpServiceResponse httpServiceResponse = httpService.DoHTTPUpload(hTTPUploadTaskStruct.get_url(), hTTPUploadTaskStruct.get_proxy(), hTTPUploadTaskStruct.get_content());
				endTaskDate = new Date(System.currentTimeMillis());
				
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				
				String task_id = hTTPUploadTaskStruct.get_task_id();
				String task_name = hTTPUploadTaskStruct.get_task_name();
				String macro_id = hTTPUploadTaskStruct.get_macro_id();
				String task_number = hTTPUploadTaskStruct.get_task_number();
				String iccid = hTTPUploadTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				String status = (httpServiceResponse.getExecState() == HttpServiceResponse.ResponseHttpServiceEnum.OK)?"OK":"NOK";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				String received_data = httpServiceResponse.getReceived_data();
				String received_data_size = httpServiceResponse.getTotalbytes()+"";
				String download_time_in_sec = httpServiceResponse.getTotalTime()+"";
				String access_time_in_sec = httpServiceResponse.getAccessTime()+"";
				String throughput = httpServiceResponse.getDebito()+"";
				
				
				HTTPUploadTaskJsonResult hTTPUploadTaskJsonResult = new HTTPUploadTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id, loc_gps, status, dateini, datefim, received_data,
						received_data_size, download_time_in_sec, access_time_in_sec, throughput, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				
				return hTTPUploadTaskJsonResult;
			
				
			} else if (taskToRun instanceof PingTaskStruct) {
			
				MyLogger.trace(logger, method, "PingTaskStruct");
				PingTaskStruct pingTaskStruct = (PingTaskStruct) taskToRun;
				Ping pingService = new Ping(appContext);
				
				startTaskDate = new Date(System.currentTimeMillis());
				PingResponse pingResponse = pingService.DoPing(pingTaskStruct.get_packet_size(), pingTaskStruct.get_interval(), pingTaskStruct.get_packet_number(), pingTaskStruct.get_timeout(), pingTaskStruct.get_ip_address());
				endTaskDate = new Date(System.currentTimeMillis());
				
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				
				String task_id = pingTaskStruct.get_task_id();
				String task_name = pingTaskStruct.get_task_name();
				String macro_id = pingTaskStruct.get_macro_id();
				String task_number = pingTaskStruct.get_task_number();
				String iccid = pingTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				String status = (pingResponse.getResponseEnum() == PingResponse.ResponsePingEnum.OK)?"OK":"NOK";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				String min_in_msec = pingResponse.getMin();
				String med_in_msec = pingResponse.getMedia();
				String max_in_msec = pingResponse.getMax();
				String sent_packets = pingResponse.getPacketTransmitted();
				String received_packets = pingResponse.getPacketReceived();
				String lost_packets = pingResponse.getPercentPacketLost();
				
				PingTaskJsonResult pingTaskJsonResult = new PingTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id, loc_gps, status, dateini, datefim, min_in_msec,
						med_in_msec, max_in_msec, sent_packets, received_packets, lost_packets, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				
				//MyLogger.debug(logger, method, "pingTaskJsonResult :"+pingTaskJsonResult.toString());
				
				return pingTaskJsonResult;
				
			} else if (taskToRun instanceof ReassociateWiFiTaskStruct) {
			
				MyLogger.trace(logger, method, "ReassociateWiFiTaskStruct");
				ReassociateWiFiTaskStruct reassociateWiFiTaskStruct = (ReassociateWiFiTaskStruct) taskToRun;
				WiFiService wiFiService = new WiFiService(appContext);
				
				startTaskDate = new Date(System.currentTimeMillis());
				boolean result = wiFiService.Reassociation();
				endTaskDate = new Date(System.currentTimeMillis());
				
				
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				
				String task_id = reassociateWiFiTaskStruct.get_task_id();
				String task_name = reassociateWiFiTaskStruct.get_task_name();
				String macro_id = reassociateWiFiTaskStruct.get_macro_id();
				String task_number = reassociateWiFiTaskStruct.get_task_number();
				String iccid = reassociateWiFiTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				String status = (result)?"OK":"NOK";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				ReassociateWiFITaskJsonResult reassociateWiFITaskJsonResult = new ReassociateWiFITaskJsonResult(task_id, task_name, macro_id, task_number, iccid, cell_id,
						loc_gps, status, dateini, datefim, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				
				return reassociateWiFITaskJsonResult;
			
				
			} else if (taskToRun instanceof ScanWiFiNetworksTaskStruct) {
			
				MyLogger.trace(logger, method, "ScanWiFiNetworksTaskStruct");
				ScanWiFiNetworksTaskStruct scanWiFiNetworksTaskStruct = (ScanWiFiNetworksTaskStruct) taskToRun;
				WiFiService wiFiService = new WiFiService(appContext);
				
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				WiFiAdvancedInfoStruct wiFiAdvancedInfoStruct = iServiceNetworksInfo.get_wifi_ref().get_WiFiAdvancedInfoStruct();
				
				startTaskDate = new Date(System.currentTimeMillis());
				// TODO: no futuro podemos meter um scan syncrono.
				endTaskDate = new Date(System.currentTimeMillis());
				
				String task_id = scanWiFiNetworksTaskStruct.get_task_id();
				String task_name = scanWiFiNetworksTaskStruct.get_task_name();
				String macro_id = scanWiFiNetworksTaskStruct.get_macro_id();
				String task_number = scanWiFiNetworksTaskStruct.get_task_number();
				String iccid = scanWiFiNetworksTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				List<WiFiNetworkJsonInformation> wifi_networks_list = new ArrayList<WiFiNetworkJsonInformation>();
				
				for (ScanResult scanResult :wiFiAdvancedInfoStruct.get_scan_Wifi_list())
					wifi_networks_list.add(new WiFiNetworkJsonInformation(scanResult));
				
				String status = (wifi_networks_list.isEmpty())?"NOK":"OK";
				
				ScanWiFiTaskJsonResult scanWiFiTaskJsonResult = new ScanWiFiTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id,
						loc_gps, status, dateini, datefim, wifi_networks_list, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				
				return scanWiFiTaskJsonResult;
			
				
			} else if (taskToRun instanceof SendMailTaskStruct) {
			
				MyLogger.trace(logger, method, "SendMailTaskStruct");
				SendMailTaskStruct sendMailTaskStruct = (SendMailTaskStruct) taskToRun;
			
			} else if (taskToRun instanceof SendMMSTaskStruct) {
			
				MyLogger.trace(logger, method, "SendMMSTaskStruct");
				SendMMSTaskStruct sendMMSTaskStruct = (SendMMSTaskStruct) taskToRun;
			
			} else if (taskToRun instanceof SendSMSTaskStruct) {
			
				MyLogger.trace(logger, method, "SendSMSTaskStruct");
				SendSMSTaskStruct sendSMSTaskStruct = (SendSMSTaskStruct) taskToRun;
			
			} else if (taskToRun instanceof TurnOFFWiFiTaskStruct) {
			
				MyLogger.trace(logger, method, "TurnOFFWiFiTaskStruct");
				TurnOFFWiFiTaskStruct turnOFFWiFiTaskStruct = (TurnOFFWiFiTaskStruct) taskToRun;
				WiFiService wiFiService = new WiFiService(appContext);
				
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				WiFiAdvancedInfoStruct wiFiAdvancedInfoStruct = iServiceNetworksInfo.get_wifi_ref().get_WiFiAdvancedInfoStruct();
				
				startTaskDate = new Date(System.currentTimeMillis());
				WiFiModuleState wiFiModuleState = wiFiService.trunWiFiOFF();
				endTaskDate = new Date(System.currentTimeMillis());
				
				String task_id = turnOFFWiFiTaskStruct.get_task_id();
				String task_name = turnOFFWiFiTaskStruct.get_task_name();
				String macro_id = turnOFFWiFiTaskStruct.get_macro_id();
				String task_number = turnOFFWiFiTaskStruct.get_task_number();
				String iccid = turnOFFWiFiTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				String status = (wiFiModuleState == WiFiModuleState.OFF || wiFiModuleState == WiFiModuleState.ALREADYOFF)?"OK":"NOK";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				
				TurnOFFWiFiTaskJsonResult turnOFFWiFiTaskJsonResult = new TurnOFFWiFiTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id, loc_gps, status, dateini, datefim, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				
				return turnOFFWiFiTaskJsonResult;
				
			
			} else if (taskToRun instanceof TurnONWiFiTaskStruct) {
			
				MyLogger.trace(logger, method, "TurnONWiFiTaskStruct");
				TurnONWiFiTaskStruct turnONWiFiTaskStruct = (TurnONWiFiTaskStruct) taskToRun;
				WiFiService wiFiService = new WiFiService(appContext);
				
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				WiFiAdvancedInfoStruct wiFiAdvancedInfoStruct = iServiceNetworksInfo.get_wifi_ref().get_WiFiAdvancedInfoStruct();
				
				startTaskDate = new Date(System.currentTimeMillis());
				WiFiModuleState wiFiModuleState = wiFiService.turnWiFiON();
				endTaskDate = new Date(System.currentTimeMillis());
				
				String task_id = turnONWiFiTaskStruct.get_task_id();
				String task_name = turnONWiFiTaskStruct.get_task_name();
				String macro_id = turnONWiFiTaskStruct.get_macro_id();
				String task_number = turnONWiFiTaskStruct.get_task_number();
				String iccid = turnONWiFiTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				String status = (wiFiModuleState == WiFiModuleState.OFF || wiFiModuleState == WiFiModuleState.ALREADYOFF)?"NOK":"OK";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				
				TurnONWiFiTaskJsonResult turnONWiFiTaskJsonResult = new TurnONWiFiTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id, loc_gps, status, dateini, datefim, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				
				return turnONWiFiTaskJsonResult;
			
			}  else if (taskToRun instanceof WiFiAuthLoginTaskStruct) {
			
				MyLogger.trace(logger, method, "WiFiAuthLoginTaskStruct");
				WiFiAuthLoginTaskStruct wiFiAuthLoginTaskStruct = (WiFiAuthLoginTaskStruct) taskToRun;
				MyLogger.trace(logger, method, "WiFiAuthLoginTaskStruct -1");
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				WiFiAdvancedInfoStruct wiFiAdvancedInfoStruct = iServiceNetworksInfo.get_wifi_ref().get_WiFiAdvancedInfoStruct();
				MyLogger.trace(logger, method, "WiFiAuthLoginTaskStruct -2");
				WisprAuth wisprAuth = new WisprAuth();
				
				startTaskDate = new Date(System.currentTimeMillis());
				LoginTaskResult loginTaskResult = wisprAuth.doLogin(wiFiAdvancedInfoStruct.get_ssid(), wiFiAuthLoginTaskStruct.get_username(), wiFiAuthLoginTaskStruct.get_password());
				endTaskDate = new Date(System.currentTimeMillis());
				MyLogger.trace(logger, method, "WiFiAuthLoginTaskStruct -3");
				
				String task_id = wiFiAuthLoginTaskStruct.get_task_id();
				String task_name = wiFiAuthLoginTaskStruct.get_task_name();
				String macro_id = wiFiAuthLoginTaskStruct.get_macro_id();
				String task_number = wiFiAuthLoginTaskStruct.get_task_number();
				String iccid = wiFiAuthLoginTaskStruct.get_iccid();
				MyLogger.trace(logger, method, "WiFiAuthLoginTaskStruct -4");
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				String status = "NOK";
				String ResponseCode = "";
				String time = "";
				MyLogger.trace(logger, method, "WiFiAuthLoginTaskStruct -5");
				if (loginTaskResult != null) {
					
					if (loginTaskResult.get_loginResult() != null) {
						
						MyLogger.debug(logger, method, "loginTaskResult :"+loginTaskResult.toString());
						MyLogger.trace(logger, method, "WiFiAuthLoginTaskStruct -6");
						status = (loginTaskResult.get_loginResult().get_LoginSuccess())?"OK":"NOK";
						ResponseCode = loginTaskResult.get_loginResult().get_ResponseCode();
						time = loginTaskResult.get_time()+"";
						
					}
					
				} else
					MyLogger.debug(logger, method, "loginTaskResult == null");
				MyLogger.trace(logger, method, "WiFiAuthLoginTaskStruct -6");
				WiFiAuthLoginTaskJsonResult wiFiAuthLoginTaskJsonResult = new WiFiAuthLoginTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id, loc_gps, status, dateini, datefim, ResponseCode, time, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				MyLogger.trace(logger, method, "WiFiAuthLoginTaskStruct -7");
				return wiFiAuthLoginTaskJsonResult;
			
			} else if (taskToRun instanceof WiFiAuthLogoutTaskStruct) {
			
				MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct");
				WiFiAuthLogoutTaskStruct wiFiAuthLogoutTaskStruct = (WiFiAuthLogoutTaskStruct) taskToRun;
				
				MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 1");
				
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
			
				MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 2");
				
				MyLogger.debug(logger, method, "wiFiAuthLogoutTaskStruct.get_url() :"+wiFiAuthLogoutTaskStruct.get_url());
				WisprAuth wisprAuth = new WisprAuth(wiFiAuthLogoutTaskStruct.get_url());
				
				MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 3");
				
				startTaskDate = new Date(System.currentTimeMillis());
				LogoffTaskResult logoffTaskResult = wisprAuth.doLogoff();
				endTaskDate = new Date(System.currentTimeMillis());
				
				MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 4");
				
				String task_id = wiFiAuthLogoutTaskStruct.get_task_id();
				String task_name = wiFiAuthLogoutTaskStruct.get_task_name();
				String macro_id = wiFiAuthLogoutTaskStruct.get_macro_id();
				String task_number = wiFiAuthLogoutTaskStruct.get_task_number();
				String iccid = wiFiAuthLogoutTaskStruct.get_iccid();
				
				MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 5");
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				Date dateini = startTaskDate;
				Date datefim = endTaskDate;
				
				MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 6");
				
				String status = "NOK";
				String ResponseCode = "";
				String time = "";
				
				MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 7");
				
				if (logoffTaskResult != null) {
					
					MyLogger.debug(logger, method, "LogoffTaskResult :"+logoffTaskResult.toString());
					
					if (logoffTaskResult.get_logoffResult() != null) {
						
						status = (logoffTaskResult.get_logoffResult().get_LoginSuccess())?"OK":"NOK";
						MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 8");
						ResponseCode = logoffTaskResult.get_logoffResult().get_ResponseCode();
						MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 9");
						time = logoffTaskResult.get_time()+"";
						MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 10");
					}
					
				}
				else
					MyLogger.debug(logger, method, "LogoffTaskResult == null");
				
				WiFiAuthLoginTaskJsonResult wiFiAuthLoginTaskJsonResult = new WiFiAuthLoginTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id, loc_gps, status, dateini, datefim, ResponseCode, time, new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				
				MyLogger.trace(logger, method, "WiFiAuthLogoutTaskStruct - 11");
				
				return wiFiAuthLoginTaskJsonResult;
				
			} else if (taskToRun instanceof TurnONMobileTaskStruct) {
			
				MyLogger.trace(logger, method, "TurnONMobileTaskStruct");
				TurnONMobileTaskStruct turnONMobileTaskStruct = (TurnONMobileTaskStruct) taskToRun;
				
				MobileONOFFAdapterTask mobileONOFFAdapterTask = new MobileONOFFAdapterTask(myRef, iServiceNetworksInfo.get_mobile_ref(), appContext, gps_information);
				mobileONOFFAdapterTask.turnONMobile(turnONMobileTaskStruct);
				
				// O valor é retornado posteriormente pela execução da task
				return null;
				
			} else if (taskToRun instanceof TurnOFFMobileTaskStruct) {
				
				MyLogger.trace(logger, method, "TurnOFFMobileTaskStruct");
				TurnOFFMobileTaskStruct turnOFFMobileTaskStruct = (TurnOFFMobileTaskStruct) taskToRun;
				
				MobileONOFFAdapterTask mobileONOFFAdapterTask = new MobileONOFFAdapterTask(myRef, iServiceNetworksInfo.get_mobile_ref(), appContext, gps_information);
				mobileONOFFAdapterTask.turnOFFMobile(turnOFFMobileTaskStruct);
				
				// O valor é retornado posteriormente pela execução da task
				return null;
			} else if (taskToRun instanceof MakeVoiceCallTaskStruct) {

				MyLogger.trace(logger, method, "MakeVoiceCallTaskStruct");
				MakeVoiceCallTaskStruct makeVoiceCallTaskStruct = (MakeVoiceCallTaskStruct) taskToRun;

				MakeVoiceCall makeVoiceCallTask = new MakeVoiceCall(appContext, makeVoiceCallTaskStruct, gps_information, iServiceNetworksInfo.get_mobile_ref(), myRef);
				makeVoiceCallTask.makeCall();

				// O valor é retornado posteriormente pela execução da task
				return null;
			} else if (taskToRun instanceof AnswerVoiceCallTaskStruct) {

				MyLogger.trace(logger, method, "AnswerVoiceCallTaskStruct");
				AnswerVoiceCallTaskStruct answerVoiceCallTaskStruct = (AnswerVoiceCallTaskStruct) taskToRun;

				AnswerVoiceCall answerVoiceCallTask = new AnswerVoiceCall(appContext, answerVoiceCallTaskStruct, gps_information, iServiceNetworksInfo.get_mobile_ref(), myRef);
				answerVoiceCallTask.answerCall();

				// O valor é retornado posteriormente pela execução da task
				return null;
			} else if (taskToRun instanceof HangUpVoiceCallTaskStruct) {

				MyLogger.trace(logger, method, "HangUpVoiceCallTaskStruct");
				HangUpVoiceCallTaskStruct hangUpVoiceCallTaskStruct = (HangUpVoiceCallTaskStruct) taskToRun;

				HangUpVoiceCall hangUpVoiceCall = new HangUpVoiceCall(appContext, hangUpVoiceCallTaskStruct, gps_information, iServiceNetworksInfo.get_mobile_ref(), myRef);
				hangUpVoiceCall.hangupCall();

				// O valor é retornado posteriormente pela execução da task
				return null;
			} else if (taskToRun instanceof RecordAudioTaskStruct) {

				MyLogger.trace(logger, method, "RecordAudioTaskStruct");
				RecordAudioTaskStruct recordAudioTaskStruct = (RecordAudioTaskStruct) taskToRun;

				RecordAudio recordAudio = new RecordAudio(appContext, recordAudioTaskStruct, gps_information, iServiceNetworksInfo.get_mobile_ref(), myRef);
				recordAudio.record();

				// O valor é retornado posteriormente pela execução da task
				return null;
			}
		
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return new TaskJsonResultError(new Date(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis()), new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
	}
	
	@Override
	protected void onProgressUpdate(Pair<TaskJsonResult,String>... msg) {
		super.onProgressUpdate(msg);
		
		final String method = "onProgressUpdate";
		
		try {
			
			Pair<TaskJsonResult,String> pair = msg[0];
			MyLogger.trace(logger, method, "In - runTaskWorkerUpdateStruct :"+pair.toString());
			
			iCallback.taskExecutionCompleted(pair.first, pair.second);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		MyLogger.trace(logger, method, "Out");
    }

	protected void onPostExecute(TaskJsonResult taskJsonResult){
		final String method = "onPostExecute";
		
		try {
			
			if (taskJsonResult != null)
				if (!checkAndSet()) {
					MyLogger.trace(logger, method, "onPostExecute!!");
					iCallback.taskExecutionCompleted(taskJsonResult, id_test);
					disableReceivers();
				}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
	}
	
	private synchronized void setReturnedOK() {
		returned = true;
	}
	
	private synchronized boolean getReturned() {
		return returned;
	}
	
	private synchronized boolean checkAndSet() {
		
		if (returned == false) {
			returned = true;
			return false;
		}
		
		return returned;
			
	}

	@Override
	public void new_alarm(String alarm_id) {
		final String method = "new_alarm";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			if (alarm_id.equals(id_test) && !checkAndSet()) {
				
				MyLogger.trace(logger, method, "Timeout!!");
				
				TaskJsonResultError taskJsonResultError = null;
				MobileBasicInfoStruct mobileBasicInfoStruct = iServiceNetworksInfo.get_mobile_ref().get_MobileBasicInfoStruct();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				
				if (taskToRun != null)
					taskJsonResultError = new TaskJsonResultError(taskToRun.get_task_id(), taskToRun.get_task_name(), taskToRun.get_macro_id(), 
							taskToRun.get_task_number(), iServiceNetworksInfo.get_mobile_ref().getSimIccid(), iServiceNetworksInfo.get_mobile_ref().getCellID(), gps_information.getFormattedLocation(),
							new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), 
							new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				else
					taskJsonResultError = new TaskJsonResultError(new Date(System.currentTimeMillis()), 
							new Date(System.currentTimeMillis()), new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext));
				
				iCallback.taskExecutionCompleted(taskJsonResultError, id_test);
				disableReceivers();
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	private void disableReceivers(){
		gps_information.stopLocationAutoUpdate();
		gps_information = null;
	}

	@Override
	public void async_result(TaskJsonResult taskJsonResult) {
		final String method = "async_result";
		
		try {
			
			if (!checkAndSet()) {
				MyLogger.trace(logger, method, "async_result!!");
				//iCallback.taskExecutionCompleted(taskJsonResult, id_test);
				publishProgress(new Pair<TaskJsonResult,String>(taskJsonResult,id_test));
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
}
