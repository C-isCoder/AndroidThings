package com.android.things;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 *
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class Main2Activity extends Activity {
  private static final String RED_LED_PIN_NAME = "BCM14";
  private static final String Green_LED_PIN_NAME = "BCM21";
  private static final String YELLOW_LED_PIN_NAME = "BCM26";
  private Gpio mRedLedGpio;
  private Gpio mGreenGpio;
  private Gpio mYellowLedGpio;

  private Handler mHandler = new Handler();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init();
  }

  private void init() {
    PeripheralManagerService service = new PeripheralManagerService();
    try {
      mRedLedGpio = service.openGpio(RED_LED_PIN_NAME);
      mGreenGpio = service.openGpio(Green_LED_PIN_NAME);
      mYellowLedGpio = service.openGpio(YELLOW_LED_PIN_NAME);

      mRedLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
      mGreenGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
      mYellowLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

      mRedLedGpio.setValue(true);
      mHandler.postDelayed(mRedRunnable, 10 * 1000);
    } catch (IOException e) {
      Log.e(TAG, "Error on PeripheralIO API", e);
    }
  }

  private Runnable mRedRunnable = new Runnable() {
    @Override public void run() {
      try {
        mRedLedGpio.setValue(false);
        mYellowLedGpio.setValue(false);
        mGreenGpio.setValue(true);
        mHandler.postDelayed(mGreenRunnable, 15 * 1000);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  };
  private Runnable mGreenRunnable = new Runnable() {
    @Override public void run() {
      try {
        mRedLedGpio.setValue(false);
        mGreenGpio.setValue(false);
        mYellowLedGpio.setValue(true);
        mHandler.postDelayed(mYellowRunnable, 2 * 1000);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  };
  private Runnable mYellowRunnable = new Runnable() {
    @Override public void run() {
      try {
        mRedLedGpio.setValue(false);
        mGreenGpio.setValue(false);
        mYellowLedGpio.setValue(!mYellowLedGpio.getValue());
        mHandler.postDelayed(mYellowRunnable, 1000);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  };

  @Override

  protected void onDestroy() {
    try {
      if (mRedLedGpio != null) {
        mRedLedGpio.close();
      }
      if (mGreenGpio != null) {
        mGreenGpio.close();
      }
      if (mYellowLedGpio != null) {
        mYellowLedGpio.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    super.onDestroy();
  }
}
