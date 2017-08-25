package com.stratio.tikitakka.common.util

import org.slf4j.{Logger, LoggerFactory}

trait LogUtils {

  val log : Logger = LoggerFactory.getLogger(getClass.getName)

  def logFunction[T](level: LogLevel)(message: String)(f: => T): T = {
    level match {
      case DEBUG => log.debug(message)
      case ERROR => log.error(message)
      case INFO => log.info(message)
      case TRACE => log.trace(message)
      case WARN => log.warn(message)
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
