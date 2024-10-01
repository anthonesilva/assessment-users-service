package com.assesment.users_service.infra.external;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.assesment.users_service.domain.ports.out.AvatarResourcePort;
import com.assesment.users_service.domain.ports.out.LoggerPort;

@Component
public class AvatarResourceHttpAdapter implements AvatarResourcePort {

    private static final String REQRES_API_URL = "https://reqres.in/api/users";
    private static final int MAX_RETRIES = 3;
    private final RestClient restClient;

    private final LoggerPort logger;
    public AvatarResourceHttpAdapter(RestClient.Builder restClientBuilder, LoggerPort logger) {
        this.restClient = restClientBuilder.baseUrl(REQRES_API_URL).build();
        this.logger = logger;
    }

    @Override
    public String findAvatarUrl(Long userId) {
        boolean success = false;
        int attempt = 0;
        String avatarUrlResponse = null;

        while (!success && attempt < MAX_RETRIES) {
            try {
                ResponseEntity<DataWrapper> response = restClient.get().uri("/{id}", userId).retrieve().toEntity(DataWrapper.class);
   
                if (response.hasBody()) {
                    DataWrapper data = response.getBody();
                    if (data != null && data.getData() != null) {
                        avatarUrlResponse = data.getData().getAvatar();
                    }
                }
                success = true;
            } catch (HttpClientErrorException e) {
                logger.log("WARN", String.format("[findAvatarUrl] Retry attempt %s time(s)", attempt));
                attempt++;
            } catch (Exception e) {
                attempt = MAX_RETRIES;
            }
        }
        return avatarUrlResponse;
    }

}
