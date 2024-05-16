package com.thaikimhuynh.miladyapp;

public class Notification {
    String head, content, description;



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Notification(String head, String content, String description) {
        this.head = head;
        this.content = content;
        this.description = description;
    }



    public Notification() {
    }
}
