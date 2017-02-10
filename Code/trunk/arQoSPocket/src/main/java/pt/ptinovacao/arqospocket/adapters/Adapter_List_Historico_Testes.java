package pt.ptinovacao.arqospocket.adapters;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.service.enums.EConnectionTechnology;
import pt.ptinovacao.arqospocket.service.enums.ETaskTechnology;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.HistoricoTestesItem;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.activities.ActivityTestesHistorico;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_List_Historico_Testes extends
		ArrayAdapter<HistoricoTestesItem> {

	private static final int rowLayout = R.layout.swipable_row_testes_historico;
	private static final String TAG = "testsHistory";
	private ActivityTestesHistorico activity;

	public Adapter_List_Historico_Testes(ActivityTestesHistorico activity,
			ArrayList<HistoricoTestesItem> historicoTestes) {
		super(activity, rowLayout, historicoTestes);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HistoricoTestesItem item = (HistoricoTestesItem) getItem(position);
		boolean success = false;

		Log.i(TAG, "testsHistory item");
		
		LayoutInflater mInflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(rowLayout, parent, false);

		ImageView imgResult = (ImageView) convertView
				.findViewById(R.id.img_historico_teste_result);
		try {
			if (item.getTest_state().equals(ETestTaskState.OK)) {
				success = true;
				imgResult.setImageResource(R.drawable.icon_sucesso_big);
			} else {
				success = false;
				imgResult.setImageResource(R.drawable.icon_erro_big);
			}
		} catch(Exception e) {
			success = false;
			imgResult.setImageResource(R.drawable.icon_erro_big);
		}

		ImageView imgTecnologia = (ImageView) convertView
				.findViewById(R.id.img_historico_teste_tecnologia);
//		ETaskTechnology tecnologia = getTechnology(item.getTask_list());

		/**
		 * TODO executar isto quando receber lista de Tasks
		 */
		// switch(tecnologia) {
		// case MOBILE:
		// if(success)
		// imgTecnologia.setImageResource(R.drawable.icon_rede_movel_sucesso_big);
		// else
		// imgTecnologia.setImageResource(R.drawable.icon_rede_movel_erro_big);
		// break;
		// case WIFI:
		// if(success)
		// imgTecnologia.setImageResource(R.drawable.icon_wi_fi_sucesso_big);
		// else
		// imgTecnologia.setImageResource(R.drawable.icon_wi_fi_erro_big);
		// break;
		// case MOBILE_WIFI:
		// if(success)
		// imgTecnologia.setImageResource(R.drawable.icon_misto_sucesso_big);
		// else
		// imgTecnologia.setImageResource(R.drawable.icon_misto_erro_big);
		// break;
		// }
		
//		if (success)
//			imgTecnologia.setImageResource(R.drawable.icon_misto_sucesso_big);
//		else
//			imgTecnologia.setImageResource(R.drawable.icon_misto_erro_big);
		
		
		/** TODO:
		 * replace following statement when technology info is available in tasks... */
//		int resource = activity.getTestBigRowIconResource(getTestTypeFromTechnology(item.getTechnologyType()), item.getTest_state());
		
		
		if(item.getTechnologyType() == null 
				|| item.getTechnologyType() == EConnectionTechnology.NA 
				|| item.getTest_state() == null) {
//			imgTecnologia.setImageResource(R.drawable.icon_misto_sucesso_big);
			imgTecnologia.setVisibility(View.INVISIBLE);
			Log.i(TAG, "testsHistory technology null");
		} else {
			int resource = activity.getTestBigRowIconResource(item.getTechnologyType(), item.getTest_state());
			imgTecnologia.setImageResource(resource);
			Log.i(TAG, "testsHistory technology: " + item.getTechnologyType().toString());
		}

		TextView type = (TextView) convertView
				.findViewById(R.id.tv_historico_teste_type);
		type.setText(item.getTest_name());

		 TextView date = (TextView) convertView.findViewById(R.id.tv_historico_teste_date);
		 date.setText(item.getDate_end_execution_string());

		ImageView imgPendente = (ImageView) convertView
				.findViewById(R.id.img_historico_teste_pendente);
		if (item.isTest_already_sent())
			imgPendente.setVisibility(View.GONE);
		else
			imgPendente.setVisibility(View.VISIBLE);

		return convertView;
	}

//	private ETaskTechnology getTechnology(List<ITaskResult> tasks) {
//		ETaskTechnology technology = null;
//
//		if (tasks != null && tasks.size() > 0) {
//			for (ITaskResult task : tasks) {
//				if (technology == null)
//					technology = task.get_task_technology();
//				else if (technology != task.get_task_technology()) {
//					technology = ETaskTechnology.MOBILE_WIFI;
//				}
//			}
//		}
//
//		return technology;
//	}
	
	private String getTestTypeFromTechnology(ETaskTechnology technology) {
		if(technology != null) {
			try {
				/* Index zero (0) of 'testTypes' is not valid in this context (used in the filtering spinner, only) */
				return activity.testTypes.get(technology.ordinal() + 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		
		/* Not supposed to reach this statement...
		 * Return the last type (MOBILE_WIFI) */
		return activity.testTypes.get(activity.testTypes.size() - 1);
	}
}
