package pmru.covid19.qrCodes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.printservice.PrintDocument;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Logger;

import pmru.covid19.R;
import pmru.covid19.qrCodes.Adapters.PdfDocumentAdapter;
import pmru.covid19.qrCodes.Adapters.SlidingImage_Adapter;

public class Show_Qr_Images extends AppCompatActivity {

    ArrayList<Integer> sortedArrayList=new ArrayList<>();
    ArrayList<String> boundsArrayList=new ArrayList<>();
    int differece;
    boolean checkprint=false;


    private ArrayList<String> imagesPathArrayList=new ArrayList<>();
    private SlidingImage_Adapter adapter;
    ViewPager pager;
    ImageView nextqr,previousqr;
    private File path;
    private int[] namesArr;
    private Object[] myarray;
    int startBound=0;
    private int last;
    private String folder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__qr__images);
        getSupportActionBar().hide();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        pager=findViewById(R.id.viewpagger);
        nextqr=findViewById(R.id.nextqr);
        previousqr=findViewById(R.id.previousqr);

         folder=getIntent().getStringExtra("folder");
        String[] filenames = new String[0];
         path = new File(Environment.getExternalStorageDirectory() + "/QRCode/"+folder);
        if (path.exists()) {
            filenames = path.list();
        }
        for (int i = 0; i < filenames.length; i++) {
            String requiredString2 = filenames[i].substring(filenames[i].indexOf("-") + 1, filenames[i].indexOf("."));

            while (imagesPathArrayList.size() < Integer.parseInt(requiredString2)) {
                imagesPathArrayList.add(null);
            }
            imagesPathArrayList.add(Integer.parseInt(requiredString2), path.getPath() + "/" + filenames[i]);
            Log.d("file names", filenames[i]);
            String requiredString = filenames[i].substring(filenames[i].indexOf("-") + 1, filenames[i].indexOf("."));


            sortedArrayList.add(Integer.valueOf(requiredString));


//            Log.e("requiredString", requiredString);


        }


        adapter = new SlidingImage_Adapter(this, imagesPathArrayList);
     /*   imagesPathArrayList.removeAll(Arrays.asList(null, ""));
        try {
            MoveFile(imagesPathArrayList.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        pager.setAdapter(adapter);


        Collections.sort(sortedArrayList);

        for (int i=0;i<sortedArrayList.size();i++){
            int number=sortedArrayList.get(i);

             myarray=sortedArrayList.toArray();


         //   Log.d("length of array",""+sortedArrayList.size());

        }
        for (int j=0;j<myarray.length;j++){
            try{
//


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
        /*openDirectoryIntent(path.getPath());*/
        checkprint=true;
        createPDFWithMultipleImage();
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
    private void createPDFWithMultipleImage(){
        File file = getOutputFile();
        if (file != null){
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                PdfDocument pdfDocument = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    pdfDocument = new PdfDocument();
                    for (int i = 0; i < imagesPathArrayList.size(); i++){
                        Bitmap bitmap = BitmapFactory.decodeFile(imagesPathArrayList.get(i));
                        PdfDocument.PageInfo pageInfo = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), (i + 1)).create();
                        }
                        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                        Canvas canvas = page.getCanvas();
                        Paint paint = new Paint();
                        paint.setColor(Color.BLUE);
                        canvas.drawPaint(paint);
                        canvas.drawBitmap(bitmap, 0f, 0f, null);
                        pdfDocument.finishPage(page);
                        bitmap.recycle();
                    }
                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();



                    if (checkprint) {
                        PrintManager printManager = (PrintManager) Show_Qr_Images.this.getSystemService(Context.PRINT_SERVICE);
                        try {
                            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(Show_Qr_Images.this, file.getAbsolutePath());
                            printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());

                        } catch (Exception e) {
                            Logger.getLogger(e.getMessage());
                        }
                    }
                    else{
                        ShareCompat.IntentBuilder.from(this)
                                .setStream(Uri.fromFile(file))
                                .setType(URLConnection.guessContentTypeFromName(file.getName()))
                                .startChooser();
                    }

                }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        // Set job name, which will be displayed in the print queue



    private File getOutputFile(){
        File root = new File(Environment.getExternalStorageDirectory() + "/QRCode/");

        boolean isFolderCreated = true;

        if (!root.exists()){
            isFolderCreated = root.mkdir();
        }

        if (isFolderCreated) {
            String timeStamp = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                timeStamp = String.valueOf(new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US));
            }
            String imageFileName = "pdf" + folder;


            File file=new File(root, imageFileName + ".pdf");
            Log.d("file name and directory",file.getName()+" / "+file.getAbsolutePath());
            return file;
        }
        else {
            Toast.makeText(this, "Folder is not created", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void sharenow(View view) {
        checkprint = false;
        createPDFWithMultipleImage();
    }

    public static void MoveFile(String path_source) throws IOException {
        File file_Source = new File(path_source);
        String des=Environment.getExternalStorageDirectory().getAbsolutePath() + "/QRCode/"+file_Source.getName();
        File file_Destination = new File(des);

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(file_Source).getChannel();
            destination = new FileOutputStream(file_Destination).getChannel();

            long count = 0;
            long size = source.size();
            while((count += destination.transferFrom(source, count, size-count))<size);
            file_Source.delete();
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }
}