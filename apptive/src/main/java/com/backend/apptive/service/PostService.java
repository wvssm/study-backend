package com.backend.apptive.service;

import com.backend.apptive.domain.Post;

import com.backend.apptive.domain.User;
import com.backend.apptive.dto.PostDto;
import com.backend.apptive.exception.ResourceNotFoundException;
import com.backend.apptive.repository.PostRepository;
import com.backend.apptive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {
    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private  final UserRepository userRepository;

    @Transactional
    public void create(PostDto.Request request) {
        postRepository.save(request.toEntity(userRepository));
    }

    public List<PostDto.Response> findAll() {
        List<PostDto.Response> posts = postRepository.findAll()
                .stream()
                .map(PostDto.Response::toDto)
                .toList();
        return posts;
    }

    public PostDto.DetailResponse findByPostId(Long postId) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물 " + postId + "이 존재하지 않습니다."));
        return PostDto.DetailResponse.toDto(post);
    }

    public List<PostDto.Response> findByUserEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("유저 " + userEmail +"가 존재하지 않습니다."));
        return postRepository.findByUserEmail(user.getEmail())
                .stream()
                .map(PostDto.Response::toDto)
                .toList();
    }
}
