package helloworld;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;

public class ApiSystemTest {

    private static RestAssuredSystemTestFixture fixture = new RestAssuredSystemTestFixture();

    @BeforeAll
    static void onBeforeAll() {
        fixture.onBefore();
    }

    @AfterAll
    static void onAfterAll() {
        fixture.onAfter();
    }

    @Test
    void getHelloWithTokenReturns200() {
        given().auth().oauth2(fixture.getAccessToken())
                .get("/cxp-sam-get-hello-java/hello")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("message", Matchers.notNullValue())
                .body("jwtToken", Matchers.notNullValue());
    }

    @Test
    void getHelloWithoutTokenReturns401() {
        given()
                .get("/cxp-sam-get-hello-java/hello")
                .then()
                .assertThat()
                .statusCode(401);
    }
}
