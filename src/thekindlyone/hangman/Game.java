package thekindlyone.hangman;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Game extends Activity {
	public String rawmovies;
	public String movie;
	public String[] movies;
	public ArrayList<Character> donelist = new ArrayList<Character>();
	public ArrayList<Character> triedlist = new ArrayList<Character>();
	public TextView tv1;
	public Random r = new Random();
	public String h=new String("HANGMAN");
	public EditText editText;
	public Button button;
	public int streak=0;
	public int played=0;
	public int won=0;
	public TextView tv2;
	public TextView tv3;
	public ImageView image;
	int[] images = {
		    R.drawable.h, R.drawable.ha,
		    R.drawable.han, R.drawable.hang,
		    R.drawable.hangm, R.drawable.hangma, R.drawable.hangman 
		    };
	
	public ImageView hang;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		AssetManager assetManager = getAssets();
		add_special();
		editText = (EditText) findViewById(R.id.input);
		tv1 = (TextView)findViewById(R.id.tv1);
        tv1.setMovementMethod(new ScrollingMovementMethod());
		tv2 = (TextView)findViewById(R.id.streak);
		tv3 = (TextView)findViewById(R.id.stats);
		button=(Button) findViewById(R.id.button);
		image=(ImageView) findViewById(R.id.img);
		hang=(ImageView) findViewById(R.id.hang);
		//final View vw=findViewById(R.id.input);
		
		InputStream input;
		try {
            input = assetManager.open("movielist.txt");
             
             int size = input.available();
             byte[] buffer = new byte[size];
             input.read(buffer);
             input.close();
 
             // byte buffer into a string
            rawmovies = new String(buffer);
            movies=rawmovies.split("\r\n|\r|\n");
            
            movie=movies[r.nextInt(movies.length)].trim().toLowerCase();
             
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		
		tv1.setMovementMethod(new ScrollingMovementMethod());
		String displaystring=stringwork()+"\nGuess Now, One letter at a time.";
		tv1.setText(displaystring);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	  @Override
	  public boolean onKeyUp(int keyCode, KeyEvent event) {
	 
	      switch (keyCode) {
	      case KeyEvent.KEYCODE_ENTER:
	          button.performClick();
	          return true;
	      default:
	    	  return super.onKeyUp(keyCode, event);
	      }
	    
	    
	  }
	
	public String stringwork(){
		ArrayList<Character> rstring=new ArrayList<Character>();
		for (int i = 0; i < movie.length(); i++){
		    char c = movie.charAt(i); 
		    if(donelist.contains(c)){
		    	rstring.add(c);
		    }
		    else{
		    	if(c==' '){
	    			rstring.add('/');}
		    	
		    	else{
		    		
		    			rstring.add('_');
		    			rstring.add(' ');
		    		
		    		
		    	}
		    }
		    
		    //Process char
		}
		return getStringRepresentation(rstring);
	}

	public String getStringRepresentation(ArrayList<Character> list)
	{    
	    StringBuilder builder = new StringBuilder(list.size());
	    for(Character ch: list)
	    {
	        builder.append(ch);
	    }
	    return builder.toString();
	}
   public void refresh_hang(int score){
	   switch(score){
	   case 0:
	   }
   }
	
	public void refresh_display(int code,char input){
    	String displaystring=stringwork();
    	String c = Character.toString(input);
    	String score = h.substring(0,triedlist.size());
    	int l=score.length();
    	if(score.length()=="HANGMAN".length()){
    		code=-3;
    	}
    	if(displaystring.indexOf('_')==-1){
    		code=3;
    	}
    	String correct_message=" Bingo, <font color=#01DF01>"+c+" is correct</font> <br>"+score+"<br>"+displaystring+"<br>Guess now, one letter at a time.";
    	String incorrect_message=" Sorry, <font color=#DF0101>"+c+" is Incorrect</font> <br>"+score+"<br>"+displaystring+"<br>Guess now, one letter at a time.";
    	String already_tried="<font color=#DF0101> You already tried "+c+"</font><br> Try Again<br>"+score+"<br>"+displaystring+"<br>Guess now, one letter at a time.";
    	switch(code){
    	case 1:
    		tv1.setText(Html.fromHtml(correct_message));
    		image.setImageResource(R.drawable.correct);
    		if(score.length()!=0){
    			hang.setImageResource(images[l-1]);
    		}

    		break;
    	case 0:
    		tv1.setText(Html.fromHtml(incorrect_message));
    		image.setImageResource(R.drawable.incorrect);
    		if(score.length()!=0){
    			hang.setImageResource(images[l-1]);
    		}
    		break;
    	case -1:
    		tv1.setText(Html.fromHtml(already_tried));
    		image.setImageResource(R.drawable.incorrect);
    		if(score.length()!=0){
    			hang.setImageResource(images[l-1]);
    		}
    		break;
    	case -2:
    		tv1.append("ENTER A LETTER");
    		image.setImageResource(R.drawable.incorrect);
    		break;
    	case -3:    //loss
    		//tv1.setText("HANGMAN\n You loose..The answer was "+movie);
    		reset_game(code,movie,displaystring);
    		streak=0;
    		image.setImageResource(R.drawable.incorrect);
    		if(score.length()!=0){
    			hang.setImageResource(images[l-1]);
    		}
    		break;
    	case 3: //victory
    	    //tv1.setText("VICTORY.. way to go.. the answer was "+movie);
    	    reset_game(code,movie,displaystring);
    	    streak=streak+1;
    	    image.setImageResource(R.drawable.correct);
    		if(score.length()!=0){
    			hang.setImageResource(images[l-1]);
    		}
    	    break;
    	}
    	if(score.length()==0){
			hang.setImageResource(R.drawable.blank);
		}
	}
	public void add_special(){
		donelist.add('-');
		donelist.add('\'');
		donelist.add('`');
		donelist.add('?');
		donelist.add('!');
		donelist.add('.');
		donelist.add(';');
		donelist.add(',');
		donelist.add('"');		
		donelist.add(')');
		donelist.add('(');
		donelist.add('*');
		donelist.add('+');
		donelist.add('$');
		donelist.add('%');
		donelist.add('&');
		donelist.add('@');
		donelist.add('^');
		donelist.add('=');
        donelist.add(':');
	}
	
	public void reset_game(int code,String lastmovie,String d_string){
		played=played+1;
		movie=movies[r.nextInt(movies.length)].trim().toLowerCase();
		donelist.clear();
		triedlist.clear();
		add_special();
		
		String result=new String("");
		if(code==3){
			 result="<font color=#01DF01>VICTORY</font>.. way to go.. the answer was <font color=#0101DF><br>"+lastmovie+"</font>"+"<br/>NEW GAME<br/>"+stringwork()+"<br/>Guess Now, one letter at a time.";
			 won=won+1;
		}
		if(code==-3){
			 result=d_string+"<br/>You loose..The answer was <font color=#0101DF><br/>"+lastmovie+"</font>"+"<br/><font color=#01A9DB>NEW GAME</font><br/>"+stringwork()+"<br/>Guess Now, one letter at a time.";
		}
		
		tv1.setText(Html.fromHtml(result));
		//tv1.append(display);
		//tv1.invalidate();   
		String text="played="+Integer.toString(played)+", won="+Integer.toString(won);
		tv3.setText(text);
	}
	
    public void handler(View view)
    {   int code=0;
    	String inp= editText.getText().toString();
		char input;
    	if(inp.length()!=0){input =inp.charAt(0);}
		else{
			input=' ';
			}
		//tv1.setText("DETECTED");
		if(input==' '){
			code=-2;
		}
		else{
		if(donelist.contains(input)||triedlist.contains(input)){
			code=-1;
		}
		else{
		if(movie.indexOf(input)!=-1){
			donelist.add(input);	
		    code=1;
		}
		else{
			triedlist.add(input);
		    code=0;
		}
		}
		}
		refresh_display(code,input);
		tv2.setText("Current Streak = "+Integer.toString(streak));
		TextKeyListener.clear(editText.getText());
	
    }

    

    
    
}
