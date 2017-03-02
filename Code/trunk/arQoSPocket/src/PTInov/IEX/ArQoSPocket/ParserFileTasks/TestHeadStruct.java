package PTInov.IEX.ArQoSPocket.ParserFileTasks;

import java.util.Date;

public class TestHeadStruct {

	private String testName;
	private Date dataInicio;
	private Date dataFim;
	private String numMacros;
	private int testType;
	private long intervaloLOOP;
	
	
	public TestHeadStruct(String ptestName, Date pdataInicio, Date pdataFim, String pnumMacros, int ptestType, long pintervaloLOOP) {
		testName = ptestName;
		dataInicio = pdataInicio; 
		dataFim = pdataFim;
		numMacros = pnumMacros;
		testType = ptestType;
		intervaloLOOP = pintervaloLOOP;
	}
	
	public void RunNowOneTime() {
		
		RunNow();
		testType = 0;
	}
	
	public void RunNow() {
		Date now = new Date();
		
		Date endDate = new Date();
		long difference = dataFim.getTime()-dataInicio.getTime();
		endDate.setTime(now.getTime()+difference);
		
		
		dataInicio = now;
		dataFim = endDate;
	}
	
	public String getTestName() {
		return testName;
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
	
	public String getDataInicioHeadFormat() {
		return null;
	}
	
	public String getDataFimHeadFormat() {
		return null;
	}
	
	public String getNumMacros() {
		return numMacros;
	}
	
	public int getTestType() {
		return testType;
	}
	
	public long getIntervaloLOOP() {
		return intervaloLOOP;
	}
	
	public String toString() {
		
		return "textName:"+testName+"|\ndataInicio:"+dataInicio.toString()+"|\ndataFim:"+dataFim.toString()+"|\nnumMacros:"+numMacros+"|\ntestType:"+testType+"|\nintervaloLOOP:"+intervaloLOOP+"|"; 
	}
	
	public TestHeadStruct clone() {
		return new TestHeadStruct(new String(testName),(Date) dataInicio.clone(), (Date) dataFim.clone(), new String(numMacros), testType, intervaloLOOP);
	}
}
