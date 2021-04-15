package com.dhc.app.ui.registration;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dhc.app.BaseActivity;
import com.dhc.app.R;
import com.dhc.app.data.Error;
import com.dhc.app.data.ResponseCallback;
import com.dhc.app.ui.home.HomeActivity;
import com.dhc.app.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrationActivity extends BaseActivity {
    EditText etUserName;
    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    Button button;
    String customerCode;
    String cardType;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setProgressBar(RegistrationActivity.this);
        Intent intent = getIntent();
        customerCode = intent.getStringExtra("customerCode");
        cardType = intent.getStringExtra("cardType");
        etUserName = findViewById(R.id.et_username);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        button = findViewById(R.id.btn_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegistrationSubmit(v);
            }
        });
    }
    public void onRegistrationSubmit(View view) {
        String userName = etUserName.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        boolean isValid = validateUserData(userName, firstName, lastName, email, password, confirmPassword);
        if (isValid) {
            Map<String, Object> user = new HashMap<>();
            user.put("userName", userName);
            user.put("firstName", firstName);
            user.put("lastName", lastName);
            user.put("email",email);
            user.put("customerCode", customerCode);
            user.put("cardType", cardType);
            showLoading();
            repository.createUser(email, password, user, new ResponseCallback() {
                @Override
                public void onSuccess(Object o) {
                    hideLoading();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(RegistrationActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(Error error) {
                    hideLoading();
                    showErrorDialog(RegistrationActivity.this, error);
                }
            });

        }

    }
    public boolean validateUserData(String userName,
                                    String firstName,
                                    String lastName,
                                    String email,
                                    String password,
                                    String confirmPassword) {
        boolean isDataValid = true;


        if (userName ==null ){
            etUserName.setError("Please enter username.");
            isDataValid = false;
        } else  if(userName != null && userName.length() < 3){
            etUserName.setError("Minimum length is 3.");
            isDataValid = false;
        }

        if (firstName ==null){
            etFirstName.setError("Please enter first name.");
            isDataValid = false;
        } else if(firstName !=null && firstName.length() < 3) {
            etFirstName.setError("Minimum length is 3.");
            isDataValid = false;
        }

        if (lastName ==null){
            etLastName.setError("Please enter last name.");
            isDataValid = false;
        } else if (lastName !=null && lastName.length() <3 ){
            etLastName.setError("Minimum length is 3.");
            isDataValid = false;
        }
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (email ==null){
            etEmail.setError("Please enter valid Email Address");
            isDataValid = false;
        } else if (email !=null && email.length() <=0 ){
            etEmail.setError("Please enter valid Email Address");
            isDataValid = false;
        } else if (!pattern.matcher(email).matches()) {
            etEmail.setError("Please enter valid Email Address");
            isDataValid = false;
        }

        if (password ==null){
            etPassword.setError("Please enter valid Password");
            isDataValid = false;

        } else if (password !=null && password.length() <8 ){
            etPassword.setError("Please enter minimum 8 char");
            isDataValid = false;
        } else if (password !=null && password.length() >= 8) {
            if (confirmPassword ==null){
                etPassword.setError("Confirm password should be same as password");
                isDataValid = false;
            } else if (confirmPassword !=null && !password.equals(confirmPassword) ){
                etPassword.setError("Confirm password should be same as password.");
                isDataValid = false;
            }
        }

        return isDataValid;
    }
}