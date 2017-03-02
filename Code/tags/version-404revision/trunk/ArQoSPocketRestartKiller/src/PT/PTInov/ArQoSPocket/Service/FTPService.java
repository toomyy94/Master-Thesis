package PT.PTInov.ArQoSPocket.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import PT.PTInov.ArQoSPocket.Enums.ResponseEnum;
import PT.PTInov.ArQoSPocket.Utils.LogType;
import PT.PTInov.ArQoSPocket.Utils.Logger;
import android.content.Context;

public class FTPService {
	final String tag = "FTPService";
	
	
	// speedtest.nfsi.pt - 81.92.192.12
	private final String urlFTPDownload = "speedtest.nfsi.pt";
	private final int portFTPDownload = 21;
	private final String remotePathDownload = "/pub/speedtest/";
	//private final String fileName = "100mb.bin";
	private final String fileName = "10mb.bin";
	private final String usernameFTPDownload = "anonymous";
	private final String passwordFTPDownload = "";
	
	private final String urlFTPUpload = "ftp.ptinovacao.pt";
	private final int portFTPUpload = 21;
	private final String remotePathUpload = "";
	private final String usernameFTPUpload = "ftp_isr";
	private final String passwordFTPUpload = "ptin-isr";
	
	private Context appContext = null;
	private FTPClient mFtp = null;
	
	private final int timeout = 16000;
	
	public FTPService(Context c) {
		this.appContext = c;
	}
	
	public FTPResponse testSendFileToFTP(String remoteFile, long sizeFileToSendInBytes) {
		final String method = "testSendFileToFTP";
		boolean result = true;
		
		long sendTime = -1;
		double bps = -1;
		long totalSendBytes = 0;
		
		try {
			
			mFtp = new FTPClient();
			mFtp.connect(urlFTPUpload,portFTPUpload); // Using port no=21
			mFtp.login(usernameFTPUpload, passwordFTPUpload);
			//mFtp.setDefaultPort(timeout);
			//mFtp.setDataTimeout(timeout);
			
			// calcla o tamanho da string para ter o numero de bytes pretendidos
			
			InputStream aInputStream = new ByteArrayInputStream(getRandomString(sizeFileToSendInBytes).getBytes());
			
			mFtp.setFileType(FTP.BINARY_FILE_TYPE);
			mFtp.enterLocalPassiveMode();
			
			long startTime = System.currentTimeMillis();
			boolean aRtn = mFtp.storeFile(remotePathUpload+remoteFile, aInputStream);// return true if successful
			aInputStream.close();
			long endTime = System.currentTimeMillis();
			
			if (!aRtn)
				result = false;
			else {
				// remove o ficheiro do servidor
				int tryCount = 0;
				while (!mFtp.deleteFile(remotePathUpload+remoteFile) && tryCount<10) {
					Logger.v(tag, method, LogType.Trace, "Fail removing the file!");
					Thread.sleep(1000);
					tryCount++;
				}
				Logger.v(tag, method, LogType.Trace, "Remove the file!");
			}
			
			mFtp.disconnect();
			
			Logger.v(tag, method, LogType.Trace, "startTime :"+startTime);
		    Logger.v(tag, method, LogType.Trace, "endTime :"+endTime);
			
		    long localFilesizeByteToBits = sizeFileToSendInBytes * 8;
		    
			// fazer as contas para sacar o debito		    
		    sendTime = (endTime - startTime); //millsec
		    bps = (1000 * localFilesizeByteToBits) / sendTime;
		    
		    Logger.v(tag, method, LogType.Trace, "sendTime :"+sendTime);
		    Logger.v(tag, method, LogType.Trace, "bps :"+bps);
		    Logger.v(tag, method, LogType.Trace, "sizeFileToSendInBytes :"+sizeFileToSendInBytes);
			
		} catch(Exception ex) {
			result = false;
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return new FTPResponse(result?ResponseEnum.OK:ResponseEnum.FAIL, sendTime, sizeFileToSendInBytes, bps);
	}
	
	private String getRandomString(long size) {
		StringBuilder sb = new StringBuilder();
		
		Random rnd = new Random();
		
		for (long i=0; i<size; i++) {
			sb.append(rnd.nextInt(9));
		}
		
		return sb.toString();
	}
	
	public FTPResponse testReadFileFromFTP(String remoteFile) {
		final String method = "testReadFileFromFTP";
		boolean result = true;
		
		double readTime = -1;
		double bps = -1;
		long totalReadBytes = 0;
		
		try {
			
			Logger.v(tag, method, LogType.Trace, "In");
			
			
			mFtp = new FTPClient();
			mFtp.connect(urlFTPDownload,portFTPDownload); // Using port no=21
			mFtp.login(usernameFTPDownload, passwordFTPDownload);
			//mFtp.setDefaultPort(timeout);
			mFtp.setDataTimeout(timeout);
			
			mFtp.changeWorkingDirectory(remotePathDownload);
			mFtp.setFileType(FTP.BINARY_FILE_TYPE);
			mFtp.enterLocalActiveMode();
			
			long startTime = System.currentTimeMillis();
			InputStream iStream = mFtp.retrieveFileStream(fileName);
			
			
			/*
			mFtp = new FTPClient();
			mFtp.connect(urlFTPDownload,portFTPDownload); // Using port no=21
			mFtp.login(usernameFTPDownload, passwordFTPDownload);
			
			Logger.v(tag, method, LogType.Trace, "1");
			
			//mFtp.setFileType(FTP.BINARY_FILE_TYPE);
			//mFtp.enterLocalPassiveMode();
			
			long startTime = System.currentTimeMillis();
			
			//InputStream iStream = mFtp.retrieveFileStream(remotePathDownload+remoteFile);
			InputStream iStream = mFtp.retrieveFileStream(remotePathDownload);
			*/
			
			BufferedInputStream bInf = new BufferedInputStream (iStream);
			
			Logger.v(tag, method, LogType.Trace, "2");
			
			while (bInf.available() > 0) 
			{
				if ((System.currentTimeMillis() - startTime) > timeout) break;
				
			  bInf.read();
			  totalReadBytes++;
			}
			
			bInf.close();
			iStream.close();
			mFtp.disconnect();
		    
		    Logger.v(tag, method, LogType.Trace, "3");
		    
		    long endTime = System.currentTimeMillis();
		    
		    Logger.v(tag, method, LogType.Trace, "startTime :"+startTime);
		    Logger.v(tag, method, LogType.Trace, "endTime :"+endTime);
		    Logger.v(tag, method, LogType.Trace, "totalReadBytes :"+totalReadBytes);
		    
		    long localFilesizeByteToBits = totalReadBytes * 8;
		    
		    // fazer as contas para sacar o debito
		    readTime = (endTime - startTime); //millsec
		    bps = (1000 * localFilesizeByteToBits) / readTime;
		    
		    Logger.v(tag, method, LogType.Trace, "readTime :"+readTime);
		    Logger.v(tag, method, LogType.Trace, "bps :"+bps);
			
		} catch(Exception ex) {
			result = false;
			Logger.v(tag, method, LogType.Error, ex.toString());
		}
		
		return new FTPResponse((result && (totalReadBytes != 0))?ResponseEnum.OK:ResponseEnum.FAIL, readTime, totalReadBytes, bps);
	}
}
