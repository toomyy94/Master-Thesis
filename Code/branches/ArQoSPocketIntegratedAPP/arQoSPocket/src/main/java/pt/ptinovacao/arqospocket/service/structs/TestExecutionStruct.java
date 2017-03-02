package pt.ptinovacao.arqospocket.service.structs;

import android.os.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pt.ptinovacao.arqospocket.service.enums.ERunTestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestTaskState;
import pt.ptinovacao.arqospocket.service.enums.ETestType;
import pt.ptinovacao.arqospocket.service.interfaces.ITask;
import pt.ptinovacao.arqospocket.service.interfaces.ITaskResult;
import pt.ptinovacao.arqospocket.service.interfaces.ITestResult;
import pt.ptinovacao.arqospocket.service.jsonparser.TaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.TestStruct;
import pt.ptinovacao.arqospocket.service.jsonresult.TaskJsonResult;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;

import static pt.ptinovacao.arqospocket.service.utils.Constants.BASE_DIR;
import static pt.ptinovacao.arqospocket.service.utils.Constants.FILENAMES_DATE_FORMAT;
import static pt.ptinovacao.arqospocket.service.utils.Constants.RECORDINGS_DIR;
import static pt.ptinovacao.arqospocket.service.utils.Constants.RESULTS_DIR;
import static pt.ptinovacao.arqospocket.service.utils.Constants.RESULTS_FILE_NAME_SEPARATOR;
import static pt.ptinovacao.arqospocket.service.utils.Constants.RESULTS_FILE_PREFIX;
import static pt.ptinovacao.arqospocket.service.utils.Utils.grabMacAddress;


public class TestExecutionStruct implements ITestResult, Serializable{
	
	private final static Logger logger = LoggerFactory.getLogger(TestExecutionStruct.class);
	
	private TestStruct testStruct;
	
	private Date date_init_execution = null;
	private Date date_end_execution = null;
	
	
	private Date next_execution_date;
	
	private List<TaskJsonResult> task_result_list;
	
	private MyLocation testLocation = null;
	private boolean test_sent_to_server = false;
	private ETestTaskState eTestTaskState = ETestTaskState.Running;
	private ETestType eTestType;
	
