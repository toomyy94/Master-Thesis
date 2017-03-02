package pt.ptinovacao.arqospocket.service.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.util.Log;

import pt.ptinovacao.arqospocket.*;
import pt.ptinovacao.arqospocket.core.CurrentConfiguration;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.jsonparser.TestStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.enums.ETestEvent;
import pt.ptinovacao.arqospocket.service.jsonparser.structs.RecursionStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.structs.TimeIntervalTestEvent;
import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.structs.TestExecutionStruct;
import pt.ptinovacao.arqospocket.service.alarm.IAlarm;
import pt.ptinovacao.arqospocket.service.alarm.MyAlarmManager;
import pt.ptinovacao.arqospocket.service.enums.ETestType;
import pt.ptinovacao.arqospocket.service.interfaces.IRunTaskWorkerCallback;
import pt.ptinovacao.arqospocket.service.interfaces.IServiceNetworksInfo;
import pt.ptinovacao.arqospocket.service.interfaces.ITestSchedulerReport;
import pt.ptinovacao.arqospocket.service.jsonparser.structs.IterationsTestEvent;
import pt.ptinovacao.arqospocket.service.jsonparser.structs.WeekTestEvent;
import pt.ptinovacao.arqospocket.service.jsonresult.TaskJsonResult;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.util.MenuOption;
import pt.ptinovacao.arqospocket.util.SharedPreferencesHelper;

import static pt.ptinovacao.arqospocket.service.jsonparser.enums.ETestEvent.BOOT;
import static pt.ptinovacao.arqospocket.service.jsonparser.enums.ETestEvent.DATE;
import static pt.ptinovacao.arqospocket.service.jsonparser.enums.ETestEvent.ITERATIONS;
import static pt.ptinovacao.arqospocket.service.jsonparser.enums.ETestEvent.TIME_INTERVAL;
import static pt.ptinovacao.arqospocket.util.SharedPreferencesHelper.RUN_DATE_TESTS_KEY;

public class TestScheduler implements IRunTaskWorkerCallback, IAlarm {

	private final static Logger logger = LoggerFactory.getLogger(TestScheduler.class);
	private final long limit_threshold_time_fail_in_sec = 3;
	private TestScheduler myRef = null;
	
	private MyAlarmManager myAlarmManager = null;
	
	private List<TestExecutionStruct> waiting_tests = null;
	private List<TestExecutionStruct> tests_running = null;
	
	private Context appContext;
	private IServiceNetworksInfo iServiceNetworksInfo;
	private ITestSchedulerReport iTestSchedulerReport;
	
	private GPSInformation gps_information = null;
	
	private boolean in_execution = false;
	
