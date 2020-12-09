package ru.polenova.repository

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.sync.Mutex
import ru.polenova.model.StatusUser
import ru.polenova.model.AuthUserModel

class UserRepositoryInMemoryWithAtomicImpl : UserRepository {
    private var nextId = atomic(0L)
    private val items = mutableListOf<AuthUserModel>()
    private val mutex = Mutex()

    override suspend fun getAllPostsUser(): List<AuthUserModel> = items.toList()

    override suspend fun getByIdUser(idUser: Long): AuthUserModel? = items.find { it.idUser == idUser }

    override suspend fun getByIdPassword(idUser: Long, password: String): AuthUserModel? {
        val item = items.find { it.idUser == idUser }
        return if (password == item?.password) {
            item
        } else {
            null
        }
    }

    override suspend fun getByUsername(username: String): AuthUserModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getByUserStatus(useStatusUser: StatusUser): AuthUserModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getByIds(ids: Collection<Long>): List<AuthUserModel> {
        TODO("Not yet implemented")
    }

    override suspend fun save(item: AuthUserModel): AuthUserModel {
        TODO("Not yet implemented")
    }

    override suspend fun saveFirebaseToken(idUser: Long, firebaseToken: String): AuthUserModel? {
        TODO("Not yet implemented")
    }
}