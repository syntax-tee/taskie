

package com.app.taiye.taskie.app.ui.register

import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.app.taiye.taskie.R
import com.app.taiye.taskie.app.utils.gone
import com.app.taiye.taskie.app.utils.toast
import com.app.taiye.taskie.app.utils.visible
import com.app.taiye.taskie.app.model.request.UserDataRequest
import com.app.taiye.taskie.app.networking.NetworkStatusChecker
import com.app.taiye.taskie.app.networking.RemoteApi
import kotlinx.android.synthetic.main.activity_register.*

/**
 * Displays the Register screen, with the options to register, or head over to Login!
 */
@RequiresApi(Build.VERSION_CODES.M)
class RegisterActivity : AppCompatActivity() {

  private val remoteApi = RemoteApi()

  private val networkStatusChecker by lazy{
    NetworkStatusChecker(getSystemService(ConnectivityManager::class.java))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
    initUi()
  }

  private fun initUi() {
    register.setOnClickListener {
      processData(nameInput.text.toString(), emailInput.text.toString(),
          passwordInput.text.toString())
    }
  }

  private fun processData(username: String, email: String, password: String) {
    networkStatusChecker.performIfConnectedToInternet {
      if (username.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
        remoteApi.registerUser(UserDataRequest(email, password, username)) { message, error ->
          if (message != null) {
            toast(message)
            onRegisterSuccess()
          } else if (error != null) {
            onRegisterError()
          }
        }
      } else {
        onRegisterError()
      }
    }
  }

  private fun onRegisterSuccess() {
    errorText.gone()
    finish()
  }

  private fun onRegisterError() {
    errorText.visible()
  }
}