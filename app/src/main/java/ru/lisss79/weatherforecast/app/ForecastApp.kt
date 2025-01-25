package ru.lisss79.weatherforecast.app

import android.app.Application
import com.google.android.material.color.DynamicColors
import org.kodein.di.DI
import org.kodein.di.DIAware
import ru.lisss79.weatherforecast.di.apiModule
import ru.lisss79.weatherforecast.di.repositoryModule
import ru.lisss79.weatherforecast.di.useCasesModule

class ForecastApp: Application(), DIAware {

    companion object {
        var instance: ForecastApp? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    override val di by DI.lazy {
        import(repositoryModule)
        import(apiModule)
        import(useCasesModule)
    }

}