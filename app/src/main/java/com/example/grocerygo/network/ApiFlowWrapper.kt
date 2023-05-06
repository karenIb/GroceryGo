package com.example.grocerygo.network

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

@Suppress("BlockingMethodInNonBlockingContext")
class ApiFlowWrapper<T : AResponse> {
    operator fun invoke(apiCallBack: suspend () -> T): Flow<Resource<T>> {
        return flow {
            try {
                emit(Resource.Loading())
                val resultObject: T = apiCallBack()
                Log.wtf("ApiWrapper ---->", resultObject.getStateObject().message)

                if (resultObject.getStateObject().code != 200) {
                    emit(Resource.Error(resultObject.getStateObject().message))
                } else
                    emit(Resource.Success(resultObject))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()
                if (errorBody != null) {
                    val httpCode = e.response()!!.code()
                    if (httpCode == 401) {
                        emit(Resource.Error("Refresh Token Failed"))
                        return@flow
                    }
                    val resp = Gson().fromJson(errorBody.byteString().utf8(), AResponse::class.java)
                    Log.wtf("ApiWrapper ---> $httpCode", resp.getStateObject()!!.message)

                    emit(Resource.Error(resp.getStateObject().message))
                } else {
                    emit(Resource.Error("Something went wrong"))
                }
            } catch (e: IOException) {
                emit(Resource.Error("Check Server ${e.message}"))
            } catch (unknown: Exception) {
                if (unknown.message != null) {
                    emit(Resource.Error(unknown.message!!))
                } else {
                    emit(Resource.Error("Something went wrong"))
                }
            }
        }.catch { cause ->
            Log.wtf("Flow Error -->", cause.cause?.message)
        }
    }

}