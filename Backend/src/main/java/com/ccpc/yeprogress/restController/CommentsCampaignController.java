package com.ccpc.yeprogress.restController;

import com.ccpc.yeprogress.dto.CommentsCampaignDTO;
import com.ccpc.yeprogress.service.CommentsCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaign-comments")
public class CommentsCampaignController {

    private final CommentsCampaignService commentsCampaignService;

    @Autowired
    public CommentsCampaignController(CommentsCampaignService commentsCampaignService) {
        this.commentsCampaignService = commentsCampaignService;
    }

    @PostMapping
    public ResponseEntity<CommentsCampaignDTO> createComment(@RequestBody CommentsCampaignDTO commentsCampaignDTO) {
        CommentsCampaignDTO createdComment = commentsCampaignService.createComment(commentsCampaignDTO);
        return ResponseEntity.ok(createdComment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentsCampaignDTO> getCommentById(@PathVariable Long id) {
        CommentsCampaignDTO commentDTO = commentsCampaignService.getCommentById(id);
        return ResponseEntity.ok(commentDTO);
    }

    @GetMapping
    public ResponseEntity<List<CommentsCampaignDTO>> getAllComments() {
        List<CommentsCampaignDTO> comments = commentsCampaignService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentsCampaignDTO> updateComment(@PathVariable Long id, @RequestBody CommentsCampaignDTO commentsCampaignDTO) {
        CommentsCampaignDTO updatedComment = commentsCampaignService.updateComment(id, commentsCampaignDTO);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentsCampaignService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
