package com.stratio.tikitakka.common.test.utils.generators

import org.scalacheck.Gen

trait GeneratorUtils {

  val exampleServiceName = List[String]("elastic", "postgres", "cassandra", "dg-agent")

  def genTags: Gen[List[String]] =
    for {
      sz <- Gen.choose(0, 4)
      tags <- Gen.listOfN(sz, Gen.oneOf("datasource", "elasticsearch", "postgresql", "cassandra"))
    }  yield tags.distinct

  def genIP: Gen[String] = for {
    x <- Gen.choose(127,186)
    y <- Gen.choose(0,18)
    z <- Gen.choose(0,18)
  } yield s"$x.0.$y.$z"

  def genPort: Gen[Int] = Gen.choose(80, 50000)

  def genName: Gen[String] = Gen.oneOf[String](exampleServiceName)

  def genID: Gen[String] = for {
    name <- Gen.oneOf[String](exampleServiceName)
    n <- Gen.choose(1, 1000000)
  } yield s"$name-$n"

}
