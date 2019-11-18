package br.com.livroandroid.trainingmockup.Entities;

public class Market {

    private String title;
    private Double latitude;
    private Double longitude;
    private String shortDesription;
    private String longDesription;


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

    public String getShortDesription() {
        return shortDesription;
    }

    public void setShortDesription(String shortDesription) {
        this.shortDesription = shortDesription;
    }

    public String getLongDesription() {
        return longDesription;
    }

    public void setLongDesription(String longDesription) {
        this.longDesription = longDesription;
    }
}
