package wilquer.lima.desafioconcrete

import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import wilquer.lima.desafioconcrete.network.service.RepositoryService
import wilquer.lima.desafioconcrete.ui.repository.RepositoryContract
import wilquer.lima.desafioconcrete.ui.repository.RepositoryPresenter


class RepositoryPresenterTest {

    private val mockView = mock<RepositoryContract.View>()
    private lateinit var mockPresenter: RepositoryPresenter
    private val service = mock<RepositoryService>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mockPresenter = RepositoryPresenter(mockView)
    }

    @Test
    fun whenGetRepositories_shouldReturnSuccess() {
        mockPresenter.getRepositories(0)
        Thread.sleep(3000)

        verify(mockView).setProgress(false)
        verify(mockView).repositories(any())
    }
}