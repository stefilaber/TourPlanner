package at.fhtw.swen2.tutorial.persistence;

import at.fhtw.swen2.tutorial.persistence.entities.TourEntity;
import at.fhtw.swen2.tutorial.persistence.repositories.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TourRepositoryTest {
	@Mock
	private TourRepository tourRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	private TourEntity createTourEntity() {
		return TourEntity.builder()
				.id(1L)
				.name("Vorarlberg")
				.tourDescription("i wanna go home")
				.tourFrom("Dornbirn")
				.tourTo("Bregenz")
				.transportType("car")
				.tourDistance(45)
				.estimatedTime(5)
				.build();
	}

	@Test
	void shouldSaveTourEntity() {
		TourEntity tourEntity = createTourEntity();

		tourRepository.save(tourEntity);

		verify(tourRepository, times(1)).save(tourEntity);
	}

	@Test
	void shouldReturnListOfTourEntities() {
		TourEntity tourEntity = createTourEntity();

		List<TourEntity> tourEntities = Arrays.asList(tourEntity);

		when(tourRepository.findAll()).thenReturn(tourEntities);

		tourRepository.findAll().forEach(System.out::println);

		verify(tourRepository, never()).save(any());
		verify(tourRepository, times(1)).findAll();
	}

	@Test
	void shouldReturnEmptyListWhenNoTourEntitiesExist() {
		when(tourRepository.findAll()).thenReturn(Arrays.asList());

		List<TourEntity> result = tourRepository.findAll();

		assertTrue(result.isEmpty());

		verify(tourRepository, never()).save(any());
		verify(tourRepository, times(1)).findAll();
	}

	@Test
	void shouldFindTourEntityById() {
		TourEntity tourEntity = createTourEntity();

		when(tourRepository.findById(1L)).thenReturn(Optional.of(tourEntity));

		Optional<TourEntity> result = tourRepository.findById(1L);

		assertTrue(result.isPresent());
		assertEquals(tourEntity, result.get());

		verify(tourRepository, never()).save(any());
		verify(tourRepository, times(1)).findById(1L);
	}

}
