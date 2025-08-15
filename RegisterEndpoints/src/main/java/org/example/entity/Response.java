package org.example.entity;



import java.sql.Timestamp;


public class Response {
    private String timestamp;
    private String message;
    public Response(){

    }
    public Response(String message){
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
