package pt.ptinovacao.arqospocket.views.anomaly;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.ArQoSBaseFragment;
import pt.ptinovacao.arqospocket.R;
import pt.ptinovacao.arqospocket.views.anomaly.anomalysubtype.AnomalySubTypeFragment;
import pt.ptinovacao.arqospocket.views.anomaly.anomalytype.AnomalyTypeFragment;
import pt.ptinovacao.arqospocket.views.anomaly.location.AnomalyLocationFragment;
import pt.ptinovacao.arqospocket.views.anomaly.send.AnomalySendFragment;

/**
 * Created by pedro on 12/04/2017.
 */
public class AnomalyBaseViewPager extends ArQoSBaseFragment implements AnomalyActivity.OnControlViewPagerListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnomalyBaseViewPager.class);

    private static int NUM_PAGES = 4;

    private ViewPager viewPager;

    private PagerAdapter pagerAdapter;

    private TextView tvTabSubTypeFailure, tvTabTypeFailure, tvTabLocal, tvTabFeedback;

    private ImageView ivTabSubTypeFailure, ivTabTypeFailure, ivTabLocal, ivTabFeedback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_anomaly, container, false);

        initializeViews(rootView);
        listeners();

        return rootView;
    }

    private void initializeViews(ViewGroup rootView) {

        viewPager = (ViewPager) rootView.findViewById(R.id.pager_anomaly);
        pagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        ivTabTypeFailure = (ImageView) rootView.findViewById(R.id.iv_tab_type_failure);
        ivTabSubTypeFailure = (ImageView) rootView.findViewById(R.id.iv_tab_sub_type_failure);
        ivTabLocal = (ImageView) rootView.findViewById(R.id.iv_tab_local);
        ivTabFeedback = (ImageView) rootView.findViewById(R.id.iv_tab_feedback);

        tvTabTypeFailure = (TextView) rootView.findViewById(R.id.tv_tab_type_failure);
        tvTabSubTypeFailure = (TextView) rootView.findViewById(R.id.tv_tab_sub_type_failure);
        tvTabLocal = (TextView) rootView.findViewById(R.id.tv_tab_local);
        tvTabFeedback = (TextView) rootView.findViewById(R.id.tv_tab_feedback);

    }

  @Override
    public void nextTabSelected() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private final Fragment typeFragment = new AnomalyTypeFragment();

        private final Fragment subTypeFragment = new AnomalySubTypeFragment();

        private final Fragment locationFragment = new AnomalyLocationFragment();

        private final Fragment sendFragment = new AnomalySendFragment();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return typeFragment;
                case 1:
                    return subTypeFragment;
                case 2:
                    return locationFragment;
                case 3:
                    return sendFragment;
                default:
                    return typeFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setMenuViewpager(0);
    }

    private void listeners() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setMenuViewpager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void nextCurrentItem() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    private void setMenuViewpager(int position) {
        switch (position) {
            case 0:
                ivTabSubTypeFailure.setImageResource(R.mipmap.step1_on);
                ivTabTypeFailure.setImageResource(R.mipmap.step2_default);
                ivTabLocal.setImageResource(R.mipmap.step3_default);
                ivTabFeedback.setImageResource(R.mipmap.step4_default);

                tvTabSubTypeFailure.setTextColor(ContextCompat.getColor(getContext(), R.color.color_step_on));
                tvTabTypeFailure.setTextColor(ContextCompat.getColor(getContext(), R.color.color_step_default));
                tvTabLocal.setTextColor(ContextCompat.getColor(getContext(), R.color.color_step_default));
                tvTabFeedback.setTextColor(ContextCompat.getColor(getContext(), R.color.color_step_default));
                break;
            case 1:
                ivTabSubTypeFailure.setImageResource(R.mipmap.step1_off);
                ivTabTypeFailure.setImageResource(R.mipmap.step2_on);
                ivTabLocal.setImageResource(R.mipmap.step3_default);
                ivTabFeedback.setImageResource(R.mipmap.step4_default);

                tvTabSubTypeFailure.setTextColor(ContextCompat.getColor(getContext(), R.color.color_branding));
                tvTabTypeFailure.setTextColor(ContextCompat.getColor(getContext(), R.color.color_step_on));
                tvTabLocal.setTextColor(ContextCompat.getColor(getContext(), R.color.color_step_default));
                tvTabFeedback.setTextColor(ContextCompat.getColor(getContext(), R.color.color_step_default));
                break;
            case 2:
                ivTabSubTypeFailure.setImageResource(R.mipmap.step1_off);
                ivTabTypeFailure.setImageResource(R.mipmap.step2_off);
                ivTabLocal.setImageResource(R.mipmap.step3_on);
                ivTabFeedback.setImageResource(R.mipmap.step4_default);

                tvTabSubTypeFailure.setTextColor(ContextCompat.getColor(getContext(), R.color.color_branding));
                tvTabTypeFailure.setTextColor(ContextCompat.getColor(getContext(), R.color.color_branding));
                tvTabLocal.setTextColor(ContextCompat.getColor(getContext(), R.color.color_step_on));
                tvTabFeedback.setTextColor(ContextCompat.getColor(getContext(), R.color.color_step_default));
                break;
            case 3:
                ivTabSubTypeFailure.setImageResource(R.mipmap.step1_off);
                ivTabTypeFailure.setImageResource(R.mipmap.step2_off);
                ivTabLocal.setImageResource(R.mipmap.step3_off);
                ivTabFeedback.setImageResource(R.mipmap.step4_on);

                tvTabSubTypeFailure.setTextColor(ContextCompat.getColor(getContext(), R.color.color_branding));
                tvTabTypeFailure.setTextColor(ContextCompat.getColor(getContext(), R.color.color_branding));
                tvTabLocal.setTextColor(ContextCompat.getColor(getContext(), R.color.color_branding));
                tvTabFeedback.setTextColor(ContextCompat.getColor(getContext(), R.color.color_step_on));
                break;
            default:
                break;
        }
    }


}