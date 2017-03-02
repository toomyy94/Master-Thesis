package PTInov.IEX.ArQoSPocket.ParserFileTasks;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class ParseTest {

	private static String tag = "ParserTest";

	String fileName;
	String path;

	// Informação contida no ficheiro
	ArrayList<TaskStruct> listTasks = null;
	TestHeadStruct headfile = null;
	FileNameStructObject fileNameInformation = null;

	public ParseTest(String pfileName, String ppath) {
		fileName = pfileName;
		path = ppath;
	}

	public ArrayList<TaskStruct> parserTestTask() {

		listTasks = new ArrayList<TaskStruct>();

		File file = new File(path + fileName);

		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);

			if (dis.available() != 0) {
				// avanço a primeira linha
				dis.readLine();

				// le o resto das tarefas
				String line;
				while (dis.available() != 0) {
					line = dis.readLine();

					// [\|]*([^|]*)\|([^|]*)\|([^|]*)\|([^|]*)\|([^|]*)\|([^|]*)\|([^|]*)
					// ([^\|]*\|)

					if (line.length() > 1)
						listTasks.add(getDecodeLine(line));
				}
			}

			fis.close();
			bis.close();
			dis.close();
		} catch (Exception ex) {
			Log.v(tag, "parserTestTask::ERROR :" + ex.toString());
		}

		return listTasks;
	}

	public TaskStruct getDecodeLine(String line) {

		Log.v(tag, "Line :" + line);

		int baseIndex = 0, charIndex = 0;
		ArrayList<String> paramList = new ArrayList<String>();

		// Carrega todos os parametros possiveis
		charIndex = line.indexOf("|", baseIndex);
		while (charIndex >= baseIndex) {
			// Log.v(tag, "charIndex :"+charIndex+" baseIndex :"+baseIndex);
			paramList.add(line.substring(baseIndex, charIndex));
			baseIndex = charIndex + 1;
			charIndex = line.indexOf("|", baseIndex);
		}
		// Nota: tou a partir do principio k tem um | no final, se nao tiver
		// esse parametro nao vai ser adicionado

		Log.v(tag, paramList.toString());

		// Carrega os 7 parametros base
		int indexArray;
		if (paramList.get(0).equals(""))
			indexArray = 1;
		else
			indexArray = 0;

		String IDmacro = paramList.get(indexArray);
		indexArray++;
		String taskNumber = paramList.get(indexArray);
		indexArray++;
		String ICCID = paramList.get(indexArray);
		indexArray++;
		// Date dataInicio = getDataInicioInDateType(paramList.get(indexArray));
		// indexArray++;
		long dataInicio = Long.parseLong(paramList.get(indexArray).replaceAll(
				" ", ""));
		indexArray++;
		String timeOut = paramList.get(indexArray);
		indexArray++;
		String IDTarefa = paramList.get(indexArray);
		indexArray++;
		// String execNow = paramList.get(indexArray); indexArray++;
		// String dependencia = paramList.get(indexArray); indexArray++;

		String execNow = paramList.get(indexArray).substring(0,
				paramList.get(indexArray).indexOf(","));
		String dependencia = paramList.get(indexArray).substring(
				paramList.get(indexArray).indexOf(",") + 1,
				paramList.get(indexArray).length());
		indexArray++;

		ArrayList<String> params = new ArrayList<String>();

		int listSize = paramList.size();
		while (indexArray < listSize) {
			params.add(paramList.get(indexArray));
			indexArray++;
		}

		TaskStruct ts = new TaskStruct(IDmacro, taskNumber, ICCID, dataInicio,
				timeOut, IDTarefa, execNow, dependencia, params);

		// Log.v(tag, ts.toString());

		return ts;
	}

	/*
	 * public Date getDataInicioInDateType(String dataInicio) { Date d = new
	 * Date();
	 * 
	 * String data = dataInicio.substring(0, dataInicio.indexOf("T")); String
	 * time = dataInicio.substring(dataInicio.indexOf("T")+1,
	 * dataInicio.indexOf("."));
	 * 
	 * int indexbase = data.indexOf("-"); String ano = data.substring(0,
	 * indexbase); String mes = data.substring(indexbase+1, data.indexOf("-",
	 * indexbase+1)); String dia = data.substring(data.indexOf("-",
	 * indexbase+1)+1,data.length());
	 * 
	 * indexbase = time.indexOf(":"); String hora = time.substring(0,
	 * indexbase); String min = time.substring(indexbase+1, time.indexOf(":",
	 * indexbase+1)); String sec = time.substring(time.indexOf(":",
	 * indexbase+1)+1,time.length());
	 * 
	 * Log.v(tag, "data: "+data+" time: "+time); Log.v(tag,
	 * "ano: "+ano+" mes: "+mes+" dia:"+dia); Log.v(data,
	 * "hora: "+hora+" min: "+min+" sec:"+sec);
	 * 
	 * d.setYear(Integer.parseInt(ano)-1900);
	 * d.setMonth(Integer.parseInt(mes)-1); d.setDate(Integer.parseInt(dia));
	 * 
	 * d.setHours(Integer.parseInt(hora)); d.setMinutes(Integer.parseInt(min));
	 * d.setSeconds(Integer.parseInt(sec));
	 * 
	 * return d; }
	 */

	// parser da head do ficheiro
	public TestHeadStruct parserHead() {

		Log.v(tag, "parserHead - In");

		File file = new File(path + fileName);

		boolean sair = false;
		int trys = 5;

		while (!sair) {

			try {
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				DataInputStream dis = new DataInputStream(bis);

				if (dis.available() != 0) {
					String head = dis.readLine();

					Log.v(tag, "parserHead - head :" + head);

					Pattern pattern = Pattern
							.compile("[\\|]*(.+)\\|(.+)\\|(.+)\\|(.+)\\|(.+)\\|([^|]+)");
					Matcher matcher = pattern.matcher(head);

					// testa para um header de 6 paramentros
					if (matcher.find()) {

						Log.v(tag, "parserHead - 6 paramentros");

						// Log.v(tag,"textName:"+matcher.group(1)+" dataInicio:"+matcher.group(2)+" dataFim:"+matcher.group(3)+" numMacros:"+matcher.group(4)+" testType:"+matcher.group(5).replaceAll(" ",
						// "")+" intervaloLOOP:"+matcher.group(6).replaceAll(" ",
						// ""));

						String textName = matcher.group(1).replaceAll(" ", "");
						Date dataInicio = getDataHead(matcher.group(2)
								.replaceAll(" ", ""));
						Date dataFim = getDataHead(matcher.group(3).replaceAll(
								" ", ""));
						String numMacros = matcher.group(4).replaceAll(" ", "");
						int testType = Integer.parseInt(matcher.group(5)
								.replaceAll(" ", ""));
						Log.v(tag, "parserHead - long :"
								+ Long.parseLong(matcher.group(6).replaceAll(
										" ", "")));
						long intervaloLOOP = Long.parseLong(matcher.group(6)
								.replaceAll(" ", ""));

						headfile = new TestHeadStruct(textName, dataInicio,
								dataFim, numMacros, testType, intervaloLOOP);
						return (headfile);
					} else {
						// testa para um header de 4 parametros

						Log.v(tag, "parserHead - 5 paramentros");

						pattern = Pattern
								.compile("[\\|]*(.+)\\|(.+)\\|(.+)\\|([^|]+)");
						matcher = pattern.matcher(head);

						if (matcher.find()) {
							String textName = matcher.group(1).replaceAll(" ",
									"");
							Date dataInicio = getDataHead(matcher.group(2)
									.replaceAll(" ", ""));
							Date dataFim = getDataHead(matcher.group(3)
									.replaceAll(" ", ""));
							String numMacros = matcher.group(4).replaceAll(" ",
									"");

							headfile = new TestHeadStruct(textName, dataInicio,
									dataFim, numMacros, 0, 0);
							return (headfile);
						}
					}
				}

				fis.close();
				bis.close();
				dis.close();
				
				sair = true;
				
			} catch (Exception ex) {
				Log.v(tag, "parserHead::ERROR :" + ex.toString());
				
				if (trys>0) {
					trys--;
					
					try {
						Thread.sleep(1000);
					} catch(Exception e) {
						
					}
					
				} else {
					sair = true;
				}
			}
		}

		Log.v(tag, "parserHead - Out");

		return headfile;
	}

	public Date getDataHead(String data) {
		Date d = new Date();

		Log
				.v(tag,
						"......................................................................");
		Log.v(tag, "getDataHead - IN with data :" + data);

		String ano = data.substring(0, 4);
		String mes = data.substring(5, 7);
		String dia = data.substring(8, 10);
		String hora = data.substring(10, 12);
		String min = data.substring(13, 15);
		String sec = data.substring(16, data.length());

		Log.v(tag, "ano: " + ano + " mes: " + mes + " dia:" + dia);
		Log.v(tag, "hora: " + hora + " min: " + min + " sec:" + sec);

		d.setYear(Integer.parseInt(ano) - 1900);
		d.setMonth(Integer.parseInt(mes) - 1);
		d.setDate(Integer.parseInt(dia));

		d.setHours(Integer.parseInt(hora));
		d.setMinutes(Integer.parseInt(min));
		d.setSeconds(Integer.parseInt(sec));

		return d;
	}

	// parser do nome do ficheiro
	public FileNameStructObject parserFileName() {

		Log.v(tag, "parserFileName - In - filename :" + fileName);

		Pattern pattern = Pattern.compile(".+_(.+)#(.+)_(.+)_(.+)_(.+)");
		Matcher matcher = pattern.matcher(fileName);

		if (matcher.find()) {
			String mac = matcher.group(1);
			String textID = matcher.group(2);
			Date dataInicio = getDataFileName(matcher.group(3));
			Date dataFim = getDataFileName(matcher.group(4));
			String moduloID = matcher.group(5);

			fileNameInformation = new FileNameStructObject(mac, textID,
					dataInicio, dataFim, moduloID);

			Log.v(tag, "parserFileName - Out");

			return (fileNameInformation);
		}

		Log.v(tag, "parserFileName - Out");

		return null;
	}

	public Date getDataFileName(String data) {
		Date d = new Date();

		Log.v(tag, "getDataFileName - IN with data :" + data);

		String ano = data.substring(0, 4);
		String mes = data.substring(4, 6);
		String dia = data.substring(6, 8);
		String hora = data.substring(8, 10);
		String min = data.substring(10, 12);
		String sec = data.substring(12, data.length());

		Log.v(tag, "ano: " + ano + " mes: " + mes + " dia:" + dia);
		Log.v(data, "hora: " + hora + " min: " + min + " sec:" + sec);

		d.setYear(Integer.parseInt(ano) - 1900);
		d.setMonth(Integer.parseInt(mes) - 1);
		d.setDate(Integer.parseInt(dia));

		d.setHours(Integer.parseInt(hora));
		d.setMinutes(Integer.parseInt(min));
		d.setSeconds(Integer.parseInt(sec));

		return d;
	}
}
