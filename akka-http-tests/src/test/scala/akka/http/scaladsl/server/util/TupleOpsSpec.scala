/*
 * Copyright (C) 2009-2022 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.http.scaladsl.server.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TupleOpsSpec extends AnyWordSpec with Matchers {
  import TupleOps._

  "The TupleOps" should {

    "support folding over tuples using a binary poly-function" in {
      object Funky extends BinaryPolyFunc {
        implicit def step1 = at[Double, Int](_ + _)
        implicit def step2 = at[Double, Symbol]((d, s) => (d + s.name.tail.toInt).toByte)
        implicit def step3 = at[Byte, String]((byte, s) => byte + s.toLong)
      }
      (1, Symbol("X2"), "3").foldLeft(0.0)(Funky) shouldEqual 6L
    }

    "support joining tuples" in {
      (1, Symbol("X2"), "3") join (()) shouldEqual ((1, Symbol("X2"), "3"))
      () join ((1, Symbol("X2"), "3")) shouldEqual ((1, Symbol("X2"), "3"))
      (1, Symbol("X2"), "3") join ((4.0, 5L)) shouldEqual ((1, Symbol("X2"), "3", 4.0, 5L))
    }
  }
}
