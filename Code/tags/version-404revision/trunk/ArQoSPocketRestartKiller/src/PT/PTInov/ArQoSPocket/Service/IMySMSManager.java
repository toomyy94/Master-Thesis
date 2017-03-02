package PT.PTInov.ArQoSPocket.Service;

import PT.PTInov.ArQoSPocket.Enums.ESMSState;

/**
 * Created by 10057273 on 01-11-2013.
 */
public interface IMySMSManager {
    public void report_sms_callback(ESMSState status);
}
