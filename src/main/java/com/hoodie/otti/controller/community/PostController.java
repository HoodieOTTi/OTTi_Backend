package com.hoodie.otti.controller.community;

import com.amazonaws.Response;
import com.hoodie.otti.dto.community.ImageResponseDto;
import com.hoodie.otti.dto.community.PostDetailResponseDto;
import com.hoodie.otti.dto.community.PostRequestDto;
import com.hoodie.otti.dto.community.PostResponseDto;
import com.hoodie.otti.dto.community.UploadImageRequestDto;
import com.hoodie.otti.service.community.PostImageService;
import com.hoodie.otti.service.community.PostService;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final PostImageService postImageService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody PostRequestDto requestDto, Principal principal) {
        long id = postService.save(requestDto, principal);
        return ResponseEntity.created(URI.create("/api/subscription/" + id)).build();
    }

    @PostMapping("/image")
    public ResponseEntity<ImageResponseDto> uploadImage(@ModelAttribute UploadImageRequestDto requestDto)
            throws IOException {
        return ResponseEntity.ok().body(postImageService.saveImage(requestDto));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> findAllPosts() {
        return ResponseEntity.ok().body(postService.findAll());
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostResponseDto>> findAllByUserId(Principal principal) {
        return ResponseEntity.ok().body(postService.findAllByUserId(principal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponseDto> findById(@PathVariable Long id) {
        postService.updateViewCount(id);
        return ResponseEntity.ok().body(postService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        postService.update(id, requestDto);
        return ResponseEntity.ok().body(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
