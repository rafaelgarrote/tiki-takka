package com.stratio.tikitakka.common.exceptions

case class ResponseException(message: String, cause: Throwable) extends Exception(message, cause)

case class ConfigurationException(message: String) extends Exception(message)
