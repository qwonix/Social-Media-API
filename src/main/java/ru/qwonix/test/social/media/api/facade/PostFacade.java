package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.AttachImageRequestDto;
import ru.qwonix.test.social.media.api.dto.DetachImageRequestDto;
import ru.qwonix.test.social.media.api.dto.PostCreateDto;
import ru.qwonix.test.social.media.api.dto.PostUpdateDto;
import ru.qwonix.test.social.media.api.result.*;

import java.util.UUID;

public interface PostFacade {
    CreatePostEntries.Result create(PostCreateDto postCreateDto, String username);

    UpdatePostEntries.Result update(UUID id, PostUpdateDto postUpdateDto);

    DeletePostEntries.Result delete(UUID id);

    FindPostEntries.Result find(UUID id);

    AttachImageToPostEntries.Result attachImage(UUID id, AttachImageRequestDto imageName);

    DetachImageFromPostEntries.Result detachImage(UUID id, DetachImageRequestDto imageName);
}
