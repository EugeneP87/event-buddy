package ru.practicum.mainService.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainService.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

}