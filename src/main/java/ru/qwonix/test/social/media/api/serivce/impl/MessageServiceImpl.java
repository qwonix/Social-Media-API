package ru.qwonix.test.social.media.api.serivce.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.qwonix.test.social.media.api.entity.Message;
import ru.qwonix.test.social.media.api.entity.UserProfile;
import ru.qwonix.test.social.media.api.repository.MessageRepository;
import ru.qwonix.test.social.media.api.serivce.MessageService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findMessagesPaginatedAndSortedBySendingDateDesc(UserProfile participant1, UserProfile participant2, Integer page, Integer size) {
        return messageRepository.findAllBySenderAndRecipient(participant1, participant2,
                PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.DESC, "createdAt")));
    }
}
