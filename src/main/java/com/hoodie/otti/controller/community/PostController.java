package com.hoodie.otti.controller.community;

import com.hoodie.otti.dto.community.CommentSaveRequestDto;
import com.hoodie.otti.dto.community.CommentUpdateRequestDto;
import com.hoodie.otti.dto.community.ImageResponseDto;
import com.hoodie.otti.dto.community.PostDetailResponseDto;
import com.hoodie.otti.dto.community.PostRequestDto;
import com.hoodie.otti.dto.community.PostResponsePageDto;
import com.hoodie.otti.dto.community.ProfileImageResponseDto;
import com.hoodie.otti.dto.community.UploadImageRequestDto;
import com.hoodie.otti.service.community.CommentService;
import com.hoodie.otti.service.community.ImageService;
import com.hoodie.otti.service.community.PostService;
import jakarta.validation.constraints.Positive;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final ImageService imageService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> savePost(@RequestBody PostRequestDto requestDto, Principal principal) {
        long id = postService.save(requestDto, principal);
        return ResponseEntity.created(URI.create("/api/post/" + id)).build();
    }

    @PostMapping("/image")
    public ResponseEntity<ImageResponseDto> uploadImage(@ModelAttribute UploadImageRequestDto requestDto) throws IOException {
        return ResponseEntity.ok().body(imageService.savePostImage(requestDto));
    }

    @PostMapping("/profile/image")
    public ResponseEntity<ProfileImageResponseDto> uploadProfileImage(@ModelAttribute UploadImageRequestDto requestDto) throws IOException {
        return ResponseEntity.ok().body(imageService.saveProfileImage(requestDto));
    }

    @PostMapping("/comment")
    public ResponseEntity<Void> saveComment(@RequestBody CommentSaveRequestDto requestDto, Principal principal) {
        return ResponseEntity.created(URI.create("/api/post/comment/" + commentService.save(requestDto, principal))).build();
    }

    @GetMapping("/comment/{commentId}/check-writer")
    public ResponseEntity<Boolean> checkAuthorizedCommentWriter(@PathVariable Long commentId, Principal principal) {
        return ResponseEntity.ok().body(commentService.checkAuthorizedCommentWriter(commentId, principal));
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Long> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequestDto requestDto) {
        return ResponseEntity.ok().body(commentService.update(commentId, requestDto));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PostResponsePageDto> findAllPosts(@RequestParam(defaultValue = "1") @Positive int page,
                                                            @RequestParam(defaultValue = "10") @Positive int size) {
        return ResponseEntity.ok().body(postService.findAll(page, size));
    }

    @GetMapping("/user")
    public ResponseEntity<PostResponsePageDto> findAllByUserId(Principal principal,
                                                               @RequestParam(defaultValue = "1") @Positive int page,
                                                               @RequestParam(defaultValue = "10") @Positive int size) {
        return ResponseEntity.ok().body(postService.findAllByUserId(principal, page, size));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> findById(@PathVariable Long postId) {
        postService.updateViewCount(postId);
        return ResponseEntity.ok().body(postService.findById(postId));
    }

    @GetMapping("/search")
    public ResponseEntity<PostResponsePageDto> searchTitle(@RequestParam(value = "title") String title,
                                                           @RequestParam(defaultValue = "1") @Positive int page,
                                                           @RequestParam(defaultValue = "10") @Positive int size) {
        return ResponseEntity.ok().body(postService.searchByTitle(title, page, size));
    }

    @GetMapping("/{postId}/check-writer")
    public ResponseEntity<Boolean> checkAuthorizedPostWriter(@PathVariable Long postId, Principal principal) {
        return ResponseEntity.ok().body(postService.checkAuthorizedPostWriter(postId, principal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody PostRequestDto requestDto, Principal principal) {
        postService.updatePost(id, requestDto, principal);
        return ResponseEntity.ok().body(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
