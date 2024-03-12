/*
 * Copyright 2016 McXross. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.mcxross.zero.internal

import android.util.Log
import androidx.annotation.VisibleForTesting

/**
 * Convenience wrapper around [android.util.Log], which evaluates the current log level of the
 * logging tag once and uses this to determine whether logging should proceed. This minimizes the
 * number of native calls made as part of logging.
 */
object Logger {

  @VisibleForTesting const val LOG_TAG = "Zero"

  @VisibleForTesting @Volatile var sInstance: Logger? = null

  private val mLog: LogWrapper = AndroidLogWrapper.INSTANCE

  private val mLogLevel: Int

  init {
    /* // determine the active logging level
    var level = Log.ASSERT
    while (level >= Log.VERBOSE && mLog.isLoggable(LOG_TAG, level)) {
        level--
    }*/
    // TODO: Fix this
    mLogLevel = Log.DEBUG
  }

  @VisibleForTesting
  @Synchronized
  fun setInstance(logger: Logger) {
    sInstance = logger
  }

  fun verbose(message: String, vararg messageParams: Any) {
    log(Log.VERBOSE, null, message, *messageParams)
  }

  fun verboseWithStack(tr: Throwable, message: String, vararg messageParams: Any) {
    log(Log.VERBOSE, tr, message, *messageParams)
  }

  fun debug(message: String, vararg messageParams: Any) {
    log(Log.DEBUG, null, message, *messageParams)
  }

  fun debugWithStack(tr: Throwable, message: String, vararg messageParams: Any) {
    log(Log.DEBUG, tr, message, *messageParams)
  }

  fun info(message: String, vararg messageParams: Any) {
    log(Log.INFO, null, message, *messageParams)
  }

  fun infoWithStack(tr: Throwable, message: String, vararg messageParams: Any) {
    log(Log.INFO, tr, message, *messageParams)
  }

  fun warn(message: String, vararg messageParams: Any?) {
    log(Log.WARN, null, message, *messageParams)
  }

  fun warnWithStack(tr: Throwable, message: String, vararg messageParams: Any) {
    log(Log.WARN, tr, message, *messageParams)
  }

  fun error(message: String, vararg messageParams: Any) {
    log(Log.ERROR, null, message, *messageParams)
  }

  fun errorWithStack(tr: Throwable, message: String, vararg messageParams: Any) {
    log(Log.ERROR, tr, message, *messageParams)
  }

  private fun log(level: Int, tr: Throwable?, message: String, vararg messageParams: Any?) {
    if (mLogLevel > level) {
      return
    }
    var formattedMessage =
        if (messageParams.isEmpty()) {
          message
        } else {
          String.format(message, *messageParams)
        }

    if (tr != null) {
      formattedMessage += "\n" + mLog.getStackTraceString(tr)
    }

    mLog.println(level, LOG_TAG, formattedMessage)
  }

  /**
   * The core interface of [android.util.Log], converted into instance methods so as to allow easier
   * mock testing.
   */
  @VisibleForTesting
  interface LogWrapper {
    fun println(level: Int, tag: String, message: String)

    fun isLoggable(tag: String, level: Int): Boolean

    fun getStackTraceString(tr: Throwable): String
  }

  /** Default [LogWrapper] implementation, using [android.util.Log] static methods. */
  private class AndroidLogWrapper private constructor() : LogWrapper {
    override fun println(level: Int, tag: String, message: String) {
      Log.println(level, tag, message)
    }

    override fun isLoggable(tag: String, level: Int): Boolean {
      return Log.isLoggable(tag, level)
    }

    override fun getStackTraceString(tr: Throwable): String {
      return Log.getStackTraceString(tr)
    }

    companion object {
      val INSTANCE = AndroidLogWrapper()
    }
  }
}
