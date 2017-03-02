package PT.PTInov.ArQoSPocket.Utils;

import java.util.Comparator;

public class MyReserseSortStoreAllTestInformation implements Comparator<StoreAllTestInformation>{
    
    public int compare(StoreAllTestInformation o1, StoreAllTestInformation o2) {
    	
    	if (o1.getRegistryDate().after(o2.getRegistryDate())) {
    		return -1;
    	} else if (o1.getRegistryDate().equals(o2.getRegistryDate())) {
    		return 0;
    	} else {
    		return 1;
    	}
    }
}