	public TestScheduler(Context appContext, GPSInformation gps_information, IServiceNetworksInfo iServiceNetworksInfo, ITestSchedulerReport iTestSchedulerReport) {
		final String method = "TestScheduler";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			in_execution = false;
			
			this.gps_information = gps_information;
			
			this.appContext = appContext;
			this.iServiceNetworksInfo = iServiceNetworksInfo;
			this.iTestSchedulerReport = iTestSchedulerReport;
			
			myRef = this;
			waiting_tests = new ArrayList<TestExecutionStruct>();
			tests_running = new ArrayList<TestExecutionStruct>();
			
			myAlarmManager = new MyAlarmManager(appContext); 
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public synchronized boolean get_in_execution() {
		return in_execution;
	}
	
	public synchronized void set_in_execution(boolean state) {
		in_execution = state;
	}
	
	public boolean add_test_to_scheduler(TestStruct testStruct, ETestType eTestType) {
		final String method = "add_test_to_scheduler";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			MyLogger.debug(logger, method, "testStruct :"+testStruct.toString());
			
			TestExecutionStruct testExecutionStruct = null;
			
			// Verifica o parametro de recursividade para voltar a correr o test se for caso disso
				
				switch(testStruct.get_testtype()) {
					case DATE:
						testExecutionStruct = new TestExecutionStruct(testStruct, eTestType, new MyLocation(gps_information));

						// Verifica se a data definida foi utrapassada
                        MyLogger.trace(logger, method, "getRunDateTests : " + SharedPreferencesHelper.getPersistedData(appContext, RUN_DATE_TESTS_KEY, true));

						if (!SharedPreferencesHelper.getPersistedData(appContext, RUN_DATE_TESTS_KEY, true) || testExecutionStruct.get_TestStruct().get_datefim().getTime() < System.currentTimeMillis())
							return false;

						
						break;
					case BOOT:
						// TODO: Para implementar mais tarde
						break;
					case ITERATIONS:
						// Não é aplicavel no arranque		
						
						
						// Decremeta as iterações e ve se já não existe mais iterações para executar
						IterationsTestEvent iterationsTestEvent = (IterationsTestEvent) testStruct.get_endparam();
						if (iterationsTestEvent.get_count() == 0)
							return false;
						
						break;
					case TIME_INTERVAL:
						// Não é aplicavel no arranque
						break;
					case WEEK:
						
						//WeekTestEvent weekTestEvent = (WeekTestEvent) testStruct.get_endparam();
						WeekTestEvent weekTestEvent = (WeekTestEvent) testStruct.get_startparam();

						if (testStruct.get_dateini() != null) {
							
							if (testStruct.get_dateini().getTime() > System.currentTimeMillis()) {
								testStruct.set_dataini(weekTestEvent.get_next_execution_date(testStruct.get_dateini()));
							} else {
								testStruct.set_dataini(weekTestEvent.get_next_execution_date(new Date(System.currentTimeMillis())));
							}
							
						} else {
							testStruct.set_dataini(weekTestEvent.get_next_execution_date(new Date(System.currentTimeMillis())));
						}
						
						testExecutionStruct = new TestExecutionStruct(testStruct, eTestType, new MyLocation(gps_information));
						
						break;
					case USER_REQUEST:
						testExecutionStruct = new TestExecutionStruct(testStruct, eTestType, new MyLocation(gps_information));
						
						
						// Verifica se a data definida foi utrapassada
						// NOTA: foi definido que a data de fim do teste bloqueia a execução de um teste apos uma determinada data
						
						if (testExecutionStruct.get_TestStruct().get_datefim().getTime() < System.currentTimeMillis())
							return false;
						
						break;
					case NONE:
						// Não é aplicavel no arranque
						
						
						// Verifica se a data definida foi utrapassada
						if (testStruct.get_datefim().getTime() < System.currentTimeMillis())
							return false;
						
						break;
					default:
						// Não é aplicavel no arranque
						break;
				}
			
			MyLogger.debug(logger, method, "testExecutionStruct :"+testExecutionStruct.toString());
			
			if (testExecutionStruct != null) {
				
				waiting_tests.add(testExecutionStruct);
				return set_alarm_or_execute(testExecutionStruct);
			}
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	private boolean add_test_to_scheduler(TestStruct testStruct) {
		final String method = "add_test_to_scheduler";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			MyLogger.debug(logger, method, "testStruct :"+testStruct.toString());
			
			TestExecutionStruct testExecutionStruct = new TestExecutionStruct(testStruct, ETestType.SCHEDULED, new MyLocation(gps_information));
			waiting_tests.add(testExecutionStruct);
			
			MyLogger.debug(logger, method, "testExecutionStruct :"+testExecutionStruct.toString());
			
			set_alarm_or_execute(testExecutionStruct);
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}
	
	private boolean set_alarm_or_execute(TestExecutionStruct testExecutionStruct) {
		final String method = "set_alarm_or_execute";
        MyLogger.trace(logger, method, "EXECUTORZ SCHEDULERZ: " + testExecutionStruct.get_test_id() + " Date: " + testExecutionStruct.get_execution_date());
		
		try {
			
			long nowTime = System.currentTimeMillis();
			long cal =  testExecutionStruct.get_execution_date().getTime() - nowTime;
			if (cal <= (limit_threshold_time_fail_in_sec * 1000)) {
			
				MyLogger.trace(logger, method, "Execute now");
				check_test_execution();
				
			} else {
				
				MyLogger.debug(logger, method, "set alarm to :"+testExecutionStruct.get_execution_date().toGMTString());
				set_alarm(testExecutionStruct.get_execution_date(), testExecutionStruct.get_test_id());
				
			}
			
			return true;
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	public synchronized void check_test_execution() {
		final String method = "check_test_execution";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			// Verifica se algum dos testes tem tasks para executar agora
			
			long nowTime = System.currentTimeMillis();
			
			List<TestExecutionStruct> tests_to_remove_from_waiting = new ArrayList<TestExecutionStruct>();
			
			for (TestExecutionStruct testExecutionStruct : waiting_tests) {
				
				long cal =  testExecutionStruct.get_execution_date().getTime() - nowTime;
				//TODO ver threshold e questao do excuta ja e agendamento
				if (cal <= (limit_threshold_time_fail_in_sec * 1000)) {
					tests_running.add(testExecutionStruct);

                    Log.d("EXECUTORZ", "Execute test: " + testExecutionStruct.get_test_id() + "onDate: " +  testExecutionStruct.get_execution_date());
					
					MyLogger.debug(logger, method, "execute test :"+testExecutionStruct.toString());
					
					RunTaskWorker runTaskWorker = new RunTaskWorker(testExecutionStruct.get_task_to_execute(), testExecutionStruct.get_test_id(), appContext, iServiceNetworksInfo, myRef, gps_information);
					runTaskWorker.startThread();

                    tests_to_remove_from_waiting.add(testExecutionStruct);

					// atualiza o estado de execução
					set_in_execution(true);
					
				}
			}
			
			for (TestExecutionStruct testExecutionStruct :tests_to_remove_from_waiting) {
				waiting_tests.remove(testExecutionStruct);
			}
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}

	@Override
	public void taskExecutionCompleted(TaskJsonResult taskJsonResult,
			String id_test) {
		final String method = "taskExecutionCompleted";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			MyLogger.debug(logger, method, "taskJsonResult :"+taskJsonResult.toString());
			
			// atribui ao teste o resultado da task e verifica se o teste foi dado como completo.
			
			for (TestExecutionStruct testExecutionStruct :tests_running) {
				
				MyLogger.debug(logger, method, "testExecutionStruct :"+testExecutionStruct.toString());
				MyLogger.debug(logger, method, "id_test :"+id_test);
				
				// adiciona o resultadado da execução da task
				if (testExecutionStruct.get_test_id().equals(id_test)) {
					
					MyLogger.trace(logger, method, "Vou adicionar os resultados ao teste!");
					testExecutionStruct.add_task_result(taskJsonResult);
					
					MyLogger.trace(logger, method, "testExecutionStruct :"+testExecutionStruct.toString());
			
					if (testExecutionStruct.more_task_to_execute()) {
					
						MyLogger.trace(logger, method, "Existe mais tarefas para executar!");
						
						long nowTime = System.currentTimeMillis();
						long cal =  testExecutionStruct.get_execution_date().getTime() - nowTime;
						
						MyLogger.debug(logger, method, "testExecutionStruct.get_execution_date().getTime() :"+testExecutionStruct.get_execution_date().getTime());
						MyLogger.debug(logger, method, "nowTime :"+nowTime);
						MyLogger.debug(logger, method, "cal :"+cal);
						
						// dou um 1segundo de delay
						if (cal > 0) {
							
							MyLogger.trace(logger, method, "Vou agendar novamente a execução da proxima tarefa!");
							
							waiting_tests.add(testExecutionStruct);
							tests_running.remove(testExecutionStruct);
							
							MyLogger.debug(logger, method, "tes_id :"+testExecutionStruct.get_test_id());
							MyLogger.debug(logger, method, "new task to execute at :"+testExecutionStruct.get_execution_date().toGMTString());
							set_alarm(testExecutionStruct.get_execution_date(), testExecutionStruct.get_test_id());
							
						} else if (Math.abs(cal) <= (limit_threshold_time_fail_in_sec * 1000)) {
							
							//corre agora a task
							MyLogger.debug(logger, method, "continua a executar a task seguinte :"+testExecutionStruct.get_task_to_execute().toString());
							RunTaskWorker runTaskWorker = new RunTaskWorker(testExecutionStruct.get_task_to_execute(), testExecutionStruct.get_test_id(), appContext, iServiceNetworksInfo, myRef, gps_information);
							runTaskWorker.startThread();
						
							
						}
						
						// reporta para o engine o resultado da execução do teste
						MyLogger.debug(logger, method, "test_execution_updated - callback!");
						iTestSchedulerReport.test_execution_updated(testExecutionStruct.get_test_id());
					
					} else {
						
						// atualiza o estado de execução
						set_in_execution(false);
					
						MyLogger.debug(logger, method, "Terminei a execução do teste :"+id_test);
						
						// remove da lista de testes em execução
						boolean result = tests_running.remove(testExecutionStruct);
						
						MyLogger.debug(logger, method, "result :"+result);
						MyLogger.debug(logger, method, "testExecutionStruct :"+testExecutionStruct.toString());
						
						TestExecutionStruct testExecutionStructClone = (TestExecutionStruct) testExecutionStruct.clone();
						
						MyLogger.debug(logger, method, "testExecutionStructClone :"+testExecutionStructClone.toString());
						
						// reporta para o engine o resultado da execução do teste
						iTestSchedulerReport.test_execution_complete(testExecutionStructClone);
						
						MyLogger.debug(logger, method, "Já reportei para o engine!");
						
						//return;
						

						// verifica se existe condição de fim de teste
						switch(testExecutionStruct.get_TestStruct().get_endevent()) {
							case DATE:
								
								// Verifica se a data definida foi utrapassada
								if (testExecutionStruct.get_TestStruct().get_datefim().getTime() < System.currentTimeMillis())
									return;
								
								break;
							case BOOT:
								// Não é aplicavel na recursividade
								break;
							case ITERATIONS:
								
								// Decremeta as iterações e ve se já não existe mais iterações para executar
								IterationsTestEvent iterationsTestEvent = (IterationsTestEvent) testExecutionStruct.get_TestStruct().get_endparam();
                                MyLogger.debug(logger, method, "BAZINGA Iterations: " + iterationsTestEvent.get_count());
                                iterationsTestEvent.decrement_count();
								testExecutionStruct.get_TestStruct().set_endparam(iterationsTestEvent);
                                MyLogger.debug(logger, method, "BAZINGA Iterations left: " + iterationsTestEvent.get_count());
								if (iterationsTestEvent.get_count() <= 0)
									return;
								
								break;
							case TIME_INTERVAL:
								// Não é aplicavel na recursividade
								break;
							case WEEK:
								// Não é aplicavel na recursividade
								break;
							case USER_REQUEST:
								// Não é aplicavel na recursividade
								
								// Verifica se a data definida foi utrapassada
								if (testExecutionStruct.get_TestStruct().get_datefim().getTime() < System.currentTimeMillis())
									return;
								
								break;
							case NONE:
								// Verifica se existe data de fim, se existir considera essa dara
								
								// Verifica se a data definida foi utrapassada
								if (testExecutionStruct.get_TestStruct().get_datefim().getTime() < System.currentTimeMillis())
									return;
								
								break;
							default:
								break;
						}
						
						MyLogger.debug(logger, method, "Não é fim de execução do teste");
						
						
						// Verifica o parametro de recursividade para voltar a correr o test se for caso disso
						if (testExecutionStruct.get_TestStruct().get_recursion() != null) {

							RecursionStruct recursionStruct = testExecutionStruct.get_TestStruct().get_recursion();
							
							switch(recursionStruct.get_event_type()) {
								case DATE:
									// Não é aplicavel na recursividade	
									break;
								case BOOT:
									// Não é aplicavel na recursividade
									break;
								case ITERATIONS:
									// Não é aplicavel na recursividade
									break;
								case TIME_INTERVAL:
									
									MyLogger.debug(logger, method, "Recursion TIME_INTERVAL");
									
									TimeIntervalTestEvent timeIntervalTestEvent = (TimeIntervalTestEvent) recursionStruct.get_test_event_params();
									
									TestStruct testStruct_TIME_INTERVAL = testExecutionStruct.get_TestStruct();

                                    testExecutionStruct.get_date_init_execution();
									
									//MyLogger.debug(logger, method, "testStruct_TIME_INTERVAL :"+testStruct_TIME_INTERVAL.toString());
									
									//Date next_execution_date = timeIntervalTestEvent.get_next_execution_date(testStruct_TIME_INTERVAL.get_dateini());
									Date next_execution_date = timeIntervalTestEvent.get_next_execution_date(testExecutionStruct.get_date_init_execution());

                                    MyLogger.debug(logger, method, "Recursion TIME_INTERVALZ last exectution date:" + testExecutionStruct.get_date_init_execution());
                                    MyLogger.debug(logger, method, "Recursion TIME_INTERVALZ next_execution_date:" + next_execution_date);
									//MyLogger.debug(logger, method, "next_execution_date :"+next_execution_date);
									
									testStruct_TIME_INTERVAL.set_dataini(next_execution_date);
									
									//MyLogger.debug(logger, method, "testStruct_TIME_INTERVAL :"+testStruct_TIME_INTERVAL.toString());
									
									add_test_to_scheduler(testStruct_TIME_INTERVAL);
									
									break;
								case WEEK:
									
									MyLogger.debug(logger, method, "Recursion WEEK");
									
									WeekTestEvent weekTestEvent = (WeekTestEvent) recursionStruct.get_test_event_params();

									TestStruct testStruct_WEEK = testExecutionStruct.get_TestStruct();
									testStruct_WEEK.set_dataini(weekTestEvent.get_next_execution_date(new Date(System.currentTimeMillis())));
									
									add_test_to_scheduler(testStruct_WEEK);
									
									break;
								case USER_REQUEST:
									// Não é aplicavel na recursividade
									break;
								case NONE:
									// Não é aplicavel na recursividade
									break;
								default:
									// Não é aplicavel na recursividade
									break;
							}
						}
						
						MyLogger.trace(logger, method, "Out after report!");

					}
					
					break;
				}
				
			}
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
			
			// atualiza o estado de execução
			set_in_execution(false);
		}
		
		MyLogger.trace(logger, method, "Out");
	}
	
	private boolean set_alarm(Date alarm_date, String alarm_label) {
		final String method = "set_alarm";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(alarm_date);
			
			myAlarmManager.setAlarm(cal, alarm_label, myRef);
			return true;
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return false;
	}

	@Override
	public void new_alarm(String alarm_id) {
		final String method = "new_alarm";
		
		try {
			
			MyLogger.trace(logger, method, "In");
			
			// NOTA: neste momento vamos verificar todos os testes em espera porque pode haver alarms que falhem
			// no futuro podemos meter a arrancar apenas o teste com o id que pretendemos
			check_test_execution();
			
		} catch (Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public List<TestExecutionStruct> get_running_tests() {
		return tests_running;
	}
	
	public List<TestExecutionStruct> get_waiting_tests() {
		return waiting_tests;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\nwaiting_tests :"+waiting_tests.toString());
			sb.append("\ntests_running :"+tests_running.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	}
}
