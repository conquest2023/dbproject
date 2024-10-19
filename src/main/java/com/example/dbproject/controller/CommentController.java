package com.example.dbproject.controller;

import com.example.dbproject.dto.EditArticleDTO;
import com.example.dbproject.dto.WriteArticleDTO;
import com.example.dbproject.dto.WriteCommentDTO;
import com.example.dbproject.entity.Article;
import com.example.dbproject.entity.Comment;
import com.example.dbproject.entity.User;
import com.example.dbproject.jwt.JwtUtil;
import com.example.dbproject.service.ArticleService;
import com.example.dbproject.service.CommentService;
import com.example.dbproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class CommentController {

    private final CommentService commentService;



    @PostMapping("/{boardId}/articles/{articleId}")
    public ResponseEntity<Comment> writeComment(@RequestBody WriteCommentDTO writeCommentDTO
                                                , @PathVariable Long boardId
                                                , @PathVariable Long articleId) {


     return ResponseEntity.ok(commentService.saveComment(articleId,boardId,writeCommentDTO));

    }


}
