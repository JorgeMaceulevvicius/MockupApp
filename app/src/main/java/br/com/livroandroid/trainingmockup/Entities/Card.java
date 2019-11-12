package br.com.livroandroid.trainingmockup.Entities;

public class Card {
    private String temp;
    private String imageUrl;

    public Card(){

    }

    public Card(String mTemp,String mImageUrl){
        if(mTemp.trim().equals("")){
            temp = "No Temp";
        }
        temp = mTemp;
        imageUrl = mImageUrl;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
