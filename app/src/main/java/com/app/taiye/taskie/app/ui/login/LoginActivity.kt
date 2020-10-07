package com.app.taiye.taskie.app.ui.login

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.app.taiye.taskie.R
import com.app.taiye.taskie.app.App
import com.app.taiye.taskie.app.model.Success
import com.app.taiye.taskie.app.model.request.UserDataRequest
import com.app.taiye.taskie.app.networking.NetworkStatusChecker
import com.app.taiye.taskie.app.ui.main.MainActivity
import com.app.taiye.taskie.app.ui.register.RegisterActivity
import com.app.taiye.taskie.app.utils.gone
import com.app.taiye.taskie.app.utils.visible
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Displays the Login screen, with the options to head over to the Register screen.
 */
@RequiresApi(Build.VERSION_CODES.M)
class LoginActivity : AppCompatActivity() {

  private val remoteApi = App.remoteApi

  private val networkStatusChecker by lazy{
    NetworkStatusChecker(getSystemService(ConnectivityManager::class.java))
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
      initUi()
      if(App.getToken().isNotBlank()){
        startActivity(MainActivity.getIntent(this))
      }
  }

  private fun initUi() {
    login.setOnClickListener {
      val email = emailInput.text.toString()
      val password = passwordInput.text.toString()

      if (email.isNotBlank() && password.isNotBlank()) {
        logUserIn(UserDataRequest(email, password))
      } else {
        showLoginError()
      }
    }
    register.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
  }

  private fun logUserIn(userDataRequest: UserDataRequest) {
    networkStatusChecker.performIfConnectedToInternet {
      remoteApi.loginUser(userDataRequest) { result ->
        if (result is Success) {
            onLoginSuccess(result.data)
          } else {
            showLoginError()
          }
      }
    }
  }

  private fun onLoginSuccess(token: String) {
    errorText.gone()
    App.saveToken(token)
    startActivity(MainActivity.getIntent(this))
  }

  private fun showLoginError() {
    errorText.visible()
  }
}