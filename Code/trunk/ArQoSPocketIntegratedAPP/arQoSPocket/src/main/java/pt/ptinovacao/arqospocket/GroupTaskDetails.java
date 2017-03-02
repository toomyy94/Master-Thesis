package pt.ptinovacao.arqospocket;

import java.util.ArrayList;
import java.util.List;

import pt.ptinovacao.arqospocket.core.RowItemTestesDetalhes;


public class GroupTaskDetails {

	public RowItemTestesDetalhes itemInfo;
	public final List<DetailItem> children = new ArrayList<DetailItem>();

	public GroupTaskDetails(RowItemTestesDetalhes string) {
		this.itemInfo = string;
	}
}
