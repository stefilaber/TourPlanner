package at.fhtw.swen2.tutorial.persistence.repositories;

import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import at.fhtw.swen2.tutorial.service.dto.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<LogEntity, Long> {
    List<LogEntity> findByTourId(Long tourId);
}
