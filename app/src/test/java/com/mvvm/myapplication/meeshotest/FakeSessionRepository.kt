package com.mvvm.myapplication.meeshotest

import com.mvvm.myapplication.meeshotest.data.localmodels.BarcodeData
import com.mvvm.myapplication.meeshotest.data.localmodels.EndSessionDisplayModel
import com.mvvm.myapplication.meeshotest.data.repository.SessionRepository

class FakeSessionRepository: SessionRepository {

	var getSessionSuccess: Boolean = true
	var endSessionSuccess: Boolean = true

	override suspend fun endSession(barcodeData: BarcodeData): EndSessionDisplayModel? {
		return if(endSessionSuccess) {
			EndSessionDisplayModel(
				timeSpent = 10,
				amountToPay = 50f,
				endTime = "2:00 PM"
			)
		} else {
			null
		}
	}

	override suspend fun addSession(barcodeData: BarcodeData) {
		/*no-op*/
	}

	override suspend fun getSession(): BarcodeData? {
		return if(getSessionSuccess) {
			BarcodeData(
				locationId = "123-butterknife",
				pricePerMin = 5f,
				locationDetail = "345 abc 4th street",
				startTimestamp = System.currentTimeMillis()
			)
		} else {
			null
		}
	}


}
