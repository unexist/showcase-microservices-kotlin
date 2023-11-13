/**
 * @package Showcase-Microservices-Kotlin
 *
 * @file Todo base class
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.Objects

@JsonInclude(JsonInclude.Include.NON_NULL)
open class TodoBase(open var title: String, open var description: String,
                    open var done: Boolean, open var dueDate: DueDate?) {

    /**
     * Set due date of the entry
     *
     * @param  dueDate  Due date of the entry
     **/

    fun setDueDate(dueDate: DueDate) {
        Objects.requireNonNull(dueDate, "DueDate cannot be null");

        this.dueDate = dueDate;

        if (null != dueDate.start && null != dueDate.due) {
            this.done = dueDate.start.isBefore(dueDate.due);
        }
    }
}