	private TestExecutionStruct(TestStruct testStruct, Date date_init_execution, Date date_end_execution, Date next_execution_date,
			List<TaskJsonResult> task_result_list, MyLocation testLocation, boolean test_sent_to_server, ETestTaskState eTestTaskState, ETestType eTestType) {
		final String method = "TestExecutionStruct - all";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			this.testStruct = testStruct;
			this.date_init_execution = date_init_execution;
			this.date_end_execution = date_end_execution;
			this.next_execution_date = next_execution_date;
			this.task_result_list = task_result_list;
			this.testLocation = testLocation;
			this.test_sent_to_server = test_sent_to_server;
			this.eTestTaskState = eTestTaskState;
			this.eTestType = eTestType;
			
			MyLogger.trace(logger, method, "Out");
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	public TestExecutionStruct(TestStruct testStruct, ETestType eTestType, MyLocation testLocation) {
		final String method = "TestExecutionStruct";
		
		try {
			
			MyLogger.trace(logger, method, "In - get_test_name :"+testStruct.get_test_name());
			
			test_sent_to_server = false;
			this.eTestType = eTestType;
			this.testLocation = testLocation;
			
			this.testStruct = testStruct;
			task_result_list = new ArrayList<TaskJsonResult>();
			
			next_execution_date = testStruct.get_dateini();
			
			// verifica delay para a primeira task, se houver delay vou adicionar esse delay a data de execução
			List<TaskStruct> task_list = testStruct.get_full_task_list();
			//MyLogger.debug(logger, method, "task_list :"+task_list.toString());
			
			for (TaskStruct taskStruct :task_list)
				MyLogger.debug(logger, method, "task_name :"+taskStruct.get_task_name());
			
			
			if (task_list != null) {
				if (task_list.size() > 0) {
					
					long delay = Long.parseLong(task_list.get(0).get_instanteExec());
					
					// se for para execução imediata, ignora o delay, ja esta definida a data de arranque como inicio do teste
					if (!task_list.get(0).get_immediate().equals("1") && delay>0) {
						next_execution_date = new Date(next_execution_date.getTime()+delay);
					} 
				}
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public TestStruct get_TestStruct() {
		return testStruct;
	}
	
	public synchronized void set_test_has_sent() {
		test_sent_to_server = true;
	}
	
	public synchronized boolean get_test_sent_state() {
		return test_sent_to_server;
	}
	
	public String get_task_id() {
		final String method = "get_test_id";
		
		try {
			
			return testStruct.get_testeid();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public Date get_execution_date() {
		final String method = "get_execution_date";
		
		try {
			
			return next_execution_date;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}
	
	public TaskStruct get_task_to_execute() {
		final String method = "get_task_to_execute";
		
		try {
			
			if (task_result_list.size() == 0)
				start_execution();
			
			//TODO: para já (e como o pedro disse ontem) vou assumir que a lista tem as tasks ordenadas por ordem de execução
			return testStruct.get_full_task_list().get(task_result_list.size());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public boolean more_task_to_execute() {
		final String method = "more_task_to_execute";
		
		try {
			
			if (date_end_execution != null)
				return false;
			else
				return true;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	public boolean add_task_result(TaskJsonResult taskJsonResult) {
		final String method = "add_task_result";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			MyLogger.debug(logger, method, "taskJsonResult :"+taskJsonResult.toString());
			
			boolean result = task_result_list.add(taskJsonResult);
			
			MyLogger.debug(logger, method, "result :"+result);
			
			// ve se a task correu bem, se nao correu.. coloca o estado do teste como falhado
			if (taskJsonResult.get_task_state() == ETestTaskState.NOK)
				eTestTaskState = ETestTaskState.NOK;
			
			
			// verifica delay para a primeira task, se houver delay vou adicionar esse delay a data de execução
			List<TaskStruct> task_list = testStruct.get_full_task_list();
						
			if (task_list != null) {
				
				MyLogger.debug(logger, method, "task_list.size :"+task_list.size());
				MyLogger.debug(logger, method, "(task_result_list.size()-1) :"+(task_result_list.size()-1));
				
				if ( task_list.size() > task_result_list.size() ) {
					
					MyLogger.trace(logger, method, "if ( task_list.size() > (task_result_list.size()-1) )");
					MyLogger.debug(logger, method, "task_list.get(task_result_list.size()).get_immediate() :"+task_list.get(task_result_list.size()).get_immediate());
					
					if (task_list.get(task_result_list.size()).get_immediate().equals("1")) {
						
						MyLogger.trace(logger, method, "if (task_list.get(task_result_list.size()).get_immediate().equals(\"1\"))");
						
						next_execution_date = new Date(System.currentTimeMillis());
						MyLogger.debug(logger, method, "next_execution_date :"+next_execution_date);
					
					} else {
						
						MyLogger.trace(logger, method, "if (task_list.get(task_result_list.size()).get_immediate().equals(\"0\"))");
						
						long delay = Long.parseLong(task_list.get(task_result_list.size()).get_instanteExec());
						//long timeout = Long.parseLong(task_list.get(task_result_list.size()-1).get_timeout());
						
						MyLogger.debug(logger, method, "delay :"+delay);
						//MyLogger.debug(logger, method, "timeout :"+timeout);
						
						// converte delay to millisec 
						delay *= 1000;
						
						MyLogger.debug(logger, method, "delay :"+delay);
						
						//next_execution_date = new Date( next_execution_date.getTime() +  timeout + delay);
						next_execution_date = new Date(get_execution_date().getTime() + delay);


						
						MyLogger.debug(logger, method, "next_execution_date :"+next_execution_date);
					}
								
				}
			}
			
			MyLogger.debug(logger, method, "result :"+result+" test_done() :"+test_done());
			
			if (result && test_done()) {
				MyLogger.trace(logger, method, "Execute end_execution!");
				end_execution();
			}
			
			return result;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	private boolean test_done() {
		final String method = "test_done";
		
		try {
			
			//TODO: de momento vamos deixar esta condição, mas no futuro temos de fazer uma avaliação task a task porque pode haver tasks que nao correram por falta de tempo
			if (task_result_list.size() == testStruct.get_task_list().size())
				return true;
			
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	private void start_execution() {
		final String method = "start_execution";
		
		try {
			
			date_init_execution = new Date(System.currentTimeMillis());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	private void end_execution() {
		final String method = "end_execution";
		
		try {
			
			date_end_execution = new Date(System.currentTimeMillis());
			
			if (eTestTaskState == ETestTaskState.Running)
				eTestTaskState = ETestTaskState.OK;

			renameAudioFiles();

            writeResultsToFile();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	private void renameAudioFiles(){
		final String method = "renameAudioFiles";
		final String audioDirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + BASE_DIR
                + File.separator
                + RECORDINGS_DIR ;


		for (TaskJsonResult taskJsonResult : task_result_list){

			String filename = taskJsonResult.getAudioFileName() ;
            MyLogger.debug(logger, method, "RENAMING FileName: " +filename);

			if (filename != null) {
				File audioFileWav = new File(audioDirPath, filename + ".wav");
				File audioFileTTA = new File(audioDirPath, filename + ".tta");

				String newFileName = filename.split(RESULTS_FILE_NAME_SEPARATOR)[0];

				newFileName += RESULTS_FILE_NAME_SEPARATOR
						+ FILENAMES_DATE_FORMAT.format(get_execution_date()) + RESULTS_FILE_NAME_SEPARATOR
						+ FILENAMES_DATE_FORMAT.format(get_date_end_execution());

				File newFileWav = new File(audioDirPath, newFileName + ".wav");
				File newFileTTA = new File(audioDirPath, newFileName + ".tta");

				if (audioFileWav.exists()){
					audioFileWav.renameTo(newFileWav);

                    taskJsonResult.setAudioFileName(newFileWav.getAbsolutePath());

                    MyLogger.debug(logger, method, "FileName: " + audioFileWav.getAbsolutePath() + " renamed to:" + newFileWav.getAbsolutePath());
				} else {
                    MyLogger.debug(logger, method, "Eror renaming file! File: " + audioFileWav.getAbsolutePath() + " not Found!");
                }

                if (audioFileTTA.exists()){
                    audioFileTTA.renameTo(newFileTTA);
                    MyLogger.debug(logger, method, "FileName: " + audioFileTTA.getAbsolutePath() + " renamed to:" + newFileTTA.getAbsolutePath());
                } else {
                    MyLogger.debug(logger, method, "Eror renaming file! File: " + audioFileTTA.getAbsolutePath() + " not Found!");
                }

			}
		}
	}


    private void writeResultsToFile(){
        final String method = "writeResultsToFile";
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator
                    + BASE_DIR;
            File recordingsDir = new File(path, RESULTS_DIR);
            if (!recordingsDir.exists()) {
                recordingsDir.mkdirs();
            }
            String fileName = RESULTS_FILE_PREFIX + RESULTS_FILE_NAME_SEPARATOR
                    + grabMacAddress(null) + "#"
                    + get_test_id() + RESULTS_FILE_NAME_SEPARATOR
                    + FILENAMES_DATE_FORMAT.format(get_execution_date()) + RESULTS_FILE_NAME_SEPARATOR
                    + FILENAMES_DATE_FORMAT.format(get_date_end_execution()) + RESULTS_FILE_NAME_SEPARATOR
                    + "R" + testStruct.get_modulo();

            MyLogger.debug(logger, method, "FileName: " + fileName);
            File file = new File(recordingsDir, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter out
                    = new PrintWriter(new BufferedWriter(new FileWriter(file)));


            for (TaskJsonResult taskJsonResult : task_result_list){

                String taskResultString = taskJsonResult.buildTaskStringResult();
                MyLogger.debug(logger, method, "taskResultString: " + taskResultString);
                out.println(taskResultString);
            }

            out.close();
        } catch (Exception ex) {
            MyLogger.error(logger, method, ex);
        }


    }
	
	public Date get_date_init_execution() {
		return date_init_execution;
	}
	
	public Date get_date_end_execution() {
		return date_end_execution;
	}
	
	public List<TaskJsonResult> get_task_result_list() {
		return task_result_list;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			if (testStruct != null)
				sb.append("\ntestStruct :"+testStruct.toString());
			else
				sb.append("\ntestStruct :null");
			sb.append("\ntest_location :"+testLocation.toString());
			
			if (date_init_execution != null)
				sb.append("\ndate_init_execution :"+date_init_execution.toLocaleString());
			else
				sb.append("\ndate_init_execution :null");
			
			if (date_end_execution != null)
				sb.append("\ndate_end_execution :"+date_end_execution.toLocaleString());
			else
				sb.append("\ndate_end_execution :null");
			
			if (next_execution_date != null)
				sb.append("\nnext_execution_date :"+next_execution_date.toLocaleString());
			else
				sb.append("\nnext_execution_date :null");
			
			if (task_result_list != null)
				sb.append("\ntask_result_list :"+task_result_list.toString());
			else
				sb.append("\ntask_result_list :null");
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
	
	/************
	 * 
	 * 		ITestResult
	 * 
	 * ***********************/

	@Override
	public ETestTaskState get_test_state() {
		final String method = "get_test_state";
		
		try {
			
			return eTestTaskState;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}

	@Override
	public ERunTestTaskState get_run_test_state() {
		final String method = "get_test_state";
		
		try {
			
			if (date_init_execution == null)
				return ERunTestTaskState.WAITING;
			else if ((date_init_execution != null) &&  (date_end_execution == null)) {
				return ERunTestTaskState.RUNNING;
			} else if (date_end_execution != null) {
				return ERunTestTaskState.DONE;
			}
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}

	@Override
	public String get_test_name() {
		final String method = "get_test_state";
		
		try {
			
			return testStruct.get_testname();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}

	@Override
	public ETestType get_test_type() {
		final String method = "get_test_state";
		
		try {
			
			return eTestType;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}

	@Override
	public List<ITaskResult> get_task_list() {
		final String method = "get_test_state";
		
		try {
			
			List<ITaskResult> itaskList = new ArrayList<ITaskResult>();
			
			for (TaskJsonResult taskJsonResult :task_result_list)
				itaskList.add((ITaskResult)taskJsonResult);
			
			return itaskList;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}

	@Override
	public boolean test_already_sent() {
		return test_sent_to_server;
	}

	@Override
	public String get_test_id() {
		final String method = "get_test_state";
		
		try {
			
			return testStruct.get_testeid();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		return null;
	}

	@Override
	public int get_number_of_tests_done() {
		final String method = "get_number_of_tests_done";
		
		try {
			
			return task_result_list.size();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return -1;
	}

	@Override
	public int get_number_of_tests() {
		final String method = "get_number_of_tests_done";
		
		try {
			
			return testStruct.get_task_list().size();
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return -1;
	}

	@Override
	public MyLocation get_test_execution_location() {
		return testLocation;
	}

	public List<ITask> get_task_list_to_do() {
		final String method = "get_task_list_to_do";
		
		try {
			return testStruct.get_task_list();
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
	
	public Object clone() {
		final String method = "clone";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			MyLogger.trace(logger, method, "this :"+this.toString());
			
			TestExecutionStruct thisClone = new TestExecutionStruct((TestStruct) this.testStruct.clone(), (Date) this.date_init_execution.clone(), (Date) this.date_end_execution.clone(), (Date) this.next_execution_date.clone(),
					(List<TaskJsonResult>) ((ArrayList<TaskJsonResult>) this.task_result_list).clone(), (MyLocation) this.testLocation.clone(), this.test_sent_to_server, this.eTestTaskState, this.eTestType);
			
			MyLogger.trace(logger, method, "thisClone :"+thisClone.toString());
			
			return thisClone;
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}
