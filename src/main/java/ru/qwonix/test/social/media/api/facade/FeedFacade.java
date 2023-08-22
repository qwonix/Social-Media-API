package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.result.GetFeedEntries;

public interface FeedFacade {
    GetFeedEntries.Result findFeedPaginated(String username, int page, int size);

}
