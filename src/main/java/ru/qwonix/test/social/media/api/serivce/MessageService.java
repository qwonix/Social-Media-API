package ru.qwonix.test.social.media.api.serivce;

import ru.qwonix.test.social.media.api.entity.Message;
import ru.qwonix.test.social.media.api.entity.UserProfile;

import java.util.List;

public interface MessageService {
    Message save(Message message);

    List<Message> getMessagesPaginated(UserProfile sender, UserProfile recipient, Integer page, Integer size);
}
