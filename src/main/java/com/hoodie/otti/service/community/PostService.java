package com.hoodie.otti.service.community;

import com.hoodie.otti.dto.community.ImageResponseDto;
import com.hoodie.otti.dto.community.PostDetailResponseDto;
import com.hoodie.otti.dto.community.PostRequestDto;
import com.hoodie.otti.dto.community.PostResponseDto;
import com.hoodie.otti.dto.ott.OttBaseResponseDto;
import com.hoodie.otti.dto.profile.UserResponseDto;
import com.hoodie.otti.model.community.Image;
import com.hoodie.otti.model.community.Post;
import com.hoodie.otti.model.ott.Ott;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.community.ImageRepository;
import com.hoodie.otti.repository.community.PostRepository;
import com.hoodie.otti.repository.ott.OttRepository;
import com.hoodie.otti.repository.pot.PotRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final OttRepository ottRepository;
    private final PotRepository potRepository;

    @Transactional
    public Long save(PostRequestDto requestDto, Principal principal) {
        Optional<User> user = userRepository.findByKakaoId(Long.parseLong(principal.getName()));

        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        Pot pot = potRepository.findById(requestDto.getPotId())
                .orElseThrow(() -> new IllegalArgumentException("해당 POT 정보를 찾을 수 없습니다."));

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .viewCount(0)
                .user(user.get())
                .pot(pot)
                .build();

        Long id = postRepository.save(post).getId();
        mappingPostAndImage(requestDto, post);

        return id;
    }

    public List<PostResponseDto> findAll() {

        return postRepository.findAll()
                .stream()
                .map(this::convertToPostResponseDto)
                .toList();
    }

    public List<PostResponseDto> findAllByUserId(Principal principal) {
        Optional<User> user = userRepository.findByKakaoId(Long.parseLong(principal.getName()));

        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        return postRepository.findByUserId_Id(user.get().getId())
                .stream()
                .map(this::convertToPostResponseDto)
                .toList();
    }

    public PostDetailResponseDto findById(Long postId) throws IllegalArgumentException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + postId));

        return convertToPostDetailResponseDto(post);
    }

    @Transactional
    public Integer updateViewCount(Long id) {
        return postRepository.updateView(id);
    }

    @Transactional
    public Long updatePost(Long postId, PostRequestDto requestDto, Principal principal) {
        Optional<User> user = userRepository.findByKakaoId(Long.parseLong(principal.getName()));

        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + postId));

        if (user.get().getId() != post.getUser().getId()) {
            throw new IllegalArgumentException("타인이 작성한 글은 수정할 수 없습니다.");
        }

        Optional<Pot> replacePot = requestDto.getPotId() != null ?
                potRepository.findById(requestDto.getPotId()) :
                Optional.empty();
        post.update(requestDto.getTitle(), requestDto.getContent(), replacePot);

        imageRepository.findByPostId_Id(postId)
                .stream()
                .peek(image -> image.update(null))
                .toList();
        mappingPostAndImage(requestDto, post);

        return postId;
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id" + id));
        postRepository.delete(post);
    }

    private void mappingPostAndImage(PostRequestDto postRequestDto, Post post) {
        imageRepository.findAllById(
                postRequestDto.getImages())
                .forEach(image -> image.mappingPost(post));
    }

    private PostResponseDto convertToPostResponseDto(Post post) {

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .userName(post.getUser().getUsername())
                .ottImage(post.getPot().getOttId().getImage())
                .createdDate(post.getCreatedDate())
                .build();
    }

    private PostDetailResponseDto convertToPostDetailResponseDto(Post post) {

        return PostDetailResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .images(convertToImageResponseDto(post.getImages()))
                .userInfo(convertToUserResponseDto(post.getUser()))
                .potId(post.getPot().getId())
                .createdDate(post.getCreatedDate())
                .build();
    }

    private List<ImageResponseDto> convertToImageResponseDto(List<Image> images) {

        return images.stream()
                .map(image -> new ImageResponseDto(image.getId(), image.getImageUrl()))
                .toList();
    }

    private UserResponseDto convertToUserResponseDto(User user) {

        return new UserResponseDto(user.getUsername(), user.getProfilePhotoUrl());
    }

    private OttBaseResponseDto convertToOttBaseResponseDto(Ott ott) {

        return OttBaseResponseDto.builder()
                .id(ott.getId())
                .name(ott.getName())
                .ratePlan(ott.getRatePlan())
                .price(ott.getPrice())
                .image(ott.getImage())
                .build();
    }
}
