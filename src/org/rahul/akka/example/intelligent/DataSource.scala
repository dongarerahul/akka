package org.rahul.akka.example.intelligent

class DataSource {
  val data = List("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten")
  val iterator = data.iterator

  def next() : Option[String] = {
    Option(iterator.next())
  }
}
