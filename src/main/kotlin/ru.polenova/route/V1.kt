package ru.polenova.route

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import ru.polenova.dto.AuthenticationRequestDto
import ru.polenova.dto.PasswordChangeRequestDto
import ru.polenova.dto.PostRequestDto
import ru.polenova.dto.UserResponseDto
import ru.polenova.model.AuthUserModel
import ru.polenova.service.FCMService
import ru.polenova.service.FileService
import ru.polenova.service.ServicePost
import ru.polenova.service.UserService

class RoutingV1(
    private val staticPath: String,
    private val postService: ServicePost,
    private val fileService: FileService,
    private val userService: UserService,
    private val fcmService: FCMService
) {
    @KtorExperimentalAPI
    fun setup(configuration: Routing) {
        with(configuration) {
            route("/api/v1/") {
                static("/static") { files(staticPath) }
            }
            route("/") {
                post("/registration") {
                    val input = call.receive<AuthenticationRequestDto>()
                    val username = input.username
                    val password = input.password
                    val response = userService.save(username, password)
                    call.respond(response)
                }
                post("/authentication") {
                    val input = call.receive<AuthenticationRequestDto>()
                    val response = userService.authenticate(input)
                    call.respond(response)
                }
            }
            authenticate("basic", "jwt") {
                route("/me") {
                    get {
                        val me = call.authentication.principal<AuthUserModel>()
                        call.respond(UserResponseDto.fromModel(me!!))
                    }
                    post("/change-password") {
                        val me = call.authentication.principal<AuthUserModel>()
                        val input = call.receive<PasswordChangeRequestDto>()
                        val response = userService.changePassword(me!!.idUser, input)
                        call.respond(response)
                    }
                }
                /*route("/firebase-token") {
                    post {
                        val me = call.authentication.principal<UserModel>()
                        val token = call.receive<TokenDto>()
                        userService.saveFirebaseToken(me!!.id, token.token)
                        call.respond(HttpStatusCode.OK)
                        fcmService.send(me!!.id, token.token, "Welcome ${me!!.username}")
                    }
                }*/
                route("/posts") {
                    get {
                        val me = call.authentication.principal<AuthUserModel>()
                        val response = postService.getAllPosts(me!!.idUser)
                        call.respond(response)
                    }
                    get("/{idPosts}") {
                        val me = call.authentication.principal<AuthUserModel>()
                        val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                            "id",
                            "Long"
                        )
                        val response = postService.getByIdPosts(id, me!!.idPost)
                        call.respond(response)
                    }
                    get("/recent") {
                        val me = call.authentication.principal<AuthUserModel>()
                        val response = postService.getRecent(me!!.idPost)
                        call.respond(response)
                    }
                    get("{id}/get-posts-after") {
                        val me = call.authentication.principal<AuthUserModel>()
                        val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                            "id",
                            "Long"
                        )
                        val response = postService.getPostsAfter(id, me!!.idUser)
                        call.respond(response)
                    }
                    get("{id}/get-posts-before") {
                        val me = call.authentication.principal<AuthUserModel>()
                        val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                            "id",
                            "Long"
                        )
                        val response = postService.getPostsBefore(id, me!!.idUser)
                        call.respond(response)
                    }
                    post {
                        val me = call.authentication.principal<AuthUserModel>()
                        val input = call.receive<PostRequestDto>()
                        postService.save(input, me!!)
                        call.respond(HttpStatusCode.OK)
                    }
                    post("/{idPost}/up") {
                        val me = call.authentication.principal<AuthUserModel>()
                        val idPost = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                            "idPost",
                            "Long"
                        )
                        val response = postService.upById(idPost, me!!)
                        call.respond(response)
                    }
                    delete("/{idPost}/disup") {
                        val me = call.authentication.principal<AuthUserModel>()
                        val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                            "idPost",
                            "Long"
                        )
                        val response = postService.disUpById(id, me!!)
                        call.respond(response)
                    }
                    post("/{idPost}/down") {
                        val me = call.authentication.principal<AuthUserModel>()
                        val idPost = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                            "idPost",
                            "Long"
                        )
                        val response = postService.downById(idPost, me!!)
                        call.respond(response)
                    }
                    delete("/{idPost}/disdown") {
                        val me = call.authentication.principal<AuthUserModel>()
                        val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                            "idPost",
                            "Long"
                        )
                        val response = postService.disDownById(id, me!!)
                        call.respond(response)
                    }
                    post {
                        val me = call.authentication.principal<AuthUserModel>()
                        val input = call.receive<PostRequestDto>()
                        postService.save(input, me!!)
                        call.respond(HttpStatusCode.OK)
                    }
                    post("/{id}") {
                        val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                            "id",
                            "Long"
                        )
                        val input = call.receive<PostRequestDto>()
                        val me = call.authentication.principal<AuthUserModel>()
                        postService.saveById(id, input, me!!)
                        call.respond(HttpStatusCode.OK)
                    }
                    delete("/{id}") {
                        val me = call.authentication.principal<AuthUserModel>()
                        val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException(
                            "id",
                            "Long"
                        )
                        if (!postService.removePostByIdPost(id, me!!)) {
                            println("You can't delete post of another user")
                        }
                    }
                }
            }
        }
    }
}
