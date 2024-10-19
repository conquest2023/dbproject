package com.example.dbproject.service;

import com.example.dbproject.dto.EditArticleDTO;
import com.example.dbproject.dto.WriteArticleDTO;
import com.example.dbproject.entity.Article;
import com.example.dbproject.entity.Board;
import com.example.dbproject.entity.User;
import com.example.dbproject.excetpion.ForbiddenException;
import com.example.dbproject.excetpion.ResourceNotFoundException;
import com.example.dbproject.repository.ArticleRepository;
import com.example.dbproject.repository.BoardRepository;
import com.example.dbproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BoardRepository boardRepository;

    private final UserRepository userRepository;



    public Article writeArticle(Long BoardId, WriteArticleDTO dto) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!this.isCanEditArticle()){
            throw new ResourceNotFoundException("can not edit article");
        }
       Optional<User> author= userRepository.findByUsername(userDetails.getUsername());
        Optional<Board> board = boardRepository.findById(dto.getBoardId());
        if (author.isEmpty()) {
            throw new ResourceNotFoundException("author not found");

        }
        if (board.isEmpty()) {
            throw new ResourceNotFoundException("Board not found");
        }

        Article article = new Article();
        article.setBoard(board.get());
        article.setAuthor(author.get());
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        articleRepository.save(article);
        return article;
    }


    public List<Article>  firstGetArticles(Long boardId) {
      return   articleRepository.findTop10ByBoardIdOrderByCreatedDateDesc(boardId);
    }

    public  List<Article> GetOldArticles(Long boardId,Long articleId) {
        return  articleRepository.findTop10ByBoardIdAndArticleIdLessThanOrderByCreatedDateDesc(boardId,articleId);
    }

    public List<Article> GetNewArticle(Long boardId,Long articleId) {
        return articleRepository.findTop10ByBoardIdAndArticleIdAndArticleIdGreaterThanOrderByCreatedDateDesc(boardId,articleId);
    }

    public Article editArticle(Long boardId,Long articleId, EditArticleDTO dto) {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> author= userRepository.findByUsername(userDetails.getUsername());
        Optional<Board> board = boardRepository.findById(boardId);
        if (author.isEmpty()) {
            throw new ResourceNotFoundException("author not found");
        }
        if (board.isEmpty()) {
            throw new ResourceNotFoundException("Board not found");
        }
        if (!this.isCanEditArticle()){
                throw new ResourceNotFoundException("can not edit article");
        }

        Optional<Article> article=articleRepository.findById(articleId);
        if (dto.getTitle()!=null) {
            article.get().setTitle(dto.getTitle().get());
        }
        if(!article.isPresent()){
            throw new ResourceNotFoundException("Article not found");
        }
        if (dto.getContent()!=null) {
            article.get().setContent(dto.getContent().get());
        }
        if (article.get().getAuthor()!=author.get()) {
            throw new ForbiddenException("article author different");

        }

        articleRepository.save(article.get());
        return article.get();
    }
    public boolean isCanWriteArticle() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Article article= articleRepository.findLatestArticleByAuthorUsernameByOrderByCreatedDate(userDetails.getUsername());

        return this.isDifferenceMoreThanFiveMinutes(article.getCreatedDate());
    }

    public boolean isCanEditArticle() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Article article= articleRepository.findLatestArticleByUsernameByOrderByUpdatedDate(userDetails.getUsername());

        return this.isDifferenceMoreThanFiveMinutes(article.getUpdatedDate());

    }



    private boolean isDifferenceMoreThanFiveMinutes(LocalDateTime localDateTime){

        LocalDateTime dateTime=new Date().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Duration duration = Duration.between(dateTime, localDateTime);


        return Math.abs(duration.toMinutes()) >5;

    }

    public boolean deleteArticle(Long boardId,Long articleId) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> author= userRepository.findByUsername(userDetails.getUsername());
        Optional<Board> board = boardRepository.findById(boardId);
        if (author.isEmpty()) {
            throw new ResourceNotFoundException("author not found");
        }
        if (board.isEmpty()) {
            throw new ResourceNotFoundException("Board not found");
        }

        Optional<Article> article=articleRepository.findById(articleId);

        if (article.get().getAuthor()!=author.get()) {
            throw new ForbiddenException("article author different");
        }
        if(article.isEmpty()){
            throw new ResourceNotFoundException("Article not found");
        }
        if (!this.isCanEditArticle()){
            throw new ResourceNotFoundException("can not edit article");
        }
        article.get().setIsDeleted(true);
        articleRepository.save(article.get());
        return  true;
    }

}
