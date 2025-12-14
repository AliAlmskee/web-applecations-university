package com.main.repository;

import com.main.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

    @Query(value = """
        select t from FCMToken t inner join User u
        on t.user.id = u.id
        where u.id = :id
        """)
    List<FCMToken> findAllTokensByUser(Long id);

    Optional<FCMToken> findByToken(String token);
}

