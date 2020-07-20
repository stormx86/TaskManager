package pl.kozhanov.TaskManager.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kozhanov.TaskManager.domain.Task;
import pl.kozhanov.TaskManager.service.TaskViewProjection;

public interface TaskRepo extends JpaRepository<Task, Long> {

    Page<TaskViewProjection> findAllByOrderByReceivedAtDesc(Pageable pageable);

    Task findById(Integer id);




}
