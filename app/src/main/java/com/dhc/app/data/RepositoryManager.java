package com.dhc.app.data;

import android.util.Log;

import androidx.annotation.NonNull;


import com.dhc.app.data.dto.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vinod Durge on  08 Nov 2020
 */

public class RepositoryManager implements Repository{

    private Customer customer;

    private final Retrofit retrofit;

    private FirebaseAuth firebaseAuth;

    private String TAG = "Repository";

    private static RepositoryManager INSTANCE = null;

    private static User user = null;

    private RepositoryManager(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300,TimeUnit.SECONDS).build();
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://117.248.109.76:9000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();;
    }

    public static RepositoryManager getRepository() {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryManager();
        }
        return(INSTANCE);
    }

    @Override
    public void getCustomerDetails(String customerId, ResponseCallback responseCallback) {
        if (customer != null) {
            responseCallback.onSuccess(customer);
        } else {
            CustomerService service = retrofit.create(CustomerService.class);
            Call<BaseResponse> call = service.getCustomerDetails(customerId);
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.code() == 201) {
                        try {
                            JSONArray jsonObject = new JSONArray(response.body().getData());
                            customer = new Customer((JSONObject) jsonObject.get(0));
                            responseCallback.onSuccess(customer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        responseCallback.onError(new Error(response.code() + "", "Technical Error"));
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    responseCallback.onError(new Error("00002", "System Error"));
                }
            });
        }
    }

    @Override
    public void fetchDocumentDetails(String objectType, String documentEntry, ResponseCallback responseCallback) {
        DocumentService service = retrofit.create(DocumentService.class);
        Call<BaseResponse<ArInvoice>> call = service.getDocumentDetails(objectType, documentEntry);
        call.enqueue(new Callback<BaseResponse<ArInvoice>>() {
            @Override
            public void onResponse(Call<BaseResponse<ArInvoice>> call, Response<BaseResponse<ArInvoice>> response) {
                if (response.code() == 200) {
                    responseCallback.onSuccess(response.body());
                } else {
                    responseCallback.onError(new Error(response.code() + "", "Something went wrong !"));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ArInvoice>> call, Throwable t) {
                responseCallback.onError(new Error("00002", "System Error"));
            }
        });
    }

    @Override
    public void userSignUp(String userName, String firstName, String lastName, String email, String password, String confirmPassword, ResponseCallback responseCallback) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                } else {
                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                }

            }
        });
    }

    @Override
    public void downloadReport(String objectType, String documentEntry, String docType, ResponseCallback responseCallback) {
        DownloadService service = retrofit.create(DownloadService.class);
//        Call<ResponseBody> call = service.getDownloadReport(objectType, documentEntry, "I");
        Call<ResponseBody> call = service.getDownloadReport("http://117.248.109.76:9000/api/DownloadReportPDF?ObjType=13&DocEntry=278&DocType=I");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    responseCallback.onSuccess(response.body());
                } else {
                  //  responseCallback.onError(new Error(response.code() + "", response.body().getErrorMessage()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                responseCallback.onError(new Error("00002", "System Error"));
            }
        });
    }

    @Override
    public void createUser(String email, String password, Map<String, Object> user, ResponseCallback responseCallback) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    user.put("userId", currentUser.getUid());
                    updateUserDetails(user, responseCallback);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    responseCallback.onError(new Error("00002", task.getException().getMessage()));
                }

            }
        });
    }

    @Override
    public void setUser(User user) {
        RepositoryManager.user = user;
    }

    private void updateUserDetails(Map<String, Object> user, ResponseCallback<String> responseCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        responseCallback.onSuccess("Successfully ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        responseCallback.onError(new Error("00002", "Technical Error"));
                    }
                });
    }

    private User getUser() {
        final User[] user = {null};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return user[0];
    }
    @Override
    public void fetchDocumentListDetails(String objectType, String cardCord, ResponseCallback responseCallback) {
        DocumentService service = retrofit.create(DocumentService.class);
        Call<BaseResponse<ArInvoice>> call = service.getDocumentList(objectType, user.getCustomerCode(), "", "", cardCord);
        call.enqueue(new Callback<BaseResponse<ArInvoice>>() {
            @Override
            public void onResponse(Call<BaseResponse<ArInvoice>> call, Response<BaseResponse<ArInvoice>> response) {
                if (response.code() == 201) {
                    BaseResponse<ArInvoice> arInvoice = response.body();
                    ArInvoice invoice = arInvoice.getData().get(0);
                    fetchDocumentDetails(objectType, invoice.getDocEntry(), responseCallback);
                }
                else {
                    responseCallback.onError(new Error(response.code() + "", "Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ArInvoice>> call, Throwable t) {
                responseCallback.onError(new Error("00002", "System Error"));
            }
        });
    }

    @Override
    public void fetchDocumentListWithDateDetails(String objectType, String fromDate, String toDate, ResponseCallback responseCallback) {
        DocumentService service = retrofit.create(DocumentService.class);
        Call<BaseResponse<DocListItem>> call = service.getDocumentListWithDate(objectType, user.getCustomerCode(), fromDate, toDate, "");
        call.enqueue(new Callback<BaseResponse<DocListItem>>() {
            @Override
            public void onResponse(Call<BaseResponse<DocListItem>> call, Response<BaseResponse<DocListItem>> response) {
                if (response.code() == 201) {
                    BaseResponse<DocListItem> arInvoice = response.body();
                    List<DocListItem> invoice = arInvoice.getData();
                    responseCallback.onSuccess(invoice);
                }
                else {
                    responseCallback.onError(new Error(response.code() + "", "Something went wrong"));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DocListItem>> call, Throwable t) {
                responseCallback.onError(new Error("00002", "System Error"));
            }
        });
    }
}
