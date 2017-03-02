package PT.PTInov.ArQoSPocket.UI;

import java.util.Date;

import PT.PTInov.ArQoSPocket.Enums.TestEnum;

public interface TestResultCallback {
	public void testResultCallBack(Boolean result, Date testExecDate );
	public void reportTestState(TestEnum state);
}
