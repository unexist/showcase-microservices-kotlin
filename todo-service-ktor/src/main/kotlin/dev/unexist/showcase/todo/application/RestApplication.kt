/**
 * @package Showcase-Microservices-Kotlin
 *
 * @file
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.application

import com.orbitz.consul.Consul
import com.orbitz.consul.model.agent.ImmutableRegistration
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, commandLineEnvironment(args))
    val service = ImmutableRegistration.builder()
            .id("todo-${server.environment.connectors[0].port}")
            .name("todo-service")
            .address("localhost")
            .port(server.environment.connectors[0].port)
            .build()

    /* Consul */
    val consulClient = Consul.builder().withUrl("http://localhost:8500").build()

    consulClient.agentClient().register(service)

    server.start(wait = true)
}
