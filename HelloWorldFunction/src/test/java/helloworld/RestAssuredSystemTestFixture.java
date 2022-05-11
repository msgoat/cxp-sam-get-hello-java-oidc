package helloworld;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Common test fixture for {@code RestAssured} based system tests.
 * <p>
 * Performs an OpenID Connect login to the OpenID Connect provider specified in either properties file
 * {@code META-INF/test-config.properties} or in system properties or in environment variables.
 * </p>
 * <p>
 * Tokens obtained during this login can be retrieved via {@code #getToken}.
 * Performs an OpenID Connect login to the
 * OpenID Connect provider specified in either properties file {@code META-INF/test-config.properties} or
 * in system properties or in environment variables.
 * </p>
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 2.0
 * @since release 0.8.0
 */
public class RestAssuredSystemTestFixture {

    private Logger logger = Logger.getLogger(getClass().getName());

    private CommonTestConfig config;
    private String accessToken;
    private String idToken;

    /**
     * Resets this fixture after all tests have been executed
     */
    public void onAfter() {
        RestAssured.reset();
    }

    /**
     * Sets up this fixture before all tests are run.
     * <p>
     * A typical setup consists of the following steps:
     * </p>
     * <ul>
     * <li>Retrieve configuration from properties file {@code META-INF/test-config.properties} or
     * from system properties or from environment variables.</li>
     * <li>Retrieve target route to REST endpoint to be tested from system property {@code target.route}
     * or environment variable {@code TARGET_ROUTE}.</li>
     * <li>Performs a login to the specified OpenID Connect provider obtaining an access accessToken and an ID accessToken.</li>
     * <li>Waits to the specified target service to become ready by checking its readiness probe
     * at URI {@code api/v1/probes/readiness}.</li>
     * </ul>
     */
    public void onBefore() {
        ensureConfiguration();
        LogConfig augmentedLogConfig = RestAssured.config.getLogConfig();
        augmentedLogConfig.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation()).logConfig(augmentedLogConfig);
        RestAssured.baseURI = config.getTargetRoute();
        ensureTokens();
        ensureApplicationReadiness();
    }

    /**
     * Returns the access token obtained during login.
     */
    public String getToken() {
        return this.accessToken;
    }

    /**
     * Returns the access token obtained during login.
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * Returns the ID token obtained during login.
     */
    public String getIdToken() {
        return this.idToken;
    }

    /**
     * Returns the common test configuration this fixture uses.
     */
    public CommonTestConfig getConfig() {
        if (this.config == null) {
            throw new IllegalStateException("Fixture has not been properly initialized! Please call onBefore() first.");
        }
        return config;
    }

    /**
     * Reads some configuration from a config datasource.
     */
    private void ensureConfiguration() {
        if (this.config == null) {
            this.config = new CommonTestConfigImpl();
        }
    }

    /**
     * Waits until the application is actually ready to accept requests.
     */
    private void ensureApplicationReadiness() {
    }

    private void ensureTokens() {
        if (this.accessToken == null && !this.config.isSkipOpenIdConnectLogin()) {
            try {
                Response response = RestAssured.given()
                        .param("scope", "openid microprofile-jwt")
                        .param("grant_type", "password")
                        .param("username", this.config.getOidcUserName())
                        .param("password", this.config.getOidcPassword())
                        .param("client_id", this.config.getOidcClientId())
                        .param("client_secret", this.config.getOidcClientSecret())
                        .when()
                        .post(this.config.getOidcAccessTokenUri());
                if (response.getStatusCode() != 200) {
                    throw new IllegalStateException(String.format("Expected status code 200 but got [%s] with status message [%s] and response body [%s]", response.getStatusCode(), response.getStatusLine(), response.getBody().asString()) );
                }
                this.accessToken = response.jsonPath().getString("access_token");
                this.idToken = response.jsonPath().getString("id_token");
                if (this.accessToken == null) {
                    throw new IllegalStateException("expected authentication provider to return access token but got none");
                } else {
                    logger.info(String.format("got access token: \"%s\"", this.accessToken));
                }
                if (this.idToken == null) {
                    logger.warning("expected authentication provider to return ID token but got none");
                } else {
                    logger.info(String.format("got ID token: \"%s\"", this.idToken));
                }
            } catch (Throwable ex) {
                logger.log(Level.SEVERE, String.format("Failed to retrieve tokens from OpenID Connect Provider at [%s]", this.config.getOidcAccessTokenUri()), ex);
                throw ex;
            }
        }
    }
}
