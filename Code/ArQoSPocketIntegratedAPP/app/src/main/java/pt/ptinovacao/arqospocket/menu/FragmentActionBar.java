package pt.ptinovacao.arqospocket.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenuController;

import pt.ptinovacao.arqospocket.R;

public class FragmentActionBar extends Fragment {

    private TextView textView;

    public FragmentActionBar() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_bar, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_action_bar_title);
        view.findViewById(R.id.fragment_action_bar_toggle).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getActivity() instanceof SlidingMenuController) {
                    ((SlidingMenuController) getActivity()).onSlidingMenuToggle();
                }

            }
        });

        return view;
    }

    public void setActionBarTitle(CharSequence title) {
        textView.setText(title);
    }
}
