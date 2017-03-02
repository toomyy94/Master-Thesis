package pt.ptinovacao.arqospocket.deprecated;

import pt.ptinovacao.arqospocket.ArqosActivity;

public class ActivityDashboardDetail extends ArqosActivity {

//	ImageView btn;
//	LinearLayout ll;
//	int screenWidth, screenHeight;
//	ListView details;
//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//
//		super.onActionBarSetTitle(getIntent().getExtras().getString("title"));
//
//		setContentView(R.layout.fragment_dashboard_detail_mobile);
//
//		DisplayMetrics displayMetrics = new DisplayMetrics();
//		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		wm.getDefaultDisplay().getMetrics(displayMetrics);
//		screenWidth = displayMetrics.widthPixels;
//		screenHeight = displayMetrics.heightPixels;
//
//		int graphicSize = (screenWidth / 3);
//
//		ll = (LinearLayout) findViewById(R.id.gaugeNeedleLay);
//
//		btn = (ImageView) findViewById(R.id.btn1);
//		final GaugeView gv = (GaugeView) findViewById(R.id.gvMobile);
//		gv.setDiameter(graphicSize);
//		gv.createNeedle(graphicSize, graphicSize, 0, -graphicSize / 2);
//		gv.setAngleRange(0, 225);
//		gv.setNeedleAnimation(true);
//		gv.setAnimationTime(250, 250);
//
//		btn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				gv.setCurrentAngle(gv.getCurrentAngle() + 40);
//			}
//		});
//		
//		
//		
//		details = (ListView) findViewById(R.id.gvMobile);
//		
//		ArrayList<DetailItem> testList = new ArrayList<DetailItem>();
//		DetailItem d1 = new DetailItem("Teste1" , "1");
//		DetailItem d2 = new DetailItem("Teste2" , "2");
//		DetailItem d3 = new DetailItem("Teste3" , "3");
//		DetailItem d4 = new DetailItem("Teste4" , "4");
//		DetailItem d5 = new DetailItem("Teste5" , "5");
//		DetailItem d6 = new DetailItem("Teste6" , "3");
//		
//		testList.add(d1);
//		testList.add(d2);
//		testList.add(d3);
//		testList.add(d4);
//		testList.add(d5);
//		testList.add(d6);
//		
//		AdapterDashboardDetail adapter = new AdapterDashboardDetail(getApplicationContext(), testList);		
//		details.setAdapter(adapter);
//		
//		
//	}
}
