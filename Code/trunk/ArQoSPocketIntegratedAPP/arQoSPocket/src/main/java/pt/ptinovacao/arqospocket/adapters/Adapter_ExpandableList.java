package pt.ptinovacao.arqospocket.adapters;

import pt.ptinovacao.arqospocket.DetailItem;
import pt.ptinovacao.arqospocket.GroupTaskDetails;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.core.RowItemTestesDetalhes;
import pt.ptinovacao.arqospocket.interfaces.IExpandableListListener;
import android.app.Activity;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Adapter_ExpandableList extends BaseExpandableListAdapter {  

	
	private final SparseArray<GroupTaskDetails> groups;
	public LayoutInflater inflater;
	public Activity activity;
	public IExpandableListListener callback;
	RowItemTestesDetalhes rowItemTestesDetalhes;
	
	public Adapter_ExpandableList(Activity act,
			SparseArray<GroupTaskDetails> groups,
			IExpandableListListener callback) {
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
		this.callback = callback;
	}
	

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final DetailItem detailItem = (DetailItem) getChild(groupPosition,
				childPosition);

		TextView taskTitle = null;
		TextView taskDetail = null;
		ImageView taskIcon = null;
		RelativeLayout layout =null;
		
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.fragment_testes_historico_task_details, null);
		}
		
		layout =(RelativeLayout)convertView
		.findViewById(R.id.layout);
		
		taskTitle = (TextView) convertView
				.findViewById(R.id.tv_historico_task_title);
		
		taskTitle.setText(detailItem.getFieldName());

		taskDetail = (TextView) convertView
				.findViewById(R.id.tv_historico_task_detail);
		taskDetail.setText(detailItem.getFieldValue());

		if (detailItem.isLastRow()) {
			
			taskIcon = (ImageView) convertView
					.findViewById(R.id.iv_historico_task_icon);
			taskIcon.setVisibility(View.VISIBLE);

			layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callback.expandOrCollapseList(groupPosition);
				}
			});
			//fechar no botão
			taskIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callback.expandOrCollapseList(groupPosition);
				}
			});
			
			
		} else {
			taskIcon = (ImageView) convertView
					.findViewById(R.id.iv_historico_task_icon);
			taskIcon.setVisibility(View.GONE);
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// super.onGroupExpanded(groupPosition);

	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_testes_detalhes,
					null);
		}

		GroupTaskDetails grp = groups.get(groupPosition);
		rowItemTestesDetalhes = grp.itemInfo;

		ImageView imgPerc = (ImageView) convertView
				.findViewById(R.id.image_Percent_deta);
		TextView txtPerc = (TextView) convertView
				.findViewById(R.id.text_percent_deta);
		ImageView imgState = (ImageView) convertView
				.findViewById(R.id.imageState_deta);
		TextView txtName = (TextView) convertView
				.findViewById(R.id.nome_teste_deta);

		ImageView imgbutton = (ImageView) convertView
				.findViewById(R.id.imagebutton_deta);
		LinearLayout linha = (LinearLayout) convertView
				.findViewById(R.id.linearLayoutLinhaTipdetalhes);
		// Clique para expandir no linha
		linha.setOnClickListener(new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			callback.expandOrCollapseList(groupPosition);
		}
	});
		
		// Clique para expandir no botao
		imgbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.expandOrCollapseList(groupPosition);
			}
		});
	
		

		imgPerc.setImageResource(rowItemTestesDetalhes.getPercImg());
		txtPerc.setText(rowItemTestesDetalhes.getTextPerc());
		txtPerc.setTextColor(activity.getResources().getColor(
				rowItemTestesDetalhes.getColor()));
		txtName.setText(rowItemTestesDetalhes.getTextName());
//		imgState.setImageResource(rowItemTestesDetalhes.getState());
//		imgbutton.setImageResource(rowItemTestesDetalhes.getButton());
		
		if (rowItemTestesDetalhes.getTextPerc() != R.string.percent_100) {
			linha.setOnClickListener(null);
		}
		if (rowItemTestesDetalhes.getTextPerc() == R.string.percent_100) {
			imgbutton.setVisibility(View.VISIBLE);
			
		} else
			// desaparecer botao
			imgbutton.setVisibility(View.GONE);


		if (isExpanded) {

			convertView.setBackgroundColor(Color.WHITE);
			txtName.setTextColor(activity.getResources().getColor(
					rowItemTestesDetalhes.getColor()));
			imgbutton.setVisibility(View.GONE);
//			txtNume.setLayoutParams(new LinearLayout.LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		} else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
			txtName.setTextColor(activity.getResources().getColor(
					R.color.color_light_grey_text));
//			txtNume.setLayoutParams(new LinearLayout.LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//			txtNume.setFocusable(true);
//			if (rowItemTestesDetalhes.getTextPerc() == R.string.percent_100) {
//				imgbutton.setVisibility(View.VISIBLE);
//				txtNume.setFocusable(false);
//				 txtName.setTextColor(activity.getResources().getColor(R.color.color_light_grey_text));
//			}
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
