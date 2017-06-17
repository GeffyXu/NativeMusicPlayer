package com.example.nativemusicplayer;

/**
 * Created by 许国辉 on 2017/6/2.
 */
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int flag = 0;
    int ini = 0;
    int i = 0;
    private int firstVisiblePosition;
    private int firstVisiblePositionTop;
    private MediaPlayer mediaPlayer;
    private ListView mListView;
    private int lastposition;
    private int positionNow;
    private String lasturl ="";
    private Uri lasturi;
    private Button mFind;
    private FindSongs finder;
    private ArrayList<Mp3Info> mp3Infos;
    public  MyListViewAdapter adapter;;
    private ImageButton imageButton;
    private ImageButton imageButtonPre;
    private ImageButton imageButtonNext;
    private TextView nameView;
    private TextView artistView;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

        imageButton = (ImageButton)findViewById(R.id.imageButton);
        imageButtonPre = (ImageButton)findViewById(R.id.imageButtonPrevious);
        imageButtonNext = (ImageButton)findViewById(R.id.imageButtonNext);
        nameView = (TextView) findViewById(R.id.music_name);
        artistView = (TextView) findViewById(R.id.music_singer);
        mListView = (ListView) findViewById(R.id.listview);
        //mFind = ((Button) findViewById(R.id.find));
        finder = new FindSongs();

       // mFind.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {
                mp3Infos = finder.getMp3Infos(MainActivity.this);
                Log.d("MainActivity",mp3Infos.toString());
                adapter = new MyListViewAdapter(getApplicationContext(), mp3Infos);
                finder.setListAdpter(getApplicationContext(), mp3Infos, mListView);

              //  mFind.setVisibility(View.GONE);
           // }
       // });




        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               Mp3Info getObject = mp3Infos.get(position);   //通过position获取所点击的对象
               String infoTitle = getObject.getTitle();    //获取信息标题
               String artist = getObject.getArtist();
               String url = getObject.getUrl();   //地址信息
               positionNow = position;
               i++;
               System.out.println("只是第几次"+i);

               if(getObject.getAvatar() == R.drawable.on) {
                   getObject.setAvatar(R.drawable.on);
               }else{
                   getObject.setAvatar(R.drawable.on);
                   if(ini == 1) {
                       Mp3Info getObject1 = mp3Infos.get(lastposition);   //通过position获取所点击的对象
                       getObject1.setAvatar(R.drawable.off);
                       lastposition = position;
                   }
                   lastposition = position;
                   firstVisiblePosition = mListView.getFirstVisiblePosition();
                   View item = mListView.getChildAt(0);
                   firstVisiblePositionTop = (item == null) ? 0 : item.getTop();

                   finder.setListAdpter(getApplicationContext(), mp3Infos, mListView);
                   // 恢复现在listView的位置，(上一次保存的位置)
                   mListView.setSelectionFromTop(firstVisiblePosition, firstVisiblePositionTop);
                   nameView.setText(infoTitle);
                   artistView.setText(artist);
                   ini =1;

                   if(lasturl!=("")) {
                       mediaPlayer.stop();
                       mediaPlayer.reset();
                   }
                   Uri uri = Uri.parse(url);
                   lasturl = url;
                   mediaPlayer = MediaPlayer.create(MainActivity.this,uri);
                   mediaPlayer.start();
                   //mediaPlayer.setDataSource(MainActivity.this, uri);
                   //mediaPlayer.setDataSource(this, Uri.parse(url));
                   //mediaPlayer.setDataSource(getApplicationContext(), uri)
                   imageButton.setBackground(getResources().getDrawable(R.drawable.on_1));;
                   flag = 1;

                   imageButton.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           if(flag == 1) {
                               mediaPlayer.pause();
                               imageButton.setBackground(getResources().getDrawable(R.drawable.off_1));
                               flag = 0;
                           }else{
                               mediaPlayer.start();
                               imageButton.setBackground(getResources().getDrawable(R.drawable.on_1));
                               flag = 1;
                           }
                       }});

                   if(ini ==1) {
                       imageButtonNext.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               positionNow++;
                               Mp3Info getObjectNext = mp3Infos.get(positionNow);   //通过position获取所点击的对象
                               String infoTitleNext = getObjectNext.getTitle();    //获取信息标题
                               String artistNext = getObjectNext.getArtist();
                               String urlNext = getObjectNext.getUrl();   //地址信息
                               lasturl = urlNext;
                               lastposition = positionNow;
                               Uri uriNow = Uri.parse(urlNext);
                               mediaPlayer.reset();
                               mediaPlayer = MediaPlayer.create(MainActivity.this, uriNow);
                               mediaPlayer.start();
                               nameView.setText(infoTitleNext);
                               artistView.setText(artistNext);
                               Mp3Info getObjectlast = mp3Infos.get(positionNow - 1);
                               getObjectlast.setAvatar(R.drawable.off);
                               getObjectNext.setAvatar(R.drawable.on);
                               finder.setListAdpter(getApplicationContext(), mp3Infos, mListView);
                               // 恢复现在listView的位置，(上一次保存的位置)

//                               firstVisiblePosition = mListView.getFirstVisiblePosition();
                               if(positionNow>3) {
                                   View item = mListView.getChildAt(0);
                                   firstVisiblePositionTop = (item == null) ? 0 : item.getTop();
                                   mListView.setSelectionFromTop(positionNow-2, firstVisiblePositionTop);
                               }else {
                                   mListView.setSelection(positionNow);
                               }

                           }
                       });


                       imageButtonPre.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               positionNow--;
                               Mp3Info getObjectNext = mp3Infos.get(positionNow);   //通过position获取所点击的对象
                               String infoTitleNext = getObjectNext.getTitle();    //获取信息标题
                               String artistNext = getObjectNext.getArtist();
                               String urlNext = getObjectNext.getUrl();   //地址信息
                               lasturl = urlNext;
                               lastposition = positionNow;
                               Uri uriNow = Uri.parse(urlNext);
                               mediaPlayer.reset();
                               mediaPlayer = MediaPlayer.create(MainActivity.this, uriNow);
                               mediaPlayer.start();
                               nameView.setText(infoTitleNext);
                               artistView.setText(artistNext);
                               Mp3Info getObjectlast = mp3Infos.get(positionNow + 1);
                               getObjectlast.setAvatar(R.drawable.off);
                               getObjectNext.setAvatar(R.drawable.on);
                               finder.setListAdpter(getApplicationContext(), mp3Infos, mListView);
                               // 恢复现在listView的位置，(上一次保存的位置)
