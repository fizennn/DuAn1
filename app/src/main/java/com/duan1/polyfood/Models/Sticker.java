package com.duan1.polyfood.Models;

public class Sticker {
    private String id;
    private String content;
    private String color; // Lưu màu dạng ARGB

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

