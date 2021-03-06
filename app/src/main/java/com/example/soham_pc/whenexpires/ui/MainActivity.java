package com.example.soham_pc.whenexpires.ui;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soham_pc.whenexpires.R;
import com.example.soham_pc.whenexpires.database.entity.ProductEntity;
import com.example.soham_pc.whenexpires.utils.Methods;
import com.example.soham_pc.whenexpires.viewmodels.ProductViewModel;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    protected @BindView(R.id.itemName)
    EditText edtItemName;
    protected @BindView(R.id.tvBcodeHeader)
    TextView tvBcodeHeader;
    protected @BindView(R.id.dateView)
    TextView tvExpDate;
    protected @BindView(R.id.datePicker)
    DatePicker dtpicker;
    Context mContext;
    private Date date;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private static String TAG = "BarcodeResult";
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final int RC_OCR_CAPTURE = 9003;
    private ProductViewModel productViewModel;
    private String barCode, expiryDate;
    private String expiryTime;
    Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, MainActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        methods = new Methods();
        mContext=this;
    }


    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.badge_icon);
        return builder.build();
    }

    private void saveProductItem() {
        String item = edtItemName.getText().toString().trim();
        if (!item.equals("") || !item.isEmpty()) {
            long result = productViewModel.saveProduct(new ProductEntity(item, barCode,
                    expiryTime, ""));
            if (result != -1) {
                Toast.makeText(mContext, "Product Saved Successfully", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mContext, "Failed To Save Product", Toast.LENGTH_SHORT).show();
            }
        } else {
            edtItemName.setError("Product Name Required");
        }
    }

    @OnClick({R.id.scandate, R.id.scanBarcode,R.id.viewProduct,R.id.btSavedItems})
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.scanBarcode:
                startBarcodeScanner();
                break;
            case R.id.scandate:
                startOcrScanner();
                break;
            case R.id.viewProduct:
                startProductList();
                break;
            case R.id.btSavedItems:
               saveProductItem();
                break;
        }
    }

    private void saveProduct() {
    }

    private void startProductList() {
        startActivity(new Intent(this, RecyclerListActivity.class));
    }


    private void startBarcodeScanner() {
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    private void startOcrScanner() {
        Intent intent = new Intent(this, OcrCaptureActivity.class);
        startActivityForResult(intent, RC_OCR_CAPTURE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE)
        {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Toast.makeText(this, "Scanned Successfully", Toast.LENGTH_SHORT).show();
                    //fetch name
                    String [] barToName = (barcode.displayValue).split("-") ;
                    edtItemName.setText(barToName[0]);
                    tvBcodeHeader.setVisibility(View.VISIBLE);
                    tvBcodeHeader.setText(barToName[1]);
                    //tvBcodeHeader.setText(barcode.displayValue);

                    barCode = barcode.displayValue;
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);

                } else {
                    tvBcodeHeader.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "No Barcode. Failed To Scan", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            }
            else {
                Toast.makeText(this, "Error Scanning", Toast.LENGTH_SHORT).show();
            }
        } //barcode ends  //date starts
        else if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    expiryDate = text;
                    if (!expiryDate.equals("")){
                        Toast.makeText(mContext, expiryDate, Toast.LENGTH_SHORT).show();
                        String [] dateItem = expiryDate.split("\\s+");
                        Log.e("ArrayLog__", dateItem[0] +dateItem[1]+dateItem[2]);
                        if (dateItem.length==3) {
                            int month = methods.getMonthNumber(dateItem[0]);
                            int day = Integer.parseInt(dateItem[1]);
                            int year = Integer.parseInt(dateItem[2]);
                            if (month != 0) {
                                tvExpDate.setVisibility(View.VISIBLE);
                                tvExpDate.setText(day+"/ "+month+"/ "+year);
                                Toast.makeText(mContext, "Date Captured", Toast.LENGTH_SHORT).show();
                                calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, (month-1));
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                date = calendar.getTime();
                                expiryTime = Objects.toString(date.getTime(), "0");
                            } else {
                                Toast.makeText(mContext, "Invalid Date", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Scanned Failed", Toast.LENGTH_SHORT).show();
                    datePickerDialog.show();
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                Toast.makeText(this, "Error Scanning", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        tvExpDate.setText(dayOfMonth+"/ "+month+"/ "+year);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date = calendar.getTime();
        expiryTime = Objects.toString(date.getTime(), "0");
    }
    private void setScannedDate(){

    }

}
