package ru.qwonix.test.social.media.api.serivce;


import ru.qwonix.test.social.media.api.entity.Post;

import java.util.Optional;
import java.util.UUID;

public interface PostService {

    Optional<Post> findById(UUID postId);

    void delete(UUID postId);

    Post createPost(Post post);

    Post updatePost(Post post);

    Boolean existsById(UUID id);

    Boolean isPostOwner(UUID postId, String username);
}
