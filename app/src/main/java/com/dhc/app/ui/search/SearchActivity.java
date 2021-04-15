package com.dhc.app.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.dhc.app.BaseActivity;
import com.dhc.app.R;
import com.dhc.app.data.ArInvoice;
import com.dhc.app.data.BaseResponse;
import com.dhc.app.data.Error;
import com.dhc.app.data.Repository;
import com.dhc.app.data.RepositoryManager;
import com.dhc.app.data.ResponseCallback;
import com.dhc.app.ui.InvoiceActivity;
import com.dhc.app.ui.customer.CustomerActivity;
import com.dhc.app.ui.documentlist.DocumentListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SearchActivity extends BaseActivity {

    EditText etSearch;
    String objectType;

    Toolbar toolbar;

    final Calendar myFromDate = Calendar.getInstance();
    final Calendar myToDate = Calendar.getInstance();

    EditText etFromDate;
    EditText etToDate;

    ContentLoadingProgressBar contentLoadingProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setProgressBar(SearchActivity.this);
        //setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        etSearch = findViewById(R.id.ed_document_code);
        etFromDate = findViewById(R.id.from_date);
        etToDate = findViewById(R.id.to_date);
        contentLoadingProgressBar = findViewById(R.id.progress_circular);
        objectType = getIntent().getIntExtra("ObjectType", 0)+"";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void onSearchButtonClick(View view) {
        String documentEntry = etSearch.getText().toString();
        if (!documentEntry.isEmpty()) {
            Repository repository = RepositoryManager.getRepository();
            contentLoadingProgressBar.setVisibility(View.VISIBLE);

            repository.fetchDocumentListDetails(objectType, documentEntry, new ResponseCallback<BaseResponse>() {
                @Override
                public void onSuccess(BaseResponse baseResponse) {
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    ArInvoice arInvoice = (ArInvoice) baseResponse.getData().get(0);
                    openDocumentDetailScreen(objectType, arInvoice);
                }

                @Override
                public void onError(Error error) {
                    contentLoadingProgressBar.setVisibility(View.GONE);
                    showErrorDialog(SearchActivity.this, error);
                }

            });
        } else {
            showErrorDialog(SearchActivity.this, "Please enter document / vendor ref no");
        }
    }

    private void openDocumentDetailScreen(String objectType, ArInvoice arInvoice) {
        Intent intent = new Intent(SearchActivity.this, InvoiceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", arInvoice);
        intent.putExtra("ObjectType", objectType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onSearchDateButtonClick(View view) {
        if (etFromDate.getText().toString().isEmpty() || etToDate.getText().toString().isEmpty()) {
            showErrorDialog(SearchActivity.this,"Please select FromDate and ToDate");
        } else if (myFromDate.getTimeInMillis() > myToDate.getTimeInMillis()) {
            showErrorDialog(SearchActivity.this, "From Date should be older than  To Date ");
        } else {
            Intent intent = new Intent(this, DocumentListActivity.class);
            intent.putExtra("ObjectType", objectType);
            intent.putExtra("fromDate", etFromDate.getText().toString());
            intent.putExtra("toDate", etToDate.getText().toString());
            startActivity(intent);
        }
    }

    public void onFromDateClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(SearchActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myFromDate.set(Calendar.YEAR, year);
                myFromDate.set(Calendar.MONTH, month);
                myFromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromDate();
            }
        }, myFromDate
                .get(Calendar.YEAR), myFromDate.get(Calendar.MONTH),
                myFromDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void updateFromDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etFromDate.setText(sdf.format(myFromDate.getTime()));
    }

    public void onToDateClick(View view) {

       DatePickerDialog datePickerDialog = new DatePickerDialog(SearchActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myToDate.set(Calendar.YEAR, year);
                myToDate.set(Calendar.MONTH, month);
                myToDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateToDate();
            }
        }, myToDate.get(Calendar.YEAR), myToDate.get(Calendar.MONTH),
                myToDate.get(Calendar.DAY_OF_MONTH));
       datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

       datePickerDialog.show();
    }

    private void updateToDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etToDate.setText(sdf.format(myToDate.getTime()));
    }
}