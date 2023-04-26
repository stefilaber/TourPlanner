package at.fhtw.swen2.tutorial.persistence.repositories;

import at.fhtw.swen2.tutorial.persistence.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntity, Long> {
}
