package pt.ptinovacao.arqospocket.service.jsonparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.jsonparser.enums.ETestEvent;
import pt.ptinovacao.arqospocket.service.jsonparser.structs.TestEventObject;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.enums.ETestType;
import pt.ptinovacao.arqospocket.service.interfaces.ITest;
import pt.ptinovacao.arqospocket.service.jsonparser.structs.IterationsTestEvent;
import pt.ptinovacao.arqospocket.service.jsonparser.structs.RecursionStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.structs.TimeIntervalTestEvent;
import pt.ptinovacao.arqospocket.service.jsonparser.structs.WeekTestEvent;
import pt.ptinovacao.arqospocket.service.jsonparser.utils.IntToETestEvent;

public class TestStruct implements ITest, Serializable, Comparable<TestStruct>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LoggerFactory.getLogger(TestStruct.class);

	private String jObjectSerialized;
	
	private String testeid;
	private String dataini;
	private String datafim;
	private int modulo;
	private String testname;
	private int macronr;
	
	private ETestEvent testtype;
	private TestEventObject startparam;
	
	private ETestEvent endevent;
	private TestEventObject endparam;
	
	private RecursionStruct recursion;	
	
	// 0 - teste desactivo e valido ate a sua data de fim. Só será activo quando o estado for alterado para 1.
	private int state;


	/*
	* prioridade do teste.
	* Se o campo for omitido é assumido o valor 0, que corresponde à prioridade máxima.
	* 2147483648 corresponde à prioridade mínima.
	*
	 */
	private int priority;
	
	private List<TaskStruct> task_list;

	private Date dateini;
	private Date datefim;
	
	public TestStruct(String testeid, String dataini, String datafim, int modulo, String testname, int macronr, ETestEvent testtype,
			TestEventObject startparam, ETestEvent endevent, TestEventObject endparam, RecursionStruct recursion, int state, int priority,
			List<TaskStruct> task_list, Date dateini, Date datefim) {
		final String method = "TestStruct - all";
		
		try {
			
			this.testeid = testeid;
			this.dataini = dataini;
			this.datafim = datafim;
			this.modulo = modulo;
			this.testname = testname;
			this.macronr = macronr;
			this.testtype = testtype;
			this.startparam = startparam;
			this.endevent = endevent;
			this.endparam = endparam;
			this.recursion = recursion;
			this.state = state;
			this.priority = priority;
			this.task_list = task_list;
			this.dateini = dateini;
			this.datefim = datefim;

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public TestStruct(JSONObject jObject) {
		final String method = "TestStruct";
		
		try {
			
			this.jObjectSerialized = jObject.toString();
			
			this.testeid = jObject.getString("testeid");
			this.dataini = jObject.getString("dataini");
			this.datafim = jObject.getString("datafim");
			this.modulo = jObject.getInt("modulo");
			this.testname = jObject.getString("testname");
			this.macronr = jObject.getInt("macronr");
			
			this.testtype = get_event(jObject,"testtype");
			this.startparam = get_params(jObject, testtype, "startparam");
			
			this.endevent = get_event(jObject,"endevent");
			this.endparam = get_params(jObject, endevent, "endparam");
			
			this.recursion = getRecursionStruct(jObject);
			
			try {
				state = jObject.getInt("state");
			} catch(Exception ex) {
				state = -1;
				MyLogger.error(logger, method, ex);
			}

            try {
                priority = jObject.getInt("priority");
            } catch(Exception ex) {
                priority = 0;
                MyLogger.error(logger, method, ex);
            }
			
			MyLogger.debug(logger, method, "testeid :"+testeid);
			MyLogger.debug(logger, method, "dataini :"+dataini);
			MyLogger.debug(logger, method, "datafim :"+datafim);
			MyLogger.debug(logger, method, "modulo :"+modulo);
			MyLogger.debug(logger, method, "testname :"+testname);
			MyLogger.debug(logger, method, "macronr :"+macronr);
			MyLogger.debug(logger, method, "testtype :"+testtype);
			
			if (startparam != null) MyLogger.debug(logger, method, "startparam :"+startparam);
			MyLogger.debug(logger, method, "endevent :"+endevent);
			if (endparam != null) MyLogger.debug(logger, method, "endparam :"+endparam);
			if (recursion != null) MyLogger.debug(logger, method, "recursion :"+recursion);
			
			// parse dates
			
			Pattern pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})");
			Matcher matcher = pattern.matcher(dataini);
			
			if (matcher.matches()) {
				int year = Integer.parseInt(matcher.group(1));
				int month = Integer.parseInt(matcher.group(2));
				int day = Integer.parseInt(matcher.group(3));
			    int hour = Integer.parseInt(matcher.group(4));
			    int minute = Integer.parseInt(matcher.group(5));
			    int second = Integer.parseInt(matcher.group(6));
			    dateini = new Date(year-1900, month-1, day, hour, minute, second);
			}
			
			pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})");
			matcher = pattern.matcher(datafim);
			
			if (matcher.matches()) {
				int year = Integer.parseInt(matcher.group(1));
				int month = Integer.parseInt(matcher.group(2));
				int day = Integer.parseInt(matcher.group(3));
			    int hour = Integer.parseInt(matcher.group(4));
			    int minute = Integer.parseInt(matcher.group(5));
			    int second = Integer.parseInt(matcher.group(6));
			    datefim = new Date(year-1900, month-1, day, hour, minute, second);
			}
			
			MyLogger.debug(logger, method, "dateini :"+dateini);
			MyLogger.debug(logger, method, "datefim :"+datefim);
			
			
			// parse das tasks
			JSONArray taskList = jObject.getJSONArray("data");
			
			MyLogger.debug(logger, method, "1");
			
			task_list = new ArrayList<TaskStruct>();
			
			MyLogger.debug(logger, method, "2");


			for (int i=0; i<taskList.length(); i++) {
				MyLogger.debug(logger, method, "2.1");
				//para suportar os testes antigos no format json e os novos no formato |||
				if (taskList.get(i) instanceof JSONObject) {
					task_list.add(TaskIDtoObject.taskID_to_Object((JSONObject) taskList.get(i)));
				} else {
					task_list.add(TaskIDtoObject.taskID_to_Object((String) taskList.get(i)));
				}
			}
			
			MyLogger.debug(logger, method, "3");
			
			MyLogger.debug(logger, method, "test_list :"+task_list.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public RecursionStruct getRecursionStruct(JSONObject jObject) {
		final String method = "getRecursionStruct";
		
		try {
			
			JSONObject jObjectRecursion = jObject.getJSONObject("recursion");
			ETestEvent paramtestType = get_event(jObjectRecursion, "event");
			return new RecursionStruct(paramtestType, get_params(jObjectRecursion, paramtestType, "param"));
			 
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public ETestEvent get_event(JSONObject jObject, String eventHeader) {
		final String method = "get_endevent";
		
		try {
			
			return IntToETestEvent.getETestEvent(jObject.getInt(eventHeader));
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return ETestEvent.NONE;
	}
	
	public TestEventObject get_params(JSONObject jObject, ETestEvent paramtestType , String paramHeader) {
		final String method = "get_startparam";
		
		try {
			
			switch(paramtestType) {
			case DATE:
				return null;
			case BOOT:
				return null;
			case ITERATIONS:
				return new IterationsTestEvent(jObject.getJSONObject(paramHeader));
			case TIME_INTERVAL:
				return new TimeIntervalTestEvent(jObject.getJSONObject(paramHeader));
			case WEEK:
				return new WeekTestEvent(jObject.getJSONObject(paramHeader));
			case USER_REQUEST:
				return null;
			case NONE:
				return null;
			default:
				return null;
		}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	
	public String get_testeid() {
		return testeid;
	}
	
	public void set_testeid(String testeid) {
		this.testeid = testeid;
	}
	
	/*
	public String get_dataini() {
		return dataini;
	}
	*/
	
	/*
	public String get_datafim() {
		return datafim;
	}*/
	
	public Date get_dateini() {
		return dateini;
	}
	
	public void set_dataini(long new_date) {
		final String method = "set_dataini";
		
		try {
			
			dateini = new Date(new_date);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public void set_dataini(Date new_date) {
		final String method = "set_dataini";
		
		try {
			
			dateini = new_date;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public Date get_datefim() {
		return datefim;
	}
	
	public int get_modulo() {
		return modulo;
	}
	
	public String get_testname() {
		return testname;
	}
	
	public int get_macronr() {
		return macronr;
	}
	
	public ETestEvent get_testtype() {
		return testtype;
	}
	
	public TestEventObject get_startparam() {
		return startparam;
	}
	
	public ETestEvent get_endevent() {
		return endevent;
	}
	
	public TestEventObject get_endparam() {
		return endparam;
	}
	
	public boolean set_endparam(TestEventObject endparam) {
		final String method = "set_endparam";
		
		try {
			
			this.endparam = endparam;
			return true;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	public RecursionStruct get_recursion() {
		return recursion;
	}
	
	public int get_state() {
		return state;
	}

    public int getPriority() {
        return priority;
    }
	
	public List<TaskStruct> get_full_task_list() {
		return task_list;
	}
	
	public List<ITask> get_task_list() {
		
		List<ITask> result_list = new ArrayList<ITask>();
		for (TaskStruct taskStruct :task_list)
			result_list.add(taskStruct);
		
		return result_list;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ntesteid :"+testeid);
			sb.append("\ndataini :"+dataini);
			sb.append("\ndatafim :"+datafim);
			sb.append("\ndateini :"+dateini.toGMTString());
			sb.append("\ndatefim :"+datefim.toGMTString());
			sb.append("\nmodulo :"+modulo);
			sb.append("\ntestname :"+testname);
			sb.append("\nmacronr :"+macronr);
			sb.append("\ntesttype :"+testtype);
			if (startparam!= null) sb.append("\nstartparam :"+startparam);
			sb.append("\nendevent :"+endevent);
			if (endparam!= null) sb.append("\nendparam :"+endparam);
			sb.append("\nrecursion :"+recursion);
			sb.append("\nstate :"+state);
			sb.append("\npriority :"+priority);

			sb.append("\ntask_list :"+task_list.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	/************
	 * 
	 * 		ITest
	 * 
	 * ***********************/

	@Override
	public String get_test_id() {
		return testeid;
	}

	@Override
	public String get_test_name() {
		return testname;
	}

	@Override
	public ETestType get_test_type() {
		final String method = "get_test_type";
		
		try {
			
			if (testtype == ETestEvent.USER_REQUEST)
					return ETestType.USER_REQUEST;
			else if (testtype == ETestEvent.DATE)
					return ETestType.SCHEDULED;
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	@Override
	public Date get_execution_date() {
		return dateini;
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			String testeidLocal = this.testeid;
			String datainiLocal = this.dataini;
			String datafimLocal = this.datafim;
			int moduloLocal = this.modulo;
			String testnameLocal = this.testname;
			int macronrLocal = this.macronr;
			ETestEvent testtypeLocal = this.testtype;
			TestEventObject startparamLocal = (TestEventObject) ((this.startparam!=null)?this.startparam.clone():null);
			ETestEvent endeventLocal = this.endevent;
			TestEventObject endparamLocal = (TestEventObject) ((this.endparam!=null)?this.endparam.clone():null);
			RecursionStruct recursionLocal = (RecursionStruct) ((this.recursion!=null)?this.recursion.clone():null);
			int stateLocal = this.state;
			int priorityLocal = this.priority;
			List<TaskStruct> task_listLocal = (List<TaskStruct>) ((this.task_list!=null)?((ArrayList<TaskStruct>)task_list).clone():null);
			Date dateiniLocal = this.dateini;
			Date datefimLocal = this.datefim;
			
			return new TestStruct(testeidLocal, datainiLocal, datafimLocal, moduloLocal, testnameLocal, macronrLocal, testtypeLocal,
					startparamLocal, endeventLocal, endparamLocal, recursionLocal, stateLocal, priorityLocal, task_listLocal, dateiniLocal, datefimLocal);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

    @Override
    public int compareTo(TestStruct testStruct) {

        if (priority > testStruct.priority) {
            return 1;
        } else if (priority < testStruct.priority) {
            return -1;
        } else {
            return 0;
        }

    }
}
