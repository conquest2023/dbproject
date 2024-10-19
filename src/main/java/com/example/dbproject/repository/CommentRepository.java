package com.example.dbproject.repository;

import com.example.dbproject.entity.Article;
import com.example.dbproject.entity.Board;
import com.example.dbproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

   // Optional<Article> findByUsername(String username);
   // Optional<Article> findByEmail(String email);
   @Query("SELECT  a FROM  Comment  a JOIN  a.author u WHERE  u.username= :username ORDER BY a.createdDate DESC limit  1")
   Comment findLatestCommentByAuthorUsernameByOrderByCreatedDate(@Param("username") String username);

    @Query("SELECT  a FROM  Comment  a JOIN  a.author u WHERE  u.username= :username ORDER BY a.updatedDate DESC  limit  1")
    Comment findLatestCommentByUsernameByOrderByUpdatedDate(@Param("username") String username);
}
