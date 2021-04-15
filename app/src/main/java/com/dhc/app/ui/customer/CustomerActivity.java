package com.dhc.app.ui.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dhc.app.BaseActivity;
import com.dhc.app.R;
import com.dhc.app.data.Error;
import com.dhc.app.data.Repository;
import com.dhc.app.data.RepositoryManager;
import com.dhc.app.data.ResponseCallback;
import com.dhc.app.data.dto.Customer;
import com.dhc.app.ui.login.LoginActivity;
import com.dhc.app.ui.registration.RegistrationActivity;
import com.google.android.material.textfield.TextInputEditText;

public class CustomerActivity extends BaseActivity {
    TextInputEditText etCustomerCode;

    TextInputEditText etPassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        setProgressBar(CustomerActivity.this);
        etCustomerCode = findViewById(R.id.etCustomerCode);
        etPassword = findViewById(R.id.etPassword);
    }


    public void onCustomerVerifyDetailBtnClick(View view) {
        String customerCode = etCustomerCode.getText().toString();
        String password = etPassword.getText().toString();
        if (checkForValidation(customerCode, password)) {
            hideKeyboard();
            checkForCustomerDetails(customerCode, password);
        } else {
            Toast.makeText(this, "Please provide a valid email and password.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkForCustomerDetails(String customerCode, String password) {
        showLoading();
        Repository repository = RepositoryManager.getRepository();
        repository.getCustomerDetails(customerCode, new ResponseCallback<Customer>() {
            @Override
            public void onSuccess(Customer customer) {
                hideLoading();
                Intent intent = new Intent(CustomerActivity.this, RegistrationActivity.class);
                intent.putExtra("customerCode", customerCode);
                intent.putExtra("cardType", customer.getCardType());
                startActivity(intent);
            }

            @Override
            public void onError(Error error) {
                hideLoading();
            }
        });

    }

    private boolean checkForValidation(String customerCode, String password) {
        if (customerCode == null || customerCode.length() <= 0) {
            etCustomerCode.setError("Please Enter Customer Code");
            return false;
        }

        if (password == null || password.length() <= 0) {
            etPassword.setError("Please Enter Password");
            return false;
        }
        return true;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}