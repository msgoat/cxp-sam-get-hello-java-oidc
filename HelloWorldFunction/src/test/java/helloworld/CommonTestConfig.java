package helloworld;

public interface CommonTestConfig {
    boolean isSkipOpenIdConnectLogin();

    String getOidcClientId();

    String getOidcClientSecret();

    String getOidcAccessTokenUri();

    String getOidcUserName();

    String getOidcPassword();

    String getTargetRoute();

    boolean isSkipReadinessProbe();

    String getReadinessProbePath();

    int getInitialDelaySeconds();

    int getFailureThreshold();

    int getPeriodSeconds();

    int getTimeoutSeconds();
}
