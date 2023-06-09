package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.service.LogService;
import at.fhtw.swen2.tutorial.service.dto.Log;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class NewLogViewModel {

    private SimpleLongProperty id = new SimpleLongProperty();
    private SimpleStringProperty comment  = new SimpleStringProperty();
    private SimpleStringProperty difficulty  = new SimpleStringProperty();
    private SimpleStringProperty totalTime  = new SimpleStringProperty();
    private SimpleIntegerProperty rating  = new SimpleIntegerProperty();
    
    private LogService logService;
    private LogListViewModel logListViewModel;

    private Log log;

    public NewLogViewModel() { }

    public NewLogViewModel(Log log) {
        this.log = log;
        this.id = new SimpleLongProperty(log.getId());
        this.comment = new SimpleStringProperty(log.getComment());
        this.difficulty = new SimpleStringProperty(log.getDifficulty());
        this.totalTime = new SimpleStringProperty(log.getTotalTime());
        this.rating = new SimpleIntegerProperty(log.getRating());
    }

    public NewLogViewModel(LogService logService, LogListViewModel logListViewModel) {
        this.logService = logService;
        this.logListViewModel = logListViewModel;
    }

    public long getId() {
        return id.get();
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public String getDifficulty() {
        return difficulty.get();
    }

    public String getTotalTime() {
        return totalTime.get();
    }

    public int getRating() {
        return rating.get();
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public SimpleStringProperty commentProperty() { return comment; }

    public SimpleStringProperty difficultyProperty() { return difficulty; }

    public SimpleStringProperty totalTimeProperty() { return totalTime; }

    public SimpleIntegerProperty ratingProperty() { return rating; }

    public void addNewLog() {
        Long selectedTourId = logListViewModel.getSelectedTourId();
        String dateTime = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime());
        Log log = Log.builder()
                .id(getId())
                .dateTime(dateTime)
                .comment(getComment())
                .difficulty(getDifficulty())
                .totalTime(getTotalTime())
                .rating(getRating())
                .tourId(selectedTourId)
                .build();
        log = logService.save(log);
        logListViewModel.addItem(log);
    }
}
