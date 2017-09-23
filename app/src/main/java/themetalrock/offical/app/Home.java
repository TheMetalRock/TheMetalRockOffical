package themetalrock.offical.app;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import nadav.tasher.lightool.Light;

public class Home extends Activity {
    private final String service="http://sign.thepuzik.com/in/services/school/themetalrock/feed.php";
    private final String serviceProvider="http://sign.thepuzik.com";
    private int color= Color.parseColor("#444444");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void splash() {
        final Window window=getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
        window.setNavigationBarColor(color);
        LinearLayout ll=new LinearLayout(this);
        ll.setGravity(Gravity.CENTER);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundColor(color);
        ImageView icon=new ImageView(this);
        icon.setImageDrawable(getDrawable(R.drawable.ic_themetalrock));
        int is=(int) (Light.Device.screenX(getApplicationContext()) * 0.8);
        icon.setLayoutParams(new LinearLayout.LayoutParams(is, is));
        ll.addView(icon);
        setContentView(ll);
    }
    private void init(){
        splash();
        if(Light.Device.isOnline(getApplicationContext())) {
            new Light.Net.Pinger(5000, new Light.Net.Pinger.OnEnd() {
                @Override
                public void onPing(String s, boolean b) {
                    if(s.equals(serviceProvider)&&b){
                        home();
                    }else if(s.equals(serviceProvider)&&!b){
                        popupRetry("Could Not Connect To Server");
                    }
                }
            }).execute(serviceProvider);
        }else{
            popupRetry("No Internet Connection");
        }
    }
    private void home(){
        final LinearLayout all=new LinearLayout(this);
        all.setOrientation(LinearLayout.VERTICAL);
        all.setGravity(Gravity.START);
        all.setBackgroundColor(color);
        final LinearLayout navbarAll=new LinearLayout(this);
        navbarAll.setBackgroundColor(color+0x333333);
        getWindow().setStatusBarColor(color+0x333333);
        navbarAll.setOrientation(LinearLayout.HORIZONTAL);
        navbarAll.setGravity(Gravity.CENTER);
        final ImageView nutIcon=new ImageView(this);
        int screenY=Light.Device.screenY(this);
        final int nutSize=(screenY / 8) - screenY / 30;
        LinearLayout.LayoutParams nutParms=new LinearLayout.LayoutParams(nutSize, nutSize);
        nutIcon.setLayoutParams(nutParms);
        nutIcon.setImageDrawable(getDrawable(R.drawable.ic_themetalrock));
        final ObjectAnimator anim=ObjectAnimator.ofFloat(nutIcon, View.TRANSLATION_X, Light.Animations.VIBRATE_SMALL);
        anim.setDuration(1500);
        anim.setRepeatMode(ObjectAnimator.RESTART);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();
        navbarAll.addView(nutIcon);
        int navY=screenY / 8;
        LinearLayout.LayoutParams navParms=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, navY);
        navParms.gravity=Gravity.START;
        navbarAll.setLayoutParams(navParms);
        all.addView(navbarAll);
        setContentView(all);
    }
    void popupRetry(String text) {
        AlertDialog.Builder pop=new AlertDialog.Builder(this);
        pop.setCancelable(true);
        pop.setMessage("Error: " + text);
        pop.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                init();
            }
        });
        pop.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                init();
            }
        });
        pop.show();
    }
}
