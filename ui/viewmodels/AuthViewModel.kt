package com.example.shopcart.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val auth0: Auth0) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail = _userEmail.asStateFlow()

    fun login(context: Context) {
        WebAuthProvider.login(auth0)
            .withScheme("demo") // Cambiado a "demo" para coincidir con el panel
            .withScope("openid profile email")
            .start(context, object : Callback<Credentials, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    // Manejar error
                }

                override fun onSuccess(result: Credentials) {
                    _isAuthenticated.value = true
                    _userEmail.value = result.user.email
                }
            })
    }

    fun logout(context: Context) {
        WebAuthProvider.logout(auth0)
            .withScheme("demo") // Cambiado a "demo"
            .start(context, object : Callback<Void?, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    // Manejar error
                }

                override fun onSuccess(result: Void?) {
                    _isAuthenticated.value = false
                    _userEmail.value = null
                }
            })
    }
}