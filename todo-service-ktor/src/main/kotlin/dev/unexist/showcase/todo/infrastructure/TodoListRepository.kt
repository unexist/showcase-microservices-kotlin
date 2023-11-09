/**
 * @package Showcase-Microservices-Kotlin
 *
 * @file Todo list repository
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure

import dev.unexist.showcase.todo.domain.Todo

class TodoListRepository {
    companion object {
        private val todos: MutableList<Todo> = ArrayList()

        fun add(account: Todo) {
            todos.add(account)
        }

        fun getAll() = todos
    }
}