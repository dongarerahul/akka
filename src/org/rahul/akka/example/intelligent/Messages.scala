package org.rahul.akka.example.intelligent

/***
  * workers must signal demand (i.e. when they are ready for processing more items)
  * the producer must produce items only when there is demand from workers
  */

// Flow :
// Producer fetches item from DataSource
// if no item is available then put context to Polling
// else pass item to the router

// Router assign items to the worker
// if downStreamQueue is empty i.e. no worker is available then store item in the upStreamQueue
// else de-queueing worker and assign item to the worker and ask Producer to send more (i.e. Message Continue)

// Worker processes item and once finished, ask Router to send one more (i.e. Message Continue)

case object Continue // Producer can continue to produce more as consumers are ready
case object PollTick // Producer can fetch if is there any item available from data-source
case class Item(name: String)