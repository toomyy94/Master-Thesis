package PTInov.IEX.ArQoSPocket.UserInterface;

import java.util.ArrayList;
import java.util.List;

import PTInov.IEX.ArQoSPocket.ApplicationSettings.SettingsStore;
import PTInov.IEX.ArQoSPocket.UserInterface.Style.RowDataTwoLinesHistory;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigInterface extends ListActivity{
	
	private final String tag = "ConfigInterface";
	
	private LayoutInflater mInflater = null;
	private ListAdapter adapter = null;
	private List<RowDataTwoLinesHistory> data = null;
	
	private LayoutInflater factory = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	  
		Log.v(tag, "Estou na ShowTestResultLog!!!");
		
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		factory = LayoutInflater.from(this);
		
		//String[] history = MainInterface.getHistoryCircularStore().getAllTask(HistoryInterface.indexHistory);
		
		data = new ArrayList<RowDataTwoLinesHistory>();
		SettingsStore ss = new SettingsStore(EngineService.getMyRef());
		
		data.add(new RowDataTwoLinesHistory("Som de alarme",SettingsStore.getAlarmDir(),true));
		data.add(new RowDataTwoLinesHistory("Activação do alarme",SettingsStore.alarmActive()?"Activo":"Desactivo",true));
		data.add(new RowDataTwoLinesHistory("Dimensão histórico",EngineService.historySize+"",true));
		data.add(new RowDataTwoLinesHistory("Keepalive",SettingsStore.getkeepaliveState()?"Activo":"Desactivo",true));
		data.add(new RowDataTwoLinesHistory("Recarregar Teste","",false));
		
		adapter = new ListAdapter(this,R.layout.rowlistwithtwolines, data);		
		setListAdapter(adapter);
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Log.v(tag, "You selected: " + position+" item");
		
		if (position==0) {
			
			//Alterar ficheiro de alarm
		
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
			final View textEntryView = factory.inflate(R.layout.inputtextsettings, null);
			alert.setTitle("Introduza a localização :").setView(textEntryView);

			final EditText input1 = (EditText) textEntryView.findViewById((int) R.id.entry);
			input1.setText(SettingsStore.getAlarmDir());
		
			alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								//Toast.makeText(myContext, "Vou correr agora o teste!", Toast.LENGTH_LONG).show();
								
								SettingsStore.setAlarmDir(input1.getText().toString());
								data.set(0, new RowDataTwoLinesHistory("Som de alarme",SettingsStore.getAlarmDir(),true));
								adapter.notifyDataSetChanged();
							}
			});
		
			alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						
					}
				});

			
			alert.show();
		} else if (position==1) {
			
			// alterar opção de alarm
			
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			
			final View checkEntryView = factory.inflate(R.layout.checkbutton, null);
			alert.setTitle("Activação do alarme").setView(checkEntryView);

			final CheckBox input1 = (CheckBox) checkEntryView.findViewById((int) R.id.checkbox);
			
			if (SettingsStore.alarmActive()) {
				input1.setChecked(true);
			}else {
				input1.setChecked(false);
			}
		
			alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								//Toast.makeText(myContext, "Vou correr agora o teste!", Toast.LENGTH_LONG).show();
								
								SettingsStore.setActive(input1.isChecked());
								data.set(1, new RowDataTwoLinesHistory("Activação do alarme",SettingsStore.alarmActive()?"Activo":"Desactivo",true));
								adapter.notifyDataSetChanged();
							}
			});
		
			alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						
					}
				});

			
			alert.show();
			
		} else if (position==2) {
			
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			
			final View textEntryView = factory.inflate(R.layout.inputtextsettings, null);
			alert.setTitle("Introduza o número de elementos do histórico :").setView(textEntryView);

			final EditText input1 = (EditText) textEntryView.findViewById((int) R.id.entry);
			input1.setText(EngineService.historySize+"");
		
			alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								try {
									EngineService.historySize = Integer.parseInt(input1.getText().toString());
									
									data.set(2, new RowDataTwoLinesHistory("Dimensão histórico",EngineService.historySize+"",true));
									adapter.notifyDataSetChanged();
									
									SettingsStore.setHistorySize(EngineService.historySize);
									
								} catch(Exception ex) {
									Toast.makeText(getBaseContext(), "Número de elementos inválido", Toast.LENGTH_LONG).show();
								}
								
							}
			});
		
			alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						
					}
				});
			
			alert.show();
			
		} else if (position==3) {
			
final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			
			final View checkEntryView = factory.inflate(R.layout.checkbutton, null);
			alert.setTitle("Keepalive").setView(checkEntryView);

			final CheckBox input1 = (CheckBox) checkEntryView.findViewById((int) R.id.checkbox);
			
			if (SettingsStore.getkeepaliveState()) {
				input1.setChecked(true);
			}else {
				input1.setChecked(false);
			}
			
			input1.setText("Keepalive Activo");
		
			alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								//Toast.makeText(myContext, "Vou correr agora o teste!", Toast.LENGTH_LONG).show();
								
								SettingsStore.setkeepaliveState(input1.isChecked());
								data.set(3, new RowDataTwoLinesHistory("Keepalive",SettingsStore.getkeepaliveState()?"Activo":"Desactivo",true));
								adapter.notifyDataSetChanged();
							}
			});
		
			alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						
					}
				});

			
			alert.show();
			
		} else {
			
			//Alterar ficheiro de alarm
			
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
			final View textEntryView = factory.inflate(R.layout.inputtextsettings, null);
			alert.setTitle("Introduza a localização :").setView(textEntryView);

			final EditText input1 = (EditText) textEntryView.findViewById((int) R.id.entry);
			input1.setText("Tab_00139501C92D#T308739_20110905133642_20110905134142_R2");
		
			alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								//Toast.makeText(myContext, "Vou recarregar agora o teste!", Toast.LENGTH_LONG).show();
								
								((EngineService) EngineService.getMyRef()).setTaks(input1.getText().toString());
								
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
	
	private class ListAdapter extends ArrayAdapter<RowDataTwoLinesHistory> {

		public ListAdapter(Context context,
				int textViewResourceId, List<RowDataTwoLinesHistory> objects) {
			super(context, textViewResourceId, objects);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			// widgets displayed by each item in your list
			TextView item = null;
			TextView description = null;

			// data from your adapter
			RowDataTwoLinesHistory rowData = getItem(position);

			// we want to reuse already constructed row views...
			if (null == convertView) {
				
				convertView = mInflater.inflate(R.layout.rowlistwithtwolines, null);			
				holder = new ViewHolder(convertView);				
				convertView.setTag(holder);
			}
			
			holder = (ViewHolder) convertView.getTag();
			item = holder.getItem();
			item.setText(rowData.mItem());
			if (!rowData.mSucess()) {
				item.setTextColor(Color.RED);
			} else {
				item.setTextColor(Color.WHITE);
			}

			description = holder.getDescription();
			description.setText(rowData.mDescription());
			
			return convertView;
		}
	}

	/**
	 * Wrapper for row data.
	 * 
	 */
	private class ViewHolder {
		private View mRow;
		private TextView description = null;
		private TextView item = null;

		public ViewHolder(View row) {
			mRow = row;
		}

		public TextView getDescription() {
			
			if (null == description) {
				description = (TextView) mRow.findViewById(R.id.logtext2);
			}
			
			return description;
		}

		public TextView getItem() {
			
			if (null == item) {
				item = (TextView) mRow.findViewById(R.id.logtext1);
			}

			return item;
		}
	}

}
