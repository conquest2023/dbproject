package com.example.dbproject.service;

import com.example.dbproject.dto.EditArticleDTO;
import com.example.dbproject.dto.WriteArticleDTO;
import com.example.dbproject.dto.WriteCommentDTO;
import com.example.dbproject.entity.Article;
import com.example.dbproject.entity.Board;
import com.example.dbproject.entity.Comment;
import com.example.dbproject.entity.User;
import com.example.dbproject.excetpion.ForbiddenException;
import com.example.dbproject.excetpion.ResourceNotFoundException;
import com.example.dbproject.repository.ArticleRepository;
import com.example.dbproject.repository.BoardRepository;
import com.example.dbproject.repository.CommentRepository;
import com.example.dbproject.repository.UserRepository;
import jakarta.transaction.Transactional;
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
public class CommentService {
    private final ArticleRepository articleRepository;

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    @Transactional
    public Comment saveComment(Long boardId , Long articleId, WriteCommentDTO writeCommentDTO) {


        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (!this.isCanWriteComment()){
            throw new ResourceNotFoundException("can not edit article");
        }
        Optional<User> author= userRepository.findByUsername(userDetails.getUsername());
        Optional<Board> board = boardRepository.findById(boardId);
        Optional<Article> article = articleRepository.findById(articleId);
        if (author.isEmpty()) {
            throw new ResourceNotFoundException("author not found");
        }
        if (board.isEmpty()) {
            throw new ResourceNotFoundException("Board not found");
        }

        if (article.isEmpty()) {
            throw new ResourceNotFoundException("article not found");
        }
        if (article.get().getIsDeleted()){
            throw new ForbiddenException("article is deleted");
        }

        Comment comment = new Comment();
        comment.setArticle(article.get());
        comment.setAuthor(author.get());
        comment.setContent(writeCommentDTO.getContent());
        commentRepository.save(comment);

        return  comment;

    }



    public boolean isCanWriteComment() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Comment comment= commentRepository.findLatestCommentByAuthorUsernameByOrderByCreatedDate(userDetails.getUsername());
        if(comment==null){
            return true;
        }

        return this.isDifferenceMoreThanFiveMinutes(comment.getCreatedDate());
    }

    public boolean isCanEditComment() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Comment comment= commentRepository.findLatestCommentByUsernameByOrderByUpdatedDate(userDetails.getUsername());
        if(comment==null){
            return true;
        }
        return this.isDifferenceMoreThanFiveMinutes(comment.getUpdatedDate());

    }



    private boolean isDifferenceMoreThanFiveMinutes(LocalDateTime localDateTime){

        LocalDateTime dateTime=new Date().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Duration duration = Duration.between(dateTime, localDateTime);


        return Math.abs(duration.toMinutes()) >1;

    }


    }


