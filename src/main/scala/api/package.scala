package io.scalac.labs.iot

import zio.Has

package object api {
  type Api = Has[Api.Service]
}
