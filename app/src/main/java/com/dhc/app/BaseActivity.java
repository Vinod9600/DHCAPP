package com.dhc.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.dhc.app.data.Error;
import com.dhc.app.data.Repository;
import com.dhc.app.data.RepositoryManager;



/**
 * Created by Vinod Durge on  06 Mar 2021
 */
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    protected Repository repository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = RepositoryManager.getRepository();
    }

    protected void setProgressBar(Activity activity) {
        progressDialog = new ProgressDialog(activity);
    }

    public void showLoading() {
        //progressDialog = new ProgressDialog(getParent());
        progressDialog.setMessage("Doing something, please wait.");
        progressDialog.show();
    }

    public void hideLoading() {
        progressDialog.dismiss();
    }

    public void showWarningDialog(Context context, String title, String details, String confirmText, DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(details);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, confirmText,
                listener);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void showErrorDialog(Context context, Error error){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(error.getErrorCode());
        alertDialog.setMessage(error.getErrorMessage());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void showErrorDialog(Context context, String error){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(error);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
