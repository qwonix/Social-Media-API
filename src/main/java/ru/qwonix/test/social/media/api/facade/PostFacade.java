package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.dto.PostCreateDto;
import ru.qwonix.test.social.media.api.dto.PostUpdateDto;
import ru.qwonix.test.social.media.api.result.CreatePostEntries;
import ru.qwonix.test.social.media.api.result.DeletePostEntries;
import ru.qwonix.test.social.media.api.result.FindPostEntries;
import ru.qwonix.test.social.media.api.result.UpdatePostEntries;

public interface PostFacade {
    CreatePostEntries.Result create(PostCreateDto postCreateDto, String username);

    UpdatePostEntries.Result update(Long id, PostUpdateDto postUpdateDto);

    DeletePostEntries.Result delete(Long id);

    FindPostEntries.Result find(Long id);
}
