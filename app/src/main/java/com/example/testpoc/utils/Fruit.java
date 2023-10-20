package com.example.testpoc.utils;

public class Fruit {

    int fruitImage;
    String fruitName;

    public Fruit(int fruitImage, String fruitName) {
        this.fruitImage = fruitImage;
        this.fruitName = fruitName;
    }

    public int getFruitImage() {
        return fruitImage;
    }

    public void setFruitImage(int fruitImage) {
        this.fruitImage = fruitImage;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }
}
