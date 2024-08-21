package com.hoodie.otti.service.community;

import com.hoodie.otti.dto.community.ImageResponseDto;
import com.hoodie.otti.dto.community.PostResponseDto;
import com.hoodie.otti.dto.community.PostSaveRequestDto;
import com.hoodie.otti.dto.community.PostUpdateRequestDto;
import com.hoodie.otti.dto.ott.OttBaseResponseDto;
import com.hoodie.otti.dto.profile.UserNameResponseDto;
import com.hoodie.otti.model.community.Image;
import com.hoodie.otti.model.community.Post;
import com.hoodie.otti.model.ott.Ott;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.community.ImageRepository;
import com.hoodie.otti.repository.community.PostRepository;
import com.hoodie.otti.repository.ott.OttRepository;
import com.hoodie.otti.repository.profile.UserRepository;
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

    @Transactional
    public Long save(PostSaveRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Ott ott = ottRepository.findById(requestDto.getOttId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid ott ID"));

        List<Image> images = imageRepository
                .findAllById(requestDto.getImages())
                .stream()
                .toList();

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .viewCount(0)
                .user(user)
                .ott(ott)
                .build();

        return postRepository.save(post).getId();
    }

    public List<PostResponseDto> findAll() {

        return postRepository.findAll()
                .stream()
                .map(this::convertToPostResponseDto)
                .toList();
    }

    public List<PostResponseDto> findAllByUserId(Long id) {

        return postRepository.findByUserId_Id(id)
                .stream()
                .map(this::convertToPostResponseDto)
                .toList();
    }

    public PostResponseDto findById(Long id) throws IllegalArgumentException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + id));

        return convertToPostResponseDto(post);
    }

    @Transactional
    public Integer updateViewCount(Long id) {
        return postRepository.updateView(id);
    }

    @Transactional
    public Long update(Long id, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + id));

        Optional<Ott> replaceOtt = ottRepository.findById(requestDto.getOttId());

        post.update(
                requestDto.getTitle(),
                requestDto.getContent(),
                replaceOtt.get());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id" + id));
        postRepository.delete(post);
    }

    private PostResponseDto convertToPostResponseDto(Post post) {

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .images(convertToImageResponseDto(post.getImages()))
                .userInfo(convertToUserNameResponseDto(post.getUser()))
                .ottInfo(convertToOttBaseResponseDto(post.getOtt()))
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();
    }

    private List<ImageResponseDto> convertToImageResponseDto(List<Image> images) {

        return images.stream()
                .map(image -> new ImageResponseDto(image.getId(), image.getUrl()))
                .toList();
    }

    private UserNameResponseDto convertToUserNameResponseDto(User user) {

        return new UserNameResponseDto(user.getId(), user.getUsername());
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
