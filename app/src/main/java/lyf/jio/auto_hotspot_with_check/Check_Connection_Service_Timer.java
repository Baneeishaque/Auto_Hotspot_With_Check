package lyf.jio.auto_hotspot_with_check;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.omt.remote.util.net.NetworkUtil;

import java.util.Timer;
import java.util.TimerTask;

public class Check_Connection_Service_Timer extends Service {
    // constant
//    public static final long NOTIFY_INTERVAL = 30 * 1000; // 10 seconds
    public static final long NOTIFY_INTERVAL = 15000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }

        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }



    public void check_connection() {
        if(NetworkUtil.isConnectedToNetwork(getBaseContext()))
        {
            Toast.makeText(getBaseContext(),"Internet Success",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getBaseContext(),"Internet Failure",Toast.LENGTH_SHORT).show();
        }


    }


    class TimeDisplayTimerTask extends TimerTask {

        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    check_connection();
                }

            });
        }


    }
}

	