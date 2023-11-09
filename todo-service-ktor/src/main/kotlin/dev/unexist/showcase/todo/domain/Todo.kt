/**
 * @package Showcase-Microservices-Kotlin
 *
 * @file Todo class
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain

data class Todo(var id: Int = 0,
                var title: String = "",
                var description: String = "")