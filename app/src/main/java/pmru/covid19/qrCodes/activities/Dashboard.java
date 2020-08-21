package pmru.covid19.qrCodes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pmru.covid19.R;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    Button qrCodeGenerator,qrCodeDownloaded,qrCodeUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();

        qrCodeGenerator=findViewById(R.id.qrCodeGenerator);
        qrCodeDownloaded=findViewById(R.id.qrCodeDownloaded);
        qrCodeUsed=findViewById(R.id.qrCodeUsed);

        qrCodeGenerator.setOnClickListener(this);
        qrCodeDownloaded.setOnClickListener(this);
        qrCodeUsed.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.qrCodeGenerator:
                startActivity(new Intent(Dashboard.this,MainActivity.class));
                break;
            case R.id.qrCodeDownloaded:
                startActivity(new Intent(Dashboard.this,FoldersActivity.class));
                break;
            case R.id.qrCodeUsed:
                startActivity(new Intent(Dashboard.this,UsedQrCode.class));
                break;

        }

    }
}