package org.rahul.akka.example.advance

case class Validated(key: String, isValid: Boolean)
case class Reject()
case class IsPresent(key: String)
case class Continue()
case class Add(key: String)
