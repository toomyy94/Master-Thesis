package pt.ptinovacao.arqospocket.service.http;


import android.graphics.Bitmap;

public interface IWebRequestCallBack {
	public void WebRequestComplete(WebResponse response, String requestCode);
	public void PhotoDownloadComplete(Bitmap photo, String requestCode);
}
