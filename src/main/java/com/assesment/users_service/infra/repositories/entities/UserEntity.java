package com.assesment.users_service.infra.repositories.entities;

import java.util.List;
import java.util.stream.Collectors;

import com.assesment.users_service.domain.User;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user")
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    @Nonnull
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String avatar;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendEntity> friends;

    public User toDomain() {
        User user = new User();
        user.setId(this.id);
        user.setEmail(this.email);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setAvatar(this.avatar);
        user.setFriends(this.friends.stream()
                .map(FriendEntity::toDomain)
                .collect(Collectors.toList()));
        return user;
    }

    public static UserEntity fromDomain(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setEmail(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setAvatar(user.getAvatar());
        userEntity.setFriends(user.getFriends().stream()
                .map(FriendEntity::fromDomain)
                .collect(Collectors.toList()));
        return userEntity;
    }

}
