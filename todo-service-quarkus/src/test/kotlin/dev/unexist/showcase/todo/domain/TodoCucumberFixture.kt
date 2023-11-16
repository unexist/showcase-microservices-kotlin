/**
 * @package Showcase-Microservices-Kotlin
 *
 * @file Todo Cucumber Steps
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
        plugin = arrayOf("pretty"),
        publish = true,
        features = ["src/test/resources/features"]
)
class TodoCucumberFixture
