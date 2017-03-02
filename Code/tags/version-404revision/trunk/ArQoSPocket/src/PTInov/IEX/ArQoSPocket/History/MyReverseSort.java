package PTInov.IEX.ArQoSPocket.History;

import java.util.Comparator;

import PTInov.IEX.ArQoSPocket.TaskStore.TaskStoreStruct;

public class MyReverseSort implements Comparator<TaskStoreStruct>{


	    public int compare(TaskStoreStruct o1, TaskStoreStruct o2) {
	    	
	    	if (o1.getNextTaskDate().after(o2.getNextTaskDate())) {
	    		return -1;
	    	} else if (o1.getNextTaskDate().equals(o2.getNextTaskDate())) {
	    		return 0;
	    	} else {
	    		return 1;
	    	}
	    }
}
