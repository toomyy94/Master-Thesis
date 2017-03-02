package PT.PTInov.ArQoSPocketEDP.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import PT.PTInov.ArQoSPocketEDP.DataStructs.StoreInformation;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowBase;


public class CircularStore {
	
	private static String tag = "CircularStore";
	
	private List<WorkFlowBase> list;
	private int maxlistSize;

	public CircularStore(int size) {
		maxlistSize = size;
		list = new ArrayList<WorkFlowBase>();
	}
	
	public synchronized void addElem(WorkFlowBase si) {
		
		Logger.v(tag, "addElem", LogType.Trace, "In");
		
		if (list.size()==maxlistSize) {
			// remove um elemento antes de adicionar
			removeLast();
		}
		
		list.add(si);
		
		//Collections.sort(taskList);
		Collections.sort(list, new MyReverseSort());
		
		Logger.v(tag, "addElem", LogType.Trace, "History list :"+list.toString());
	}
	
	public List<WorkFlowBase> getAllElems() {
		return list;
	}
	
	public WorkFlowBase getElemAt(int index) {
		try {
			
			return list.get(index);
			
		} catch(Exception ex) {
			Logger.v(tag, "getElemAt", LogType.Trace, ex.toString());
		}
		return null;
	}
	
	private void removeLast() {
		try {
			
			list.remove(list.size()-1);
			
		} catch(Exception ex) {
			Logger.v(tag, "removeLast", LogType.Trace, ex.toString());
		}
	}
	
	public void clearAll() {
		list = new ArrayList<WorkFlowBase>();
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("......CircularStore :: list size :"+list.size());
		
		for (WorkFlowBase sf :list) {
			sb.append("\n----\n");
			sb.append(sf.toString());
			sb.append("\n----\n");
		}
		
		return sb.toString();
	}
	
	public List<RowDataTwoLines> getAllResultByIndex(int testIndex) {
		List<RowDataTwoLines> resultList = new ArrayList<RowDataTwoLines>();
		
		if (testIndex<0 || testIndex>=list.size()) {
			
			resultList.add(new RowDataTwoLines("N�o existem Resultados","",false));
			return resultList;
		}
		
		int i;
		for (i=0;i<list.size();i++) {
			
			// TODO: tenho de fazer isto para dar a lista.........................................................
			//resultList.add(new RowDataTwoLines(list.get(i).getuserLocationInfo(),list.get(i).getRegistryDateFormated(),true));
		}
		
		return resultList;
	}
}
