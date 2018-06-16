package lyf.jio.auto_hotspot_with_check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Banee-Ishaque-K on 02-12-2016.
 */
public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(),"Starting Service",Toast.LENGTH_SHORT).show();
        startService(new Intent(getBaseContext(), Check_Connection_Service_Timer.class));

    }
}
