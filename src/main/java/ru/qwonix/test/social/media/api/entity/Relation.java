package ru.qwonix.test.social.media.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "relation")
@IdClass(RelationId.class)
public class Relation {

    public Relation(UserProfile sourceUser, UserProfile targetUser, RelationType relationType) {
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
        this.relationType = relationType;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "source_user_id", referencedColumnName = "id")
    private UserProfile sourceUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "target_user_id", referencedColumnName = "id")
    private UserProfile targetUser;

    @Id
    @Enumerated(EnumType.STRING)
    private RelationType relationType;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
