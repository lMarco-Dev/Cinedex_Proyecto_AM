package com.example.cinedex.Data.Models;

public class Discount {
    public String title;
    public String subtitle;
    public String imageUrl;
    public String validity;

    public Discount(String title, String subtitle, String imageUrl, String validity) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.validity = validity;
    }
}
