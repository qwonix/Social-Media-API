package ru.qwonix.test.social.media.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.qwonix.test.social.media.api.entity.Relation;
import ru.qwonix.test.social.media.api.entity.RelationId;
import ru.qwonix.test.social.media.api.entity.RelationType;
import ru.qwonix.test.social.media.api.entity.UserProfile;

import java.util.List;


public interface RelationRepository extends JpaRepository<Relation, RelationId> {


}
