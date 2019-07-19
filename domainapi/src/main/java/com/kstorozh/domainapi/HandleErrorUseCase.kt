package com.kstorozh.domainapi

import com.kstorozh.domainapi.model.DomainErrors

interface HandleErrorUseCase {
    suspend fun getErrors(): Pair<DomainErrors?, DomainErrors?>
}
