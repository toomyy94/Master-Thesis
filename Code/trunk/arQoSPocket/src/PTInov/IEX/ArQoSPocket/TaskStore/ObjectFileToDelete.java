package PTInov.IEX.ArQoSPocket.TaskStore;

import java.io.File;

import android.util.Log;

public class ObjectFileToDelete implements Comparable<ObjectFileToDelete> {
	
	private final String tag = "ObjectFileToDelete";
	
	private File fpointer;
	private long lastModify;

	public ObjectFileToDelete(File pf) {
		fpointer = pf;
		
		try {
			lastModify = pf.lastModified();
		} catch(Exception ex) {
			Log.v(tag, "ObjectFileToDelete::ERROR :"+ex.toString());
			lastModify = -1;
		}
	}
	
	public long getLastModify() {
		return lastModify;
	}
	
	public boolean deleteFile() {
		try {
			
			return fpointer.delete();
			
		} catch(Exception ex) {
			Log.v(tag, "deleteFile::ERROR: "+ex.toString());
			
			return false;
		}
	}

	public int compareTo(ObjectFileToDelete another) {
		// TODO Auto-generated method stub
		if (another.getLastModify()==lastModify) {
			return 0;
		} else if (another.getLastModify()>lastModify) {
			return 1;
		} else {
			return -1;
		}
	}
	
	
}
