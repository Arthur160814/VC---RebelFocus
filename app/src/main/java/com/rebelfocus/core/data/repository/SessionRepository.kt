package com.rebelfocus.core.data.repository

import com.rebelfocus.core.model.Session
import com.rebelfocus.core.model.SessionState
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun insert(session: Session)
    suspend fun update(session: Session)
    suspend fun getById(id: String): Session?
    fun observeById(id: String): Flow<Session?>
    fun observeAll(): Flow<List<Session>>
    fun observeByStates(states: List<SessionState>): Flow<List<Session>>
    fun observeActiveSession(): Flow<Session?>
    fun observeEnforcementSession(): Flow<Session?>
    suspend fun getActiveSession(): Session?
    suspend fun deleteById(id: String)
    suspend fun count(): Int
}
