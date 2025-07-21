import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetStoreTests {

    static final long PET_ID = 88888;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
    }

    @Test
    @Order(1)
    @DisplayName("Создание питомца")
    void createPet() {
        String body = String.format("""
        {
            "id": %d,
            "name": "Тестик",
            "category": { "id": 1, "name": "dogs" },
            "photoUrls": [ "https://example.com/photo.jpg" ],
            "tags": [ { "id": 1, "name": "tag1" } ],
            "status": "available"
        }
        """, PET_ID);

        given()
            .contentType("application/json")
            .body(body)
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            .body("id", equalTo((int) PET_ID))
            .body("name", equalTo("Тестик"))
            .body("status", equalTo("available"));
    }

    @Test
    @Order(2)
    @DisplayName("Проверка питомца")
    void getPet() {
        given()
            .accept("application/json")
        .when()
            .get("/pet/{petId}", PET_ID)
        .then()
            .statusCode(200)
            .body("id", equalTo((int) PET_ID))
            .body("name", equalTo("Тест"))
            .body("status", equalTo("available"));
    }
}
