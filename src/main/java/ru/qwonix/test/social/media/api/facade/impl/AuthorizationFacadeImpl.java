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

    @Override
    public Boolean isPostOwner(UUID postId, String username) {
        return postService.isPostOwner(postId, username);
    }

    @Override
    public Boolean isImageOwner(String imageName, String username) {
        return imageService.isImageOwner(imageName, username);
    }
}
