package PT.PTInov.ArQoSPocket.Utils;

import PT.PTInov.ArQoSPocket.Enums.TestEnum;

public class TestEnumToInt {
	
	public static int TestEnumToInt(TestEnum testEnum) {
		
		switch (testEnum) {
			case RadioMeasurements:
				return 1;
			case AccessTest:
				return 2;
			case BandwidthTest:
				return 3;
			case FTPTest:
				return 4;
			case SendInformation:
				return 5;
		}
		
		return -1;
	}
	
	public static TestEnum intToTestEnum(int testEnumInt) {
		
		switch (testEnumInt) {
			case 1:
				return TestEnum.RadioMeasurements;
			case 2:
				return TestEnum.AccessTest;
			case 3:
				return TestEnum.BandwidthTest;
			case 4:
				return TestEnum.FTPTest;
			case 5:
				return TestEnum.SendInformation;
		}
		
		return TestEnum.NA;
	}
}
