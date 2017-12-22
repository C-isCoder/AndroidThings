package com.android.things

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService
import java.io.IOException

private val TAG = MainActivity::class.java.simpleName
private val INTERVAL_BETWEEN_BLINKS_MS = 100L
private val RED_LED_PIN_NAME = "BCM21"
private val BLUE_LED_PIN_NAME = "BCM4"
private val WHITE_LED_PIN_NAME = "BCM27"
private var mRedLedGpio: Gpio? = null
private var mBuleLedGpio: Gpio? = null
private var mWhiteLedGpio: Gpio? = null
private var mHandler: Handler = Handler()

class MainActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Step 1. Create GPIO connection.
    val service = PeripheralManagerService()
    try {
      mRedLedGpio = service.openGpio(RED_LED_PIN_NAME)
      mBuleLedGpio = service.openGpio(BLUE_LED_PIN_NAME)
      mWhiteLedGpio = service.openGpio(WHITE_LED_PIN_NAME)
      // Step 2. Configure as an output.
      mRedLedGpio!!.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
      mBuleLedGpio!!.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
      mWhiteLedGpio!!.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

      // Step 4. Repeat using a handler.
      mHandler.post(mBlinkRunnable)
    } catch (e: IOException) {
      Log.e(TAG, "Error on PeripheralIO API", e)
    }

  }

  private val mBlinkRunnable = object : Runnable {
    override fun run() {
      // Exit if the GPIO is already closed
      if (mRedLedGpio == null || mBuleLedGpio == null || mWhiteLedGpio == null) {
        return
      }

      try {
        // Step 3. Toggle the LED state
        mRedLedGpio!!.value = !mRedLedGpio!!.value
        mBuleLedGpio!!.value = !mBuleLedGpio!!.value
        mWhiteLedGpio!!.value = !mWhiteLedGpio!!.value

        // Step 4. Schedule another event after delay.
        mHandler.postDelayed(this, INTERVAL_BETWEEN_BLINKS_MS)
      } catch (e: IOException) {
        Log.e(TAG, "Error on PeripheralIO API", e)
      }

    }
  }

  override fun onDestroy() {
    super.onDestroy()

    // Step 4. Remove handler events on close.
    mHandler.removeCallbacks(mBlinkRunnable)
    // Step 5. Close the resource.
    try {
      mRedLedGpio!!.close()
      mBuleLedGpio!!.close()
      mWhiteLedGpio!!.close()
    } catch (e: IOException) {
      Log.e(TAG, "Error on PeripheralIO API", e)
    }
  }

}
