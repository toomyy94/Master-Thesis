package pt.ptinovacao.arqospocket.service.interfaces;

import pt.ptinovacao.arqospocket.service.tasks.Mobile;
import pt.ptinovacao.arqospocket.service.tasks.Wifi;

public interface IServiceNetworksInfo {
	
	public Mobile get_mobile_ref();
	
	public Wifi get_wifi_ref();
}
