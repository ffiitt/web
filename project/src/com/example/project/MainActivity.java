package com.example.project;

//import com.example.helloandroid.MainActivity;
//import com.example.helloandroid.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import org.apache.http.HttpResponse;

public class MainActivity extends Activity implements ViewFactory,OnTouchListener{
    private ImageSwitcher mImageSwitcher;
    private int[] images;
    private int flag = 1;
    private int currentPosition;
    private float downX;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		images = new int[]{R.drawable.bai1,R.drawable.bai2,R.drawable.bai3};
		mImageSwitcher = (ImageSwitcher)findViewById(R.id.imageSwitcher1);
		mImageSwitcher.setFactory(this);
		mImageSwitcher.setOnTouchListener(this);
		currentPosition = getIntent().getIntExtra("positon",0);
		mImageSwitcher.setImageResource(images[currentPosition]);	
		final Button my = (Button)findViewById(R.id.my);
		my.setOnClickListener(new listen_my());
	}
	class listen_my implements OnClickListener{
		@Override
		public void onClick(View v){
			if(flag == 1){
				Intent intent = new Intent(MainActivity.this,test.class);//testÆäÊµÊÇlogin
				startActivity(intent);
			}
		}
	}
    @Override
    public View makeView(){
    	final ImageView i = new ImageView(this);
    	i.setBackgroundColor(0xff000000);
    	i.setScaleType(ImageView.ScaleType.CENTER_CROP);
    	i.setLayoutParams(new ImageSwitcher.LayoutParams(android.widget.TableLayout.LayoutParams.FILL_PARENT,android.widget.TableLayout.LayoutParams.FILL_PARENT));
    	return i;
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    @Override
    public boolean onTouch(View v,MotionEvent event){
    	switch(event.getAction()){
    	  case MotionEvent.ACTION_DOWN:{
    		  downX = event.getX();
    		  break;
    	  }
    	  case MotionEvent.ACTION_UP:{
    		  float lastX = event.getX();
    		  if(lastX > downX){
    			  if(currentPosition > 0){
    				  mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_in));
    				  mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_out));
    				  currentPosition--;
    				  mImageSwitcher.setImageResource(images[currentPosition]);
    			  }
    			  else{
    				  Toast.makeText(getApplication(), "the first picture", Toast.LENGTH_SHORT).show();
    			  }
    		  }
    		  if(lastX < downX){
    			  if(currentPosition < images.length-1){
    				  mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.right_in));
    				  mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.left_out));
    				  currentPosition++;
    				  mImageSwitcher.setImageResource(images[currentPosition]);
    			  }
    			  else{
    				  Toast.makeText(getApplication(), "the last picture",Toast.LENGTH_SHORT).show();
    			  }
    		  }
    	  }
    	  break;
    	}
    	return true;
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
