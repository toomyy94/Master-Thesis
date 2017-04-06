package pt.ptinovacao.arqospocket.util;

import android.content.Context;
import android.util.Log;

import pt.ptinovacao.arqospocket.R;

public enum RadiologsIcons_Enum {


	/**
	 * Radiolog.
	 */
	RADIOLOG(R.string.radiolog, R.drawable.icon_menu_voz),
	/**
	 * EVENT.
	 */
	EVENT(R.string.radiolog_event, R.drawable.icon_menu_internet),
	/**
	 * Snapshot.
	 */
	SNAPSHOT(R.string.snapshot, R.drawable.icon_menu_messaging);

	private int namesRes;
	private int iconRes;

	private RadiologsIcons_Enum(int namesRes, int iconRes) {
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
		for(RadiologsIcons_Enum entry : values()) {
			if(entry.namesRes == anomId) {
				anomType = entry.getIconRes();
				break;
			}
		}
		return anomType;
	}
	
	public static int getIconOfRadiolog(Context ctx, String radiolog) {
		int radioIcon = 0;
		for(RadiologsIcons_Enum entry : values()) {
			String name = ctx.getResources().getString(entry.namesRes);
//          Radiolog=Radiolog // Snapshot=Snapshot
			if(name.equals(radiolog)) {
				radioIcon = entry.getIconRes();
				break;
			}
		}
		return radioIcon;
	}
}
