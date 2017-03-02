package pt.ptinovacao.arqospocket.util;

import android.content.Context;
import pt.ptinovacao.arqospocket.R;

public enum AnomaliasIcons_Enum {
	
	
	/**
	 * Voz.
	 */
	VOZ(R.string.anomalias_ic_01, R.drawable.icon_menu_voz),
	/**
	 * Internet.
	 */
	INTERNET(R.string.anomalias_ic_02, R.drawable.icon_menu_internet),
	/**
	 * Messaging.
	 */
	MESSAGING(R.string.anomalias_ic_03, R.drawable.icon_menu_messaging),
	/**
	 * Cobertura.
	 */
	COBERTURA(R.string.anomalias_ic_04, R.drawable.icon_menu_cobertura),
	/**
	 * Outra.
	 */
	OUTRA(R.string.anomalias_ic_05, R.drawable.icon_menu_outra);
	
	
	
	
	private int namesRes;
	private int iconRes;
	
	private AnomaliasIcons_Enum(int namesRes, int iconRes) {
		this.namesRes = namesRes;
		this.iconRes = iconRes;
	}

	/**
	 * Returns the resource ID with the name of this tab.
	 * 
	 * @return text resource ID
	 */
	public int getNamesRes() {
		return namesRes;
	}

	
	/**
	 * Returns the resource ID with the icon of this tab.
	 * 
	 * @return drawable resource ID
	 */
	public int getIconRes() {
		return iconRes;
	};
	
	public static int getIconOfAnom(Context ctx, int anomId) {
		int anomType = 0;
		for(AnomaliasIcons_Enum entry : values()) {
			if(entry.namesRes == anomId) {
				anomType = entry.getIconRes();
				break;
			}
		}
		return anomType;
	}
	
	public static int getIconOfAnom(Context ctx, String anomaly) {
		int anomIcon = 0;
		for(AnomaliasIcons_Enum entry : values()) {
			String name = ctx.getResources().getString(entry.namesRes);
			if(name.equals(anomaly)) {
				anomIcon = entry.getIconRes();
				break;
			}
		}
		return anomIcon;
	}
}
