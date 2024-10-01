package com.assesment.users_service.infra.repositories.entities;

import com.assesment.users_service.domain.Friend;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "friend")
@Table(name = "friends")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Nonnull
    private String firstName;

    @Column
    @Nonnull
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public Friend toDomain() {
        Friend friend = new Friend();
        friend.setId(this.id);
        friend.setFirstName(this.firstName);
        friend.setLastName(this.lastName);
        return friend;
    }

    public static FriendEntity fromDomain(Friend friend) {
        FriendEntity friendEntity = new FriendEntity();
        if (friend.getId() != null) friendEntity.setId(friend.getId());
        if (friend.getFirstName() != null) friendEntity.setFirstName(friend.getFirstName());
        if (friend.getLastName() != null) friendEntity.setLastName(friend.getLastName());
        if (friend.getUser() != null) friendEntity.setUser(UserEntity.fromDomain(friend.getUser()));
        return friendEntity;
    }
}
