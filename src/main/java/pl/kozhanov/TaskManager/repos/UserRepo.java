package pl.kozhanov.TaskManager.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kozhanov.TaskManager.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
