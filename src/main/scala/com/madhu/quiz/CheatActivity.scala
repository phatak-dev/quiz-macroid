package com.madhu.quiz

import android.os.Bundle
import android.widget.{LinearLayout, TextView, Button,FrameLayout}
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

// import macroid stuff
import macroid._
import macroid.util.Ui
import macroid.FullDsl._
import macroid.contrib.ExtraTweaks._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object CheatActivity{
  val EXTRA_ANSWER_IS_TRUE = "com.madhu.quiz.answer_is_true"
}

class CheatActivity extends Activity with Helper with  
Contexts[Activity] {
   var answerTextView = slot[TextView]
   var mAnswerIsTrue:Boolean = _  
   override def onCreate(savedInstanceState: Bundle) = {
   	super.onCreate(savedInstanceState)
   val view = l[LinearLayout](
   	  w[TextView] <~ text("Are you sure you want this")
   	  <~ layoutParams[LinearLayout](WRAP_CONTENT,WRAP_CONTENT)
   	  <~ padding(all = 24 dp),
   	  w[TextView] <~ wire(answerTextView)
   	  <~ layoutParams[LinearLayout](WRAP_CONTENT,WRAP_CONTENT)
   	  <~ padding(all = 24 dp),
   	  w[Button] <~ text("show Answer") <~ On.click{
   	  	answerTextView <~ text( if(mAnswerIsTrue) "true"
   	  	 else "false" )
   	  }
   	  <~ layoutParams[LinearLayout](WRAP_CONTENT,WRAP_CONTENT)
   	) <~ layoutParams[LinearLayout](MATCH_PARENT,MATCH_PARENT) <~ 
      (vertical) <~
       Tweak[LinearLayout] { view ⇒
         view.setGravity(Gravity.CENTER)
      }
  
    mAnswerIsTrue = getIntent().getBooleanExtra(
    	CheatActivity.EXTRA_ANSWER_IS_TRUE, false)
    setContentView(getUi(view))
  }
   

}