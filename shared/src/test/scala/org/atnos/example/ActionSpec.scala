package org.atnos
package example

import cats.Eval
import org.atnos.eff.*
import org.atnos.eff.ErrorEffect.*
import org.atnos.eff.EvalEffect.*
import org.atnos.eff.Member.<=
import org.atnos.eff.syntax.error.given
import org.atnos.example.Action.runAction
import org.atnos.example.ActionCreation.*
import org.atnos.example.ConsoleEffect.*
import org.atnos.example.WarningsEffect.*
import org.specs2.*

class ActionSpec extends Specification with ScalaCheck {
  def is = s2"""

 The action stack can be used to
   compute values                      $computeValues
   stop when there is an error         $stop
   display log messages                $logMessages
   collect warnings                    $collectWarnings
   emit a warning then fail            $warningAndFail
   do an action or else warn           $orElseWarn

"""

  def computeValues =
    runWith(2, 3)._1 must beRight(5)

  def stop =
    runWith(20, 30)._1 ==== Left(Right("too big"))

  def logMessages = {
    val messages = new scala.collection.mutable.ListBuffer[String]
    runWith(1, 2, m => messages.append(m))

    messages.toList === List("got the value 1", "got the value 2")
  }

  def collectWarnings =
    runWith(2, 3)._2 must be_==(Vector("the sum is big: 5"))

  def warningAndFail = {
    import ActionImplicits._

    val action = for {
      i <- EvalEffect.delay[ActionStack, Int](1)
      _ <- Action.warnAndFail[ActionStack, String]("hmm", "let's stop")
    } yield i

    runAction(action)._1 must beLeft
  }

  def orElseWarn = {
    import ActionImplicits._
    val action =
      ErrorEffect.fail[ActionStack, Unit]("failed").orElse(warn[ActionStack]("that didn't work"))

    runAction(action)._1 must beRight
  }

  /**
   * HELPERS
   */

  def runWith(i: Int, j: Int, printer: String => Unit = s => ()): (Either[Error, Int], List[String]) =
    runAction(actions(i, j), printer)

  /** specifying the stack is enough to run it */
  def runWithUnbound(i: Int, j: Int, printer: String => Unit = s => ()): (Either[Error, Int], List[String]) = {
    import ActionImplicits._
    runAction(unboundActions[ActionStack](i, j), printer)
  }

  /**
   * ActionStack actions
   */
  def actions(i: Int, j: Int): Eff[ActionStack, Int] = {
    import ActionImplicits._
    for {
      x <- delay[ActionStack, Int](i)
      _ <- log[ActionStack]("got the value " + x)
      y <- delay[ActionStack, Int](j)
      _ <- log[ActionStack]("got the value " + y)
      s <- if (x + y > 10) fail[ActionStack, Int]("too big") else ErrorEffect.ok[ActionStack, Int](x + y)
      _ <- if (s >= 5) warn[ActionStack]("the sum is big: " + s) else Eff.unit[ActionStack]
    } yield s
  }

  /**
   * "open" effects version of the same actions
   * this one can be reused with more effects
   */
  def unboundActions[R](i: Int, j: Int)(using
    Eval <= R,
    Console <= R,
    Warnings <= R,
    ErrorOrOk <= R
  ): Eff[R, Int] = for {
    x <- delay(i)
    _ <- log("got the value " + x)
    y <- delay(j)
    _ <- log("got the value " + y)
    s <- if (x + y > 10) fail("too big") else ErrorEffect.ok(x + y)
    _ <- if (s >= 5) warn("the sum is big: " + s) else Eff.unit[R]
  } yield s

}
