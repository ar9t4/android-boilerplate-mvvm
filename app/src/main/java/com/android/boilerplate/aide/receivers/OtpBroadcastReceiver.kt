package com.android.boilerplate.aide.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.boilerplate.aide.utils.Utils
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/**
 * @author Abdul Rahman
 */
class OtpBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
                intent.extras?.let { extras ->
                    try {
                        val status: Status? = if (Utils.isTiramisuPlus()) {
                            extras.getParcelable(SmsRetriever.EXTRA_STATUS, Status::class.java)
                        } else {
                            @Suppress("DEPRECATION")
                            extras.get(SmsRetriever.EXTRA_STATUS) as Status
                        }
                        status?.let {
                            when (status.statusCode) {
                                CommonStatusCodes.SUCCESS -> {
                                    val message = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
                                    message?.let {
                                        val parts = message.split(" ")
                                        val otp = parts[parts.size - 2]
                                        val localIntent = Intent("intent_otp")
                                        localIntent.putExtra("otp", otp)
                                        context?.let {
                                            LocalBroadcastManager.getInstance(context)
                                                .sendBroadcast(localIntent)
                                        }
                                    }
                                }

                                CommonStatusCodes.TIMEOUT -> {
                                    val localIntent = Intent("intent_otp")
                                    localIntent.putExtra("timeout", true)
                                    context?.let { appContext ->
                                        LocalBroadcastManager.getInstance(appContext)
                                            .sendBroadcast(localIntent)
                                    }
                                }

                                else -> {
                                }
                            }
                        }
                    } catch (exception: Exception) {
                        Log.e(TAG, exception.toString())
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = OtpBroadcastReceiver::class.java.simpleName
    }
}