package com.hoodie.otti.controller.community;

import com.hoodie.otti.dto.community.PostResponseDto;
import com.hoodie.otti.dto.community.PostSaveRequestDto;
import com.hoodie.otti.dto.community.PostUpdateRequestDto;
import com.hoodie.otti.service.community.PostService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody PostSaveRequestDto requestDto) {
        long id = postService.save(requestDto);
        return ResponseEntity.created(URI.create("/api/subscription/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> findAllPosts() {
        return ResponseEntity.ok().body(postService.findAll());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PostResponseDto>> findAllByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(postService.findAllByUserId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findById(@PathVariable Long id) {
        postService.updateViewCount(id);
        return ResponseEntity.ok().body(postService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto) {
        postService.update(id, requestDto);
        return ResponseEntity.ok().body(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
