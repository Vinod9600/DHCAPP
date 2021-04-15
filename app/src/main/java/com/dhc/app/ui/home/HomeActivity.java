package com.dhc.app.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dhc.app.BaseActivity;
import com.dhc.app.R;
import com.dhc.app.ui.login.LoginActivity;
import com.dhc.app.ui.search.SearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends BaseActivity {

    GridView grid;
    String[] web = {
            "Sales Quotation",
            "Sales Order",
            "Delivery Challan",
            "Invoice/Credit Note",
            "Payments",
            "Account Statement"

    } ;
    int[] imageId = {
            R.drawable.account,
            R.drawable.order,
            R.drawable.delivery,
            R.drawable.invoice,
            R.drawable.payment,
            R.drawable.quotation

    };

    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CustomGrid adapter = new CustomGrid(HomeActivity.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onGridItemClick(web[position]);

            }
        });
    }

    private void onGridItemClick(String menu) {
        switch (menu) {
            case "Sales Quotation" :

                break;
            case "Sales Order" :
                break;
            case "Delivery Challan" :
                break;
            case "Invoice/Credit Note" :
                navigateToSearchScreen(13);
                break;
            case "Payments" :
                break;
            case "Account Statement" :
                break;

            default : break;
        }
    }

    private void navigateToSearchScreen(int objectType) {
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        intent.putExtra("ObjectType", objectType);
        startActivity(intent);
    }

    public void onLogOutButtonClick(View view) {
        showWarningDialog(HomeActivity.this, "Are you sure? ", "you will be log out", "Log out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}