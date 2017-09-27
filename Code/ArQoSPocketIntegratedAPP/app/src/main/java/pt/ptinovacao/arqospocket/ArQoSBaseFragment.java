package pt.ptinovacao.arqospocket;

import android.support.v4.app.Fragment;

/**
 * ArQoSBaseFragment.
 * <p>
 * Created by pedro on 12/04/2017.
 */
public class ArQoSBaseFragment extends Fragment {

    protected ArQosApplication getArQosApplication() {
        return (ArQosApplication) getActivity().getApplication();
    }

    public int getActivityTitle() {
        return R.string.app_name;
    }
}
