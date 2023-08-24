package ru.qwonix.test.social.media.api.facade;


import java.util.UUID;

public interface AuthorizationFacade {

    boolean isPostOwnerOrIsPostNotFound(UUID postId, String username);

    boolean isImageOwnerOrIsImageNotFound(String imageName, String username);
}
