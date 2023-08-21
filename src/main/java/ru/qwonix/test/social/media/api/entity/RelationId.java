package ru.qwonix.test.social.media.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationId implements Serializable {
    private UserProfile sourceUser;
    private UserProfile targetUser;
    private RelationType relationType;
}
