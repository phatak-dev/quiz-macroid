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
 
 override def onCreate(savedInstanceState: Bundle) = {
  super.onCreate(savedInstanceState)  
     val view = l[LinearLayout](
      w[TextView] <~ text("Some question?")
      <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ padding(all = 24 dp),
      l[LinearLayout](
         w[Button] <~ text("true")
         <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT),
         w[Button] <~ text("false")
         <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ On.click {
          caption("clicked false")          
         }
      ) <~ layoutParams[LinearLayout](WRAP_CONTENT,
        WRAP_CONTENT) <~ (horizontal)
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
 