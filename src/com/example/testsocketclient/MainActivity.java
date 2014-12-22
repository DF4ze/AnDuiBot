/***************************************************************\
 * 
 *  Application Cliente qui permet de se connecter a un serveur 
 *  tournant sur un PC (RaspDuiBot.jar) avec la mise en place
 *  de Socket.
 *  
 *  le but est de récupérer le clic sur 4 boutons directionnels
 *  et d'envoyer des objets au serveur distant
 *  
 *  03/12/2014
 *  Clément ORTIZ
 *  
 *  ( Application de test qui se veut tendre vers du MVC... 
 *  mais ne l'ai pas totalement, j'en ai conscience!
 *  C'est en réalité une adaptation d'une application deja 
 *  existante sous JavaSE... 
 *  C'est pour cela qu'il reste quelques fioritures )
 * 
 * 
 *  Les parametres de connexion IP et PORT sont dans ArduiBotServer.java
 *  RaspDuiBot.jar -h pour plus de config pour le serveur
 *  
 *  !! Attention la rotation de la tablette n'est pas géré !!
 *  	
 * 
\***************************************************************/


package com.example.testsocketclient;

import modeles.dao.communication.beansactions.DirectionAction;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beansactions.TourelleAction;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import controleur.Controleur;
import controleur.socketclient.Emission;

public class MainActivity extends Activity {

	// Attribut Controleur et Emission via socket
	private Controleur _c;
	private Emission _e;
	private int maxPWM = 255;
	private int posX = 90;
	private int posY = 90;
	private int delta = 5;
	private int minServo = 0;
	private int maxServo = 180;
	
	final Handler h = new Handler();
	private MyTask task;
	private int delay = 125;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
        setRequestedOrientation(0);


		// Instanciation du controleur
		_c = new Controleur( this );
		
		// déclaration des listeners Direction
		Button btnDirUp = (Button) findViewById(R.id.btnUp);
		Button btnDirRight = (Button) findViewById(R.id.btnRight);
		Button btnDirDown = (Button) findViewById(R.id.btnDown);
		Button btnDirLeft = (Button) findViewById(R.id.btnLeft);
		
		btnDirUp.setOnTouchListener( onClickUp );
		btnDirRight.setOnTouchListener( onClickRight );
		btnDirDown.setOnTouchListener( onClickDown );
		btnDirLeft.setOnTouchListener( onClickLeft );
		
		// déclaration des listeners Tourelle
		Button btnTourUp = (Button) findViewById(R.id.btnUpTour);
		Button btnTourRight = (Button) findViewById(R.id.btnRightTour);
		Button btnTourDown = (Button) findViewById(R.id.btnDownTour);
		Button btnTourLeft = (Button) findViewById(R.id.btnLeftTour);
		
