package ru.qwonix.test.social.media.api.entity;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelationId implements Serializable {
    private UserProfile sourceUser;
    private UserProfile targetUser;
    private RelationType relationType;
}
