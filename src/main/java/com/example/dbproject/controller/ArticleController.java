package com.example.dbproject.controller;

import com.example.dbproject.dto.EditArticleDTO;
import com.example.dbproject.dto.SignUpUser;
import com.example.dbproject.dto.WriteArticleDTO;
import com.example.dbproject.entity.Article;
import com.example.dbproject.entity.User;
import com.example.dbproject.jwt.JwtUtil;
import com.example.dbproject.service.ArticleService;
import com.example.dbproject.service.CustomUserDetailsService;
import com.example.dbproject.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class ArticleController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    private final UserDetailsService userDetailsService;

    private  final ArticleService articleService;

    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public ResponseEntity<List<User>> createUser() {

       return  ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/{boardId}/articles")
    public ResponseEntity<Article> writeArticle(@RequestBody WriteArticleDTO writeArticleDTO,@PathVariable Long boardId) {

     Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

// //    if(authentication != null && authentication.getPrincipal() instanceof User) {
//         User user = (User) authentication.getPrincipal();
//
//   //  }
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();

     return ResponseEntity.ok(articleService.writeArticle(boardId,writeArticleDTO));

    }

    @GetMapping("/{boardId}/articles")
    public ResponseEntity<List<Article>> getArticles(@PathVariable Long boardId,
                                                     @RequestParam(required = false) Long lastId,
                                                     @RequestParam(required = false) Long firstId) {
        if(lastId!=null){
        return  ResponseEntity.ok(articleService.GetOldArticles(boardId,lastId));

        }
        if(firstId!=null){
        return  ResponseEntity.ok(articleService.GetNewArticle(boardId,firstId));

        }

        return ResponseEntity.ok(articleService.firstGetArticles(boardId));
    }

    @PutMapping("/{boardId}/articles/{articleId}")
    public ResponseEntity<Article> editArticles(@PathVariable Long boardId, @PathVariable Long articleId,
                                                      @RequestBody EditArticleDTO editArticleDTO) {


        return ResponseEntity.ok(articleService.editArticle(boardId,articleId,editArticleDTO));
    }


    @DeleteMapping("/{boardId}/articles/{articleId}")
    public ResponseEntity<String> deleteArticles(@PathVariable Long boardId, @PathVariable Long articleId,
                                                @RequestBody EditArticleDTO editArticleDTO) {

        articleService.deleteArticle(boardId,articleId);

        return ResponseEntity.ok("article is deleted");
    }

}
