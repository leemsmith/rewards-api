package com.rewards.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.rewards.dto.RewardsDTO;

/**
 * Integration tests for the HTTP layer of {@link RewardsController}.  Verifies endpoint responses,
 * status codes, and JSON error handling.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RewardsControllerHttpLayerTest {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    /**
     * Verifies that GET /rewards/1 returns HTTP 200 with valid JSON containing customer rewards.
     */
    @Test
    void getRewards_returns200WithJsonForValidCustomerId() {
        webTestClient.get().uri("/rewards/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RewardsDTO.class)
                .consumeWith(response -> {
                    RewardsDTO body = response.getResponseBody();
                    assertNotNull(body);
                    assertEquals(1L, body.getCustomerId());
                    assertEquals(90, body.getTotalRewards());
                    assertNotNull(body.getMonthlyRewards());
                });
    }

    /**
     * Verifies that GET /rewards/45 returns HTTP 404 with JSON error when customer does not exist.
     */
    @Test
    void getRewards_returns404WithErrorJsonForNonexistentCustomer() {
        webTestClient.get().uri("/rewards/45")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").exists()
                .jsonPath("$.message").isEqualTo("Customer not found: 45");
    }

    /**
     * Verifies that GET /rewards/q returns HTTP 400 with JSON error for invalid path variable.
     */
    @Test
    void getRewards_returns400WithErrorJsonForInvalidCustomerId() {
        webTestClient.get().uri("/rewards/q")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").exists();
    }
}
