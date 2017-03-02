package pt.ptinovacao.arqospocket.util;

import pt.ptinovacao.arqospocket.R;

public class Utils {

	public static final int INVALID_RESOURCE = -1;
	
	public static final int HISTORY_FIRST_ITEM = 0;
	
	public static final int ZOOM_VALUE = 10;
	
	public static final String EMPTY_STRING = "";
	
	public static final long DASH_ID = 10000;
	public static final long NETWORK_DASH_ID = 98765;
	public static final long WIFI_DASH_ID = 56789;
	
	
	public static final int Anomalies_icons[] = { R.drawable.todas_anomalias, R.drawable.selector_voz, R.drawable.selector_internet, R.drawable.selector_messaging, R.drawable.selector_cobertura, R.drawable.selector_outra };
	
	/** TODO
	 * quando forem disponibilizados icons dos testes */	
	public static final int Tests_icons[] = { R.drawable.todos_testes, R.drawable.rede_movel, R.drawable.wifi, R.drawable.misto};
	
	
	public static boolean isZoomFartherThanDefault(float zoomLevel) {
		return zoomLevel < ZOOM_VALUE;
	}

}