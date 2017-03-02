package pt.ptinovacao.arqospocket;

import pt.ptinovacao.arqospocket.activities.ActivityAjuda;
import pt.ptinovacao.arqospocket.activities.ActivityAnomalias;
import pt.ptinovacao.arqospocket.activities.ActivityAnomaliasHistorico;
import pt.ptinovacao.arqospocket.activities.ActivityConfiguracoes;
import pt.ptinovacao.arqospocket.activities.ActivityDashboardMain;
import pt.ptinovacao.arqospocket.activities.ActivitySplash;
import pt.ptinovacao.arqospocket.activities.ActivityTestes;
import pt.ptinovacao.arqospocket.activities.ActivityTestesHistorico;
import pt.ptinovacao.arqospocket.activities.Activity_Development;
import pt.ptinovacao.arqospocket.activities.Activityo_arQos;
import pt.ptinovacao.arqospocket.core.CoreActivity;
import pt.ptinovacao.arqospocket.fragments.FragmentActionBar;
import pt.ptinovacao.arqospocket.map.MapAct;
import pt.ptinovacao.arqospocket.map.PreferencesFragmentActivity;
import pt.ptinovacao.arqospocket.util.Logger;
import pt.ptinovacao.arqospocket.util.MenuOption;
import pt.ptinovacao.arqospocket.views.SuperTextView;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ArqosActivity extends CoreActivity implements ActionBarController,
		SlidingMenuController, OnClickListener {
	SlidingMenu menu;

	FragmentActionBar actionBar;
	LinearLayout dashboard, anomalias, anamoliashist, testes, testeshist,
			configuracoes, 
			//ajuda,
			info, development;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		super.setContentView(R.layout.activity_navigation);

		if (isTablet()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		actionBar = (FragmentActionBar) getSupportFragmentManager()
				.findFragmentById(R.id.activity_arqos_actionbar);

		findViewById(R.id.activity_arqos_sliding_menu_cover)
				.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (slidingMenuIsOpen()) {
							if (event.getAction() == MotionEvent.ACTION_UP) {
								onSlidingMenuToggle();
							}
							return true;
						} else {
							return false;
						}

					}
				});

		if (slidingMenuEnabled()) {

			menu = new SlidingMenu(this);
			menu.setMode(SlidingMenu.LEFT);
			
			menu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
//			 menu.setShadowDrawable(R.drawable.shadow);
			menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
			menu.setFadeDegree(0.35f);
			menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
			menu.setMenu(R.layout.sliding_menu);
			
			
			dashboard = (LinearLayout) findViewById(R.id.dashboard);
			anomalias = (LinearLayout) findViewById(R.id.anomalias);
			anamoliashist = (LinearLayout) findViewById(R.id.historico_anomalias);
			testes = (LinearLayout) findViewById(R.id.testes);
			testeshist = (LinearLayout) findViewById(R.id.historico_testes);
			configuracoes = (LinearLayout) findViewById(R.id.configuracoes);
			//ajuda = (LinearLayout) findViewById(R.id.ajuda);
			info = (LinearLayout) findViewById(R.id.info);
			development = (LinearLayout) findViewById(R.id.development);

			dashboard.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					normalText();

					if(ArqosActivity.this instanceof ActivityDashboardMain) {
						menu.toggle(false);
					} else {
						startActivity(ActivityDashboardMain.class);
						finish();
						menu.toggle(false);
					}
				}
				
			});

			anomalias.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					normalText();
					startActivity(ActivityAnomalias.class);
					
					menu.toggle(false);
					finish();
				}
			});
			anamoliashist.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					normalText();

