package com.hoodie.otti.service.community;

import com.hoodie.otti.dto.community.CommentResponseDto;
import com.hoodie.otti.dto.community.ImageResponseDto;
import com.hoodie.otti.dto.community.PostDetailResponseDto;
import com.hoodie.otti.dto.community.PostRequestDto;
import com.hoodie.otti.dto.community.PostResponseDto;
import com.hoodie.otti.dto.community.PostResponsePageDto;
import com.hoodie.otti.dto.ott.OttBaseResponseDto;
import com.hoodie.otti.dto.profile.UserResponseDto;
import com.hoodie.otti.model.community.Comment;
import com.hoodie.otti.model.community.Image;
import com.hoodie.otti.model.community.Post;
import com.hoodie.otti.model.ott.Ott;
import com.hoodie.otti.model.pot.Pot;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.community.CommentRepository;
import com.hoodie.otti.repository.community.ImageRepository;
import com.hoodie.otti.repository.community.PostRepository;
import com.hoodie.otti.repository.pot.PotRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PotRepository potRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long save(PostRequestDto requestDto, Principal principal) {
        User user = userRepository.findByKakaoId(Long.parseLong(principal.getName()))
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Pot pot = potRepository.findById(requestDto.getPotId())
                .orElseThrow(() -> new IllegalArgumentException("해당 POT 정보를 찾을 수 없습니다."));

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .viewCount(0)
                .user(user)
                .pot(pot)
                .build();

        Long id = postRepository.save(post).getId();
        mappingPostAndImage(requestDto, post);

        return id;
    }

    public PostResponsePageDto findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").descending());

        return convertToPostResponsePage(postRepository.findAll(pageRequest));
    }

    public PostResponsePageDto findAllByUserId(Principal principal, int page, int size) {
        User user = userRepository.findByKakaoId(Long.parseLong(principal.getName()))
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").descending());

        return convertToPostResponsePage((postRepository.findByUserId_Id(user.getId(), pageRequest)));
    }

    public PostDetailResponseDto findById(Long postId) throws IllegalArgumentException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + postId));

        return convertToPostDetailResponseDto(post);
    }

    public PostResponsePageDto searchByTitle(String title, int page, int size) {
        if (title == null || title.isEmpty()) {
            return PostResponsePageDto.builder()
                    .contents(Collections.emptyList())
                    .currentPage(page)
                    .size(size)
                    .totalPages(0)
                    .totalElements(0L)
                    .build();
        }

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<Post> byTitleContaining = postRepository.findByTitleContaining(title, pageRequest);

        return convertToPostResponsePage(byTitleContaining);
    }

    public PostResponsePageDto findPostsByOttName(String ottName, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").descending());
        return convertToPostResponsePage(postRepository.findByOttName(ottName, pageRequest));
    }

    @Transactional
    public Integer updateViewCount(Long id) {
        return postRepository.updateView(id);
    }

    public Boolean checkAuthorizedPostWriter(Long postId, Principal principal) {
        User user = userRepository.findByKakaoId(Long.parseLong(principal.getName()))
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + postId));

        return user.getId() == post.getUser().getId();
    }

    @Transactional
    public Long updatePost(Long postId, PostRequestDto requestDto, Principal principal) {
        User user = userRepository.findByKakaoId(Long.parseLong(principal.getName()))
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + postId));

        if (user.getId() != post.getUser().getId()) {
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

    private PostResponsePageDto convertToPostResponsePage(Page<Post> postPage) {
        List<PostResponseDto> postResponseDtos = postPage.getContent()
                .stream()
                .map(post -> {
                    int commentCount = commentRepository.countByPost_Id(post.getId());
                    return convertToPostResponseDto(post, commentCount);
                })
                .toList();

        return PostResponsePageDto.builder()
                .contents(postResponseDtos)
                .currentPage(postPage.getNumber() + 1)
                .size(postPage.getSize())
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .build();
    }

    private PostResponseDto convertToPostResponseDto(Post post, int commentCount) {

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .commentCount(commentCount)
                .userName(post.getUser().getUsername())
                .ottImage(post.getPot().getOttId().getImage())
                .createdDate(post.getCreatedDate().format(DATE_TIME_FORMATTER))
                .build();
    }

    private PostDetailResponseDto convertToPostDetailResponseDto(Post post) {

        return PostDetailResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .images(convertToImageResponseDto(post.getImages()))
                .comments(convertToCommentResponseDto(post.getComments()))
                .userInfo(convertToUserResponseDto(post.getUser()))
                .potId(post.getPot().getId())
                .createdDate(post.getCreatedDate().format(DATE_TIME_FORMATTER))
                .build();
    }

    private List<ImageResponseDto> convertToImageResponseDto(List<Image> images) {

        return images.stream()
                .map(image -> new ImageResponseDto(image.getId(), image.getImageUrl()))
                .toList();
    }

    private List<CommentResponseDto> convertToCommentResponseDto(List<Comment> comments) {

        return comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getText(),
                        convertToUserResponseDto(
                                comment.getUser()),
                        comment.getCreatedDate().format(DATE_TIME_FORMATTER),
                        comment.getModifiedDate().format(DATE_TIME_FORMATTER)))
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
