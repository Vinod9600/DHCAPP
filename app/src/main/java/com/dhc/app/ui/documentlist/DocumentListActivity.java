package com.dhc.app.ui.documentlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.dhc.app.BaseActivity;
import com.dhc.app.R;
import com.dhc.app.data.ArInvoice;
import com.dhc.app.data.BaseResponse;
import com.dhc.app.data.DocListItem;
import com.dhc.app.data.Error;
import com.dhc.app.data.Movie;
import com.dhc.app.data.Repository;
import com.dhc.app.data.RepositoryManager;
import com.dhc.app.data.ResponseCallback;
import com.dhc.app.ui.InvoiceActivity;
import com.dhc.app.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class DocumentListActivity extends BaseActivity {
    String objectType;

    String fromDate;

    String toDate;

    Repository repository;

    private List<DocListItem> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);

        //setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setProgressBar(DocumentListActivity.this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        repository = RepositoryManager.getRepository();
        objectType = getIntent().getStringExtra("ObjectType");
        fromDate = getIntent().getStringExtra("fromDate");
        toDate = getIntent().getStringExtra("toDate");

//        List<ArInvoice> list = new ArrayList<>();
//        list.add(arInvoice);
//        DocumentAdapter adapter = new DocumentAdapter(list);
//        recyclerView.setAdapter(adapter);
//        // Set layout manager to position the items
//        recyclerView.setLayoutManager(new LinearLayoutManager(DocumentListActivity.this, LinearLayoutManager.VERTICAL, false));
        showLoading();
        repository.fetchDocumentListWithDateDetails(objectType, fromDate, toDate, new ResponseCallback<List<DocListItem>>() {
            @Override
            public void onSuccess(List<DocListItem> o) {
                hideLoading();
                updateData(o);
            }

            @Override
            public void onError(Error error) {
                hideLoading();
                showErrorDialog(DocumentListActivity.this, error);
            }
        });

        mAdapter = new MoviesAdapter(movieList, new RowClickListener() {
            @Override
            public void onItemClickCallBack(String position) {
                openDocumentDetails(position);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


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


    private void updateData(List<DocListItem> o) {
        movieList.addAll(o);
        mAdapter.notifyDataSetChanged();
    }

 private void openDocumentDetails(String docEntry) {
        showLoading();
        repository.fetchDocumentDetails(objectType, docEntry, new ResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                hideLoading();
                ArInvoice arInvoice = (ArInvoice) baseResponse.getData().get(0);
                Intent intent = new Intent(DocumentListActivity.this, InvoiceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", arInvoice);
                intent.putExtra("ObjectType", objectType);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onError(Error error) {
                hideLoading();
                showErrorDialog(DocumentListActivity.this, error);
            }
        });
 }
}