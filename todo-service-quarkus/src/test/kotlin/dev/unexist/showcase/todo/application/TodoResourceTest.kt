/**
 * @package Showcase-Microservices-Kotlin
 *
 * @file Stupid integration test
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.application;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test;

@QuarkusTest
class TodoResourceTest {

    @Test
    fun shouldGetEmptyResult() {
        given()
          .get("/todo")
          .then()
             .statusCode(204);
    }
}