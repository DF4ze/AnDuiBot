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
import android.view.View;
import android.widget.TextView;
import controleur.Controleur;
import controleur.socketclient.Emission;

public class MainActivity extends Activity {

	// Attribut Controleur et Emission via socket
	private Controleur _c;
	private Emission _e;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
        setRequestedOrientation(0);


		// Instanciation du controleur
		_c = new Controleur();
	}

	// Lors de la demande de connexion au socket
	public void onClickConnect(View view) {
		
		// On demande au controleur de démarrer le thread seulement si on n'est pas deja connecté
		if( _e == null )
			_e = _c.startThreadClient();
		
		// Affichage si nous sommes connectés ou pas.
		TextView tv = (TextView) findViewById(R.id.tv);
		if( _e != null )
			tv.setText("Connected");
		else
			tv.setText("Error Connecting");
			
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
	
	public void onClickUp(View view) {
		DirectionAction ad = new DirectionAction(127, 0, IAction.prioHight);
		if( _e != null )
		_e.addAction(ad);
	}

	public void onClickDown(View view) {
		DirectionAction ad = new DirectionAction(-127, 0, IAction.prioHight);
		
		if( _e != null )
		_e.addAction(ad);
	}

	public void onClickRight(View view) {
		DirectionAction ad = new DirectionAction(0, 127, IAction.prioHight);
		
		if( _e != null )
		_e.addAction(ad);
	}

	public void onClickLeft(View view) {
		DirectionAction ad = new DirectionAction(0, -127, IAction.prioHight);
		
		if( _e != null )
		_e.addAction(ad);
	}


	// Partie Tourelle
	public void onClickUpTour(View view) {
		TourelleAction ad = new TourelleAction(127, 0, IAction.prioHight);
		if( _e != null )
		_e.addAction(ad);
	}

	public void onClickDownTour(View view) {
		TourelleAction ad = new TourelleAction(-127, 0, IAction.prioHight);
		
		if( _e != null )
		_e.addAction(ad);
	}

	public void onClickRightTour(View view) {
		TourelleAction ad = new TourelleAction(0, 127, IAction.prioHight);
		
		if( _e != null )
		_e.addAction(ad);
	}

	public void onClickLeftTour(View view) {
		TourelleAction ad = new TourelleAction(0, -127, IAction.prioHight);
		
		if( _e != null )
		_e.addAction(ad);
	}

	

}


