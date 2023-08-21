package ru.qwonix.test.social.media.api.facade;


import java.util.UUID;

public interface AuthorizationFacade {

    Boolean isPostOwnerOrIsPostNotFound(UUID postId, String username);

    Boolean isImageOwnerOrIsImageNotFound(String imageName, String username);
}
