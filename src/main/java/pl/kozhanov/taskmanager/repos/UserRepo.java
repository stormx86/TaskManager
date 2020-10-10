package pl.kozhanov.taskmanager.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kozhanov.taskmanager.domain.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    List<User> findAll();
}
