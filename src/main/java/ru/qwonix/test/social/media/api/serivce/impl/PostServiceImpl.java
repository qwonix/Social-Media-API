package ru.qwonix.test.social.media.api.serivce.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.qwonix.test.social.media.api.entity.Post;
import ru.qwonix.test.social.media.api.entity.UserProfile;
import ru.qwonix.test.social.media.api.repository.PostRepository;
import ru.qwonix.test.social.media.api.serivce.PostService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Boolean existsById(UUID id) {
        return postRepository.existsById(id);
    }

    @Override
    public Boolean isPostOwner(UUID postId, String username) {
        return postRepository.existsByIdAndUserUsername(postId, username);
    }

    @Override
    public List<Post> findUsersPostsPaginatedAndSortedByDate(List<UserProfile> userProfiles, int page, int size) {
        return postRepository.findAllByUserIn(userProfiles,
                PageRequest.of(
                        page,
                        size,
                        Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Optional<Post> findById(UUID id) {
        return postRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
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
