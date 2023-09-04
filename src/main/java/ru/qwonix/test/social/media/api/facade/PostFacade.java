package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.AttachImageRequest;
import ru.qwonix.test.social.media.api.dto.CreatePostRequest;
import ru.qwonix.test.social.media.api.dto.DetachImageRequest;
import ru.qwonix.test.social.media.api.dto.UpdatePostRequest;
import ru.qwonix.test.social.media.api.result.*;

import java.util.UUID;

public interface PostFacade {
    CreatePost.Result create(CreatePostRequest createPostRequest, String username);

    UpdatePost.Result update(UUID id, UpdatePostRequest updatePostRequest);

    DeletePost.Result delete(UUID id);

    FindPost.Result find(UUID id);

    AttachImageToPost.Result attachImage(UUID id, AttachImageRequest imageName);

    DetachImageFromPost.Result detachImage(UUID id, DetachImageRequest imageName);
}
