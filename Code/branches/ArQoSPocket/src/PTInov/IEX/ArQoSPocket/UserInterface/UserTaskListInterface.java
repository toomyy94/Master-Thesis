package PTInov.IEX.ArQoSPocket.UserInterface;

import PTInov.IEX.ArQoSPocket.TaskStore.TaskStoreStruct;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class UserTaskListInterface extends ListActivity{
	
	private final String tag = "UserTestListInterface";
	
	public static int indexNumber = -1;
	public static int indexTask = -1;
	public static ListActivity myRef = null;
	public static ArrayAdapter aa = null;
	
	private LayoutInflater factory = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  Log.v(tag, "Estou na UserTaskListInterface!!!");
	  
	  factory = LayoutInflater.from(this);
	  
		String[] names = MainInterface.getTestTaskList().getTaskList(UserTestListInterface.indexTest);
		
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, names);
		this.setListAdapter(aa);
		
		myRef = this;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		indexNumber = position;
		Log.v(tag, "You selected: " + position+" item");
		
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		final View textEntryView = factory.inflate(R.layout.inputtextsettings, null);
		alert.setTitle("Introduza o novo número :").setView(textEntryView);

		final EditText input1 = (EditText) textEntryView
				.findViewById((int) R.id.entry);
		
		alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								//Toast.makeText(myContext, "Vou correr agora o teste!", Toast.LENGTH_LONG).show();
								
								
								Log.v("tesssssssss", input1.getText().toString());
								Log.v("tesssssssss", "result: "+MainInterface.getTestTaskList().SetNewNumber(UserTestListInterface.indexTest, indexNumber, input1.getText().toString()));
								startActivity(new Intent(myRef, UserTaskListInterface.class));
								myRef.finish();
							}
		});
		
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
					}
				});

		alert.show();
	}
}
