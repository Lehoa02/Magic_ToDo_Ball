package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import android.Manifest;

import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;


public class MainActivity extends AppCompatActivity {
    private ArrayList<String> tasksList = new ArrayList<>();
    private ArrayList<String> completedTasksList = new ArrayList<>();
    private FrameLayout firstScene;
    private FrameLayout secondScene;
    private TextView displayTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magic_8_ball_start_page);







        firstScene = findViewById(R.id.firstScene);
        secondScene = findViewById(R.id.secondScene);
        displayTask = findViewById(R.id.text_in_triangle);


        EditText todoEditText = findViewById(R.id.todoEditText);
        Button addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = todoEditText.getText().toString().trim();
                if (!task.isEmpty()) {
                    tasksList.add(task);
                    todoEditText.setText(""); // Clear EditText
                }
            }
        });
        Button completedButton = findViewById(R.id.completedButton);
        KonfettiView konfettiView = findViewById(R.id.konfetti);


        firstScene.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Create a shake animation for the ball
                ObjectAnimator shakeAnimator = ObjectAnimator.ofFloat(firstScene, "translationX", 20f, -30f, 30f, -30f, 30f, -25f, 25f, 20f);
                shakeAnimator.setDuration(1200); // Duration of the animation
                shakeAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // Set the animation interpolator

                // Start the animation
                shakeAnimator.start();

                // Trigger vibration
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(800, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(800);
                    }
                }

                // Change visibility of scenes after a delay (adjust delay time as needed)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!tasksList.isEmpty()){
                        // Change visibility of scenes
                        firstScene.setVisibility(View.GONE);
                        secondScene.setVisibility(View.VISIBLE);
                        shakeMagic8Ball(); // Trigger the magic 8 ball shake
                        completedButton.setVisibility(View.VISIBLE);}
                    }
                }, 1000); // Delay time after the animation completes
            }
        });
        //TextView displayText = findViewById(R.id.text_in_triangle);
        secondScene.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Create a shake animation for the ball
                ObjectAnimator shakeAnimator = ObjectAnimator.ofFloat(secondScene, "translationX", 20f, -30f, 30f, -30f, 30f, -25f, 25f, 20f);
                shakeAnimator.setDuration(1200); // Duration of the animation
                shakeAnimator.setInterpolator(new AccelerateDecelerateInterpolator()); // Set the animation interpolator

                // Start the animation
                shakeAnimator.start();

                // Trigger vibration
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(800, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(800);
                    }

                    new CountDownTimer(1200, 200) { // Update every 200ms (adjust timing as needed)


                        public void onTick(long millisUntilFinished) {
                            shakeMagic8Ball();
                        }

                        public void onFinish() {
                            shakeMagic8Ball();
                        }
                    }.start();
            }}
        });

        Shape.DrawableShape drawableShapes= new Shape.DrawableShape(AppCompatResources.getDrawable(this, R.drawable.ic_android),true);
        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tasksList.isEmpty()) {
                    // Remove the current task from tasksList
                    String completedTask = displayTask.getText().toString();
                    tasksList.remove(completedTask);

                    // Trigger confetti right after a task is removed
                    triggerConfetti();

                    // Check if the task list is empty
                    if (tasksList.isEmpty()) {
                        // If tasksList is empty, revert to the firstScene
                        firstScene.setVisibility(View.VISIBLE);
                        secondScene.setVisibility(View.GONE);
                        completedButton.setVisibility(View.GONE);
                    } else {
                        // Get a new random task
                        shakeMagic8Ball();
                    }
                }
            }
        });


    }

    private void triggerConfetti() {
        EmitterConfig emitterConfig = new Emitter(300, TimeUnit.MILLISECONDS).max(300);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .shapes(Shape.Circle.INSTANCE, Shape.Square.INSTANCE, drawableShapes)
                        .spread(360)
                        .position(0.0, 0.0, 1.0, 1.0)  // Cover the entire screen
                        .sizes(new Size(8, 50, 10))
                        .timeToLive(3000)
                        .fadeOutEnabled(true)
                        .build()
        );
    }


    private void shakeMagic8Ball() {
        if (!tasksList.isEmpty()) {
            int randomIndex = new Random().nextInt(tasksList.size());
            String randomTask = tasksList.get(randomIndex);
            displayTask.setText(randomTask); // Display the random task in the TextView
            // Other actions based on the random task
        }
    }
}






