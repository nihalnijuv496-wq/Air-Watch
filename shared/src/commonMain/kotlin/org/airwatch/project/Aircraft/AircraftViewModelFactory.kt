package org.airwatch.project.Aircraft

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.airwatch.project.APICommunication.httpClient

val AircraftViewModelFactory = viewModelFactory {
    initializer {
        AircraftViewModel(httpClient)
    }
}