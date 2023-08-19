package ru.qwonix.test.social.media.api.serivce.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.qwonix.test.social.media.api.entity.Post;
import ru.qwonix.test.social.media.api.repository.PostRepository;
import ru.qwonix.test.social.media.api.serivce.PostService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Boolean existsById(Long id) {
        return postRepository.existsById(id);
    }

    @Override
    public Boolean isPostOwner(Long postId, String username) {
        return postRepository.existsByIdAndUserUsername(postId, username);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }


}
