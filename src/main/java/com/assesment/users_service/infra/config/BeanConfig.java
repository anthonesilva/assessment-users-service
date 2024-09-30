package com.assesment.users_service.infra.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.assesment.users_service.domain.ports.in.UserServicePort;
import com.assesment.users_service.domain.ports.out.AvatarResourcePort;
import com.assesment.users_service.domain.ports.out.FriendRepositoryPort;
import com.assesment.users_service.domain.ports.out.LoggerPort;
import com.assesment.users_service.domain.ports.out.UserRepositoryPort;
import com.assesment.users_service.domain.services.UserService;

@Configuration
public class BeanConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public UserServicePort userServicePort(
            UserRepositoryPort userRepositoryPort,
            FriendRepositoryPort friendRepositoryPort,
            AvatarResourcePort avatarResourcePort,
            LoggerPort loggerPort) {
        return new UserService(userRepositoryPort, friendRepositoryPort, avatarResourcePort, loggerPort);
    }

}
