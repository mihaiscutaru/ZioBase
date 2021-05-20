package io.scalac.labs.iot

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.Url

final case class AppConfig(uRadBaseUrl: String Refined Url){

}
