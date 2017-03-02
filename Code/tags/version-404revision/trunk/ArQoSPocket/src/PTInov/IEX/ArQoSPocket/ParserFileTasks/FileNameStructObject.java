package PTInov.IEX.ArQoSPocket.ParserFileTasks;

import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class FileNameStructObject {
	
	private final String tag = "FileNameStructObject";
	
	private String mac;
	private String testID;
	private Date dataInicio;
	private Date dataFim;
	private String moduloID;

	public FileNameStructObject(String pmac, String ptextID, Date pdataInicio, Date pdataFim, String pmoduloID) {
		mac = pmac;
		testID = ptextID;
		dataInicio = pdataInicio;
		dataFim = pdataFim;
		moduloID = pmoduloID;
	}
	
	public void RunNow() {
		
		Date now = new Date();
		
		Date endDate = new Date();
		long difference = dataFim.getTime()-dataInicio.getTime();
		endDate.setTime(now.getTime()+difference);
		
		
		dataInicio = now;
		dataFim = endDate;
	}
	
	public String getMac() {
		return mac;
	}
	
	public String getTestID() {
		return testID;
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}
	
	public Date getDataFim() {
		return dataFim;
	}
	
	public void addDateDelay(long delayInMiliSec) {
		
		// Verifica se a data de inicio e inferior a de agora, se for actualiza para a dar de agora
		Date now = new Date();
		if (dataInicio.before(now)) {
			
			//Mater o intervalo de diferença entre inicio e fim de teste
			Date endDate = new Date();
			long difference = dataFim.getTime()-dataInicio.getTime();
			//Log.v(tag, "..........................difference :"+difference);
			endDate.setTime(now.getTime()+difference);
			
			dataInicio.setTime(now.getTime()+delayInMiliSec);
			dataFim.setTime(endDate.getTime()+delayInMiliSec);
		} else {
			dataInicio.setTime(dataInicio.getTime()+delayInMiliSec);
			dataFim.setTime(dataFim.getTime()+delayInMiliSec);
		}
	}
	
	public String getDataInicioInterfaceFormat() {
		// o dia nao esta aparecer bem com o date
		return dataInicio.getDate()+"-"+(dataInicio.getMonth()+1)+"-"+(dataInicio.getYear()+1900)+" "+dataInicio.getHours()+":"+dataInicio.getMinutes()+":"+dataInicio.getSeconds();
	}
	
	public String getDataInicioResultFormat() {
		// o dia nao esta aparecer bem com o date
		return dataInicio.getYear()+""+dataInicio.getMonth()+""+dataInicio.getDate()+""+dataInicio.getHours()+""+dataInicio.getMinutes()+""+dataInicio.getSeconds();
	}
	
	public String getDataFimResultFormat() {
		// o dia nao esta aparecer bem com o date
		return dataFim.getYear()+""+dataFim.getMonth()+""+dataFim.getDate()+""+dataFim.getHours()+""+dataFim.getMinutes()+""+dataFim.getSeconds();
	}
	
	public String getModuloID() {
		return moduloID;
	}
	
	public String toString() {
		
		return "Mac:"+mac+"|\ntestID:"+testID+"|\ndataInicio:"+getDataInicioResultFormat()+"|\ndataFim:"+getDataFimResultFormat()+"|\nmoduloID:"+moduloID+"|";
	}
	
	public FileNameStructObject clone() {
		return new FileNameStructObject(new String(mac),new String(testID),(Date) dataInicio.clone(),(Date) dataFim.clone(), new String(moduloID));
	}
}
