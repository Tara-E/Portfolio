package com.tara.coxintv.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("totalMilliseconds")
    @Expose
    private int totalMilliseconds;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalMilliseconds() {
        return totalMilliseconds;
    }

    public void setTotalMilliseconds(int totalMilliseconds) {
        this.totalMilliseconds = totalMilliseconds;
    }

    @Override
    public java.lang.String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nIsSuccess:\n");
        sb.append(success + "\n");
        if (message != null) {
            sb.append("\nMessage:\n");
            sb.append(message + "\n");
        }
        sb.append("\nTotalMilliseconds:\n");
        sb.append(totalMilliseconds + "\n");

        return sb.toString();
    }
}