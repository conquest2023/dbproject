package com.example.dbproject.repository;

import com.example.dbproject.entity.Article;
import com.example.dbproject.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

   // Optional<Article> findByUsername(String username);
   // Optional<Article> findByEmail(String email);

}
