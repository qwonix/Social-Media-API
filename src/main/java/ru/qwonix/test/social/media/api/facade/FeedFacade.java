package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.result.FindFeedEntries;

public interface FeedFacade {
    FindFeedEntries.Result findFeedPaginated(String username, int page, int size);

}
