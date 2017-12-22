package com.android.things

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManagerService
import java.io.IOException

private val TAG = MainActivity::class.java.simpleName
private val RED_LED_PIN_NAME = "BCM14"
private val BLUE_LED_PIN_NAME = "BCM26"
private val WHITE_LED_PIN_NAME = "BCM21"
private var mRedLedGpio: Gpio? = null
private var mBuleLedGpio: Gpio? = null
private var mWhiteLedGpio: Gpio? = null

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
      mRedLedGpio!!.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
      mBuleLedGpio!!.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
      mWhiteLedGpio!!.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
      Thread(Runnable {
        while (true) {

        }
      }).start()

    } catch (e: IOException) {
      Log.e(TAG, "Error on PeripheralIO API", e)
    }

  }

  override fun onDestroy() {
    super.onDestroy()
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
