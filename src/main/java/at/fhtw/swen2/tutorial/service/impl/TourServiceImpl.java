package at.fhtw.swen2.tutorial.service.impl;

import at.fhtw.swen2.tutorial.persistence.entities.TourEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.TourRepository;
import at.fhtw.swen2.tutorial.service.dto.Tour;
import at.fhtw.swen2.tutorial.service.mapper.TourMapper;
import at.fhtw.swen2.tutorial.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TourServiceImpl implements TourService {

    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private TourMapper tourMapper;


    @Override
    public List<Tour> getTourList() {
        System.out.println(tourMapper.fromEntity(tourRepository.findAll()));
        return tourMapper.fromEntity(tourRepository.findAll());
    }

    @Override
    public Tour addNew(Tour tour) {
        if (tour == null){
            return null;
        }
        TourEntity entity = tourRepository.save(tourMapper.toEntity(tour));
        return tourMapper.fromEntity(entity);
    }
}
