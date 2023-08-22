package ru.qwonix.test.social.media.api.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.qwonix.test.social.media.api.entity.Message;
import ru.qwonix.test.social.media.api.entity.UserProfile;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("from Message m where m.sender = ?1 and m.recipient = ?2 or  m.sender = ?2 and m.recipient = ?1")
    List<Message> findAllBySenderAndRecipient(UserProfile sender, UserProfile recipient, Pageable pageable);
}
