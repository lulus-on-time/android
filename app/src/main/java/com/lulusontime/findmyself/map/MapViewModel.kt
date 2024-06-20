package com.lulusontime.findmyself.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lulusontime.findmyself.map.data.DummyGeoJson
import com.lulusontime.findmyself.map.data.FloorRepository
import com.lulusontime.findmyself.map.data.GeojsonResponse
import com.lulusontime.findmyself.wifiscan.repository.WifiScanRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapViewModel(private val wifiScanRepository: WifiScanRepository, private val
floorRepository: FloorRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            wifiScanRepository.location.collectLatest {newLocation ->
                _uiState.update {
                    if (newLocation.data.floorId != _uiState.value.geojsonResponse.floor.id) {
                        try {
                            val geojsonResponse = floorRepository.getFloor(newLocation.data.floorId)
                            _uiState.update {
                                it.copy(
                                    geojsonResponse = geojsonResponse,
                                    isMapReady = true
                                )
                            }
                        } catch (e: HttpException) {
                            // do nothing
                            // TODO: Maybe do something
                        }
                    }
                    it.copy(
                        myLoc = newLocation.data.location
                    )
                }
            }
        }

        viewModelScope.launch {
            if (_uiState.value.geojsonResponse == GeojsonResponse()) {
                try {
                    val newLocation = floorRepository.getFirstFloor()
                    _uiState.update {
                        it.copy(
                            geojsonResponse = newLocation,
                            isMapReady = true
                        )
                    }
                } catch (e: HttpException) {
                    // do nothing
                    // TODO: Maybe do something
                } catch (e: Exception) {
                    // do nothing
                    // TODO: Maybe do something
                }

            }
        }
    }
}