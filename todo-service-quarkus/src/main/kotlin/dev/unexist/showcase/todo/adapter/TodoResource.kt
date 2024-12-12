/**
 * @package Showcase-Microservices-Kotlin
 *
 * @file Todo resource
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.adapter;

import dev.unexist.showcase.todo.domain.todo.Todo
import dev.unexist.showcase.todo.domain.todo.TodoBase
import dev.unexist.showcase.todo.domain.todo.TodoService
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.UriInfo
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.net.URI
import java.util.Optional

@Path("/todo")
class TodoResource(@Inject var todoService: TodoService) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create new todo")
    @Tag(name = "Todo")
    @APIResponses(
        APIResponse(responseCode = "201", description = "Todo created"),
        APIResponse(responseCode = "406", description = "Bad data"),
        APIResponse(responseCode = "500", description = "Server error")
    )
    fun create(todoBase: TodoBase, @Context uriInfo: UriInfo): Response {
        val response: Response.ResponseBuilder

        val todo: Optional<Todo> = this.todoService.create(todoBase)

        if (todo.isPresent) {
            val uri: URI = uriInfo.absolutePathBuilder
                    .path(todo.get().id.toString())
                    .build();

            response = Response.created(uri).entity(todo.get())
        } else {
            response = Response.status(Response.Status.NOT_ACCEPTABLE);
        }

        return response.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all todos")
    @Tag(name = "Todo")
    @APIResponses(
        APIResponse(responseCode = "200", description = "List of todo", content = [
            Content(schema = Schema(type = SchemaType.ARRAY, implementation = Todo::class))]),
        APIResponse(responseCode = "204", description = "Nothing found"),
        APIResponse(responseCode = "500", description = "Server error")
    )
    fun getAll(): Response {
        val todoList: List<Todo> = this.todoService.getAll()

        val response: Response.ResponseBuilder

        if (todoList.isEmpty()) {
            response = Response.noContent();
        } else {
            response = Response.ok(Entity.json(todoList));
        }

        return response.build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get todo by id")
    @Tag(name = "Todo")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Todo found", content = [
            (Content(schema = Schema(implementation = Todo::class)))]),
        APIResponse(responseCode = "404", description = "Todo not found"),
        APIResponse(responseCode = "500", description = "Server error")
    )
    fun findById(@PathParam("id") id: Int): Response {
        val result: Optional<Todo> = this.todoService.findById(id)

        val response: Response.ResponseBuilder

        if (result.isPresent) {
            response = Response.ok(Entity.json(result.get()));
        } else {
            response = Response.status(Response.Status.NOT_FOUND);
        }

        return response.build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update todo by id")
    @Tag(name = "Todo")
    @APIResponses(
        APIResponse(responseCode = "204", description = "Todo updated"),
        APIResponse(responseCode = "404", description = "Todo not found"),
        APIResponse(responseCode = "500", description = "Server error")
    )
    fun update(@PathParam("id") id: Int, base: TodoBase): Response {
        val response: Response.ResponseBuilder

        if (this.todoService.update(id, base)) {
            response = Response.noContent();
        } else {
            response = Response.status(Response.Status.NOT_FOUND);
        }

        return response.build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete todo by id")
    @Tag(name = "Todo")
    fun delete(@PathParam("id") id: Int, base: TodoBase): Response {
        val response: Response.ResponseBuilder

        if (this.todoService.delete(id)) {
            response = Response.noContent();
        } else {
            response = Response.status(Response.Status.NOT_FOUND);
        }

        return response.build();
    }
}
