package PT.PTInov.ArQoSPocketEDP.Utils;

import java.util.Comparator;

import PT.PTInov.ArQoSPocketEDP.DataStructs.StoreInformation;
import PT.PTInov.ArQoSPocketEDP.DataStructs.WorkFlowBase;

public class MyReverseSort implements Comparator<WorkFlowBase>{


	    public int compare(WorkFlowBase o1, WorkFlowBase o2) {
	    	
	    	if (o1.getCreateTestDate().after(o2.getCreateTestDate())) {
	    		return -1;
	    	} else if (o1.getCreateTestDate().equals(o2.getCreateTestDate())) {
	    		return 0;
	    	} else {
	    		return 1;
	    	}
	    }
}
