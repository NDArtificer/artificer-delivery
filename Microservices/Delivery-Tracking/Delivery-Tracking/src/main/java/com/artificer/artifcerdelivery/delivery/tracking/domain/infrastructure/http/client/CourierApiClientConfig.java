package com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.http.client;

import com.artificer.artifcerdelivery.delivery.tracking.domain.infrastructure.fake.CourierApiClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;
import java.util.List;

@Configuration
public class CourierApiClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }


    @Bean
    public CourierApiClient courierApiClient(RestClient.Builder builder,
    AuthorizedClientServiceOAuth2AuthorizedClientManager clientManager
    ) {
        RestClient restClient = builder.baseUrl("http://courier-management")
                .requestFactory(generateClientHttpRequestFactory(clientManager))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();

        return proxyFactory.createClient(CourierApiClient.class);
    }

    private ClientHttpRequestFactory generateClientHttpRequestFactory(AuthorizedClientServiceOAuth2AuthorizedClientManager clientManager) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(1000));
        factory.setReadTimeout(20000);

        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("artificer-client")
                    .principal("courier-backend")
                    .build();

            OAuth2AuthorizedClient authorizedClient = clientManager.authorize(authorizeRequest);
            String token = authorizedClient.getAccessToken().getTokenValue();

            request.getHeaders().add("Authorization", "Bearer %s".formatted(token));
            return execution.execute(request, body);
        };
        return new InterceptingClientHttpRequestFactory(factory, List.of(authInterceptor));
    }



}
