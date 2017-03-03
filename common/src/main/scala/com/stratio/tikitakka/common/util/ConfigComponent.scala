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

import scala.util.Try

import com.typesafe.config.ConfigFactory

object ConfigComponent {

  val config = ConfigFactory.load()

  def getString(key: String): Option[String] = Try(config.getString(key)).toOption

  def getString(key: String, default: String): String = getString(key) getOrElse default

  def getInt(key: String): Option[Int] = Try(config.getInt(key)).toOption

  def getInt(key: String, default: Int): Int = getInt(key) getOrElse default

  def getBoolean(key: String): Option[Boolean] = Try(config.getBoolean(key)).toOption

  def getBoolean(key: String, default: Boolean): Boolean = getBoolean(key) getOrElse default

}