//					startActivity(ActivityAnomaliasHistorico.class);
					Intent it = new Intent(ArqosActivity.this, ActivityAnomaliasHistorico.class);
					startActivity(it);
					menu.toggle(false);
					finish();
				}
			});
			testes.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					normalText();
					startActivity(ActivityTestes.class);
					menu.toggle(false);
					finish();
				}
			});
			testeshist.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					normalText();

					startActivity(ActivityTestesHistorico.class);
					menu.toggle(false);
					finish();
				}
			});
			configuracoes.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					normalText();

					startActivity(ActivityConfiguracoes.class);
					menu.toggle(false);
					finish();
				}
			});
			/*
			ajuda.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					normalText();
					
					startActivity(ActivityAjuda.class);
					menu.toggle(false);
					finish();
				}
			});
			*/
			info.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					normalText();
				
					startActivity(Activityo_arQos.class);
					menu.toggle(false);
					finish();
				}

			});
			development.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					normalText();

					startActivity(Activity_Development.class);
					menu.toggle(false);
					finish();
				}

			});

		}

		if (!actionBarEnabled()) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.hide(actionBar).commit();
		} else {
			onActionBarSetTitle(getAppTitle());
		}
	}

	@Override
	public void setContentView(int layoutResID) {
		ViewGroup vg = (ViewGroup) findViewById(R.id.activity_arqos_content);

		vg.removeAllViews();

		LayoutInflater inflater = getLayoutInflater();
		inflater.inflate(layoutResID, vg, true);
	}

	public String getAppTitle() {
		return getString(R.string.app_name);
	}

	@Override
	public void onSlidingMenuToggle() {
		if (slidingMenuEnabled()) {
			menu.toggle(true);
		}

	}

	public boolean slidingMenuEnabled() {
		return true;
	}

	protected boolean slidingMenuIsOpen() {
		if (slidingMenuEnabled()) {
			return menu.isMenuShowing();
		}
		return false;
	}

	public boolean actionBarEnabled() {
		return true;
	}

	public static enum ScreenOrientation {
		landscape, portrait, sensor
	};

	public void setScreenOrientation(ScreenOrientation orientation) {
		if (orientation == ScreenOrientation.landscape) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else if (orientation == ScreenOrientation.portrait) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if (orientation == ScreenOrientation.sensor) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
	}

	ScreenOrientation getScreenOrientation(Configuration newConfig) {
		if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
			return ScreenOrientation.landscape;
		if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			return ScreenOrientation.portrait;
		if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER)
			return ScreenOrientation.landscape;

		return null;
	}

	public boolean isTablet() {
		try {

			if (Build.VERSION.SDK_INT >= 11) {
				boolean xlarge = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE;

				return xlarge;
			}
		} catch (Exception e) {
			Logger.logE(e);
		}
		return false;
	}

	public void showActionBar() {
		showHideActionBar(true);
	}

	public void hideActionBar() {
		showHideActionBar(false);
	}

	void showHideActionBar(boolean show) {

		if (actionBarEnabled() && actionBar != null) {
			if (show) {
				getSupportFragmentManager().beginTransaction().show(actionBar)
						.commit();
			} else {
				getSupportFragmentManager().beginTransaction().hide(actionBar)
						.commit();
			}

		}
	}

	@Override
	public void onActionBarSetTitle(String title) {
		if (actionBarEnabled() && actionBar != null) {
			ActionBarController controller = actionBar;
			controller.onActionBarSetTitle(title);
		}
	}

	public void startActivityForResult(Class c) {
		startActivityForResult(c, null);
	}

	public void startActivityForResult(Class c, Bundle options) {
		Intent intent = new Intent(this, c);
		intent.putExtras(options);
		startActivityForResult(intent, 123);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	public void startActivity(Class c, Bundle bundle) {
		Intent intent = new Intent(this, c);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);

	}

	public void startActivity(Class c) {
		startActivity(c, (Bundle) null);

	}

	@Override
	public void onClick(View v) {

	}

	public void boldText(int resource) {
		SuperTextView question_value = (SuperTextView) findViewById(resource);
		question_value.setTypeface(null, Typeface.BOLD);
	}

	public void setMenuOption(MenuOption o) {

		switch (o) {
		case Dashboard:
			boldText(R.id.dashboardtext);
			break;
		case Anomalias:
			boldText(R.id.anomaliastext);
			break;
		case HistoricoAnomalias:
			boldText(R.id.historico_anomaliastext);
			break;
		case Testes:
			boldText(R.id.testestext);
			break;
		case HistoricoTestes:
			boldText(R.id.historico_testestext);
			break;
		case Configuracoes:
			boldText(R.id.configuracoestext);
			break;
		case Ajuda:
			//boldText(R.id.ajudatext);
			break;
		case Info:
			boldText(R.id.infotext);
			break;
		default:
			break;
		}

	}

	private void normalText() {
		SuperTextView question_valuedashboardtext = (SuperTextView) findViewById(R.id.dashboardtext);
		question_valuedashboardtext.setTypeface(null, Typeface.NORMAL);

		SuperTextView question_valueanomaliastext = (SuperTextView) findViewById(R.id.anomaliastext);
		question_valueanomaliastext.setTypeface(null, Typeface.NORMAL);

		SuperTextView question_valuehistorico_anomaliastext = (SuperTextView) findViewById(R.id.historico_anomaliastext);
		question_valuehistorico_anomaliastext
				.setTypeface(null, Typeface.NORMAL);

		SuperTextView question_valuetestestext = (SuperTextView) findViewById(R.id.testestext);
		question_valuetestestext.setTypeface(null, Typeface.NORMAL);

		SuperTextView question_valuehistorico_testestext = (SuperTextView) findViewById(R.id.historico_testestext);
		question_valuehistorico_testestext.setTypeface(null, Typeface.NORMAL);

		SuperTextView question_valueconfiguracoestext = (SuperTextView) findViewById(R.id.configuracoestext);
		question_valueconfiguracoestext.setTypeface(null, Typeface.NORMAL);

		///SuperTextView question_valueajudatext = (SuperTextView) findViewById(R.id.ajudatext);
		//question_valueajudatext.setTypeface(null, Typeface.NORMAL);

		SuperTextView question_valueinfotext = (SuperTextView) findViewById(R.id.infotext);
		question_valueinfotext.setTypeface(null, Typeface.NORMAL); 
		
		
		}
	public void onBackPressed() {
		if (slidingMenuIsOpen()) {
			onSlidingMenuToggle();
		}
		else 
//			finish();
			super.onBackPressed();
	}
}