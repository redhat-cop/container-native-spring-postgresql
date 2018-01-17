/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.openshift.booster;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.SSLContext;

import com.jayway.restassured.response.ValidatableResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.arquillian.cube.openshift.impl.enricher.RouteURL;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.util.JsonSerialization;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.core.Is.is;

/**
 * @author Heiko Braun
 * @author Ales Justin
 */
@RunWith(Arquillian.class)
public class OpenShiftIT {

    @RouteURL("secure-sso")
    private URL ssoUrlBase;

    @RouteURL("${service.route}")
    private URL applicationUrl;

    private AuthzClient authzClient;

    private AuthzClient getAuthzClient() throws Exception {
        if (authzClient == null) {
            authzClient = createAuthzClient();
        }
        return authzClient;
    }

    private void verifyGreeting(int statusCode, String token, String from) {
        String url = applicationUrl.toString() + "api/greeting" + (from != null ? "?name=" + from : "");

        ValidatableResponse response = given().header("Authorization", "Bearer " + token)
                .get(url)
                .then()
                .statusCode(statusCode);

        if (statusCode == 200) {
            response.body("content", is(String.format("Hello, %s!", from != null ? from : "World")));
        }
    }

    @Test
    public void defaultUser_defaultFrom() throws Exception {
        AccessTokenResponse accessTokenResponse = getAuthzClient().obtainAccessToken("alice", "password");

        verifyGreeting(200, accessTokenResponse.getToken(), null);
    }

    @Test
    public void defaultUser_customFrom() throws Exception {
        AccessTokenResponse accessTokenResponse = getAuthzClient().obtainAccessToken("alice", "password");

        verifyGreeting(200, accessTokenResponse.getToken(), "Scott");
    }

    // This test checks the "authenticated, but not authorized" flow.
    @Test
    public void adminUser() throws Exception {
        AccessTokenResponse accessTokenResponse = getAuthzClient().obtainAccessToken("admin", "admin");

        verifyGreeting(403, accessTokenResponse.getToken(), null);
    }

    @Test
    public void badPassword() throws Exception {
        try {
            getAuthzClient().obtainAccessToken("alice", "bad");
            fail("401 Unauthorized expected");
        } catch (HttpResponseException t) {
            assertThat(t.getStatusCode()).isEqualTo(401);
        }
    }

    /**
     * We need a simplified setup that allows us to work with self-signed certificates.
     * To support this we need to provide a custom http client.
     */
    private AuthzClient createAuthzClient() throws Exception {
        InputStream configStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("keycloak.json");
        if (configStream == null) {
            throw new IllegalStateException("Could not find any keycloak.json file in classpath.");
        }

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial((chain, authType) -> true)
                .build();
        HttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();

        // the injected @RouteURL always contains a port number, which means the URL is different from SSO_AUTH_SERVER_URL,
        // and that causes failures during token validation
        String ssoUrl = ssoUrlBase.toString().replace(":443", "") + "auth";

        System.setProperty("SSO_AUTH_SERVER_URL", ssoUrl);
        Configuration baseline = JsonSerialization.readValue(
                configStream,
                Configuration.class,
                true // system property replacement
        );

        return AuthzClient.create(
                new Configuration(
                        baseline.getAuthServerUrl(),
                        baseline.getRealm(),
                        baseline.getClientId(),
                        baseline.getClientCredentials(),
                        httpClient
                )
        );
    }
}