package com.example.grocerygo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException


private const val STORE_NAME = "pref_go_grocery"

class PrefStore(context: Context) {

    private val mutex = Mutex(false)
    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(STORE_NAME)
    private val dataStore: DataStore<Preferences> = context._dataStore

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[PreferencesKeys.TOKEN_KEY] = token
        }
    }

    suspend fun token() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.TOKEN_KEY] ?: "" }


    suspend fun setIsLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun setIsFirst() {
        dataStore.edit {
            it[PreferencesKeys.IS_FIRST_START] = false
        }
    }


    suspend fun numberOfFavorites() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.NUMBER_FAVORITE] ?: 0 }


    suspend fun setNumberInCart(number:Int) =  dataStore.edit {
        it[PreferencesKeys.NUMBER_ITEM_CART] = number
    }

    suspend fun numberInCart() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.NUMBER_ITEM_CART] ?: 0 }


    suspend fun incrementFavorite() {
        mutex.withLock {
            val currentNumber = dataStore.data.catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { it[PreferencesKeys.NUMBER_FAVORITE] ?: 0 }.first()

            dataStore.edit {
                it[PreferencesKeys.NUMBER_FAVORITE] = currentNumber + 1
            }
        }
    }


    suspend fun decrementFavorite() {
        mutex.withLock {
            val currentNumber = dataStore.data.catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { it[PreferencesKeys.NUMBER_FAVORITE] ?: 0 }.first()

            dataStore.edit {
                it[PreferencesKeys.NUMBER_FAVORITE] = currentNumber - 1
            }
        }
    }


    suspend fun incrementCart() {
        mutex.withLock {
            val currentNumber = dataStore.data.catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { it[PreferencesKeys.NUMBER_ITEM_CART] ?: 0 }.first()

            dataStore.edit {
                it[PreferencesKeys.NUMBER_ITEM_CART] = currentNumber + 1
            }
        }
    }

    suspend fun decremenCart() {
        mutex.withLock {
            val currentNumber = dataStore.data.catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { it[PreferencesKeys.NUMBER_ITEM_CART] ?: 0 }.first()

            dataStore.edit {
                it[PreferencesKeys.NUMBER_ITEM_CART] = currentNumber - 1
            }
        }
    }

    suspend fun isFirst() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.IS_FIRST_START] ?: true }


    fun isLoggedIn(): Flow<Boolean> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.IS_LOGGED_IN] ?: false }


    companion object {
        private lateinit var store: PrefStore
        fun initialize(context: Context) {
            store = PrefStore(context)
        }

        fun getInstance(): PrefStore = store
    }
}


private object PreferencesKeys {

    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    val IS_FIRST_START = booleanPreferencesKey("is_first")
    val TOKEN_KEY = stringPreferencesKey("token_key")
    val NUMBER_FAVORITE = intPreferencesKey("number_favorite")

    val NUMBER_ITEM_CART = intPreferencesKey("number_item_cart")
}