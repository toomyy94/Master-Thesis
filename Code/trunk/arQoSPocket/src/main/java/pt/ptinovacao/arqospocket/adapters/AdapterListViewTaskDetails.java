package pt.ptinovacao.arqospocket.adapters;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;

import pt.ptinovacao.arqospocket.DetailItem;
import pt.ptinovacao.arqospocket.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterListViewTaskDetails extends ArrayAdapter<DetailItem> {
    private static final String TAG = "AdapterTaskDetails";

	Context context;

	private static int resId = R.layout.fragment_testes_historico_task_details;

    private OnPlayClickListener onPlayClickListener;
	
	public AdapterListViewTaskDetails(Context context, int resourceId,
			List<DetailItem> items, OnPlayClickListener listener) {
		super(context, resourceId, items);
		this.context = context;
		resId = resourceId;
        onPlayClickListener = listener;
	}
	
	public AdapterListViewTaskDetails(Context context, List<DetailItem> items, OnPlayClickListener listener) {
		super(context, resId, items);
		this.context = context;
        onPlayClickListener = listener;
	}

	@SuppressLint("ResourceAsColor")
	public View getView(int position, View convertView, ViewGroup parent) {

		final DetailItem detailItem = (DetailItem) getItem(position);

		TextView taskTitle = null;
		TextView taskDetail = null;
		ImageView playButton = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			convertView = mInflater.inflate(resId, null);
		}
		
		taskTitle = (TextView) convertView
				.findViewById(R.id.tv_historico_task_title);
		
		taskTitle.setText(detailItem.getFieldName());

        taskDetail = (TextView) convertView
                .findViewById(R.id.tv_historico_task_detail);

        String value = detailItem.getFieldValue();
		if(isNumber(value)) {
			Double numericValue = Double.parseDouble(value);
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			nf.setGroupingUsed(false);
			value = nf.format(numericValue);
		} else if (value.endsWith(".wav")){
			/*value = value.substring(value.lastIndexOf(File.separator)+1, value.length());
			value = value.substring(1,7);*/
            playButton = (ImageView) convertView
                    .findViewById(R.id.iv_play_recorded_audio_icon);

            taskDetail.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);

            playButton.setOnClickListener(new View.OnClickListener() {
                final String audioFilePath = detailItem.getFieldValue();
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "OnClickListener file: " + audioFilePath);
                    if (onPlayClickListener != null) {
                        Log.d(TAG, "Calling onPlayClickListener");
                        onPlayClickListener.onPlayClickListener(audioFilePath);
                    }
                }
            });



		}
				

		if(value != null)
			taskDetail.setText(value);

		return convertView;
	}
	
	
	
	public static boolean isNumber(String strNum) {
		boolean ret = true;
		try {
			Double.parseDouble(strNum);
		} catch (Exception e) {
			ret = false;
		}
		return ret;
	}

    public interface OnPlayClickListener{
        public void onPlayClickListener(String filePath);
    }
}
