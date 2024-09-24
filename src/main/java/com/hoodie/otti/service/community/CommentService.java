package com.hoodie.otti.service.community;

import com.hoodie.otti.dto.community.CommentSaveRequestDto;
import com.hoodie.otti.dto.community.CommentUpdateRequestDto;
import com.hoodie.otti.model.community.Comment;
import com.hoodie.otti.model.community.Post;
import com.hoodie.otti.model.profile.User;
import com.hoodie.otti.repository.community.CommentRepository;
import com.hoodie.otti.repository.community.PostRepository;
import com.hoodie.otti.repository.profile.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long save(CommentSaveRequestDto requestDto, Principal principal) {
        Optional<User> user = userRepository.findByKakaoId(Long.parseLong(principal.getName()));

        if (user.isEmpty()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        Post post = postRepository.findById(requestDto.getPost())
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .text(requestDto.getText())
                .post(post)
                .user(user.get())
                .build();

        return commentRepository.save(comment).getId();
    }

    public Boolean checkAuthorizedCommentWriter(Long commentId, Principal principal) {
        User user = userRepository.findByKakaoId(Long.parseLong(principal.getName()))
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + commentId));

        return user.getId() == comment.getUser().getId();
    }

    @Transactional
    public Long update(Long commentId, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        comment.update(requestDto.getText());

        return commentId;
    }

    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }
}
