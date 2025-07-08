package kr.jm.domain.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.whenever
import org.mockito.stubbing.OngoingStubbing

fun <T> OngoingStubbing<T>.thenReturnResult(result: T): OngoingStubbing<T> {
    return this.thenReturn(Result.success(result))
}

fun <T> OngoingStubbing<T>.thenReturnFailure(exception: Exception): OngoingStubbing<T> {
    return this.thenReturn(Result.failure(exception))
}

fun <T> Flow<T>.toTestFlow(): Flow<T> = flowOf(*this.toList().toTypedArray())

inline fun <reified T> mockResult(result: T): Result<T> {
    return Result.success(result)
}

inline fun <reified T> mockFailure(exception: Exception): Result<T> {
    return Result.failure(exception)
}

fun <T> List<T>.toResultFlow(): Flow<Result<T>> {
    return flowOf(*this.map { Result.success(it) }.toTypedArray())
}

fun <T> Exception.toResultFlow(): Flow<Result<T>> {
    return flowOf(Result.failure(this))
}

suspend fun <T> Flow<T>.collectFirst(): T {
    return this.toList().first()
}

suspend fun <T> Flow<T>.collectAll(): List<T> {
    return this.toList()
}