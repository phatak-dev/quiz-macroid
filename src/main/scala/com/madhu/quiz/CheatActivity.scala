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



class CheatActivity extends Activity with Helper with  
Contexts[Activity] {
   var answerTextView = slot[TextView]
   override def onCreate(savedInstanceState: Bundle) = {
   val view = l[LinearLayout](
   	  w[TextView] <~ text("Are you sure you want this")
   	  <~ layoutParams[LinearLayout](WRAP_CONTENT,WRAP_CONTENT)
   	  <~ padding(all = 24 dp),
   	  w[TextView] <~ wire(answerTextView)
   	  <~ layoutParams[LinearLayout](WRAP_CONTENT,WRAP_CONTENT)
   	  <~ padding(all = 24 dp),
   	  w[Button] <~ text("show Answer")
   	  <~ layoutParams[LinearLayout](WRAP_CONTENT,WRAP_CONTENT)
   	) <~ layoutParams[LinearLayout](MATCH_PARENT,MATCH_PARENT) <~ 
      (vertical) <~
       Tweak[LinearLayout] { view ⇒
         view.setGravity(Gravity.CENTER)
      }
  
    setContentView(getUi(view))
  }
   

}