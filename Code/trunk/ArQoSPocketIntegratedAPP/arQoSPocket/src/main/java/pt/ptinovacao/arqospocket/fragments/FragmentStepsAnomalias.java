package pt.ptinovacao.arqospocket.fragments;

import pt.ptinovacao.arqospocket.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentStepsAnomalias extends Fragment {

	TextView textabtfalha, texttipfalha, textidtlocal, textenvfeedback;
	ImageView imgabtfalha, imgtipfalha, imgidtlocal, imgenvfeedback;
	LinearLayout linabtfalha, lintipfalha, linidtlocal, linenvfeedback;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_anomalias_steps, container,
				false);

		imgabtfalha = (ImageView) v.findViewById(R.id.imgabtfalha);
		imgtipfalha = (ImageView) v.findViewById(R.id.imgtipfalha);
		imgidtlocal = (ImageView) v.findViewById(R.id.imgidtlocal);
		imgenvfeedback = (ImageView) v.findViewById(R.id.imgenvfeedback);

		textabtfalha = (TextView) v.findViewById(R.id.textabtfalha);
		texttipfalha = (TextView) v.findViewById(R.id.texttipfalha);
		textidtlocal = (TextView) v.findViewById(R.id.textidtlocal);
		textenvfeedback = (TextView) v.findViewById(R.id.textenvfeedback);

		Item(0);
		return v;

	}

	public void setItemPosition(int position) {
		Item(position);
	}

	public void Item(int value) {
		switch (value) {
		case 0:
			imgabtfalha.setImageResource(R.drawable.step1_on);
			imgtipfalha.setImageResource(R.drawable.step2_default);
			imgidtlocal.setImageResource(R.drawable.step3_default);
			imgenvfeedback.setImageResource(R.drawable.step4_default);

			textabtfalha.setTextColor(getResources().getColor(
					R.color.color_step_on));
			texttipfalha.setTextColor(getResources().getColor(
					R.color.color_step_default));
			textidtlocal.setTextColor(getResources().getColor(
					R.color.color_step_default));
			textenvfeedback.setTextColor(getResources().getColor(
					R.color.color_step_default));

			break;
		case 1:
			imgabtfalha.setImageResource(R.drawable.step1_off);
			imgtipfalha.setImageResource(R.drawable.step2_on);
			imgidtlocal.setImageResource(R.drawable.step3_default);
			imgenvfeedback.setImageResource(R.drawable.step4_default);

			textabtfalha.setTextColor(getResources().getColor(
					R.color.color_branding));
			texttipfalha.setTextColor(getResources().getColor(
					R.color.color_step_on));
			textidtlocal.setTextColor(getResources().getColor(
					R.color.color_step_default));
			textenvfeedback.setTextColor(getResources().getColor(
					R.color.color_step_default));
			break;
		case 2:
			imgabtfalha.setImageResource(R.drawable.step1_off);
			imgtipfalha.setImageResource(R.drawable.step2_off);
			imgidtlocal.setImageResource(R.drawable.step3_on);
			imgenvfeedback.setImageResource(R.drawable.step4_default);

			textabtfalha.setTextColor(getResources().getColor(
					R.color.color_branding));
			texttipfalha.setTextColor(getResources().getColor(
					R.color.color_branding));
			textidtlocal.setTextColor(getResources().getColor(
					R.color.color_step_on));
			textenvfeedback.setTextColor(getResources().getColor(
					R.color.color_step_default));
			break;
		case 3:
			imgabtfalha.setImageResource(R.drawable.step1_off);
			imgtipfalha.setImageResource(R.drawable.step2_off);
			imgidtlocal.setImageResource(R.drawable.step3_off);
			imgenvfeedback.setImageResource(R.drawable.step4_on);

			textabtfalha.setTextColor(getResources().getColor(
					R.color.color_branding));
			texttipfalha.setTextColor(getResources().getColor(
					R.color.color_branding));
			textidtlocal.setTextColor(getResources().getColor(
					R.color.color_branding));
			textenvfeedback.setTextColor(getResources().getColor(
					R.color.color_step_on));
			break;
		default:
			break;
		}
	}

}
