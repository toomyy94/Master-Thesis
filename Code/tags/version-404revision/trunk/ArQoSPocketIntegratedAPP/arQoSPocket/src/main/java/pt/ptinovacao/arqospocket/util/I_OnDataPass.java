package pt.ptinovacao.arqospocket.util;

public interface I_OnDataPass {

	public void setPositionSelected(Integer posselected);

	public Integer getPositionSelected();

	public void setSelectTip(Integer selectTip);

	public Integer getSelectTip();

	public void setTextFed(String Textsrt);

	public String getTextFed();
	
	public void setTextAbt(String TextAbt);

	public String getTextABT();
	
	public void setTextTdf(String TextTdf);

	public String getTextTdf();
	
	public void setEnvFed(boolean Fed);

	public boolean getEnvFed();
	
	public void setLong(double Long);

	public Double getLong();
	
	public void setLat(double Lat);

	public Double getLat();
}
