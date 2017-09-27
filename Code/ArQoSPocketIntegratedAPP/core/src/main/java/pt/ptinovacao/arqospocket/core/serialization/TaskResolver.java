package pt.ptinovacao.arqospocket.core.serialization;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import pt.ptinovacao.arqospocket.core.serialization.resolvers.AnswerCallTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.AssociateWifiTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.DisassociateWifiTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.HangUpCallTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.HttpDownloadTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.HttpUploadTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.InstanceResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.MakeCallTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.PingTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.ReAssociateWifiTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.ReceiveSmsTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.RecordAudioTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.ScanWifiNetworkTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.SendMailTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.SendMmsTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.SendSmsTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.SubInstanceResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.TurnOffMobileTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.TurnOffWifiTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.TurnOnMobileTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.TurnOnWifiTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.WifiAuthLoginTaskResolver;
import pt.ptinovacao.arqospocket.core.serialization.resolvers.WifiAuthLogoutTaskResolver;
import pt.ptinovacao.arqospocket.core.tests.BaseExecutableTask;
import pt.ptinovacao.arqospocket.core.tests.BaseTaskExecutionResult;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Helper task to resolve a task instance from a task type.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class TaskResolver {

    private static final Map<String, InstanceResolver> RESOLVERS = new ImmutableMap.Builder<String, InstanceResolver>()
            .put(TaskType.ASSOCIATE_WIFI_TASK, new AssociateWifiTaskResolver())
            .put(TaskType.DISASSOCIATE_WIFI_TASK, new DisassociateWifiTaskResolver())
            .put(TaskType.HTTP_DOWNLOAD_TASK, new HttpDownloadTaskResolver())
            .put(TaskType.HTTP_UPLOAD_TASK, new HttpUploadTaskResolver())
            .put(TaskType.PING_TASK, new PingTaskResolver())
            .put(TaskType.RE_ASSOCIATE_WIFI_TASK, new ReAssociateWifiTaskResolver())
            .put(TaskType.SCAN_WIFI_NETWORK_TASK, new ScanWifiNetworkTaskResolver())
            .put(TaskType.TURN_OFF_WIFI_TASK, new TurnOffWifiTaskResolver())
            .put(TaskType.TURN_ON_WIFI_TASK, new TurnOnWifiTaskResolver())
            .put(TaskType.WIFI_AUTH_LOGIN_TASK, new WifiAuthLoginTaskResolver())
            .put(TaskType.WIFI_AUTH_LOGOUT_TASK, new WifiAuthLogoutTaskResolver())
            .put(TaskType.TURN_OFF_MOBILE_TASK, new TurnOffMobileTaskResolver())
            .put(TaskType.TURN_ON_MOBILE_TASK, new TurnOnMobileTaskResolver())
            .put(TaskType.MAKE_CALL_TASK, new MakeCallTaskResolver())
            .put(TaskType.ANSWER_CALL_TASK, new AnswerCallTaskResolver())
            .put(TaskType.HANG_UP_CALL_TASK, new HangUpCallTaskResolver())
            .put(TaskType.RECORD_AUDIO_TASK, new RecordAudioTaskResolver(TaskType.RECORD_AUDIO_TASK, "Record Audio"))
            .put(TaskType.PESQ_DST_TASK, new RecordAudioTaskResolver(TaskType.PESQ_DST_TASK, "PESQ Destination"))
            .put(TaskType.SEND_SMS_TASK, new SendSmsTaskResolver())
            .put(TaskType.RECEIVE_SMS_TASK, new ReceiveSmsTaskResolver())
            .put(TaskType.SEND_MMS_TASK, new SendMmsTaskResolver())
            .put(TaskType.SEND_MAIL_TASK, new SendMailTaskResolver()).build();

    /**
     * Tries to get an instance of a {@link TaskData} descendant for the provided type.
     *
     * @param type the type of task type to obtain.
     * @return the instance of the resolved {@link TaskData}.
     * @throws UnsupportedOperationException if the provided type does not match any supported task.
     */
    static TaskData taskDataForType(String type) {
        return taskDataForType(type, null);
    }

    /**
     * Tries to get an instance of a {@link TaskData} descendant for the provided type and sub type.
     *
     * @param type the type of task type to obtain.
     * @param subType the su type of the task to obtain.
     * @return the instance of the resolved {@link TaskData}.
     * @throws UnsupportedOperationException if the provided type does not match any supported task.
     */
    static TaskData taskDataForType(String type, String subType) {
        return resolverForType(type, subType).task();
    }

    /**
     * Tries to get an instance of a {@link BaseExecutableTask} descendant for the provided {@link TaskData}.
     *
     * @param data the {@link TaskData} with the required {@link ResolverInfo}.
     * @return the instance of the resolved {@link BaseExecutableTask}.
     */
    public static BaseExecutableTask executableTaskForTaskData(TaskData data) {
        ResolverInfo resolverInfo = data.getResolverInfo();
        return resolverForType(resolverInfo.getType(), resolverInfo.getSubType()).executableTask(data);
    }

    /**
     * Tries to get an instance of a {@link TaskResult} descendant for the provided type.
     *
     * @param type the type of task type to obtain.
     * @return the instance of the resolved {@link TaskResult}.
     * @throws UnsupportedOperationException if the provided type does not match any supported result.
     */
    static TaskResult taskResultForType(String type) {
        return taskResultForType(type, null);
    }

    /**
     * Tries to get an instance of a {@link TaskResult} descendant for the provided type and sub type.
     *
     * @param type the type of task type to obtain.
     * @param subType the su type of the task to obtain.
     * @return the instance of the resolved {@link TaskResult}.
     * @throws UnsupportedOperationException if the provided type does not match any supported result.
     */
    static TaskResult taskResultForType(String type, String subType) {
        return resolverForType(type, subType).result();
    }

    /**
     * Tries to get an instance of a {@link TaskResult} descendant for the provided {@link TaskData}.
     *
     * @param data the {@link TaskData} for the corresponding {@link TaskResult}.
     * @return the instance of the resolved {@link TaskResult}.
     * @throws UnsupportedOperationException if the provided type does not match any supported result.
     */
    public static TaskResult taskResultForData(TaskData data) {
        ResolverInfo resolverInfo = data.getResolverInfo();
        return resolverForType(resolverInfo.getType(), resolverInfo.getSubType()).result();
    }

    /**
     * Tries to get an instance of a {@link BaseTaskExecutionResult} descendant for the provided {@link TaskResult}.
     *
     * @param result the {@link TaskResult} with the required {@link ResolverInfo}.
     * @return the instance of the resolved {@link BaseTaskExecutionResult}.
     */
    public static BaseTaskExecutionResult executionResultForTaskResult(TaskResult result) {
        ResolverInfo resolverInfo = result.getResolverInfo();
        return resolverForType(resolverInfo.getType(), resolverInfo.getSubType()).executionResult(result);
    }

    private static InstanceResolver resolverForType(String type, String subType) {
        InstanceResolver resolver = RESOLVERS.get(type);
        if (resolver == null) {
            throw new UnsupportedOperationException("Task type " + type + " is not supported or it's not registered.");
        }

        if (Strings.emptyToNull(subType) != null && resolver instanceof SubInstanceResolver) {
            return ((SubInstanceResolver) resolver).resolver(subType);
        }

        return resolver;
    }
}
