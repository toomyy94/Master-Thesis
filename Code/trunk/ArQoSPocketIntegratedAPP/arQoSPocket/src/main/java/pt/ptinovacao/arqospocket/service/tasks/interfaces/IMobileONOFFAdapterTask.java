package pt.ptinovacao.arqospocket.service.tasks.interfaces;

import pt.ptinovacao.arqospocket.service.enums.EMobileState;

public interface IMobileONOFFAdapterTask {
	public void DataPlanChange(EMobileState eMobileState);
}