package com.otmetkaX.repository;

import java.util.List;
import java.util.Optional;

import com.otmetkaX.model.Notification;
import com.otmetkaX.model.Security;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SecurityRepository extends JpaRepository<Security, Long> {

    Optional<Security> findByLogin(String login);
    Optional<Security> findByToken(String token);
    Optional<Security> findById(Long id);

    List<Security> findAll();
    void deleteByLogin(String login);

    @Query("SELECT n FROM Security n WHERE n.login LIKE %:searchText%")
    List<Security> findByTextContaining(@Param("searchText") String searchText);

}

