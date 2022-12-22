package com.example.chromepaymobile.Models;

public class EMIModel {

    String installmentNo;
    String installmentPayAmount;
    String payDate;
    String installmentDate;
    String status;
    String id;

    public String getInstallmentNo() {
        return installmentNo;
    }

    public void setInstallmentNo(String installmentNo) {
        this.installmentNo = installmentNo;
    }

    public String getInstallmentPayAmount() {
        return installmentPayAmount;
    }

    public void setInstallmentPayAmount(String installmentPayAmount) {
        this.installmentPayAmount = installmentPayAmount;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getInstallmentDate() {
        return installmentDate;
    }

    public void setInstallmentDate(String installmentDate) {
        this.installmentDate = installmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
