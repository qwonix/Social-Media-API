package ru.qwonix.test.social.media.api.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.qwonix.test.social.media.api.facade.AuthorizationFacade;
import ru.qwonix.test.social.media.api.serivce.ImageService;
import ru.qwonix.test.social.media.api.serivce.PostService;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AuthorizationFacadeImpl implements AuthorizationFacade {
    private final PostService postService;
    private final ImageService imageService;

    /**
     * @param postId   verifiable post id
     * @param username user profile name
     * @return true if the post belongs to the user or if the post with this postId does not exist
     */
    @Override
    public Boolean isPostOwnerOrIsPostNotFound(UUID postId, String username) {
        return postService.isPostOwner(postId, username) || !postService.existsById(postId);
    }

    /**
     * @param imageName verifiable image id
     * @param username  user profile name
     * @return true if the image belongs to the user or if the image with this imageName does not exist
     */
    @Override
    public Boolean isImageOwnerOrIsImageNotFound(String imageName, String username) {
        return imageService.isImageOwner(imageName, username) || !imageService.existsByName(imageName);
    }
}
