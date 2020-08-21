
 package pmru.covid19.qrCodes.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import pmru.covid19.R;
import pmru.covid19.qrCodes.Adapters.CustomAdapter;
import pmru.covid19.qrCodes.Adapters.SequenceAdapter;
import pmru.covid19.qrCodes.Adapters.SlidingImage_Adapter;
import pmru.covid19.qrCodes.Model.DataModel;

 public class CheckSequence extends AppCompatActivity {

     ArrayList<Integer> sortedArrayList=new ArrayList<>();
     ArrayList<String> boundsArrayList=new ArrayList<>();
     int differece;
     private ArrayList<String> imagesPathArrayList=new ArrayList<>();
     private File path;
     private Object[] myarray;
     int startBound=0;
     private int last;


     private static SequenceAdapter adapter;
     private static RecyclerView recyclerView;
     private static ArrayList<DataModel> data;
     private LinearLayout layoutManager;
     TextView distName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_sequence);
        getSupportActionBar().hide();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        distName =  findViewById(R.id.title);
        recyclerView.setHasFixedSize(true);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new SequenceAdapter(this,boundsArrayList);
        recyclerView.setAdapter(adapter);


        String folder=getIntent().getStringExtra("folder");
        distName.setText("Used Qr Code For "+folder);
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




        }
        Collections.sort(sortedArrayList);

        for (int i=0;i<sortedArrayList.size();i++){
            int number=sortedArrayList.get(i);

            myarray=sortedArrayList.toArray();


            //   Log.d("length of array",""+sortedArrayList.size());

        }
        for (int j=0;j<myarray.length;j++){
            try{
//                Log.d("my array","actual number"+myarray[j]+"next number"+myarray[j+1]+  " previous "+myarray[j-1]);
                if (j<myarray.length-1) {
                    int new_next = Integer.parseInt("" + myarray[j + 1]);
                    int pre_value = Integer.parseInt("" + myarray[j]);

                    differece = new_next - pre_value;
                    last=(int)myarray[j+1];
                }
//5,6,7,8,9,10  12,13,14,15

                if (differece>=2){
                    //  Toast.makeText(this, "differece"+differece, Toast.LENGTH_SHORT).show();
                    boundsArrayList.add(myarray[startBound]+" "+myarray[j]);
                    startBound=j+1;
                    Log.d("differece block",""+differece);





                }





                // Log.d("differce ",""+differece);

            }catch (Exception e)
            {
                e.printStackTrace();
            }


        }
        boundsArrayList.add(myarray[startBound]+" "+last);
        adapter.notifyDataSetChanged();
        for (int i=0;i<boundsArrayList.size();i++){
            Log.d("bound here",boundsArrayList.get(i)+boundsArrayList.size());
        }
    }
}