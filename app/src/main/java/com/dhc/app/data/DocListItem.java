package com.dhc.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vinod Durge on  21 Mar 2021
 */
public class DocListItem  implements Parcelable {

    @SerializedName("CardCode")
    private String cardCode;
    @SerializedName("CardName")
    private String cardName;
    @SerializedName("DeliveryDate")
    private String deliveryDate;
    @SerializedName("DocDate")
    private String docDate;
    @SerializedName("DocEntry")
    private String docEntry;
    @SerializedName("DocNum")
    private String docNumber;
    @SerializedName("DocStatus")
    private String docStatus;
    @SerializedName("DocTotal")
    private String docTotal;
    @SerializedName("DocType")
    private String docType;
    @SerializedName("VendorRefNo")
    private String vendorRefNo;
    @SerializedName("Remarks")
    private String remarks;
    @SerializedName("TaxonExpense")
    private String taxonExpense;
    @SerializedName("TotalBeforeDiscount")
    private String totalBeforeDiscount;
    @SerializedName("TotalFreightAmount")
    private String totalFreightAmount;
    @SerializedName("TotalTaxAmount")
    private String totalTaxAmount;


    public DocListItem() {
    }

    protected DocListItem(Parcel in) {
        cardCode = in.readString();
        cardName = in.readString();
        deliveryDate = in.readString();
        docDate = in.readString();
        docEntry = in.readString();
        docNumber = in.readString();
        docStatus = in.readString();
        docTotal = in.readString();
        docType = in.readString();
        vendorRefNo = in.readString();
        remarks = in.readString();
        taxonExpense = in.readString();
        totalBeforeDiscount = in.readString();
        totalFreightAmount = in.readString();
        totalTaxAmount = in.readString();
    }

    public static final Creator<ArInvoice> CREATOR = new Creator<ArInvoice>() {
        @Override
        public ArInvoice createFromParcel(Parcel in) {
            return new ArInvoice(in);
        }

        @Override
        public ArInvoice[] newArray(int size) {
            return new ArInvoice[size];
        }
    };

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getDocEntry() {
        return docEntry;
    }

    public void setDocEntry(String docEntry) {
        this.docEntry = docEntry;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public String getDocTotal() {
        return docTotal;
    }

    public void setDocTotal(String docTotal) {
        this.docTotal = docTotal;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getRefNo() {
        return vendorRefNo;
    }

    public void setRefNo(String refNo) {
        this.vendorRefNo = refNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTaxonExpense() {
        return taxonExpense;
    }

    public void setTaxonExpense(String taxonExpense) {
        this.taxonExpense = taxonExpense;
    }

    public String getTotalBeforeDiscount() {
        return totalBeforeDiscount;
    }

    public void setTotalBeforeDiscount(String totalBeforeDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
    }

    public String getTotalFreightAmount() {
        return totalFreightAmount;
    }

    public void setTotalFreightAmount(String totalFreightAmount) {
        this.totalFreightAmount = totalFreightAmount;
    }

    public String getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(String totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardCode);
        dest.writeString(cardName);
        dest.writeString(deliveryDate);
        dest.writeString(docDate);
        dest.writeString(docEntry);
        dest.writeString(docNumber);
        dest.writeString(docStatus);
        dest.writeString(docTotal);
        dest.writeString(docType);
        dest.writeString(vendorRefNo);
        dest.writeString(remarks);
        dest.writeString(taxonExpense);
        dest.writeString(totalBeforeDiscount);
        dest.writeString(totalFreightAmount);
        dest.writeString(totalTaxAmount);
    }
}
