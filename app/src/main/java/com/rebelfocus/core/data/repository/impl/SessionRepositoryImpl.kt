package com.rebelfocus.core.data.repository.impl

import com.rebelfocus.core.data.mapper.toDomain
import com.rebelfocus.core.data.mapper.toEntity
import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.database.dao.SessionDao
import com.rebelfocus.core.model.Session
import com.rebelfocus.core.model.SessionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao
) : SessionRepository {

    companion object {
        /** States considered "active" for observeActiveSession. */
        private val ACTIVE_STATES = listOf(
            SessionState.ActiveFocus.name,
            SessionState.Break.name,
            SessionState.Paused.name
        )
        /** States considered "blocking/enforcement" including completion for summary. */
        private val ENFORCEMENT_STATES = listOf(
            SessionState.ActiveFocus.name,
            SessionState.Break.name,
            SessionState.Paused.name,
            SessionState.Completed.name
        )
    }

    override suspend fun insert(session: Session) {
        sessionDao.insert(session.toEntity())
    }

    override suspend fun update(session: Session) {
        sessionDao.update(session.toEntity())
    }

    override suspend fun getById(id: String): Session? {
        return sessionDao.getById(id)?.toDomain()
    }

    override fun observeById(id: String): Flow<Session?> {
        return sessionDao.observeById(id).map { it?.toDomain() }
    }

    override fun observeAll(): Flow<List<Session>> {
        return sessionDao.observeAll().map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeByStates(states: List<SessionState>): Flow<List<Session>> {
        return sessionDao.observeByStates(states.map { it.name })
            .map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeActiveSession(): Flow<Session?> {
        return sessionDao.observeActiveSession(ACTIVE_STATES).map { it?.toDomain() }
    }

    override fun observeEnforcementSession(): Flow<Session?> {
        return sessionDao.observeActiveSession(ENFORCEMENT_STATES).map { it?.toDomain() }
    }

    override suspend fun getActiveSession(): Session? {
        return sessionDao.getActiveSession(ACTIVE_STATES)?.toDomain()
    }

    override suspend fun deleteById(id: String) {
        sessionDao.deleteById(id)
    }

    override suspend fun count(): Int {
        return sessionDao.count()
    }
}
