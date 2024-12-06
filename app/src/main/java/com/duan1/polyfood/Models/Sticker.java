package com.duan1.polyfood.Models;

import java.io.Serializable;

public class Sticker implements Serializable {
    private String id;
    private String content;
    private String color; // Lưu màu dạng ARGB

    private String imageUri;
    private String selected;

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Sticker() {
    }

    public Sticker(String id, String content, String color) {
        this.id = id;
        this.content = content;
        this.color = color;
    }

    // Getters và setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    @Override
    public String toString() {
        return content; // Giá trị hiển thị trên Spinner
    }
}

