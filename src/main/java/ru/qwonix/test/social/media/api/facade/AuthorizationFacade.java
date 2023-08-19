package ru.qwonix.test.social.media.api.facade;


import java.util.UUID;

public interface AuthorizationFacade {

    Boolean isPostOwner(UUID postId, String username);
}
