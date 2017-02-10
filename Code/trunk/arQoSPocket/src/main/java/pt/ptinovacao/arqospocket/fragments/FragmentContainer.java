package pt.ptinovacao.arqospocket.fragments;

import com.google.android.gms.maps.SupportMapFragment;

import pt.ptinovacao.arqospocket.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentContainer extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.content_frame,
				container, false);
		
		return view;
	}
	
	public void onDestroyView() {
		   super.onDestroyView(); 
		   Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
		   FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		   ft.remove(fragment);
		   ft.commit();
		}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		FragmentAnomaliasHistorico entryFrag = new FragmentAnomaliasHistorico();
		
		getChildFragmentManager()
        .beginTransaction()
        .add(R.id.content_frame, entryFrag)
        .commit();
	}
	
	public void changeFrag(Fragment fragment) {
		getFragmentManager()
		        .beginTransaction()
		        .replace(R.id.content_frame, fragment)
		        .addToBackStack(null)
		        .commit();
	}
	
	
}