//                               firstVisiblePosition = mListView.getFirstVisiblePosition();
                               if(positionNow>3) {
                                   View item = mListView.getChildAt(0);
                                   firstVisiblePositionTop = (item == null) ? 0 : item.getTop();
                                   mListView.setSelectionFromTop(positionNow-2, firstVisiblePositionTop);
                               }else {
                                   mListView.setSelection(positionNow);
                               }

                           }
                       });
                   }
                       //进行播放完自动播放下一曲
                       mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                           @Override
                           public void onCompletion(MediaPlayer mp) {
                            //   i++;
                               //  System.out.println("只是第几次"+i);
                               positionNow++;
                               Mp3Info getObjectNext = mp3Infos.get(positionNow);   //通过position获取所点击的对象
                               String infoTitleNext = getObjectNext.getTitle();    //获取信息标题
                               String artistNext = getObjectNext.getArtist();
                               String urlNext = getObjectNext.getUrl();   //地址信息
                               lasturl = urlNext;
                               lastposition = positionNow;
                               Uri uriNow = Uri.parse(urlNext);
                               mediaPlayer.reset();
                               mediaPlayer = MediaPlayer.create(MainActivity.this, uriNow);
                               mediaPlayer.start();
                               nameView.setText(infoTitleNext);
                               artistView.setText(artistNext);
                               Mp3Info getObjectlast = mp3Infos.get(positionNow - 1);
                               getObjectlast.setAvatar(R.drawable.off);
                               getObjectNext.setAvatar(R.drawable.on);
                               finder.setListAdpter(getApplicationContext(), mp3Infos, mListView);
                               // 恢复现在listView的位置，(上一次保存的位置)
                               mListView.setSelectionFromTop(firstVisiblePosition, firstVisiblePositionTop);

                           }
                       });
               }
           }
        });
    }
}
