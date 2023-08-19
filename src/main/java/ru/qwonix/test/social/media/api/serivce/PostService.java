package ru.qwonix.test.social.media.api.serivce;


import ru.qwonix.test.social.media.api.entity.Post;

import java.util.Optional;

public interface PostService {

    Optional<Post> findById(Long postId);

    void delete(Long postId);

    Post createPost(Post post);

    Post updatePost(Post post);

    Boolean existsById(Long id);

    Boolean isPostOwner(Long postId, String username);
}
