package com.example.fiszki.domain.usecase

import com.example.fiszki.domain.Fiszka
import com.example.fiszki.domain.repository.FiszkiRepository
import javax.inject.Inject

class GetNextExampleUseCase @Inject constructor(
    val repository: FiszkiRepository
) {
    suspend operator fun invoke (): Fiszka? {
        return repository.getNextExample()
    }
}