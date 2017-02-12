package org.rahul.akka.example.intelligent

/***
  * workers must signal demand (i.e. when they are ready for processing more items)
  * the producer must produce items only when there is demand from workers
  */

// Flow :
// data-source => Producer => upstreamQueue
// downStreamQueue => Worker => process
// router

case object Continue // Producer can continue to produce more as consumers are ready
case object PollTick // Producer can fetch if is there any item available from data-source
case class Item()

//worker messages
case class Join()