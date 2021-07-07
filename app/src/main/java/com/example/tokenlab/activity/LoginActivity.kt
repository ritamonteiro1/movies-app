package com.example.tokenlab.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.tokenlab.R
import com.example.tokenlab.domains.EmailStatus
import com.example.tokenlab.domains.User
import com.example.tokenlab.extensions.createLoadingDialog
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

        }
    }

    private fun treatInvalidUserPassword(validUserPassword: Boolean) {
        TODO("Not yet implemented")
    }

    private fun treatInvalidUserEmail(validUserEmail: EmailStatus) {
        TODO("Not yet implemented")
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