package org.rahul.akka.example.intelligent

import java.util.logging.Logger

class DataSource {
  val data = List("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten")
  val iterator = data.iterator
  val logger = Logger.getLogger("DataSource")

  def next() : Option[String] = {
    logger.info("******** DataSource->Next")
    Option(iterator.next())
  }
}
