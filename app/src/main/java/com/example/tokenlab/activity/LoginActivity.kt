package com.example.tokenlab.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.tokenlab.R
import com.example.tokenlab.constants.Constants
import com.example.tokenlab.domains.EmailStatus
import com.example.tokenlab.domains.User
import com.example.tokenlab.extensions.createLoadingDialog
import com.example.tokenlab.extensions.showErrorDialog
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private var loginEmailTextInputLayout: TextInputLayout? = null
    private var loginPasswordTextInputLayout: TextInputLayout? = null
    private var loginEmailEditText: EditText? = null
    private var loginPasswordEditText: EditText? = null
    private var loginButton: Button? = null
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewsById()
        loadingDialog = this.createLoadingDialog()
        setupLoginButton()
    }

    private fun setupLoginButton() {
        loginButton?.setOnClickListener {
            val user = createUser()
            val isValidUserEmail = user.isValidEmail()
            val isValidUserPassword = user.isValidPassword()
            treatInvalidUserEmail(isValidUserEmail)
            treatInvalidUserPassword(isValidUserPassword)
            if (!isValidUserPassword || isValidUserEmail != EmailStatus.VALID) return@setOnClickListener
            loadingDialog?.show()
            doLogin(user)
        }
    }

    private fun doLogin(user: User) {
        loadingDialog?.dismiss()
        if (user.email == Constants.AUTHORIZED_USER_EMAIL && user.password == Constants.AUTHORIZED_USER_PASSWORD) {
            moveToMainActivity()
        } else if (user.email != Constants.AUTHORIZED_USER_EMAIL || user.password != Constants.AUTHORIZED_USER_PASSWORD) {
            treatUnauthorizedUserEmailOrPassword()
        } else {
            this.showErrorDialog(getString(R.string.occurred_error))
        }
    }

    private fun treatUnauthorizedUserEmailOrPassword() {
        loginEmailTextInputLayout?.error = Constants.BLANK_SPACE
        loginPasswordTextInputLayout?.error =
            getString(R.string.login_error_email_password)
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun treatInvalidUserPassword(validUserPassword: Boolean) {
        if (validUserPassword) {
            loginPasswordTextInputLayout?.error = Constants.EMPTY
        } else {
            loginPasswordTextInputLayout?.error = getString(R.string.fill_the_field)
        }
    }

    private fun treatInvalidUserEmail(validUserEmail: EmailStatus) {
        when (validUserEmail) {
            EmailStatus.VALID -> {
                loginEmailTextInputLayout?.error = Constants.EMPTY
            }
            EmailStatus.EMPTY -> {
                loginEmailTextInputLayout?.error = getString(R.string.fill_the_field)
            }
            else -> {
                loginEmailTextInputLayout?.error = getString(R.string.login_incorrect_email)
            }
        }
    }

    private fun createUser() = User(
        loginEmailEditText?.text?.toString().orEmpty(),
        loginPasswordEditText?.text?.toString().orEmpty()
    )

    private fun findViewsById() {
        loginEmailTextInputLayout = findViewById(R.id.loginEmailTextInputLayout)
        loginPasswordTextInputLayout = findViewById(R.id.loginPasswordTextInputLayout)
        loginEmailEditText = findViewById(R.id.loginEmailEditText)
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText)
        loginButton = findViewById(R.id.loginButton)
    }
}