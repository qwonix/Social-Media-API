package ru.qwonix.test.social.media.api.serivce;

import ru.qwonix.test.social.media.api.entity.Message;
import ru.qwonix.test.social.media.api.entity.UserProfile;

import java.util.List;

public interface MessageService {
    /**
     * Saves the given message
     *
     * @param message message to be saved
     * @return saved message
     */
    Message save(Message message);

    /**
     * Retrieves a paginated list of messages between the specified users,
     * sorted by descending date of message sending - new first
     *
     * @param participant1 first participant of the correspondence
     * @param participant2 second participant of the correspondence
     * @param page         page number for pagination (0-indexed)
     * @param size         number of messages per page
     * @return list of messages
     */
    List<Message> findMessagesPaginatedAndSortedByDate(UserProfile participant1, UserProfile participant2, Integer page, Integer size);
}