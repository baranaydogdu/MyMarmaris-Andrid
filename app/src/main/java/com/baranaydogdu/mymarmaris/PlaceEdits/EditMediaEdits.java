package com.baranaydogdu.mymarmaris.PlaceEdits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.baranaydogdu.mymarmaris.Classes.EventClass;
import com.baranaydogdu.mymarmaris.Classes.MyMediaController;
import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditMediaEdits extends AppCompatActivity {

    Intent intent;
    PlaceCollectionClass maincollection,subcollection;
    PlaceClass place;

    EventClass event;

    AlertDialog.Builder builder;

    Button apply_button,downloadbutton;
    ProgressBar progressBar;

    VideoView videoView;
    ImageView video_image,video_trash;
    MyMediaController mediaController;
    ImageView mapimage, map_trash;
    TextView main_tv;

    RecyclerView top_recyclerview, down_recyclerview;
    EditMedit_TOP_photos_Adaper topAdaper;
    EditMedit_DOWN_photos_Adaper downAdapter;

    Uri videoUri;
    Uri mapUri;
    ArrayList<Uri> topUris,downUris;
    int focusedtype;
    int focusimage_position;

    Boolean isvideochange=false;
    Boolean ismapchange=false;

    MediaClass mediaClass;

    TextView progress_text;
    String progress;

    DatabaseReference mref;
    private StorageReference mStorageRef;

    int from;
    public static final int FROM_PLACE=0;
    public static final int FROM_EVENT=1;

    public static final int TOP_FOCUSED=0;
    public static final int DOWN_FOCUSED=1;
    public static final int MAP_FOCUSED=2;

    Activity activity;
    Boolean ismapdownloaded,isvideodownloaded;
    int donwload_topindex,download_downindex;
    MediaClass downloadMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_media_edits);

        intent = getIntent();
        from = intent.getIntExtra("from", FROM_PLACE);

        builder = new AlertDialog.Builder(this);
        activity=this;

        initviews();

        progress = "";
        mediaController = new MyMediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        topUris = new ArrayList<>();
        downUris = new ArrayList<>();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        if (from == FROM_PLACE) {

            maincollection = (PlaceCollectionClass) intent.getSerializableExtra("MainCollection");
            subcollection = (PlaceCollectionClass) intent.getSerializableExtra("SubCollection");
            place = (PlaceClass) intent.getSerializableExtra("place");

            mref = FirebaseDatabase.getInstance().getReference().child("Places").child(maincollection.getId()).child("subs").child(subcollection.getId()).child("places").child(place.getId());
            mediaClass = new MediaClass(place);
            main_tv.setText("Edit Place Media");
            downloadMedia=new MediaClass(place);


        } else if (from == FROM_EVENT) {

            event = (EventClass) intent.getSerializableExtra("event");
            mref = FirebaseDatabase.getInstance().getReference().child("Activities").child(event.getId());
            mediaClass = new MediaClass(event);
            main_tv.setText("Edit Event Media");
            downloadMedia=new MediaClass(event);

        }


        if (downloadMedia.getMapversion().equals("default"))  ismapdownloaded=true;
        else ismapdownloaded=false;

        if (downloadMedia.getVideoversion().equals("default"))  isvideodownloaded=true;
        else isvideodownloaded=false;

        donwload_topindex   = downloadMedia.getTop_photos().size();
        download_downindex  = downloadMedia.getDownphotos().size();

        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startinupload();
                textedit("Starting upload...");

                upload(0);
            }
        });

        video_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectvideo();
            }
        });

        video_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (videoView.isPlaying()) videoView.stopPlayback();
                videoView.setVisibility(View.INVISIBLE);
                isvideochange = true;
                video_image.setVisibility(View.VISIBLE);
                videoUri = null;
                video_trash.setVisibility(View.INVISIBLE);

            }
        });

        mapimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusedtype = MAP_FOCUSED;
                selectphoto();
            }
        });

        map_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ismapchange = true;
                mapUri = null;
                mapimage.setImageResource(R.drawable.mapimage);
                mapimage.setEnabled(true);
                map_trash.setVisibility(View.INVISIBLE);
            }
        });


        if (mediaClass.getVideoversion().equals("default")) {

            video_image.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
            video_trash.setVisibility(View.INVISIBLE);

        } else {

            video_image.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);
            video_trash.setVisibility(View.VISIBLE);

        }

        if (mediaClass.getMapversion().equals("default")) {

            mapimage.setImageResource(R.drawable.mapimage);
            map_trash.setVisibility(View.INVISIBLE);
            mapimage.setEnabled(true);

        } else {

            PreSets.set_MAP_photo(mapimage,mediaClass.getId());
            map_trash.setVisibility(View.VISIBLE);
            mapimage.setEnabled(false);

        }


        topAdaper = new EditMedit_TOP_photos_Adaper();
        top_recyclerview.setAdapter(topAdaper);
        top_recyclerview.setLayoutManager(new GridLayoutManager(activity,3));

        downAdapter = new EditMedit_DOWN_photos_Adaper();
        down_recyclerview.setAdapter(downAdapter);
        down_recyclerview.setLayoutManager(new GridLayoutManager(activity,3));

        downloadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadcontent();
            }
        });
    }

    private void initviews() {

        apply_button=findViewById(R.id.apply_button);
        downloadbutton=findViewById(R.id.downloadbutton);
        videoView=findViewById(R.id.videoView);
        video_image=findViewById(R.id.image_videoview);
        video_trash=findViewById(R.id.garbage_video);

        mapimage=findViewById(R.id.image_map);
        map_trash =findViewById(R.id.garbage_map);

        top_recyclerview=findViewById(R.id.photo_TOP_recyclerview);
        down_recyclerview=findViewById(R.id.photo_DOWN_recyclerview);

        progress_text=findViewById(R.id.progress_tv);
        progressBar=findViewById(R.id.progressBar2);
        main_tv=findViewById(R.id.addorplace_textvieww);

    }

    private void downloadcontent() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},10);

        } else {

            startinupload();

            if (!isvideodownloaded){

                textedit("Downloading Video...");

                File file =new File(Environment.getExternalStorageDirectory() + "/mymarmaris"+Calendar.getInstance().getTime()+"video.mp4");
                StorageReference downloadstorageReference=FirebaseStorage.getInstance().getReference().child(PreSets.firebase_videoname(downloadMedia.getId()));
                downloadstorageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        textedit("Completed");
                        isvideodownloaded=true;
                        downloadcontent();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setfailuremessage(e);
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                        yuzdegoster(taskSnapshot);
                    }
                });

            } else if(!ismapdownloaded){

                textedit("Downloading Map Photo...");
                File file =new File(Environment.getExternalStorageDirectory() + "/mymarmaris"+Calendar.getInstance().getTime()+"map.jpg");
                StorageReference downloadstorageReference=FirebaseStorage.getInstance().getReference().child(PreSets.firebase_map_name(downloadMedia.getId()));

                downloadstorageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        textedit("Completed");
                        ismapdownloaded=true;
                        downloadcontent();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setfailuremessage(e);
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                        yuzdegoster(taskSnapshot);
                    }
                });


            } else if (donwload_topindex!=0){

                donwload_topindex--;
                textedit("Downloading Top Photo:"+donwload_topindex);
                File file =new File(Environment.getExternalStorageDirectory() + "/mymarmaris"+Calendar.getInstance().getTime()+"top"+donwload_topindex+".jpg");
                StorageReference downloadstorageReference=FirebaseStorage.getInstance().getReference().child(PreSets.firebase_TOP_photoname(downloadMedia.getId(),donwload_topindex));

                downloadstorageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        textedit("Completed");
                        downloadcontent();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setfailuremessage(e);
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                        yuzdegoster(taskSnapshot);
                    }
                });



            } else if (download_downindex!=0){

                download_downindex--;
                textedit("Downloading Down Photo:"+download_downindex);
                File file =new File(Environment.getExternalStorageDirectory() + "/mymarmaris"+Calendar.getInstance().getTime()+"down"+download_downindex+".jpg");
                StorageReference downloadstorageReference=FirebaseStorage.getInstance().getReference().child(PreSets.firebase_DOWN_photoname(downloadMedia.getId(),download_downindex));

                downloadstorageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        textedit("Completed");
                        downloadcontent();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setfailuremessage(e);
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                        yuzdegoster(taskSnapshot);
                    }
                });

            } else {

                finishedupload();
                textedit("All media files are downloaded succesfuly.");
                builder.setIcon(R.drawable.checkicon);
                builder.setTitle("SUCCESS");
                builder.setMessage("Download success");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(null,null);
                builder.setCancelable(false).show();



            }


        }




    }


    private void selectvideo() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }

    }

    private void selectphoto() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3);
        } else {

            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,4);
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);

            }

        } else if (requestCode==3) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 4);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==2 && resultCode==RESULT_OK && data!=null){   //VIDEO ISLERI

            video_trash.setVisibility(View.VISIBLE);
            video_image.setVisibility(View.INVISIBLE);
            videoUri=data.getData();
            videoView.setVideoPath(String.valueOf(videoUri));
            videoView.setVisibility(View.VISIBLE);
            videoView.start();
            isvideochange=true;

        }else if (requestCode==4 && resultCode==RESULT_OK && data!=null) {

            Uri getUri=data.getData();
            Uri newUri=null;

            if (focusedtype==MAP_FOCUSED){

                newUri=Uri.fromFile(PreSets.get_MAP_photo_File(mediaClass.getId()));
                UCrop.of(getUri, newUri).withAspectRatio(15,8)
                        .start(this);


            } else if (focusedtype==TOP_FOCUSED){
                newUri=Uri.fromFile(PreSets.get_TOP_photo_File(mediaClass.getId(),focusimage_position));

                if (from==FROM_PLACE){
                    UCrop.of(getUri, newUri).withAspectRatio(16,9)
                            .start(this);

                } else {UCrop.of(getUri, newUri).withAspectRatio(16,22)
                        .start(this);

                }


            } else if (focusedtype==DOWN_FOCUSED){
                newUri=Uri.fromFile(PreSets.get_DOWN_photo_File(mediaClass.getId(),focusimage_position));
                UCrop.of(getUri, newUri).start(this);

            }

        }else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data!=null) {

            if (focusedtype==MAP_FOCUSED){
                ismapchange=true;
                map_trash.setVisibility(View.VISIBLE);
                mapUri=Uri.fromFile(PreSets.get_MAP_photo_File(mediaClass.getId()));
                mapimage.setImageResource(R.drawable.mapimage);
                mapimage.setImageURI(mapUri);
                mapimage.setEnabled(false);

            } else if (focusedtype==TOP_FOCUSED){

                if (mediaClass.getTop_photos().size()==focusimage_position){    //NEW ADDED

                    mediaClass.addnewtopfoto(focusimage_position);

                }else {  //CHANGE

                   mediaClass.update_TOP_photo(focusimage_position);

                }

                topAdaper.notifyDataSetChanged();

            } else if (focusedtype==DOWN_FOCUSED){

                if (mediaClass.getDownphotos().size()==focusimage_position){    //NEW ADDED

                    mediaClass.addnew_DOWN_foto(focusimage_position);

                }else {  //CHANGE

                    mediaClass.update_DOWN_photo(focusimage_position);
                }

                downAdapter.notifyDataSetChanged();

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void upload(final int i){

        if (isvideochange){     //VIDEO DEGISTI ISE

            if (videoUri==null){  //VIDEO SILINDI ISE

                textedit("Deleting video...");
                mStorageRef.child(PreSets.firebase_videoname(mediaClass.getId())).delete();
                mediaClass.setVideoversion("default");
                isvideochange=false;
                if (PreSets.getvideo_File(mediaClass.getId()).exists())
                    PreSets.getvideo_File(mediaClass.getId());
                upload(i);

            } else {    //VIDEO EKLENDI ISE

                textedit("Uploading video...");
                mStorageRef.child(PreSets.firebase_videoname(mediaClass.getId())).putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        mediaClass.setVideoversion(mref.push().getKey());

                        textedit("Completed");
                        isvideochange = false;
                        upload(i);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        setfailuremessage(e);

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        yuzdegoster(taskSnapshot);
                    }
                });

            }


        } else if (ismapchange) {

            if (mapUri == null) {  //MAP FOTO SILINDI ISE

                textedit("Deleting Map photo...");
                mStorageRef.child(PreSets.firebase_map_name(mediaClass.getId())).delete();
                mediaClass.setMapversion("default");
                ismapchange = false;
                if (PreSets.get_MAP_photo_File(mediaClass.getId()).exists())
                    PreSets.get_MAP_photo_File(mediaClass.getId());
                upload(i);

            } else {    //MAP FOTO EKLENDI ISE

                textedit("Uploading Map photo...");
                mStorageRef.child(PreSets.firebase_map_name(mediaClass.getId())).putFile(mapUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        mediaClass.setMapversion(mref.push().getKey());

                        textedit("Completed");
                        ismapchange = false;
                        upload(i);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        setfailuremessage(e);

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                        double progress = (double) 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                        yuzdegoster(taskSnapshot);

                    }
                });

            }


        } else {       //FOTO ISLERI

            if (i<15){      //TOP PHOTOS

                if (i<mediaClass.getTop_photos().size()){

                    if (mediaClass.getIs_TOP_changed().get(i)){     //FOTO DEGISTI ISE

                        textedit("Uploading Top Photo"+i);
                        mStorageRef.child(PreSets.firebase_TOP_photoname(mediaClass.getId(),i)).
                                putFile(Uri.fromFile(PreSets.get_TOP_photo_File(mediaClass.getId(),i)))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        mediaClass.getTop_photos().set(i,mref.push().getKey());

                                        textedit("Completed");
                                        isvideochange = false;
                                        upload(i+1);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                setfailuremessage(e);
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                                double progress = (double) 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                                yuzdegoster(taskSnapshot);

                            }
                        });






                    } else {        //TOP FOTO DA DEGISIKLIK YOK ISE

                        upload(i+1);

                    }




                } else {

                    mStorageRef.child(PreSets.firebase_TOP_photoname(mediaClass.getId(),i)).delete();

                    if (PreSets.get_TOP_photo_File(mediaClass.getId(),i).exists())
                        PreSets.get_TOP_photo_File(mediaClass.getId(),i).delete();
                    upload(i+1);
                }




            } else if (i<30){   //DOWN PHOTOS


                if (i-15<mediaClass.getDownphotos().size()){

                    if (mediaClass.getIs_DOWN_changed().get(i-15)){     //FOTO DEGISTI ISE

                        textedit("Uploading Down Photo"+(i-15));
                        mStorageRef.child(PreSets.firebase_DOWN_photoname(mediaClass.getId(),i-15)).
                                putFile(Uri.fromFile(PreSets.get_DOWN_photo_File(mediaClass.getId(),i-15)))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        mediaClass.getDownphotos().set(i-15,mref.push().getKey());

                                        textedit("Completed");
                                        isvideochange = false;
                                        upload(i+1);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                setfailuremessage(e);
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                                double progress = (double) 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                                yuzdegoster(taskSnapshot);

                            }
                        });






                    } else {        //TOP FOTO DA DEGISIKLIK YOK ISE

                        upload(i+1);
                    }




                }else {

                    mStorageRef.child(PreSets.firebase_DOWN_photoname(mediaClass.getId(),i-15)).delete();
                    if (PreSets.get_DOWN_photo_File(mediaClass.getId(),i-15).exists())
                        PreSets.get_DOWN_photo_File(mediaClass.getId(),i-15).delete();
                    upload(i+1);


                }

            } else {

                textedit("Deleting caches");

                if (PreSets.get_MAP_photo_File(mediaClass.getId()).exists() && mediaClass.getMapversion().equals("default")){
                    PreSets.get_MAP_photo_File(mediaClass.getId()).delete();
                }
                if (PreSets.getvideo_File(mediaClass.getId()).exists() && mediaClass.getVideoversion().equals("default")){
                    PreSets.getvideo_File(mediaClass.getId()).delete();
                }

                for (int k=0;i<15;k++){

                    if (PreSets.get_TOP_photo_File(mediaClass.getId(),k).exists() && k>mediaClass.getTop_photos().size()-1){
                        PreSets.get_TOP_photo_File(mediaClass.getId(),k).delete();
                    }

                    if (PreSets.get_DOWN_photo_File(mediaClass.getId(),k).exists() && k>mediaClass.getDownphotos().size()-1){
                        PreSets.get_DOWN_photo_File(mediaClass.getId(),k).delete();
                    }


                }

                if (from==FROM_PLACE) {
                    place.setMapphotoversion(mediaClass.mapversion);
                    place.setVideoversion(mediaClass.videoversion);
                    place.setTopphotos(mediaClass.getTop_photos());
                    place.setDownphotos(mediaClass.getDownphotos());

                    mref.setValue(place).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            textedit("Upload completed successfully.");

                            finishedupload();
                            builder.setIcon(R.drawable.checkicon);
                            builder.setTitle("SUCCESS");
                            builder.setMessage("Upload success");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.setNegativeButton(null,null);
                            builder.setCancelable(false).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            setfailuremessage(e);
                        }
                    });

                } else {

                    event.setMapphotoversion(mediaClass.mapversion);
                    event.setVideoversion(mediaClass.videoversion);
                    event.setTopphotos(mediaClass.getTop_photos());
                    event.setDownphotos(mediaClass.getDownphotos());

                    mref.setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            textedit("Upload completed successfully.");

                            finishedupload();
                            builder.setIcon(R.drawable.checkicon);
                            builder.setTitle("SUCCESS");
                            builder.setMessage("Upload success");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.setNegativeButton(null,null);
                            builder.setCancelable(false).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            setfailuremessage(e);
                        }
                    });

                }


            }
        }
    }

    public void startinupload(){

        progressBar.setVisibility(View.VISIBLE);
        if (videoView.isPlaying()) videoView.stopPlayback();
        videoView.setEnabled(false);

        apply_button.setEnabled(false);
        video_image.setEnabled(false);
        mapimage.setEnabled(false);
        map_trash.setEnabled(false);

        down_recyclerview.setEnabled(false);
        top_recyclerview.setEnabled(false);



    }

    public void finishedupload(){

        progressBar.setVisibility(View.INVISIBLE);
        videoView.setEnabled(true);

        apply_button.setEnabled(true);
        video_image.setEnabled(true);
        mapimage.setEnabled(true);
        map_trash.setEnabled(true);

        down_recyclerview.setEnabled(true);
        top_recyclerview.setEnabled(true);


    }

    public void textedit(String add){

        progress=progress+"\n"+add;
        progress_text.setText(progress);

    }

    public void yuzdegoster(FileDownloadTask.TaskSnapshot taskSnapshot){

        String yuzdetext = ""+(int)((double) 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

        String bytetext=""+new DecimalFormat("##.##").format((double) taskSnapshot.getBytesTransferred() / (1024 * 1024)) + " Mb/ " +
                new DecimalFormat("##.##").format((double) taskSnapshot.getTotalByteCount() / (1024 * 1024)) + " Mb";

        String yuzde=bytetext;
        progress_text.setText(progress+"  "+yuzde);

    }


    public void yuzdegoster(UploadTask.TaskSnapshot taskSnapshot){

        String yuzdetext = ""+(int)((double) 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

        String bytetext=""+new DecimalFormat("##.##").format((double) taskSnapshot.getBytesTransferred() / (1024 * 1024)) + " Mb/ " +
                new DecimalFormat("##.##").format((double) taskSnapshot.getTotalByteCount() / (1024 * 1024)) + " Mb";

        String yuzde=bytetext;
        progress_text.setText(progress+"  "+yuzde);

    }

    public void setfailuremessage(Exception e){

        finishedupload();
        builder.setIcon(R.drawable.error_icon);
        builder.setTitle("ERROR");
        builder.setMessage("Error : " +e.getLocalizedMessage());
        builder.setPositiveButton("OK",null);
        builder.setNegativeButton(null,null);
        builder.setCancelable(true).show();

    }

    public class EditMedit_TOP_photos_Adaper extends RecyclerView.Adapter<EditMedit_TOP_photos_Adaper.EditMedit_TOP_photos_ViewHolder>{

        public EditMedit_TOP_photos_Adaper() {

        }

        @Override
        public EditMedit_TOP_photos_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (from==FROM_PLACE) view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_edit_top_photos_custum_item,
                    parent, false);
            else view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_edit_top_photos_event_custum_item,
                    parent, false);
            return new EditMedit_TOP_photos_ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EditMedit_TOP_photos_ViewHolder holder, final int position) {

            if (position<mediaClass.getTop_photos().size()){

                PreSets.set_TOP_photo(holder.image,mediaClass.getId(),position);

            } else {

                holder.image.setImageResource(R.drawable.photo);

            }

            if (mediaClass.getTop_photos().size()>0){

                if (position==mediaClass.getTop_photos().size()-1) holder.trashicon.setVisibility(View.VISIBLE);
                else holder.trashicon.setVisibility(View.INVISIBLE);

            }  else holder.trashicon.setVisibility(View.INVISIBLE);


            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    focusedtype=TOP_FOCUSED;
                    focusimage_position=position;
                    selectphoto();
                }
            });

            holder.trashicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mediaClass.delete_TOP_photo(position);
                    topAdaper.notifyDataSetChanged();

                }
            });






        }


        @Override
        public int getItemCount() {
            if (mediaClass.getTop_photos().size()==15) return 15;
            else return mediaClass.getTop_photos().size()+1;
        }

        public class EditMedit_TOP_photos_ViewHolder extends RecyclerView.ViewHolder {

            ImageView image,trashicon;

            public EditMedit_TOP_photos_ViewHolder(View itemView) {
                super(itemView);

                image    = itemView.findViewById(R.id.place_custum_iconsource);
                trashicon= itemView.findViewById(R.id.trash_icon);

            }
        }
    }

    public class EditMedit_DOWN_photos_Adaper extends RecyclerView.Adapter<EditMedit_DOWN_photos_Adaper.EditMedit_DOWN_photos_ViewHolder>{

        public EditMedit_DOWN_photos_Adaper() {

        }

        @Override
        public EditMedit_DOWN_photos_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_edit_down_photos_custum_item,
                    parent, false);
            return new EditMedit_DOWN_photos_ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EditMedit_DOWN_photos_ViewHolder holder, final int position) {

            if (position<mediaClass.getDownphotos().size()){

                PreSets.set_DOWN_photo(holder.image,mediaClass.getId(),position);

            } else {

                holder.image.setImageResource(R.drawable.photo);

            }

            if (mediaClass.getDownphotos().size()>0){

                if (position==mediaClass.getDownphotos().size()-1) holder.trashicon.setVisibility(View.VISIBLE);
                else holder.trashicon.setVisibility(View.INVISIBLE);

            }  else holder.trashicon.setVisibility(View.INVISIBLE);


            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    focusedtype=DOWN_FOCUSED;
                    focusimage_position=position;
                    selectphoto();
                }
            });

            holder.trashicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mediaClass.delete_DOWN_photo(position);
                    downAdapter.notifyDataSetChanged();

                }
            });

        }


        @Override
        public int getItemCount() {

            if (mediaClass.getDownphotos().size()==15) return 15;
            else return mediaClass.getDownphotos().size()+1;
        }

        public class EditMedit_DOWN_photos_ViewHolder extends RecyclerView.ViewHolder {

            ImageView image,trashicon;


            public EditMedit_DOWN_photos_ViewHolder(View itemView) {
                super(itemView);

                image    = itemView.findViewById(R.id.place_custum_iconsource);
                trashicon= itemView.findViewById(R.id.trash_icon);





            }
        }
    }

    class MediaClass {

        ArrayList<String> top_photos, downphotos;

        String id;
        String videoversion;
        String mapversion;
        ArrayList<Boolean> is_TOP_changed, is_DOWN_changed;



        public MediaClass(PlaceClass place) {

            is_TOP_changed=new ArrayList<>();
            is_DOWN_changed=new ArrayList<>();

            id = place.getId();
            top_photos = place.getTopphotos();
            downphotos = place.getDownphotos();
            mapversion = place.getMapphotoversion();
            videoversion = place.getVideoversion();

            if (top_photos == null) top_photos = new ArrayList<>();
            if (downphotos == null) downphotos = new ArrayList<>();

            for (int i = 0; i < top_photos.size(); i++) is_TOP_changed.add(false);
            for (int i = 0; i < downphotos.size(); i++) is_DOWN_changed.add(false);

        }

        public MediaClass(EventClass place) {

            is_TOP_changed=new ArrayList<>();
            is_DOWN_changed=new ArrayList<>();

            id = place.getId();
            top_photos = place.getTopphotos();
            downphotos = place.getDownphotos();
            mapversion = place.getMapphotoversion();
            videoversion = place.getVideoversion();

            if (top_photos == null) top_photos = new ArrayList<>();
            if (downphotos == null) downphotos = new ArrayList<>();

            for (int i = 0; i < top_photos.size(); i++) is_TOP_changed.add(false);
            for (int i = 0; i < downphotos.size(); i++) is_DOWN_changed.add(false);

        }


        public void addnewtopfoto(int position){

            top_photos.add(mref.push().getKey());
            is_TOP_changed.add(true);

        }

        public void addnew_DOWN_foto(int position){

            downphotos.add(mref.push().getKey());
            is_DOWN_changed.add(true);

        }

        public void delete_TOP_photo(int position){

            top_photos.remove(position);
            is_TOP_changed.remove(position);

        }

        public void delete_DOWN_photo(int position){

            downphotos.remove(position);
            is_DOWN_changed.remove(position);

        }

        public void update_TOP_photo(int position){

            top_photos.set(position,mref.push().getKey());
            is_TOP_changed.set(position,true);


        }

        public void update_DOWN_photo(int position){

            downphotos.set(position,mref.push().getKey());
            is_DOWN_changed.set(position,true);

        }





        public ArrayList<String> getTop_photos() {
            return top_photos;
        }

        public void setTop_photos(ArrayList<String> top_photos) {
            this.top_photos = top_photos;
        }

        public ArrayList<String> getDownphotos() {
            return downphotos;
        }

        public void setDownphotos(ArrayList<String> downphotos) {
            this.downphotos = downphotos;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVideoversion() {
            return videoversion;
        }

        public void setVideoversion(String videoversion) {
            this.videoversion = videoversion;
        }

        public String getMapversion() {
            return mapversion;
        }

        public void setMapversion(String mapversion) {
            this.mapversion = mapversion;
        }

        public ArrayList<Boolean> getIs_TOP_changed() {
            return is_TOP_changed;
        }

        public void setIs_TOP_changed(ArrayList<Boolean> is_TOP_changed) {
            this.is_TOP_changed = is_TOP_changed;
        }

        public ArrayList<Boolean> getIs_DOWN_changed() {
            return is_DOWN_changed;
        }

        public void setIs_DOWN_changed(ArrayList<Boolean> is_DOWN_changed) {
            this.is_DOWN_changed = is_DOWN_changed;
        }
    }

}
