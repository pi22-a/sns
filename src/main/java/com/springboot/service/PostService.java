package com.springboot.service;

import com.springboot.domain.entity.Post;
import com.springboot.domain.entity.User;
import com.springboot.dto.PostCreateRequest;
import com.springboot.dto.PostCreateResponse;
import com.springboot.dto.PostDetailsResponse;
import com.springboot.repository.PostRepository;
import com.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository ur;
    private final PostRepository pr;



    public PostCreateResponse newPost(PostCreateRequest dto, Authentication authentication) {

        Optional<User> userOpt = ur.findByUserName(authentication.getName());
        Post post = Post.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
                .userName(authentication.getName())
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .user(userOpt.get())
                .build();

        Post savedPost = pr.save(post);
        return PostCreateResponse.builder()
                .message("포스트 등록 완료")
                .postId(savedPost.getPostId())
                .build();
    }

    public PostDetailsResponse showOnePost(Long id) {
        Optional<Post> postOpt = pr.findById(id);

        return PostDetailsResponse.builder()
                .id(postOpt.get().getPostId())
                .title(postOpt.get().getTitle())
                .body(postOpt.get().getBody())
                .userName(postOpt.get().getUserName())
                .createdAt(postOpt.get().getCreatedAt())
                .LastModifiedAt(postOpt.get().getLastModifiedAt())
                .build();

    }

    public List<PostDetailsResponse> showPosts(Pageable pageable) {
        Page<Post> posts = pr.findAll(pageable);
        List<PostDetailsResponse> postResponses = posts.stream()
                .map(post -> Post.of(post)).collect(Collectors.toList());
        return postResponses;
    }


}