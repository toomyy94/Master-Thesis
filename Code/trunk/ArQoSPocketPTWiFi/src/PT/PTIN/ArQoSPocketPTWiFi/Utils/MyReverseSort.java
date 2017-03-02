package PT.PTIN.ArQoSPocketPTWiFi.Utils;

import java.util.Comparator;

import PT.PTIN.ArQoSPocketPTWiFi.Store.StoreInformation;

public class MyReverseSort implements Comparator<StoreInformation>{


	    public int compare(StoreInformation o1, StoreInformation o2) {
	    	
	    	if (o1.getDate().after(o2.getDate())) {
	    		return -1;
	    	} else if (o1.getDate().equals(o2.getDate())) {
	    		return 0;
	    	} else {
	    		return 1;
	    	}
	    }
}
