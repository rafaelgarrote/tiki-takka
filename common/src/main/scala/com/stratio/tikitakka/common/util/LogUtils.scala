/*
 * Copyright (C) 2017 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
