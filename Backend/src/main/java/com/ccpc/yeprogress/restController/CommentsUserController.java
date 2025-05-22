package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.CommentsUserDTO;
import com.ccpc.yeprogress.service.CommentsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentsUserController {

    private final CommentsUserService commentsUserService;

    @Autowired
    public CommentsUserController(CommentsUserService commentsUserService) {
        this.commentsUserService = commentsUserService;
    }

    @PostMapping
    public ResponseEntity<CommentsUserDTO> createComment(@RequestBody CommentsUserDTO commentsUserDTO) {
        if (commentsUserDTO.getUserAuthorId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        CommentsUserDTO createdComment = commentsUserService.createComment(commentsUserDTO);
        return ResponseEntity.ok(createdComment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentsUserDTO> getCommentById(@PathVariable Long id) {
        CommentsUserDTO commentDTO = commentsUserService.getCommentById(id);
        return ResponseEntity.ok(commentDTO);
    }

    @GetMapping
    public ResponseEntity<List<CommentsUserDTO>> getAllComments() {
        List<CommentsUserDTO> comments = commentsUserService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentsUserDTO> updateComment(@PathVariable Long id, @RequestBody CommentsUserDTO commentsUserDTO) {
        if (commentsUserDTO.getUserAuthorId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        CommentsUserDTO updatedComment = commentsUserService.updateComment(id, commentsUserDTO);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentsUserService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}