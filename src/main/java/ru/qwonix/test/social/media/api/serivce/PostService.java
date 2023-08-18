package ru.qwonix.test.social.media.api.serivce;


import ru.qwonix.test.social.media.api.entity.Post;

import java.util.Optional;

public interface PostService {

    Optional<Post> findPostById(Long postId);

    void deletePost(Long postId);
}
