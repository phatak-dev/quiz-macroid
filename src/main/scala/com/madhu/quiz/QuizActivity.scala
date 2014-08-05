package com.madhu.quiz

import android.os.Bundle
import android.widget.{LinearLayout, TextView, Button, FrameLayout}
import android.view.ViewGroup.LayoutParams._
import android.view.ViewGroup
import android.view.{Gravity, View}
import android.app.Activity
import android.text.method.LinkMovementMethod;
import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.Spanned;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log.d
import android.content.Intent

// import macroid stuff

import macroid._
import macroid.util.Ui
import macroid.FullDsl._
import macroid.contrib.ExtraTweaks._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


trait Helper {
  // sets text, large font size and a long click handler
  def caption(cap: String)(implicit ctx: AppContext):
  Ui[Boolean] = {
    (toast(cap) <~ gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
      <~ fry) ~
      Ui(true)
  }

  def setFrameLayoutGravity(value: Int)(implicit ctx: AppContext):
  Tweak[View] = {
    new Tweak((view: View) => {
      val params = new FrameLayout.LayoutParams(
        WRAP_CONTENT, WRAP_CONTENT)
      params.gravity = value
      view.setLayoutParams(params)
    })
  }


}


class QuizActivity extends Activity with Helper with
Contexts[Activity] {

  case class TrueFalse(val question: String, mTrue:
  Boolean)

  val questions = Vector(TrueFalse("The pacific ocean" +
    "is larger than Atlantic ocean", true),
    TrueFalse("Nerds rule the world", true),
    TrueFalse("In Scala None==None return true", false))
  var currentIndex = 0
  val tag = "QuizActivity"
  val key_index = "index"
  var mIsCheater: Boolean = _


  def checkAnswer(userAnswer: Boolean) = {
    val truthful = questions(currentIndex).mTrue
    val text = if (mIsCheater) "you cheated"
    else if (userAnswer == truthful)
      "correct answer"
    else "wrong answer"
    caption(text)
  }

  override def onCreate(savedInstanceState: Bundle) = {
    var questionView = slot[TextView]
    var prev = slot[Button]
    var next = slot[Button]
    super.onCreate(savedInstanceState)
    d(tag, "onCreate is called")
    currentIndex = if (savedInstanceState != null)
      savedInstanceState.getInt(key_index)
    else currentIndex


    val actionBar = getActionBar();
    actionBar.setSubtitle("Bodies of Water")


    val prevNextLayout = l[LinearLayout](
      w[Button] <~ text("Prev")
        <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ On.click {
        currentIndex = if (currentIndex > 0) currentIndex - 1
        else
          questions.length - 1
        questionView <~ text(questions(currentIndex).question)
      } <~ wire(prev),
      w[Button] <~ text("Next") <~ wire(next)
        <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ On.click {
        currentIndex = (currentIndex + 1) % questions.length
        mIsCheater = false
        questionView <~ text(questions(currentIndex).question)
      }
    ) <~ layoutParams[LinearLayout](WRAP_CONTENT,
      WRAP_CONTENT) <~ (horizontal) <~ padding(all = 10 dp)


    val questionTextView = w[TextView] <~ wire(questionView) <~
      text(questions(currentIndex).question) <~
      layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ padding(all = 24 dp)
    val answerView = l[LinearLayout](
      w[Button] <~ text("true")
        <~ On.click {
        checkAnswer(true)
      }
        <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT),
      w[Button] <~ text("false")
        <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ On.click {
        checkAnswer(false)
      }
    ) <~ layoutParams[LinearLayout](WRAP_CONTENT,
      WRAP_CONTENT) <~ (horizontal)


    val cheatButton = w[Button] <~ text("Cheat!") <~
      layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ On.click {
      val intent =
        new android.content.Intent(QuizActivity.this, classOf[
          CheatActivity])
      val answer = questions(currentIndex).mTrue
      intent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE,
        answer)
      startActivityForResult(intent, 0)
      Ui(true)
    }

    val portraitLayout = l[LinearLayout](
      questionTextView,
      answerView,
      cheatButton,
      prevNextLayout
    ) <~ (vertical) <~
      Tweak[LinearLayout] {
        view ⇒
          view.setGravity(Gravity.CENTER)
      }

    val landscapeLayout = l[FrameLayout](
      questionTextView <~ setFrameLayoutGravity(Gravity.
        CENTER_HORIZONTAL),
      answerView <~ setFrameLayoutGravity(Gravity.CENTER_VERTICAL
        | Gravity.CENTER_HORIZONTAL),
      cheatButton <~ setFrameLayoutGravity(Gravity.BOTTOM
        | Gravity.CENTER),
      prevNextLayout <~ setFrameLayoutGravity(Gravity.BOTTOM
        | Gravity.RIGHT)
    ) <~ layoutParams[LinearLayout](MATCH_PARENT,
      MATCH_PARENT)

    val layout = if (portrait) portraitLayout else landscapeLayout

    setContentView(getUi(layout))
  }

  override def onDestroy() {
    super.onDestroy()
    d(tag, "on onDestroy called")
  }

  override def onSaveInstanceState(savedInstanceState: Bundle) = {
    d(tag, "inside savedInstanceState")
    savedInstanceState.putInt(key_index, currentIndex)
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    mIsCheater = data != null && (requestCode match {
      case 0 => data.getBooleanExtra(
        CheatActivity.EXTRA_ANSWER_SHOWN, false)
      case _ => false
    })
  }


}
 