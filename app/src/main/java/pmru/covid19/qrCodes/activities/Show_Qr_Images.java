package pmru.covid19.qrCodes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import pmru.covid19.R;
import pmru.covid19.qrCodes.Adapters.SlidingImage_Adapter;

public class Show_Qr_Images extends AppCompatActivity {

    ArrayList<Integer> sortedArrayList=new ArrayList<>();
    ArrayList<String> boundsArrayList=new ArrayList<>();
    int differece;


    private ArrayList<String> imagesPathArrayList=new ArrayList<>();
    private SlidingImage_Adapter adapter;
    ViewPager pager;
    ImageView nextqr,previousqr;
    private File path;
    private int[] namesArr;
    private Object[] myarray;
    int startBound=0;
    private int last;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__qr__images);
        getSupportActionBar().hide();

        pager=findViewById(R.id.viewpagger);
        nextqr=findViewById(R.id.nextqr);
        previousqr=findViewById(R.id.previousqr);

        String folder=getIntent().getStringExtra("folder");
        String[] filenames = new String[0];
         path = new File(Environment.getExternalStorageDirectory() + "/QRCode/"+folder);
        if (path.exists()) {
            filenames = path.list();
        }
        for (int i = 0; i < filenames.length; i++) {
            imagesPathArrayList.add(path.getPath() + "/" + filenames[i]);
            String requiredString = filenames[i].substring(filenames[i].indexOf("-") + 1, filenames[i].indexOf("."));

            sortedArrayList.add(Integer.valueOf(requiredString));



//            Log.e("requiredString", requiredString);



            adapter = new SlidingImage_Adapter(this, imagesPathArrayList);
            pager.setAdapter(adapter);

        }
        Collections.sort(sortedArrayList);

        for (int i=0;i<sortedArrayList.size();i++){
            int number=sortedArrayList.get(i);

             myarray=sortedArrayList.toArray();


         //   Log.d("length of array",""+sortedArrayList.size());

        }
        for (int j=0;j<myarray.length;j++){
            try{
                Log.d("my array","actual number"+myarray[j]+"next number"+myarray[j+1]+  " previous "+myarray[j-1]);
                 int new_next=Integer.parseInt(""+ myarray[j+1]);
                 int pre_value = Integer.parseInt(""+ myarray[j]);

                 differece = new_next-pre_value;

//5,6,7,8,9,10  12,13,14,15

                 if (differece>=2){
                   //  Toast.makeText(this, "differece"+differece, Toast.LENGTH_SHORT).show();
                     boundsArrayList.add(myarray[startBound]+" "+myarray[j]);
                     startBound=j+1;
                     Log.d("differece block",""+differece);





                 }

                 last=(int)myarray[j+1];



                // Log.d("differce ",""+differece);

            }catch (Exception e)
            {
                e.printStackTrace();
            }


        }
        boundsArrayList.add(myarray[startBound]+" "+last);
        for (int i=0;i<boundsArrayList.size();i++){
            Log.d("bound here",boundsArrayList.get(i)+boundsArrayList.size());
        }

        nextqr.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pager.setCurrentItem(pager.getCurrentItem()+1,true);
        }
    });
        previousqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(pager.getCurrentItem()-1,true);
            }
        });

    }

    /* Path should be the complete path of the folder. */
    public void openDirectoryIntent(String path)
    {
        Uri selectedUri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");
        startActivity(intent);
    }

    public void openfolder(View view) {
        openDirectoryIntent(path.getPath());
    }

    public void bubbleSort(int[] array) {
        boolean swapped = true;
        int j = 0;
        int tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < array.length - j; i++) {
                if (array[i] > array[i + 1]) {
                    tmp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = tmp;
                    swapped = true;
                }
            }
        }
    }
}