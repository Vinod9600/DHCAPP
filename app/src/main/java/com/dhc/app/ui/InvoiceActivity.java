package com.dhc.app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dhc.app.R;
import com.dhc.app.data.ArInvoice;
import com.dhc.app.data.Error;
import com.dhc.app.data.Repository;
import com.dhc.app.data.RepositoryManager;
import com.dhc.app.data.ResponseCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class InvoiceActivity extends AppCompatActivity {

    String objectType;
    Toolbar toolbar;

    ArInvoice arInvoice;

    private DownloadManager mgr=null;
    private long lastDownload=-1L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

//        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        objectType = getIntent().getStringExtra("ObjectType");
         arInvoice = getIntent().getExtras().getParcelable("data");
        setArInvoice(arInvoice);

        mgr=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(onNotificationClick,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    private void setArInvoice(ArInvoice arInvoice) {
        TextView docNumber = findViewById(R.id.tv_document_number);
        docNumber.setText(arInvoice.getDocNumber());

        TextView status = findViewById(R.id.tv_doc_status);
        status.setText(arInvoice.getDocStatus());

        TextView remark = findViewById(R.id.tv_remark_data);
        remark.setText(arInvoice.getRemarks());

        TextView quotationDate = findViewById(R.id.tv_quotation_data);
        quotationDate.setText(arInvoice.getDocDate());

        TextView reference = findViewById(R.id.tv_reference_number);
        reference.setText(arInvoice.getRefNo());

        TextView deliveryDate = findViewById(R.id.tv_delivery_date_data);
        deliveryDate.setText(arInvoice.getDeliveryDate());

        TextView totalBefore = findViewById(R.id.tv_total_before_amount_data);
        totalBefore.setText(arInvoice.getTotalBeforeDiscount());

        TextView totalTax = findViewById(R.id.tv_tax_amount_data);
        totalTax.setText(arInvoice.getTotalTaxAmount());

        TextView frightAmount = findViewById(R.id.tv_total_Fright_amount_data);
        frightAmount.setText(arInvoice.getTotalFreightAmount());

        TextView expense = findViewById(R.id.tv_tax_on_expense_data);
        expense.setText(arInvoice.getTaxonExpense());

        TextView total = findViewById(R.id.tv_doc_amount_data);
        total.setText(arInvoice.getDocTotal());
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

    public void onDownloadButton(View view) {
        String urlString = String.format("http://117.248.109.76:9000/api/DownloadReportPDF?ObjType=%s&DocEntry=%s&DocType=%s", objectType, arInvoice.getDocEntry(), "I");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        startActivity(browserIntent);
//        startDownload(view, urlString);
//        Repository repository = new RepositoryManager();
//        repository.downloadReport(objectType, arInvoice.getDocEntry(), "I", new ResponseCallback<ResponseBody>() {
//            @Override
//            public void onSuccess(ResponseBody body) {
//                writeResponseBodyToDisk(body);
//            }
//
//            @Override
//            public void onError(Error error) {
//
//            }
//        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.DIRECTORY_DCIM   + File.separator + "test.pdf");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

//                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            findViewById(R.id.start).setEnabled(true);
        }
    };

    BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(onComplete);
        unregisterReceiver(onNotificationClick);
    }

    public void startDownload(View v, String downloadLink) {
        Uri uri=Uri.parse(downloadLink);

        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .mkdirs();

        lastDownload=
                mgr.enqueue(new DownloadManager.Request(uri)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(true)
                        .setTitle("Document")
                        .setDescription("Downloading.")
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS,
                                "tax_invoice.pdf"));

        v.setEnabled(false);
//        findViewById(R.id.query).setEnabled(true);
    }

    public void onDownloadManager(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }
}