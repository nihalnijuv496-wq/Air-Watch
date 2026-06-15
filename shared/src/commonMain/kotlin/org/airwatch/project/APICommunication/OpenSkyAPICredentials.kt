package org.airwatch.project.APICommunication

import org.airwatch.project.shared.BuildConfig

object Secrets
{
    val clientID: String = BuildConfig.OPENSKY_CLIENT_ID
    val clientSecret: String = BuildConfig.OPENSKY_CLIENT_SECRET
}
