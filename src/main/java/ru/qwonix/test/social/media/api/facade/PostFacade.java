package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.AttachImageRequest;
import ru.qwonix.test.social.media.api.dto.CreatePostRequest;
import ru.qwonix.test.social.media.api.dto.DetachImageRequest;
import ru.qwonix.test.social.media.api.dto.UpdatePostRequest;
import ru.qwonix.test.social.media.api.result.*;

import java.util.UUID;

public interface PostFacade {
    CreatePostEntries.Result create(CreatePostRequest createPostRequest, String username);

    UpdatePostEntries.Result update(UUID id, UpdatePostRequest updatePostRequest);

    DeletePostEntries.Result delete(UUID id);

    FindPostEntries.Result find(UUID id);

    AttachImageToPostEntries.Result attachImage(UUID id, AttachImageRequest imageName);

    DetachImageFromPostEntries.Result detachImage(UUID id, DetachImageRequest imageName);
}
