package com.dhc.app.data;

import java.util.Map;

/**
 * Created by Vinod Durge on  08 Nov 2020
 */
public interface Repository {
    void getCustomerDetails(String customerId, ResponseCallback responseCallback);

    void fetchDocumentDetails(String objectType, String documentEntry, ResponseCallback responseCallback);

    void fetchDocumentListDetails(String objectType, String VendorRefNo, ResponseCallback responseCallback);

    void fetchDocumentListWithDateDetails(String objectType, String fromDate, String toDate, ResponseCallback responseCallback);

    void userSignUp(String userName,
                    String firstName,
                    String lastName,
                    String email,
                    String password,
                    String confirmPassword, ResponseCallback responseCallback);

    void downloadReport(String objectType, String documentEntry,String docType, ResponseCallback responseCallback);

    void createUser(String email, String password, Map<String, Object> user, ResponseCallback responseCallback);

    void setUser(User user);
}
