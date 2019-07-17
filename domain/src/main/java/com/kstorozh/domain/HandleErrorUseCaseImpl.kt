package com.kstorozh.domain

/*
class HandleErrorUseCaseImpl(
    val userRepository: UserRepository,
    val deviceRepository: DeviseRepository,
    val mapper: ErrorMapper
) : HandleErrorUseCase {
    val mediatorLiveData: MediatorLiveData<DomainError> = MediatorLiveData()

    override suspend fun getErrors(): MediatorLiveData<DomainError> {
        mediatorLiveData.addSource(userRepository.getErrors()) {
            mediatorLiveData.postValue(mapper.mapToDomainError(it))
        }
        mediatorLiveData.addSource(deviceRepository.getErrors()) {
            mediatorLiveData.value = it
        }
        return mediatorLiveData
    }
}*/
