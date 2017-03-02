package PT.PTInov.ArQoSPocket.Utils;

import java.util.Comparator;

public class MyReverseSort implements Comparator<StoreInformation>{


	    public int compare(StoreInformation o1, StoreInformation o2) {
	    	
	    	if (o1.getRegistryDate().after(o2.getRegistryDate())) {
	    		return -1;
	    	} else if (o1.getRegistryDate().equals(o2.getRegistryDate())) {
	    		return 0;
	    	} else {
	    		return 1;
	    	}
	    }
}
