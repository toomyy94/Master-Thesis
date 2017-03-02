package pt.ptinovacao.arqospocket.service.tasks.speedTestFacebook;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Class used to read from TrafficStats periodically, in order to determine a ConnectionClass.
 */
public class DeviceBandwidthSampler {
	
	private final static String className = DeviceBandwidthSampler.class.getSimpleName();

  /**
   * The DownloadBandwidthManager that keeps track of the moving average and ConnectionClass.
   */
  private final ConnectionClassManager mConnectionClassManager;

  private AtomicInteger mSamplingCounter;

  private SamplingHandler mHandler;
  private HandlerThread mThread;

  private long mLastTimeReading;
  private static long sPreviousBytes = -1;
  
  private TrafficType trafficType = TrafficType.Download;

  // Singleton.
  private static class DeviceBandwidthSamplerHolder {
      public static final DeviceBandwidthSampler instance =
              new DeviceBandwidthSampler(ConnectionClassManager.getInstance(TrafficType.Download), TrafficType.Download);
  }
  
  public double getCurrentBandwidth() {
	  return mConnectionClassManager.getCurrentBandwidth();
  }

  /**
   * Retrieval method for the DeviceBandwidthSampler singleton.
   * @return The singleton instance of DeviceBandwidthSampler.
   */
  public static DeviceBandwidthSampler getInstance(TrafficType trafficType) {
    //return DeviceBandwidthSamplerHolder.instance;
	  return new DeviceBandwidthSampler(ConnectionClassManager.getInstance(trafficType), trafficType);
  }

  private DeviceBandwidthSampler(ConnectionClassManager connectionClassManager, TrafficType trafficType) {
	  
	  this.trafficType = trafficType;
    mConnectionClassManager = connectionClassManager;
    mSamplingCounter = new AtomicInteger();
    mThread = new HandlerThread("ParseThread");
    mThread.start();
    mHandler = new SamplingHandler(mThread.getLooper());
  }

  /**
   * Method call to start sampling for download bandwidth.
   */
  public void startSampling() {
    if (mSamplingCounter.getAndIncrement() == 0) {
      mHandler.startSamplingThread();
      mLastTimeReading = SystemClock.elapsedRealtime();
    }
    
    if (mConnectionClassManager != null)
    	mConnectionClassManager.startSampling();
  }
  
  public double getAccessTime() {
	  return mConnectionClassManager.getAccessTime();
  }

  /**
   * Finish sampling and prevent further changes to the
   * ConnectionClass until another timer is started.
   */
  public void stopSampling() {
    if (mSamplingCounter.decrementAndGet() == 0) {
      mHandler.stopSamplingThread();
      addFinalSample();
    }
  }

  /**
   * Method for polling for the change in total bytes since last update and
   * adding it to the BandwidthManager.
   */
  protected void addSample() {
	  final String method = "addSample";
	  
    long newBytes = 0;
    
    if (trafficType == TrafficType.Download)
    	newBytes = TrafficStats.getTotalRxBytes();
    else
    	newBytes = TrafficStats.getTotalTxBytes();
    	
    long byteDiff = newBytes - sPreviousBytes;
    
    if (sPreviousBytes >= 0) {
      synchronized (this) {
        long curTimeReading = SystemClock.elapsedRealtime();
        mConnectionClassManager.addBandwidth(byteDiff, curTimeReading - mLastTimeReading);

        mLastTimeReading = curTimeReading;
      }
    }
    sPreviousBytes = newBytes;
  }

  /**
   * Resets previously read byte count after recording a sample, so that
   * we don't count bytes downloaded in between sampling sessions.
   */
  protected void addFinalSample() {
    addSample();
    sPreviousBytes = -1;
  }

  /**
   * @return True if there are still threads which are sampling, false otherwise.
   */
  public boolean isSampling() {
    return (mSamplingCounter.get() != 0);
  }

  private class SamplingHandler extends Handler {
      /**
       * Time between polls in ms.
       */
      static final long SAMPLE_TIME = 1000;

      static private final int MSG_START = 1;

      public SamplingHandler(Looper looper) {
          super(looper);
      }

      @Override
      public void handleMessage(Message msg) {
          switch (msg.what) {
              case MSG_START:
                  addSample();
                  sendEmptyMessageDelayed(MSG_START, SAMPLE_TIME);
                  break;
              default:
                  throw new IllegalArgumentException("Unknown what=" + msg.what);
          }
      }


      public void startSamplingThread() {
          sendEmptyMessage(SamplingHandler.MSG_START);
      }

      public void stopSamplingThread() {
          removeMessages(SamplingHandler.MSG_START);
      }
  }
}
