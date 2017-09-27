package pt.ptinovacao.arqospocket.views.help;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;

/**
 * HelpFragment.
 * <p>
 * Created by pedro on 12/04/2017.
 */
public class HelpFragment extends ArQoSBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_help, container, false);

        TextView title = (TextView) rootView.findViewById(R.id.texttitulo1);
        title.setText(getVersion());

        return rootView;
    }

    @NonNull
    private String getVersion() {
        return getArQosApplication().getApplicationContext().getResources().getString(R.string.about_dialog_title) +
                " v." + getVersionInfo();
    }

    public String getVersionInfo() {
        StringBuilder versionName = new StringBuilder();
        PackageInfo packageInfo;
        try {
            packageInfo = getArQosApplication().getApplicationContext().getPackageManager()
                    .getPackageInfo(getArQosApplication().getApplicationContext().getPackageName(), 0);
            versionName.append(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionName.append("Unknown");
        }

        return versionName.toString();
    }
}