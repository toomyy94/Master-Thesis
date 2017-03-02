package pt.ptinovacao.arqospocket.interfaces;

import java.util.ArrayList;

import pt.ptinovacao.arqospocket.util.History;

public interface IHistoryProvider {

	public ArrayList<? extends History> getHistory();
}
