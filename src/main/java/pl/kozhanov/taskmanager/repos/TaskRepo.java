package pl.kozhanov.taskmanager.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kozhanov.taskmanager.domain.Task;
import pl.kozhanov.taskmanager.service.TaskViewProjection;

public interface TaskRepo extends JpaRepository<Task, Long> {

    Page<TaskViewProjection> findAllByOrderByReceivedAtDesc(Pageable pageable);

    Task findById(Integer id);
}
