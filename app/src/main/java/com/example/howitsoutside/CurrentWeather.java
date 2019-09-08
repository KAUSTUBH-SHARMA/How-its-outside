package com.example.howitsoutside;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {
    private String locationLabel;
    private String icon;
    private long time;
    private double temperture;
    private double humidity;
    private double precipChance;
    private String summary;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    private String timeZone;


    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public CurrentWeather(String locationLabel, String icon, long time, double temperture, double humidity, double precipChance, String summary, String timeZone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.time = time;
        this.temperture = temperture;
        this.humidity = humidity;
        this.precipChance = precipChance;
        this.summary = summary;
        this.timeZone = timeZone;
    }

    public CurrentWeather() {
    }

    public int getIconId(){
        int iconId=R.drawable.clear_day;

        switch(icon){
            case "clear-day":
                iconId=R.drawable.clear_day;
                break;
            case "clear-night":
                iconId=R.drawable.clear_night;
                break;
            case "rain":
                iconId=R.drawable.rain;
                break;
            case "snow":
                iconId=R.drawable.snow;
                break;
            case "sleet":
                iconId=R.drawable.sleet;
                break;
            case "wind":
                iconId=R.drawable.wind;
                break;
            case "fog":
                iconId=R.drawable.fog;
                break;
            case "cloudy":
                iconId=R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId=R.drawable.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconId=R.drawable.cloudy_night;
                break;



        }return iconId;
    }
    public long getTime() {
        return time;
    }
    public String getFormattedTime(){
        SimpleDateFormat formatter= new SimpleDateFormat("h:mm a");

        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date dateTime= new Date(time*1000);

        return formatter.format(dateTime);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTemperture() {
        return temperture;
    }

    public void setTemperture(double temperture) {
        this.temperture = (temperture-32)*0.5556;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity*100;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


}
