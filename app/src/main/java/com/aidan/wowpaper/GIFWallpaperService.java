package com.aidan.wowpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.Handler;
import 	android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by aidan on 11/10/2017.
 */

public class GIFWallpaperService extends WallpaperService {

    private class GIFWallpaperEngine extends WallpaperService.Engine {
        private final int frameDuration = 20;

        private SurfaceHolder holder;
        private Movie movie;
        private boolean visible;
        private Handler handler;

        public GIFWallpaperEngine(Movie movie) {
            this.movie = movie;
            handler = new Handler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
        }
        private void draw() {
            if (visible) {
                Canvas canvas = holder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                canvas.save();
                // Adjust size and position so that
                // the image looks good on your screen
                canvas.scale(5, 5);
                //movie.draw(canvas, -100, 0);
                movie.draw(canvas, 112, 210);
//                Canvas bg = new Canvas();
//                bg.drawARGB(0, 225, 225, 255);
//                movie.draw(bg,movie.height(), movie.width());
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                movie.setTime((int) (System.currentTimeMillis() % movie.duration()));

                handler.removeCallbacks(drawGIF);
                handler.postDelayed(drawGIF, frameDuration);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawGIF);
            } else {
                handler.removeCallbacks(drawGIF);
            }
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawGIF);
        }


        private Runnable drawGIF = new Runnable() {
            public void run() {
                draw();
            }
        };
    }

    @Override
    public WallpaperService.Engine onCreateEngine() {
        try {
            Movie movie = Movie.decodeStream(
                    getResources().getAssets().open("wow.gif"));

            return new GIFWallpaperEngine(movie);
        }catch(IOException e){
            Log.d("GIF", "Could not load asset");
            return null;
        }
    }



}
