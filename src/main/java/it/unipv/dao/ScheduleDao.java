package it.unipv.dao;

import it.unipv.model.MovieSchedule;

import java.util.List;

public interface ScheduleDao {
    List<MovieSchedule> retrieveMovieSchedules();
    void insertNewMovieSchedule(MovieSchedule toInsert);
    void deleteMovieSchedule(MovieSchedule toDelete);
}
