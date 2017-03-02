package PTInov.IEX.ArQoSPocket.UserInterface;


import PTInov.IEX.ArQoSPocket.TaskStore.TaskStoreStruct;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class UserTestListInterface extends ListActivity{
	
	private final String tag = "UserTestListInterface";
	
	public static int indexTest = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  Log.v(tag, "Estou na UserTestListInterface!!!");
	  
		String[] names = MainInterface.getTestTaskList().getTestList();
		
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		/*this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names));
				*/
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names));
		
		ListView lv = getListView();
		
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
		    public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
		        return onLongListItemClick(v,pos,id);
		    }
		});

	}
	
	protected boolean onLongListItemClick(View v, int pos, long id) {
		
		final ListActivity myContext = this;
		
		indexTest = pos;
		Log.v(tag, "You selected: " + pos+" item");
		
		
		// Criar uma janela para perguntar ao utilizador se pretende fazer start ao teste ou cancel
		
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Deseja correr agora o teste?");
		
		alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								//Toast.makeText(myContext, "Vou correr agora o teste!", Toast.LENGTH_LONG).show();
								
								TaskStoreStruct tss = MainInterface.getTestTaskList().getTaskStoreStructToRun(indexTest);
								tss.RunNowOneTime();
								MainInterface.runTestNow(tss.clone());
								
								myContext.finish();
							}
		});
		
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
					}
				});

		alert.show();
		
		
		//Toast.makeText(this, "E pra correr o teste? ", Toast.LENGTH_LONG).show();
		
	    return true;
	}

	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		indexTest = position;
		Log.v(tag, "You selected: " + position+" item");
		
		startActivity(new Intent(this, UserTaskListInterface.class));
	}
	
	
}
