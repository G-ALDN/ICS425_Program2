package edu.metrostate.program2.repo;

import edu.metrostate.program2.model.Task;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    Optional<List<Task>> findAllByUserId(Long userId);
    Optional<List<Task>> findAllByCategoryAndUserId(String category, Long userId);
    Optional<List<Task>> findAllByCompletedAndUserId(boolean completed, Long userId);

}
