package com.example.androidapplicationbradyminer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper applicationDatabase;

    private EditText usernameInput;
    private EditText passwordInput;

    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();

        applicationDatabase = DatabaseHelper.getInstance(this);
    }

    private void initializeViews() {
        usernameInput = findViewById(R.id.editTextUsername);
        passwordInput = findViewById(R.id.editTextPassword);

        loginBtn = findViewById(R.id.buttonLogin);
        registerBtn = findViewById(R.id.buttonCreateAccount);

        // Disable both buttons by default
        loginBtn.setEnabled(false);
        registerBtn.setEnabled(false);

        // Listen to any text changes on these fields
        usernameInput.addTextChangedListener(textWatcher);
        passwordInput.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            enableButtons(!getUsername().isEmpty() && !getPassword().isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void enableButtons(boolean enable) {
        loginBtn.setEnabled(enable);
        registerBtn.setEnabled(enable);
    }

    public void login(View view) {
        if (!validCredentials()) {
            showError(getString(R.string.failed_login));
            return;
        }

        boolean isLoggedIn = applicationDatabase.checkUser(getUsername(), getPassword());
        handleLoginResult(isLoggedIn, getString(R.string.failed_login));
    }

    public void register(View view) {
        if (!validCredentials()) {
            showError(getString(R.string.failed_registration));
            return;
        }

        boolean userCreated = applicationDatabase.addUser(getUsername(), getPassword());
        handleLoginResult(userCreated, getString(R.string.failed_registration));
    }

    private void handleLoginResult(boolean success, String errorMessage) {
        if (success) {
            navigateToMainActivity();
        } else {
            showError(errorMessage);
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private boolean validCredentials() {
        return !getUsername().isEmpty() && !getPassword().isEmpty();
    }

    private String getUsername() {
        return usernameInput.getText().toString().trim().toLowerCase();
    }

    private String getPassword() {
        return passwordInput.getText().toString().trim();
    }

    private void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
