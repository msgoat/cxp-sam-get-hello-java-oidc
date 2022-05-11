package helloworld;

/**
 * Encapsulates all the configuration part of the system test components.
 */
public final class CommonTestConfigImpl implements CommonTestConfig {

    private boolean skipOpenIdConnectLogin;
    private String oidcClientId;
    private String oidcClientSecret;
    private String oidcAccessTokenUri;
    private String oidcUserName;
    private String oidcPassword;
    private String targetRoute;
    private boolean skipReadinessProbe;
    private String readinessProbePath;
    private int initialDelaySeconds;
    private int failureThreshold;
    private int periodSeconds;
    private int timeoutSeconds;

    public CommonTestConfigImpl() {
        load();
    }

    private void load() {
        targetRoute = System.getProperty("test.target.route");
        skipReadinessProbe = true;
        skipOpenIdConnectLogin = Boolean.parseBoolean(System.getProperty("test.oidc.skip", "false"));
        if (!skipOpenIdConnectLogin) {
            oidcClientId = System.getProperty("test.oidc.client.clientId", "cxp-serverless-tester");
            oidcClientSecret = System.getProperty("test.oidc.client.clientSecret", "c046f3ed-250e-4f91-ad12-c3e0dec7a865");
            oidcAccessTokenUri = System.getProperty("test.oidc.client.accessTokenUri", "https://oidc.cloudtrain.aws.msgoat.eu/auth/realms/CloudExpertProgram/protocol/openid-connect/token");
            oidcUserName = System.getProperty("test.oidc.client.user", "cxp-tester");
            oidcPassword = System.getProperty("test.oidc.client.password", "CXP!2022_");
        }
    }

    @Override
    public boolean isSkipOpenIdConnectLogin() {
        return skipOpenIdConnectLogin;
    }

    @Override
    public String getOidcClientId() {
        return oidcClientId;
    }

    @Override
    public String getOidcClientSecret() {
        return oidcClientSecret;
    }

    @Override
    public String getOidcAccessTokenUri() {
        return oidcAccessTokenUri;
    }

    @Override
    public String getOidcUserName() {
        return oidcUserName;
    }

    @Override
    public String getOidcPassword() {
        return oidcPassword;
    }

    @Override
    public String getTargetRoute() {
        return targetRoute;
    }

    @Override
    public boolean isSkipReadinessProbe() {
        return skipReadinessProbe;
    }

    @Override
    public String getReadinessProbePath() {
        return readinessProbePath;
    }

    @Override
    public int getInitialDelaySeconds() {
        return initialDelaySeconds;
    }

    @Override
    public int getFailureThreshold() {
        return failureThreshold;
    }

    @Override
    public int getPeriodSeconds() {
        return periodSeconds;
    }

    @Override
    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }
}
