package com.madhu.flash

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

trait CamHandling {
  // sets text, large font size and a long click handler
  var camInstance:Camera = null
  def onCamera(surfaceView:SurfaceView)(implicit ctx: AppContext):
    Ui[Boolean]
    = {        
    if(camInstance==null)  {
    camInstance = Camera.open()
    val properties = camInstance.getParameters()
    properties.setFlashMode("torch")
    camInstance.setParameters(properties)
    camInstance.setPreviewDisplay(surfaceView.getHolder())
    camInstance.startPreview()         
   }
  Ui(true)    
 }

  def offCamera(surfaceView:SurfaceView)
   (implicit ctx: AppContext) = {    
    println("switched off")
    if(camInstance!=null){
    camInstance.stopPreview()
    val properties = camInstance.getParameters()
    properties.setFlashMode("off")
    camInstance.setParameters(properties);
    camInstance.release();    
    camInstance=null            
    }            
   
   Ui(true)  
 }
}


import scala.concurrent.ExecutionContext.Implicits.global

class Flash extends Activity with CamHandling with Contexts[Activity] {
 var surfaceView = slot[SurfaceView]
 //var clickButton = slot[Button]

 override def onCreate(savedInstanceState: Bundle) = {
  super.onCreate(savedInstanceState)  
   val view = l[LinearLayout](
    w[SurfaceView] <~ wire(surfaceView) <~ 
      layoutParams[LinearLayout](1, 1)     
    ) <~
      // match layout orientation to screen orientation
      (vertical) <~ Transformer {
        // here we set a padding of 4 dp for all inner views
        case x: View ⇒ x <~ padding(all = 4 dp)
      }

    setContentView(getUi(view)) 
    Future{   
    onCamera(surfaceView.get)           
  }
 }

 override def onPause() = {
  super.onPause()  
  offCamera(surfaceView.get)  
 }
 
 /*override def onResume() = {
  super.onResume()
  onCamera(surfaceView.get)       
  }*/

 /*override def onStart() = {
  super.onStart()  
  onCamera(surfaceView.get)       
 }
 override def onRestart() = {
  super.onRestart()
  onCamera(surfaceView.get)       
 }
  
  override def onStop() = {
   super.onStop()
   offCamera(surfaceView.get)
  }
  override def onDestroy() = {
   super.onDestroy()
   offCamera(surfaceView.get)
  }*/

}

