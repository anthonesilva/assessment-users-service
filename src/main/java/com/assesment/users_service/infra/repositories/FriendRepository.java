package com.assesment.users_service.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assesment.users_service.infra.repositories.entities.FriendEntity;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
}
