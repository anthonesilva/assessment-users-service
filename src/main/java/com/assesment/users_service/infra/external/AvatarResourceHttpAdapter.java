package com.assesment.users_service.infra.external;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.assesment.users_service.domain.ports.out.AvatarResourcePort;

@Component
public class AvatarResourceHttpAdapter implements AvatarResourcePort {

    private static final String REQRES_API_URL = "https://reqres.in/api/users";
    private final RestClient restClient;

    public AvatarResourceHttpAdapter(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(REQRES_API_URL).build();
    }

    @Override
    public String findAvatarUrl(Long userId) throws Exception {
        DataWrapper data = restClient.get().uri("/{id}", userId).retrieve().body(DataWrapper.class);
        System.err.println(data);

        if (data != null && data.getData() != null) {
            return data.getData().getAvatar();
        } else {
            return new String();
        }        
    }

}
