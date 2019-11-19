package br.com.livroandroid.trainingmockup.Entities;

public class Market {

    private String title;
    private Double latitude;
    private Double longitude;
    private String UrlFirstImage;
    private String UrlSecondImage;
    private String adress;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUrlFirstImage() {
        return UrlFirstImage;
    }

    public void setUrlFirstImage(String urlFirstImage) {
        UrlFirstImage = urlFirstImage;
    }

    public String getUrlSecondImage() {
        return UrlSecondImage;
    }

    public void setUrlSecondImage(String urlSecondImage) {
        UrlSecondImage = urlSecondImage;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
