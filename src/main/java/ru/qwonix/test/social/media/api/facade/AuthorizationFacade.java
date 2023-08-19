package ru.qwonix.test.social.media.api.facade;


public interface AuthorizationFacade {

    Boolean isPostOwner(Long postId, String username);
}
