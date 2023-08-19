package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.PostCreateDto;
import ru.qwonix.test.social.media.api.dto.PostUpdateDto;
import ru.qwonix.test.social.media.api.result.CreatePostEntries;
import ru.qwonix.test.social.media.api.result.DeletePostEntries;
import ru.qwonix.test.social.media.api.result.FindPostEntries;
import ru.qwonix.test.social.media.api.result.UpdatePostEntries;

import java.util.UUID;

public interface PostFacade {
    CreatePostEntries.Result create(PostCreateDto postCreateDto, String username);

    UpdatePostEntries.Result update(UUID id, PostUpdateDto postUpdateDto);

    DeletePostEntries.Result delete(UUID id);

    FindPostEntries.Result find(UUID id);
}
