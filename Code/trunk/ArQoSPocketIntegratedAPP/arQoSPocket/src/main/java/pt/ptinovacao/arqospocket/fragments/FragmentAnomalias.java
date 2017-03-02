package pt.ptinovacao.arqospocket.fragments;

import pt.ptinovacao.arqospocket.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentAnomalias extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.anomalias_steps, container, false);

		return v;

	}
}
