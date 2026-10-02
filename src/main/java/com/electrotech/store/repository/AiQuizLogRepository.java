package com.electrotech.store.repository;

import com.electrotech.store.model.AiQuizLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AiQuizLogRepository extends JpaRepository<AiQuizLog, Integer> {
    List<AiQuizLog> findByUserId(Integer userId);
}
