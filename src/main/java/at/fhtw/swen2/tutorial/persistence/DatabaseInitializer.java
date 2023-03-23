package at.fhtw.swen2.tutorial.persistence;

import at.fhtw.swen2.tutorial.persistence.entities.TourEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.TourRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer implements InitializingBean {

    @Autowired
    private TourRepository tourRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<TourEntity> tourList = new ArrayList<>();
        tourList.add(TourEntity.builder().id(5L).name("Paris").tourDescription("aaa").tourFrom("Vienna").tourTo("Paris").transportType("Bus").tourDistance(100).estimatedTime(4).build());
        tourList.add(TourEntity.builder().id(7L).name("NewYork").tourDescription("bbb").tourFrom("Vienna").tourTo("NewYork").transportType("Train").tourDistance(200).estimatedTime(7).build());
        tourList.add(TourEntity.builder().id(11L).name("Bucharest").tourDescription("ccc").tourFrom("Vienna").tourTo("Bucharest").transportType("Airplane").tourDistance(300).estimatedTime(5).build());
        tourRepository.saveAll(tourList);
    }
}
