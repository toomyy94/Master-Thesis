package pt.ptinovacao.arqospocket.fragments;

import pt.ptinovacao.arqospocket.ActionBarController;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.SlidingMenuController;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentActionBar extends Fragment implements ActionBarController {
	TextView tv_title;

	public FragmentActionBar() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_action_bar, container,
				false);

		tv_title = (TextView) v.findViewById(R.id.fragment_action_bar_title);

		v.findViewById(R.id.fragment_action_bar_toggle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (getActivity() instanceof SlidingMenuController) {
							((SlidingMenuController) getActivity())
									.onSlidingMenuToggle();
						}

					}
				});

		return v;

	}

	@Override
	public void onActionBarSetTitle(String title) {
		tv_title.setText(title);

	}
}
