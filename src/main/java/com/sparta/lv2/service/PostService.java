package com.sparta.lv2.service;

import com.sparta.lv2.dto.PostListResponseDto;
import com.sparta.lv2.dto.PostRequestDto;
import com.sparta.lv2.dto.PostResponseDto;
import com.sparta.lv2.entity.Post;
import com.sparta.lv2.entity.User;
import com.sparta.lv2.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;


    // PostRequestDto 와 User 객체를 인자로 받아서 PostResponseDto 를 반환하는 메서드
    // PostRequestDto 는 새로운 포스트를 생성하기 위해 클라이언트에서 보내온 데이터를 담고 있다
    // User 는 해당 포스트를 작성한 사용자를 나타내는 객체이다!
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {

        // Post 객체를 생성
        // -> Post 클래스의 생성자에 requestDto 를 전달해 새로운 Post 객체를 만든다
        // PostRequestDot 는 클라이언트로부터 전달된 데이터를 가지고 있고,
        // 이를 사용해서 새로운 Post 객체를 초기화 한다
        Post post = new Post(requestDto);

        // 생성된 Post 객체에 작성자인 User 객체를 설정한다
        // 호출해서 해당 포스트의 작성자 정보를 설정한다
        post.setUser(user);


        // 새로운 포스트를 데이터베이스에 저장한다!
        postRepository.save(post);

        // PostResponseDto 객체를 생성하고, 생성된 Post 객체를 이용해 해당 DTO 를 초기화함
        // -> 그리고 반환! 클라이언트에게 새로 생성된 포스트의 정보를 응답하기 위함
        return new PostResponseDto(post);

    }

    public PostListResponseDto getPosts() {
        List<PostResponseDto> postList = postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostListResponseDto(postList);
    }


    public PostResponseDto getPostById(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }




    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = findPost(id);

        // !! getUserId !!!!!!!!
        if (!post.getUser().getUsername().equals(user.getUsername())) {
            throw new RejectedExecutionException();
        } // throw new~~ 잘 모르겠음~~

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());

        return new PostResponseDto(post);

    }

    public void deletePost(Long id, User user) {
        Post post = findPost(id);


        // post.getUser() : 삭제하려는 포스트의 작성자
        // user : 삭제를 시도하는 사용자
        // 만약 작성자와 사용자가 다르면 예외를 발생시킴
        // 해당 사용자가 다른 사용자의 포스트를 삭제할 수 없도록 보안적인 조치
        if (!post.getUser().equals(user)) {
            throw new RejectedExecutionException();
        }

        postRepository.delete(post);
    }



    // 존재하지 않는 포스트에 대한 예외 처리
    private Post findPost(Long id) {

        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }
}
