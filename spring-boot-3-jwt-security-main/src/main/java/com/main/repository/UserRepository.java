package com.main.repository;

import java.util.Optional;

import com.main.core.repository.BaseRepository;
import com.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends BaseRepository<User> {

  Optional<User> findByPhone(String phone);

}
