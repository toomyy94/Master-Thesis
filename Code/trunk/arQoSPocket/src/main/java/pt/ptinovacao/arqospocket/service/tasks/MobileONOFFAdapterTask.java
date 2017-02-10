package pt.ptinovacao.arqospocket.service.tasks;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.jsonresult.TurnOFFMobileTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.TurnONMobileTaskJsonResult;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.service.GPSInformation;
import pt.ptinovacao.arqospocket.service.utils.ConnectionInfo;
import pt.ptinovacao.arqospocket.service.enums.EMobileState;
import pt.ptinovacao.arqospocket.service.interfaces.IRunTaskWorker;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.TurnOFFMobileTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.TurnONMobileTaskStruct;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.service.tasks.interfaces.IMobileONOFFAdapterTask;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileBasicInfoStruct;

public class MobileONOFFAdapterTask implements IMobileONOFFAdapterTask {
	
	private final static Logger logger = LoggerFactory.getLogger(MobileONOFFAdapterTask.class);
	
	private MobileONOFFAdapterTask myRef = null;
	
	private IRunTaskWorker callbackRef = null;
	private EMobileAction eMobileAction = EMobileAction.NA;
	
	private Mobile mobileRef = null;
	
	private Context appContext = null;
	private GPSInformation gps_information = null;
	
	private TurnONMobileTaskStruct turnONMobileTaskStruct = null;
	private TurnOFFMobileTaskStruct turnOFFMobileTaskStruct = null;
	
	private Date dateini = null;

	public MobileONOFFAdapterTask(IRunTaskWorker callbackRef, Mobile mobileRef, Context appContext, GPSInformation gps_information) {
		final String method = "MobileONOFFAdapterTask";
		
		try {
			
			this.myRef = this;
			eMobileAction = EMobileAction.NA;
			
			this.callbackRef = callbackRef;
			this.mobileRef = mobileRef;
			
			this.appContext = appContext;
			this.gps_information = gps_information;
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public void turnONMobile(TurnONMobileTaskStruct turnONMobileTaskStruct) {
		final String method = "turnONMobile";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			this.turnONMobileTaskStruct = turnONMobileTaskStruct;
			eMobileAction = EMobileAction.ON;
			
			dateini = new Date(System.currentTimeMillis());
			
			boolean resultAction = mobileRef.connectDataPlan(myRef);
			MyLogger.trace(logger, method, "resultAction :"+resultAction);
			
			if (!resultAction) {
				Date datefim = new Date(System.currentTimeMillis());
				mobileRef.remove_data_plan_change_listener(myRef);
				
				MobileBasicInfoStruct mobileBasicInfoStruct = mobileRef.get_MobileBasicInfoStruct();
				
				String task_id = turnONMobileTaskStruct.get_task_id();
				String task_name = turnONMobileTaskStruct.get_task_name();
				String macro_id = turnONMobileTaskStruct.get_macro_id();
				String task_number = turnONMobileTaskStruct.get_task_number();
				String iccid = turnONMobileTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				String status = "NOK";
				
				MyLogger.trace(logger, method, "async_result");
				callbackRef.async_result(new TurnONMobileTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id, loc_gps, status, dateini, datefim,
						new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext)));
			}
				
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public void turnOFFMobile(TurnOFFMobileTaskStruct turnOFFMobileTaskStruct) {
		final String method = "turnOFFMobile";
		
		try {
			MyLogger.trace(logger, method, "In");
			
			this.turnOFFMobileTaskStruct = turnOFFMobileTaskStruct;
			eMobileAction = EMobileAction.OFF;
			
			dateini = new Date(System.currentTimeMillis());
			
			boolean resultAction = mobileRef.disconnectDataPlan(myRef);
			MyLogger.trace(logger, method, "resultAction :"+resultAction);
			
			if (!resultAction) {
				
				Date datefim = new Date(System.currentTimeMillis());
				
				mobileRef.remove_data_plan_change_listener(myRef);
				
				MobileBasicInfoStruct mobileBasicInfoStruct = mobileRef.get_MobileBasicInfoStruct();
				
				String task_id = turnOFFMobileTaskStruct.get_task_id();
				String task_name = turnOFFMobileTaskStruct.get_task_name();
				String macro_id = turnOFFMobileTaskStruct.get_macro_id();
				String task_number = turnOFFMobileTaskStruct.get_task_number();
				String iccid = turnOFFMobileTaskStruct.get_iccid();
				
				String cell_id = mobileBasicInfoStruct.get_id_cell();
				String loc_gps = "";
				String status = "NOK";
				
				MyLogger.trace(logger, method, "async_result");
				callbackRef.async_result(new TurnOFFMobileTaskJsonResult(task_id, task_name, macro_id, task_number, iccid,
						cell_id, loc_gps, status, dateini, datefim, new MyLocation(gps_information),
						EConnectionTechnology.NA));
			}
				
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	private enum EMobileAction {
		ON, OFF, NA
	}

	@Override
	public void DataPlanChange(EMobileState eMobileState) {
		final String method = "DataPlanChange";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			Date datefim = new Date(System.currentTimeMillis());
			
			if (eMobileAction == EMobileAction.OFF) {
				
				MyLogger.trace(logger, method, "eMobileAction == EMobileAction.OFF");
				
				if (eMobileState == EMobileState.DISCONNECTED) {
					
					MyLogger.trace(logger, method, "eMobileState == EMobileState.DISCONNECTED");
				
					MobileBasicInfoStruct mobileBasicInfoStruct = mobileRef.get_MobileBasicInfoStruct();
				
					String task_id = turnOFFMobileTaskStruct.get_task_id();
					String task_name = turnOFFMobileTaskStruct.get_task_name();
					String macro_id = turnOFFMobileTaskStruct.get_macro_id();
					String task_number = turnOFFMobileTaskStruct.get_task_number();
					String iccid = turnOFFMobileTaskStruct.get_iccid();
				
					String cell_id = mobileBasicInfoStruct.get_id_cell();
					String loc_gps = "";
					String status = "OK";
				
					MyLogger.trace(logger, method, "async_result - 1");
					callbackRef.async_result(new TurnOFFMobileTaskJsonResult(task_id, task_name,
						macro_id, task_number, iccid, cell_id, loc_gps, status, dateini, datefim,
						new MyLocation(gps_information), ConnectionInfo.get_active_connection(appContext)));
				}
			}
			
			if (eMobileAction == EMobileAction.ON) {
				
				MyLogger.trace(logger, method, "eMobileAction == EMobileAction.ON");
				
				if (eMobileState == EMobileState.CONNECTED) {
					
					MyLogger.trace(logger, method, "eMobileState == EMobileState.CONNECTED");

					MobileBasicInfoStruct mobileBasicInfoStruct = mobileRef.get_MobileBasicInfoStruct();

					String task_id = turnONMobileTaskStruct.get_task_id();
					String task_name = turnONMobileTaskStruct.get_task_name();
					String macro_id = turnONMobileTaskStruct.get_macro_id();
					String task_number = turnONMobileTaskStruct.get_task_number();
					String iccid = turnONMobileTaskStruct.get_iccid();

					String cell_id = mobileBasicInfoStruct.get_id_cell();
					String loc_gps = "";
					String status = "OK";

					MyLogger.trace(logger, method, "async_result - 2");
					callbackRef.async_result(new TurnONMobileTaskJsonResult(task_id, task_name, macro_id, task_number, iccid,
							cell_id, loc_gps, status, dateini, datefim,new MyLocation(gps_information), 
							ConnectionInfo.get_active_connection(appContext)));
				}
			}
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
}
