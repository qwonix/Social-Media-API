package ru.qwonix.test.social.media.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation relation = (Relation) o;
        return Objects.equals(sourceUser, relation.sourceUser) && Objects.equals(targetUser, relation.targetUser) && relationType == relation.relationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceUser, targetUser, relationType);
    }
}
