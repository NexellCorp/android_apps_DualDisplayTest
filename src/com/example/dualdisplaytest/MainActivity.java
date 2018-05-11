package com.example.dualdisplaytest;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.hardware.display.DisplayManager;
import android.util.DisplayMetrics;
import android.view.Display;

import android.util.Log;

public class MainActivity extends Activity {

    static final String TAG = "DualDisplayTest";
    static final String checkStatusFile = "/sys/devices/platform/c0000000.soc/c0101000.display_drm_tv/enable";
    static final String controlTVOutFile = "/sys/devices/platform/c0000000.soc/c0101000.display_drm_tv/enable";
    static final String controlPALFile = "/sys/devices/platform/c0000000.soc/c0101000.display_drm_tv/type";

    private DemoPresentation mDemoPresentation = null;
	private boolean mFirstEnable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);

        if (getTVOutStatus())
            checkBox.setChecked(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkBox1:
                if (checked) {
                    enableTVOut(true);
                } else {
                    enableTVOut(false);
                }
                break;

            case R.id.checkBox2:
                showTVOut(checked);
                break;

			case R.id.checkBox3:
				if (checked) {
					enablePALMode(true);
				} else {
					enablePALMode(false);
				}
        }
    }

    private boolean getTVOutStatus() {
        BufferedReader br = null;
        try {
            StringBuffer output = new StringBuffer();
            br = new BufferedReader(new FileReader(checkStatusFile));
            String line = "";
            line = br.readLine();
            Log.d(TAG, "read val: " + line);
            br.close();
            if ("1".equals(line)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void enableTVOut(boolean enable) {
        String out = "";
        if (enable)
            out = "1";
        else
            out = "0";

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(controlTVOutFile));
            bw.write(out);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void enablePALMode(boolean enable) {
        String out = "";
        if (enable)
            out = "PAL-BGHI";
        else
            out = "NTSC-M";

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(controlPALFile));
            bw.write(out);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    private void showTVOut(boolean enable) {
        if (enable) {
            DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
            Display[] presentationDisplays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            Log.d(TAG, "available display count: " + presentationDisplays.length);

            if (presentationDisplays.length > 0) {
                Display display = presentationDisplays[0];

                DemoPresentation presentation = new DemoPresentation(this, display);
                presentation.show();

                mDemoPresentation = presentation;
            }
        }  else {
            if (mDemoPresentation != null) {
                mDemoPresentation.dismiss();
                mDemoPresentation = null;
            }
        }
    }
}
