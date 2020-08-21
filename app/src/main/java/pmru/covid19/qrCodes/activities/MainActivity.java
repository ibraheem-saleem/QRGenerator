package pmru.covid19.qrCodes.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import pmru.covid19.R;
import pmru.covid19.qrCodes.Helpers.App_SharedPreferences;
import pmru.covid19.qrCodes.Helpers.DatabaseHandler;
import pmru.covid19.qrCodes.Model.DataModel;
import pmru.library.qrgenearator.QRGContents;
import pmru.library.qrgenearator.QRGEncoder;
import pmru.library.qrgenearator.QRGSaver;

import static java.lang.System.in;
import static java.lang.System.out;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private EditText edtValue;
    private EditText edt_value_max;
    private ImageView qrImage;
    private String inputValue;
    private String savePath =Environment.getExternalStorageDirectory().getPath() + "/QRCode";

    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private AppCompatActivity activity;

    String[] country = { "Abbottabad", "Bajaur", "Bannu", "Battagram", "Charsadda"
    ,"Chitral Lower","Chitral Upper","Dera Ismail Khan","Dir Lower","Dir Upper"
    ,"Hangu","Haripur","Karak","Khyber","Kohat","Kohistan Upper","Kohistan Lower"
    ,"Kolai Palas","Kurram","Lakki Marwat","Malakand","Mansehra","Mardan","Mohmand","North Waziristan"
    ,"South Waziristan","Nowshera","Orakzai","Peshawar","Shangla","Swabi","Swat","Tank","Torghar"};


    String district_code;
    private Spinner spin;
    private String inputValue_end;
    App_SharedPreferences appSharedPreferences;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        appSharedPreferences=new App_SharedPreferences(this);
        boolean checkIfFirstTime=appSharedPreferences.isOpenFirstTime(this);
         db = new DatabaseHandler(this);
        if (checkIfFirstTime){
            Toast.makeText(this, "first time", Toast.LENGTH_SHORT).show();
            appSharedPreferences.saveFirstTimeOpenStatus(this,false);



            addAllData(db);

        }
        else{
            Toast.makeText(this, "second time", Toast.LENGTH_SHORT).show();


        }


          savePath =Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

        String folder_main = "QRCode";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        qrImage = findViewById(R.id.qr_image);
        edtValue = findViewById(R.id.edt_value);
        edt_value_max = findViewById(R.id.edt_value_max);
        activity = this;

        findViewById(R.id.generate_barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputValue = edtValue.getText().toString().trim();
                inputValue_end = edt_value_max.getText().toString().trim();
                if (inputValue.length() > 0) {

                    int starting_value= Integer.parseInt(inputValue);
                    int ending_values= Integer.parseInt(inputValue_end);
                    new qrCode().execute(starting_value,ending_values);




                } else {
                    edtValue.setError(getResources().getString(R.string.value_required));
                }
            }
        });

        findViewById(R.id.save_barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        boolean save = new QRGSaver().save(savePath, edtValue.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
                        String result = save ? "Image Saved" : "Image Not Saved";
                        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                        edtValue.setText(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            }
        });

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
         spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

    }

    private void addAllData(DatabaseHandler db) {
        db.addUsedQrCodes(new DataModel("1","Abbottabad","0 5,100 202,6 30,90 99,50 89"));
        db.addUsedQrCodes(new DataModel("2","Bajaur","0 0"));
        db.addUsedQrCodes(new DataModel("3","Bannu","0 0"));
        db.addUsedQrCodes(new DataModel("4","Battagram","0 0"));
        db.addUsedQrCodes(new DataModel("5","Charsadda","0 0"));
        db.addUsedQrCodes(new DataModel("6","Chitral Lower","0 0"));
        db.addUsedQrCodes(new DataModel("7","Chitral Upper","0 0"));
        db.addUsedQrCodes(new DataModel("8","Dera Ismail Khan","0 0"));
        db.addUsedQrCodes(new DataModel("9","Dir Lower","0 0"));
        db.addUsedQrCodes(new DataModel("10","Dir Dir Upper","0 0"));
        db.addUsedQrCodes(new DataModel("11","Hangu","0 0"));
        db.addUsedQrCodes(new DataModel("12","Haripur","0 0"));
        db.addUsedQrCodes(new DataModel("13","Karak","0 0"));
        db.addUsedQrCodes(new DataModel("14","Khyber","0 0"));
        db.addUsedQrCodes(new DataModel("15","Kohat","0 0"));
        db.addUsedQrCodes(new DataModel("16","Kohistan Upper","0 0"));
        db.addUsedQrCodes(new DataModel("17","Kohistan Lower","0 0"));
        db.addUsedQrCodes(new DataModel("18","Kolai Palas","0 0"));
        db.addUsedQrCodes(new DataModel("19","Kurram","0 0"));
        db.addUsedQrCodes(new DataModel("20","Lakki Marwat","0 0"));
        db.addUsedQrCodes(new DataModel("21","Malakand","0 0"));
        db.addUsedQrCodes(new DataModel("22","Mansehra","0 0"));
        db.addUsedQrCodes(new DataModel("23","Mardan","0 0"));
        db.addUsedQrCodes(new DataModel("24","Mohmand","0 0"));
        db.addUsedQrCodes(new DataModel("25","North Waziristan","0 0"));
        db.addUsedQrCodes(new DataModel("26","South Waziristan","0 0"));
        db.addUsedQrCodes(new DataModel("27","Nowshera","0 0"));
        db.addUsedQrCodes(new DataModel("28","Orakzai","0 0"));
        db.addUsedQrCodes(new DataModel("29","Peshawar","0 0"));
        db.addUsedQrCodes(new DataModel("30","Shangla","0 0"));
        db.addUsedQrCodes(new DataModel("31","Swabi","0 0"));
        db.addUsedQrCodes(new DataModel("31","Tank","0 0"));
        db.addUsedQrCodes(new DataModel("33","Torghar","0 0"));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        if (spin.getSelectedItem().toString().equalsIgnoreCase("Abbottabad")){

            district_code="ABT";
           savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Abbottabad/";
        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Bajaur")){

            district_code="BJR";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Bajaur/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Bannu")){

            district_code="BNU";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Bannu/";

        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Battagram")){

            district_code="BTG";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Battagram/";
        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Charsadda")){

            district_code="CHD";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Charsadda/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Chitral Lower")){

            district_code="CHL";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Chitral_Lower/";
        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Chitral Upper")){

            district_code="CHU";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Chitral_Upper/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Dera Ismail Khan")){

            district_code="DIK";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Dera_Ismail_Khan/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Dir Lower")){

            district_code="DLO";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Dir_Lower/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Dir Upper")){

            district_code="DUP";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Dir_Upper/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Hangu")){

            district_code="HGU";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Hangu/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Haripur")){

            district_code="HPR";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Haripur/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Karak")){

            district_code="KRK";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Karak/";
        }


        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Khyber")){

            district_code="KHY";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Khyber/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Kohat")){

            district_code="KHT";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Kohat/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Kohistan Upper")){

            district_code="KUP";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Kohistan_Upper/";
        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Kohistan Lower")){

            district_code="KLO";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Kohistan_Lower/";
        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Kolai Palas")){

            district_code="KPA";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Kolai_Palas/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Kurram")){

            district_code="KRM";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Kurram/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Lakki Marwat")){

            district_code="LKM";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Lakki_Marwat/";
            /*Toast.makeText(activity, "position "+i, Toast.LENGTH_SHORT).show();*/
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Malakand")){

            district_code="MLD";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Malakand/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Mansehra")){

            district_code="MNS";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Mansehra/";
        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Mardan")){

            district_code="MDN";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Mardan/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Mohmand")){

            district_code="MHD";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Mohmand/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("North Waziristan")){

            district_code="NWZ";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Mohmand/";
        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("South Waziristan")){

            district_code="SWZ";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/South_Waziristan/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Nowshera")){

            district_code="NWA";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Nowshera/";
        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Orakzai")){

            district_code="ORK";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Orakzai/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Peshawar")){

            district_code="PSH";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Peshawar/";
        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Shangla")){

            district_code="SHA";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Shangla/";
        }
        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Swabi")){

            district_code="SWB";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Swabi/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Swat")){

            district_code="SWT";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Swat/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Tank")){

            district_code="TNK";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Tank/";
        }

        else if (spin.getSelectedItem().toString().equalsIgnoreCase("Torghar")){

            district_code="TGR";
            savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/Torghar/";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        savePath= Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

    }

    public class qrCode extends AsyncTask<Integer,Integer,Void>{

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        private String prefex_Seven;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(activity, "Started", Toast.LENGTH_SHORT).show();
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(activity, "finished", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int starting_value= params[0];
            int ending_values= params[1];
            for (int i=starting_value;i<=ending_values;i++){


                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 1 / 7;

                qrgEncoder = new QRGEncoder(
                        district_code+"-"+i, null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                qrgEncoder.setColorBlack(Color.BLACK);
                qrgEncoder.setColorWhite(Color.WHITE);
                try {
                    bitmap = qrgEncoder.getBitmap();

                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK); // Text Color
                    paint.setTextSize(12); // Text Size

                    paint.setTextAlign(Paint.Align.CENTER);

                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
                    // some more settings...
                    Canvas canvas = new Canvas(bitmap);

                    canvas.drawBitmap(bitmap, 0, 0, paint);

                    if (i<=9){
                         prefex_Seven="000000"+i;
                    }
                    else if (i>9 && i<=99){
                         prefex_Seven="00000"+i;
                    }

                    else if (i>99 && i<=999){
                         prefex_Seven="0000"+i;
                    }

                    else if (i>999 && i<=9999){
                         prefex_Seven="000"+i;
                    }
                    else if (i>9999 && i<=99999){
                         prefex_Seven="00"+i;
                    }

                    else if (i>99999 && i<=999999){
                         prefex_Seven="0"+i;
                    }
                    else if (i>999999 && i<=9999999){
                         prefex_Seven= String.valueOf(i);
                    }
                    String mText=district_code+"-"+prefex_Seven;
                    // draw text to the Canvas center
                    int horizontalSpacing = 24;
                    int verticalSpacing = 36;
                    int x = canvas.getWidth() /2;

                    int y = bitmap.getHeight();
                    canvas.drawText(mText, x,y, paint);

                    // NEWLY ADDED CODE ENDS HERE ]

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    /*qrImage.setImageBitmap(bitmap);*/
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            boolean save = new QRGSaver().save(savePath, district_code+"-"+i, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                            String result = save ? "Image Saved" : "Image Not Saved";
                            Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                            edtValue.setText(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public class QrToImage extends AsyncTask<String,String,String>{
        private String prefex_Seven;
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        private String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(activity, "Started", Toast.LENGTH_SHORT).show();
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(activity, "finished"+s, Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            int starting_value= Integer.parseInt(params[0]);
            int ending_values= Integer.parseInt(params[1]);
            String path=params[2];
            Log.d("path",path);
            for (int i=starting_value;i<=ending_values;i++){


                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 1 / 4;

                qrgEncoder = new QRGEncoder(
                        district_code+"-"+i, null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                qrgEncoder.setColorBlack(Color.BLACK);
                qrgEncoder.setColorWhite(Color.WHITE);
                try {
                    bitmap = qrgEncoder.getBitmap();

                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK); // Text Color
                    paint.setTextSize(12); // Text Size
                    paint.setTextAlign(Paint.Align.CENTER);

                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
                    // some more settings...
                    Canvas canvas = new Canvas(bitmap);

                    canvas.drawBitmap(bitmap, 0, 0, paint);

                    if (i<=9){
                        prefex_Seven="000000"+i;
                    }
                    else if (i>9 && i<=99){
                        prefex_Seven="00000"+i;
                    }

                    else if (i>99 && i<=999){
                        prefex_Seven="0000"+i;
                    }

                    else if (i>999 && i<=9999){
                        prefex_Seven="000"+i;
                    }
                    else if (i>9999 && i<=99999){
                        prefex_Seven="00"+i;
                    }

                    else if (i>99999 && i<=999999){
                        prefex_Seven="0"+i;
                    }
                    else if (i>999999 && i<=9999999){
                        prefex_Seven= String.valueOf(i);
                    }
                    String mText=district_code+"-"+prefex_Seven;
                    // draw text to the Canvas center
                    int horizontalSpacing = 24;
                    int verticalSpacing = 36;
                    int x = canvas.getWidth() /2;

                    int y = bitmap.getHeight()-10;
                    canvas.drawText(mText, x,y, paint);

                    // NEWLY ADDED CODE ENDS HERE ]

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    /*qrImage.setImageBitmap(bitmap);*/
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            boolean save = new QRGSaver().save(savePath, district_code+"-"+i, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                             result = save ? "Image Saved" : "Image Not Saved";
                            Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                            edtValue.setText(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }

    public void Alog1(){
        DataModel dataModel=db.getDistById(spin.getSelectedItemPosition()+1);
        Log.d("District Record", String.valueOf(dataModel.getUsed_qr_number()));

        String[] Allvalues=dataModel.getUsed_qr_number().split(",");
        for (int i=0;i<Allvalues.length;i++){

            String[] singleValue=Allvalues[i].split(" ");
//                        Log.d("Check All Split :"," All Split Values = "+Allvalues[i]);
            for (int j=0;j<singleValue.length-1;j++){
                Log.d("Check Single Split :"," Single Split Values = "+singleValue[0]+" and "+singleValue[1]);

                int starting_value=52;
                int ending_values=85;
                if (starting_value >= Integer.parseInt(singleValue[0]) && starting_value <= Integer.parseInt(singleValue[0])
                        &&ending_values >= Integer.parseInt(singleValue[1]) && ending_values <= Integer.parseInt(singleValue[1])) {


                    // do action
                    Log.d("Value Checking :","found");
                }
            }}
    }

}
