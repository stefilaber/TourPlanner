package at.fhtw.swen2.tutorial.service.dto;

public class Statistic {

    private String tourName;
    private String avgTime;
    private String avgDifficulty;
    private double avgRating;

    public Statistic(String tourName, String avgTime, String avgDifficulty, double avgRating) {
        this.tourName = tourName;
        this.avgTime = avgTime;
        this.avgDifficulty = avgDifficulty;
        this.avgRating = avgRating;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(String avgTime) {
        this.avgTime = avgTime;
    }

    public String getAvgDifficulty() {
        return avgDifficulty;
    }

    public void setAvgDifficulty(String avgDifficulty) {
        this.avgDifficulty = avgDifficulty;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(long avgRating) {
        this.avgRating = avgRating;
    }
}
