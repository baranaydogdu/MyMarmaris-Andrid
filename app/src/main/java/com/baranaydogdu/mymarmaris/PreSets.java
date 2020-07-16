package com.baranaydogdu.mymarmaris;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.widget.ImageView;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class PreSets {

    public PreSets() {

    }

    public void setImage(Context context, ImageView imageView, String url) {

        if (url == "") {
            imageView.setImageResource(R.drawable.app_logo);

        } else {

            final String filename = "/data/user/0/com.baranaydogdu.mymarmaris/cache/" + url
                    .replaceAll(":", "")
                    .replaceAll("/", "") + ".png";

            final File file = new File(filename);

            if (file.exists()) {
                imageView.setImageURI(Uri.fromFile(file));

            } else {

                imageView.setImageResource(R.drawable.app_logo);
                new DownloadAsynTask(imageView, url, file).execute();

            }

        }

    }

    public class DownloadAsynTask extends AsyncTask<String, Void, String> {

        Boolean taskComp = false;
        String url = "";
        File file;
        ImageView imageView;

        public DownloadAsynTask(ImageView imageView, String url, File file) {
            this.url = url;
            this.file = file;
            this.imageView = imageView;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (taskComp) {
                imageView.setImageURI(Uri.fromFile(file));
                System.out.println("ICON DOWNLOADED");
            } else {

                try {
                    if (file.exists()) file.delete();

                } catch (Exception ex) {
                    System.out.println("File Error : " + ex.getLocalizedMessage());
                }

            }

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL u = new URL(url);
                InputStream is = u.openStream();

                DataInputStream dis = new DataInputStream(is);

                byte[] buffer = new byte[1024];
                int length;

                FileOutputStream fos = new FileOutputStream(file);
                while ((length = dis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                taskComp = true;

            } catch (Exception ex) {
                taskComp = false;
                System.out.println("Download Error : " + ex.getLocalizedMessage() + "url : "+url);
                try {
                    if (file.exists()) file.delete();

                } catch (Exception ex1) {
                    System.out.println("File Error : " + ex1.getLocalizedMessage());
                }
            }

            return "";
        }
    }

    public static int getDp(Context context,int pixel) {
        return (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, pixel, context.getResources().getDisplayMetrics());
    }
}
