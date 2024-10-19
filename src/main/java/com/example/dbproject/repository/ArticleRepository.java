package com.example.dbproject.repository;

import com.example.dbproject.entity.Article;
import com.example.dbproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {


    @Query("SELECT a FROM  Article  a where  a.board.id=:boardId AND a.isDeleted=false order by a.createdDate DESC ")
    List<Article> findTop10ByBoardIdOrderByCreatedDateDesc(@Param("boardId") Long boardId);

    @Query("select  a from Article  a where  a.board.id=:boardId AND a.id< :articleId AND a.isDeleted=false order by a.createdDate desc LIMIT 10")
    List<Article> findTop10ByBoardIdAndArticleIdLessThanOrderByCreatedDateDesc(@Param("boardId") Long boardId, @Param("articleId") Long articleId);

    @Query("select  a from Article  a where  a.board.id=:boardId AND a.id > :articleId AND a.isDeleted=false order by a.createdDate desc limit  10")
    List<Article> findTop10ByBoardIdAndArticleIdAndArticleIdGreaterThanOrderByCreatedDateDesc(@Param("boardId") Long boardId,
                                                                                              @Param("articleId") Long articleId);



    @Query("SELECT  a FROM  Article  a JOIN  a.author u WHERE  u.username= :username ORDER BY a.createdDate DESC limit  1")
    Article findLatestArticleByAuthorUsernameByOrderByCreatedDate(@Param("username") String username);

    @Query("SELECT  a FROM  Article  a JOIN  a.author u WHERE  u.username= :username ORDER BY a.updatedDate DESC  limit  1")
    Article findLatestArticleByUsernameByOrderByUpdatedDate(@Param("username") String username);

    // Optional<Article> findByUsername(String username);
   // Optional<Article> findByEmail(String email);
}
