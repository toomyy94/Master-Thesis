package PTInov.IEX.ArQoSPocket.RegistryAndFactory;

import java.util.TreeMap;

import android.util.Log;
import PTInov.IEX.ArQoSPocket.ServicesInstance.NewCallService;
import PTInov.IEX.ArQoSPocket.ServicesInterfaces.EngineInterface;
import PTInov.IEX.ArQoSPocket.ServicesInterfaces.ServiceInterface;

public class ServiceRegistry {
	
	private final static String tag = "ServiceRegistry";
	
	private static TreeMap<String,ServiceInterface> createdObjects = null;
	
	public static ServiceInterface getServiceInstance(String TaskID, EngineInterface ie) {
		
		ServiceInterface si = null;
		
		try {
			
		
			if (createdObjects == null) {
			
				Log.v(tag, "getServiceInstance :: Create a Reference list and add reference");
			
				// cria o objecto a guardar a lista das referencias para os serviços
				createdObjects = new TreeMap<String,ServiceInterface>();
			
				// cria e adiciona o serviço a lista
				si = getPrivateServiceInstance(TaskID,ie);
				createdObjects.put(TaskID, si);
			} else {
			
				//Verifica se já existe uma instancia do serviço
				if (createdObjects.containsKey(TaskID)) {
				
					Log.v(tag, "getServiceInstance :: Get Object from the list");
				
					si = createdObjects.get(TaskID);
				
				} else {
				
					Log.v(tag, "getServiceInstance :: Create Object and to the list");
				
					// cria e adiciona o serviço a lista
					si = getPrivateServiceInstance(TaskID, ie);
					createdObjects.put(TaskID, si);
				}
			
			}
		
		} catch(Exception ex) {
			Log.v(tag, "getServiceInstance :: ERROR :"+ex.toString());
		}
		
		return si;
	}
	
	public static boolean deleteService(String TaskID) {
		
		try {
		
			if (createdObjects.containsKey(TaskID)) {
			
				ServiceInterface si = createdObjects.get(TaskID);
				si = null;
				createdObjects.remove(TaskID);
			
				return true;
			}
			
		} catch(Exception ex) {
			Log.v(tag, "deleteService :: ERROR :"+ex.toString());
		}
		
		return false;
	}
	
	
	private static ServiceInterface getPrivateServiceInstance(String TaskID, EngineInterface ie) {
		ServiceInterface si = null;
		
		Log.v(tag, "getPrivateServiceInstance :: Create TaskID:"+TaskID);
		
		try {
			
			int taskid = Integer.parseInt(TaskID);
		
			switch(taskid) {
				case 1: // New Call
					Log.v(tag, "getPrivateServiceInstance :: New Call Service");
					si = new NewCallService(ie);
					// secalhar vai ser necessario correr aki a thread.....................................
					break;
			}
		
		} catch(Exception ex) {
			Log.v(tag, "getPrivateServiceInstance :: ERROR :"+ex.toString());
		}
		
		return si;
	}
}
