package com.artificer.artifcerdelivery.courier.management.domain.api.controller;

import com.artificer.artifcerdelivery.courier.management.domain.model.Courier;
import com.artificer.artifcerdelivery.courier.management.domain.repository.CourierRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourierControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CourierRepository courierRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        basePath = "/api/v1/couriers";
    }


    @Test
    @Order(1)
    @DisplayName("TESTE01 - Should return a list with all couriers")
    public void shouldReturnAllCouriers() {
        given()
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    @DisplayName("TESTE02 - Should Register a New Courier")
    public void shouldRegisterCourier() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                    .body("{\"name\": \"John Doe\", \"phone\": \"1234567890\"}")
                .when()
                    .post()
                .then()
                    .statusCode(CREATED.value())
                    .body("id", notNullValue())
                    .body("name", equalTo("John Doe"));
    }

    @Test
    @Order(3)
    @DisplayName("TESTE03 - Should Return a Courier By ID")
    public void shouldReturnCourierById() {
        Courier courier = courierRepository.saveAndFlush(
                Courier.brandNew("Jane Doe", "0987654321"));

        UUID courierId = courier.getId();

        given()
                .pathParam("courierId", courierId)
                .when()
                    .get("/{courierId}")
                .then()
                    .statusCode(OK.value())
                    .body("id", equalTo(courierId.toString()))
                    .body("name", equalTo("Jane Doe"))
                    .body("phone", equalTo("0987654321"));
    }

    @Test
    @Order(4)
    @DisplayName("TESTE04 - Should Update a Courier Sucessfully")
    public void shouldUpdateCourier() {
        Courier courier = courierRepository.saveAndFlush(
                Courier.brandNew("Alice Smith", "1234567890"));

        UUID courierId = courier.getId();

        given()
                .pathParam("courierId", courierId)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                    .body("{\"name\": \"Alice Johnson\", \"phone\": \"0987654321\"}")
                .when()
                    .put("/{courierId}")
                .then()
                    .statusCode(OK.value())
                    .body("id", equalTo(courierId.toString()))
                    .body("name", equalTo("Alice Johnson"))
                    .body("phone", equalTo("0987654321"));
    }

    @Test
    @Order(5)
    @DisplayName("TESTE05 - Should Calculate Courier Payout Successfully")
    public void shouldCalculateCourierPayout() {

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"distanceInKm\": 10.0}")
                .when()
                    .post("/payout-calculation")
                .then()
                    .statusCode(OK.value())
                    .body("payoutFee", equalTo(100.00f));
    }

    @Test
    @Order(6)
    @DisplayName("TESTE06 - Should Delete a Courier Successfully")
    public void shouldDeleteCourier() {
        Courier courier = courierRepository.saveAndFlush(
                Courier.brandNew("Bob Brown", "1234567890"));

        UUID courierId = courier.getId();

        given()
                .pathParam("courierId", courierId)
                .when()
                    .delete("/{courierId}")
                .then()
                    .statusCode(NO_CONTENT.value());

        // Verify that the courier is deleted
        given()
                .pathParam("courierId", courierId)
                .when()
                    .get("/{courierId}")
                .then()
                    .statusCode(NOT_FOUND.value());
    }

    @Test
    @Order(7)
    @DisplayName("TESTE07 - Should Return Not Found for Non-Existent Courier")
    public void shouldReturnNotFoundForNonExistentCourier() {
        UUID nonExistentCourierId = UUID.randomUUID();
        given()
                .pathParam("courierId", nonExistentCourierId)
                .when()
                    .get("/{courierId}")
                .then()
                    .statusCode(NOT_FOUND.value());
    }

    @Test
    @Order(8)
    @DisplayName("TESTE08 - Should Return Bad Request for Invalid Courier Input")
    public void shouldReturnBadRequestForInvalidCourierInput() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"name\": \"\", \"phone\": \"\"}") // Invalid input
                .when()
                    .post()
                .then()
                    .statusCode(BAD_REQUEST.value());
    }

    @Test
    @Order(9)
    @DisplayName("TESTE09 - Should Return Bad Request for Invalid Payout Calculation Input")
    public void shouldReturnBadRequestForInvalidPayoutCalculationInput() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"distanceInKm\": -5.0}") // Invalid input
                .when()
                    .post("/payout-calculation")
                .then()
                    .statusCode(BAD_REQUEST.value());
    }

    @Test
    @Order(10)
    @DisplayName("TESTE10 - Should Return Bad Request for Invalid Courier Update Input")
    public void shouldReturnBadRequestForInvalidCourierUpdateInput() {
        Courier courier = courierRepository.saveAndFlush(
                Courier.brandNew("Charlie Green", "1234567890"));
        UUID courierId = courier.getId();

        given()
                .pathParam("courierId", courierId)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"name\": \"\", \"phone\": \"\"}") // Invalid input
                .when()
                    .put("/{courierId}")
                .then()
                    .statusCode(BAD_REQUEST.value());
    }

    @Test
    @Order(11)
    @DisplayName("TESTE11 - Should Return Bad Request for Invalid Courier Deletion Input")
    public void shouldReturnBadRequestForInvalidCourierDeletionInput() {
        UUID invalidCourierId = UUID.randomUUID();
        given()
                .pathParam("courierId", invalidCourierId)
                .when()
                    .delete("/{courierId}")
                .then()
                    .statusCode(NOT_FOUND.value());
    }

}