package com.stratio.tikitakka.common.util

import com.typesafe.scalalogging.LazyLogging

trait LogUtils extends LazyLogging {

  def logFunction[T](level: LogLevel)(message: String)(f: => T): T = {
    level match {
      case DEBUG => logger.debug(message)
      case ERROR => logger.error(message)
      case INFO => logger.info(message)
      case TRACE => logger.trace(message)
      case WARN => logger.warn(message)
    }
    f
  }

  sealed trait LogLevel
  case object DEBUG extends LogLevel
  case object ERROR extends LogLevel
  case object INFO extends LogLevel
  case object TRACE extends LogLevel
  case object WARN extends LogLevel

}
