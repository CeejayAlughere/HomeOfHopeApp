package com.example.homeofhopeapp.data

class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun loginUser(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    suspend fun isUsernameTaken(username: String): Boolean {
        return userDao.getUserByUsername(username) != null
    }
}
