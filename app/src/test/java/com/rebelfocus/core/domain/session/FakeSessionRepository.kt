package com.rebelfocus.core.domain.session

import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.model.Session
import com.rebelfocus.core.model.SessionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory [SessionRepository] for unit tests — no Room needed.
 */
class FakeSessionRepository : SessionRepository {

    private val sessions = mutableMapOf<String, Session>()
    private val flow = MutableStateFlow<List<Session>>(emptyList())

    private val activeStates = setOf(
        SessionState.ActiveFocus,
        SessionState.Break,
        SessionState.Paused
    )

    private val enforcementStates = setOf(
        SessionState.ActiveFocus,
        SessionState.Break,
        SessionState.Paused,
        SessionState.Completed
    )

    private fun emitAll() {
        flow.value = sessions.values.toList()
    }

    override suspend fun insert(session: Session) {
        sessions[session.id] = session
        emitAll()
    }

    override suspend fun update(session: Session) {
        sessions[session.id] = session
        emitAll()
    }

    override suspend fun getById(id: String): Session? = sessions[id]

    override fun observeById(id: String): Flow<Session?> =
        flow.map { list -> list.firstOrNull { it.id == id } }

    override fun observeAll(): Flow<List<Session>> = flow

    override fun observeByStates(states: List<SessionState>): Flow<List<Session>> =
        flow.map { list -> list.filter { it.state in states } }

    override fun observeActiveSession(): Flow<Session?> =
        flow.map { list -> list.firstOrNull { it.state in activeStates } }

    override fun observeEnforcementSession(): Flow<Session?> =
        flow.map { list -> list.firstOrNull { it.state in enforcementStates } }

    override suspend fun getActiveSession(): Session? =
        sessions.values.firstOrNull { it.state in activeStates }

    override suspend fun deleteById(id: String) {
        sessions.remove(id)
        emitAll()
    }

    override suspend fun count(): Int = sessions.size
}
