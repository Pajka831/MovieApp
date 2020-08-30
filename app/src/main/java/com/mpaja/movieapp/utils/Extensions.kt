package com.mpaja.movieapp.utils

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mpaja.movieapp.data.api.Tls12SocketFactory
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import javax.net.ssl.SSLContext


inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProvider(this, provider).get(VM::class.java)

inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    provider: ViewModelProvider.Factory
) =
    ViewModelProvider(this, provider).get(VM::class.java)


fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
    if (Build.VERSION.SDK_INT < 22) {
        try {
            val sc: SSLContext = SSLContext.getInstance("TLSv1.2")
            sc.init(null, null, null)
            client.sslSocketFactory(
                Tls12SocketFactory(
                    sc.socketFactory
                )
            )
            val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build()
            val specs: MutableList<ConnectionSpec> = ArrayList()
            specs.add(cs)
            specs.add(ConnectionSpec.COMPATIBLE_TLS)
            specs.add(ConnectionSpec.CLEARTEXT)
            client.connectionSpecs(specs)
        } catch (exc: Exception) {
            Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
        }
    }
    return client
}

fun Fragment.showErrorDialog(
    messageText: Int,
    listener: DialogInterface.OnClickListener? = null,
    buttonText: Int,
    cancelable: Boolean = false
) {
    AlertDialog.Builder(requireContext())
        .setMessage(messageText)
        .setPositiveButton(buttonText, listener)
        .setCancelable(cancelable)
        .show()

}
