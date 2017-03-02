package PT.PTInov.ArQoSPocket.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;


public class CircularStore {
	
	private static String tag = "CircularStore";
	private static String fileName = "CircularStore.bin";
	
	private List<StoreAllTestInformation> list;
	private int maxlistSize;
	
	private Context appContext = null;

	public CircularStore(int size, Context c) {
		
		appContext = c;
		
		maxlistSize = size;
		list = new ArrayList<StoreAllTestInformation>();
	}
	
	public void loadAsyncFromStore() {
		final String method = "loadAsyncFromStore";
		
		Runnable runnable = new Runnable() {

			public void run() {

				try {
					File file = new File(appContext.getFilesDir(), fileName);

					FileInputStream fis = new FileInputStream(file);
					ObjectInputStream is = new ObjectInputStream(fis);
					List<StoreAllTestInformation> localList = (List<StoreAllTestInformation>) is.readObject();
					is.close();
					
					for(StoreAllTestInformation sati :localList) {
						addElemInternal(sati);
					}

				} catch (Exception ex) {
					Logger.v(tag, method, LogType.Error, ex.toString());
				}

			}

		};

		new Thread(runnable).start();
	}
	
	public void saveAsyncToStore() {
		final String method = "saveAsyncToStore";
		
		Runnable runnable = new Runnable() {

			public void run() {

				try {

					File file = new File(appContext.getFilesDir(), fileName);

					FileOutputStream fos = new FileOutputStream(file);
					ObjectOutputStream os = new ObjectOutputStream(fos);
					os.writeObject(list);
					os.close();

				} catch (Exception ex) {
					Logger.v(tag, method, LogType.Error, ex.toString());
				}
			}

		};

		new Thread(runnable).start();
	}
	
	private synchronized void addElemInternal(StoreAllTestInformation si) {
		
		Logger.v(tag, "addElemInternal", LogType.Trace, "In");
		
		if (list.size()==maxlistSize) {
			// remove um elemento antes de adicionar
			removeLast();
		}
		
		list.add(si);
		
		//Collections.sort(taskList);
		Collections.sort(list, new MyReserseSortStoreAllTestInformation());
		
		Logger.v(tag, "addElemInternal", LogType.Trace, "History list :"+list.toString());
	}
	
	public synchronized void addElem(StoreAllTestInformation si) {
		
		Logger.v(tag, "addElem", LogType.Trace, "In");
		
		if (list.size()==maxlistSize) {
			// remove um elemento antes de adicionar
			removeLast();
		}
		
		list.add(si);
		
		//Collections.sort(taskList);
		Collections.sort(list, new MyReserseSortStoreAllTestInformation());
		
		saveAsyncToStore();
		
		Logger.v(tag, "addElem", LogType.Trace, "History list :"+list.toString());
	}
	
	public List<StoreAllTestInformation> getAllElems() {
		return list;
	}
	
	public StoreAllTestInformation getElemAt(int index) {
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
		list = new ArrayList<StoreAllTestInformation>();
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("......CircularStore :: list size :"+list.size());
		
		for (StoreAllTestInformation sf :list) {
			sb.append("\n----\n");
			sb.append(sf.toString());
			sb.append("\n----\n");
		}
		
		return sb.toString();
	}
	
	public List<RowDataTwoLines> getAllResultByIndex(int testIndex) {
		List<RowDataTwoLines> resultList = new ArrayList<RowDataTwoLines>();
		
		if (testIndex<0 || testIndex>=list.size()) {
			
			resultList.add(new RowDataTwoLines("Não existem Resultados","",false));
			return resultList;
		}
		
		int i;
		for (i=0;i<list.size();i++) {
			
			resultList.add(new RowDataTwoLines(list.get(i).getStoreInformation().getuserLocationInfo(),list.get(i).getStoreInformation().getRegistryDateFormated(),true));
		}
		
		return resultList;
	}
}
