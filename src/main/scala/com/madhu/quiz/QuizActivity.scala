package com.madhu.quiz

import android.os.Bundle
import android.widget.{LinearLayout, TextView, Button}
import android.view.ViewGroup.LayoutParams._
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
  Ui[Boolean] ={
      (toast(cap) <~ gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL) 
        <~ fry) ~
      Ui(true)
    }
}


class QuizActivity extends Activity with Helper with  
Contexts[Activity] {
 
 case class TrueFalse(val question:String, mTrue :
  Boolean)
 val questions = Vector(TrueFalse("hi",false),
   TrueFalse("catchme",true))
 var currentIndex = 0

 
 override def onCreate(savedInstanceState: Bundle) = {
  var questionView = slot[TextView]
  var prev = slot[Button]
  var next = slot[Button]
  super.onCreate(savedInstanceState)       
     val prevNextLayout = l[LinearLayout](
       w[Button] <~ text("Prev")
         <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~  On.click {
          currentIndex = if(currentIndex>0) currentIndex-1 else 0
          questionView <~ text(questions(currentIndex).question) 
        } <~ wire(prev)
        /*<~ Tweak[Button](button => {
            button.setCompoundDrawablesWithIntrinsicBounds(0,0,0,
              R.drawable.left-button.png)
          })*/,
        w[Button] <~ text("Next") <~ wire(next)
         <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~  On.click {
          currentIndex =  currentIndex+1
          questionView <~ text(questions(currentIndex).question)           
        } 
    ) <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ (horizontal) <~ padding(all = 10 dp)

     val view = l[LinearLayout](
      w[TextView] <~ wire(questionView)
      <~ text(questions(currentIndex).question)
      <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ padding(all = 24 dp),      
      l[LinearLayout](
         w[Button] <~ text("true")
         <~ On.click {
          val truthful = questions(currentIndex).mTrue
          if(truthful) caption("correct answer")
          else caption("wrong answer")
         }
         <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT),
         w[Button] <~ text("false")
         <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ On.click {
          val truthful = questions(currentIndex).mTrue
          if(!truthful) caption("correct answer")
          else caption("wrong answer")
         }
      ) <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ (horizontal),
        /*w[Button] <~ text("Next")
         <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~  On.click {
          currentIndex = currentIndex+1
          questionView <~ text(questions(currentIndex).question) 
        } */
        prevNextLayout
    ) <~ (vertical) <~
       Tweak[LinearLayout] { view ⇒
         view.setGravity(Gravity.CENTER)
      }/* <~ Transformer {
        // here we set a padding of 4 dp for all inner views
        case x: View ⇒ x <~ padding(all = 4 dp)
      }*/
    setContentView(getUi(view))
 }

}
 