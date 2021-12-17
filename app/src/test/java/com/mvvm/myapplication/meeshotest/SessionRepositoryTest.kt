package com.mvvm.myapplication.meeshotest

import com.mvvm.myapplication.meeshotest.SessionStub.barcodeData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Test

@ExperimentalCoroutinesApi
class SessionRepositoryTest {

	private val dispatch = TestCoroutineDispatcher()
	private val coroutineScope: CoroutineScope = TestCoroutineScope(dispatch)
	private val fakeRepo = FakeSessionRepository()

	@Test
	fun `when endSession is called but there is an error`(){
		fakeRepo.endSessionSuccess = false
		coroutineScope.launch {
			val result = fakeRepo.endSession(barcodeData)
			assert(result == null)
		}
	}

	@Test
	fun `when endSession is called and it is successful`(){
		fakeRepo.endSessionSuccess = true
		coroutineScope.launch {
			val result = fakeRepo.endSession(barcodeData)
			assert(result != null && result.errorString == null)
		}
	}

	@Test
	fun `when getSession is called but there is an error`(){
		fakeRepo.getSessionSuccess = false
		coroutineScope.launch {
			val result = fakeRepo.getSession()
			assert(result == null)
		}
	}

	@Test
	fun `when getSession is called and it is successful`(){
		fakeRepo.getSessionSuccess = true
		coroutineScope.launch {
			val result = fakeRepo.getSession()
			assert(result != null && result.locationId.isNotEmpty() && result.locationDetail.isNotEmpty())
		}
	}
}
