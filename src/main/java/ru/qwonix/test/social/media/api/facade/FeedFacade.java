package ru.qwonix.test.social.media.api.facade;

import ru.qwonix.test.social.media.api.result.FindFeed;

public interface FeedFacade {
    FindFeed.Result findFeedPaginated(String username, int page, int size);

}