		btnTourUp.setOnTouchListener( onClickUpTour );
		btnTourRight.setOnTouchListener( onClickRightTour );
		btnTourDown.setOnTouchListener( onClickDownTour );
		btnTourLeft.setOnTouchListener( onClickLeftTour );
		
	}

	// Lors de la demande de connexion au socket
	public void onClickConnect(View view) {
		
		TextView tv = (TextView) findViewById(R.id.tv);
		
		// mise à jour du modele avec l'IP de l'edittext
		if( _c.majIP() ){
		
			// On demande au controleur de démarrer le thread seulement si on n'est pas deja connecté
			if( _e == null )
				_e = _c.startThreadClient();
			
			// Affichage si nous sommes connectés ou pas.
			if( _e != null )
				tv.setText("Connected");
			else
				tv.setText("Error Connecting");
		}else{
			tv.setText("Wrong IP:PORT");
			
		}
			
	}

	// lors de la demande d'arret du thread
	public void onClickStop(View view) {
		_c.stopThreadClient();
		_e = null;
		
		TextView tv = (TextView) findViewById(R.id.tv);
		tv.setText("Disconnected");
		
	}
	
	/**
	 * Partie qui devrait etre un peu plus MVC ;)
	 * 
	 */
	
	// Pour chaque click sur un bouton on génère un IAction et on demande au Thread d'emission de l'envoyer.
	
	
	// Partie Direction
	public OnTouchListener  onClickUp = new OnTouchListener() {
		public boolean onTouch(View yourButton , MotionEvent theMotion) {
			DirectionAction ad = null;
			switch ( theMotion.getAction() ) {
		    case MotionEvent.ACTION_DOWN: 
		    	ad = new DirectionAction(0, maxPWM, IAction.prioHight);
		    	break;
		    case MotionEvent.ACTION_UP: 
		    	ad = new DirectionAction(0, 0, IAction.prioHighest);
		    	break;
		    }
		    
			if( _e != null )
			_e.addAction(ad);
		    return true;
		}
	};

	public OnTouchListener onClickDown = new OnTouchListener() {
		public boolean onTouch( View yourButton , MotionEvent theMotion) {
			DirectionAction ad = null;
			switch ( theMotion.getAction() ) {
		    case MotionEvent.ACTION_DOWN: 
		    	ad = new DirectionAction(0, -maxPWM, IAction.prioHight);
		    	break;
		    case MotionEvent.ACTION_UP: 
		    	ad = new DirectionAction(0, 0, IAction.prioHighest);
		    	break;
		    }
		    
			if( _e != null )
			_e.addAction(ad);
		    return true;
		}
	};

	public OnTouchListener onClickRight = new OnTouchListener() {
		public boolean onTouch(View yourButton , MotionEvent theMotion) {
			DirectionAction ad = null;
			switch ( theMotion.getAction() ) {
		    case MotionEvent.ACTION_DOWN: 
		    	ad = new DirectionAction(maxPWM, 0, IAction.prioHight);
		    	break;
		    case MotionEvent.ACTION_UP: 
		    	ad = new DirectionAction(0, 0, IAction.prioHighest);
		    	break;
		    }
		    
			if( _e != null )
			_e.addAction(ad);
		    return true;
		}
	};

	public OnTouchListener onClickLeft = new OnTouchListener() {
		public boolean onTouch(View yourButton , MotionEvent theMotion) {
			DirectionAction ad = null;
			switch ( theMotion.getAction() ) {
		    case MotionEvent.ACTION_DOWN: 
		    	ad = new DirectionAction(-maxPWM, 0, IAction.prioHight);
		    	break;
		    case MotionEvent.ACTION_UP: 
		    	ad = new DirectionAction(0, 0, IAction.prioHighest);
		    	break;
		    }
		    
			if( _e != null )
			_e.addAction(ad);
		    return true;
		}

	};
	
	
	
	
	

	// Partie Tourelle    
    
	public OnTouchListener onClickUpTour = new OnTouchListener() {
		public boolean onTouch(View yourButton , MotionEvent theMotion) {
			
			switch ( theMotion.getAction() ) {
		    case MotionEvent.ACTION_DOWN: 
				task = new MyTask( delta, "Y" );
		    	h.postDelayed(task, delay);
		    	break;
		    case MotionEvent.ACTION_UP: 
		    	task.stop();
		    	break;
		    }
		    

		    return true;
		}

	};

	
	public OnTouchListener onClickDownTour = new OnTouchListener() {
		public boolean onTouch(View yourButton , MotionEvent theMotion) {
			switch ( theMotion.getAction() ) {
		    case MotionEvent.ACTION_DOWN: 
				task = new MyTask( -delta, "Y" );
		    	h.postDelayed(task, delay);
		    	break;
		    case MotionEvent.ACTION_UP: 
		    	task.stop();
		    	break;
		    }
		    return true;
		}

	};

	
	public OnTouchListener onClickRightTour = new OnTouchListener() {
		public boolean onTouch(View yourButton , MotionEvent theMotion) {
			switch ( theMotion.getAction() ) {
		    case MotionEvent.ACTION_DOWN: 
				task = new MyTask( delta, "X" );
		    	h.postDelayed(task, delay);
		    	break;
		    case MotionEvent.ACTION_UP: 
		    	task.stop();
		    	break;
		    }
		    return true;
		}

	};


	public OnTouchListener onClickLeftTour = new OnTouchListener() {
		public boolean onTouch(View yourButton , MotionEvent theMotion) {
			switch ( theMotion.getAction() ) {
		    case MotionEvent.ACTION_DOWN: 
				task = new MyTask( -delta, "X" );
		    	h.postDelayed(task, delay);
		    	break;
		    case MotionEvent.ACTION_UP: 
		    	task.stop();
		    	break;
		    }
		    return true;
		}

	};


	

	class MyTask implements Runnable{
		 
		public int move;
		public String axis;
		public boolean repeat = true;
		
		public MyTask(){
			
		}
		
		public MyTask( int m, String a ){
			setMove(m);
			setAxis(a);
		}
		
		public void setMove( int m ){
			move = m;
		}
		public void setAxis( String a ){
			axis = a;
		}
		public void stop(){
			repeat = false;
		}
       
        public void run(){
        	if( axis.equals("x") || axis.equals("X") )
        		posX += move;
        	else if( axis.equals("y") || axis.equals("Y") )
        		posY += move;
        	
        	if( posX > maxServo ){
        		posX = maxServo;
        		repeat = false;
        	}else if( posX < minServo ){
        		posX = minServo;
        		repeat = false;
        	}
        	if( posY > maxServo ){
        		posY = maxServo;
        		repeat = false;
        	}else if( posY < minServo ){
        		posY = minServo;
        		repeat = false;
        	}       	
        	
			TourelleAction ad = new TourelleAction(posX, posY, IAction.prioHight);
			_e.addAction(ad);

        	
        	if( repeat )
        		h.postDelayed(this, delay);
        	
        	repeat = true;
        }
    }
}


