package br.com.angelorobson.animals

import br.com.angelorobson.animals.di.ApiModule
import br.com.angelorobson.animals.model.AnimalApiService

class ApiModuleTest(val mockService: AnimalApiService) : ApiModule() {

    override fun provideAnimalService(): AnimalApiService {
        return mockService
    }
}