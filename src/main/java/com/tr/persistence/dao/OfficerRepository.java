package com.tr.persistence.dao;

import com.tr.persistence.Entities.Officer;
import com.tr.persistence.Entities.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficerRepository extends JpaRepository<Officer, Integer> {
    List<Officer> findByRank(@Param("rank")Rank rank);
    List<Officer> findByLast(@Param("last") String last);
    List<Officer> findByFirst(@Param("first") String first);
}


