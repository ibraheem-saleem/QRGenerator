package pmru.covid19.qrCodes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import pmru.covid19.R;
import pmru.covid19.qrCodes.Adapters.CustomAdapter;
import pmru.covid19.qrCodes.Model.DataModel;

public class FoldersActivity extends AppCompatActivity {
    ArrayList<DataModel> folderList=new ArrayList<>();

    private static CustomAdapter adapter;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);
        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        String path= Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

        File f = new File(path);
        File[] files = f.listFiles();
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                // is directory
                DataModel dataModel=new DataModel();
                dataModel.setName(inFile.getName());
                Log.d("check",inFile.getName());
                folderList.add(dataModel);
            }
        }
        Log.d("check list size",folderList.size()+" here");
        adapter = new CustomAdapter(this,folderList,0);
        recyclerView.setAdapter(adapter);
    }
}