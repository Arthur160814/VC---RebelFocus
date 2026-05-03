package com.rebelfocus.core.domain.logging

import com.rebelfocus.core.database.dao.DiagnosticLogDao
import com.rebelfocus.core.database.entity.DiagnosticLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiagnosticLogger @Inject constructor(
    private val diagnosticLogDao: DiagnosticLogDao
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun info(component: String, message: String, extra: String? = null) {
        log("INFO", component, message, extra)
    }

    fun warn(component: String, message: String, extra: String? = null) {
        log("WARN", component, message, extra)
    }

    fun error(component: String, message: String, extra: String? = null) {
        log("ERROR", component, message, extra)
    }

    private fun log(level: String, component: String, message: String, extra: String?) {
        scope.launch {
            diagnosticLogDao.logAndPrune(
                DiagnosticLogEntity(
                    timestamp = System.currentTimeMillis(),
                    level = level,
                    component = component,
                    message = message,
                    extraData = extra
                )
            )
        }
    }
}
