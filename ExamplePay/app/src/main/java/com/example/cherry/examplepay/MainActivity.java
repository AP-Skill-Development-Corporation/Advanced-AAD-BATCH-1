package com.example.cherry.examplepay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Checkout.preload(this);
    }

    public void pay(View view) {
        Checkout ch = new Checkout();
        ch.setKeyID("rzp_test_dVBx0MrwRr16Ns");
        try {
            JSONObject obj = new JSONObject();
            obj.put("name", "Chaitanya");
            obj.put("currency","INR");
            obj.put("amount","30000");
            ch.open(this,obj);
        }catch (Exception e){
            Log.e("Apssdc","Error occured in razor pay");
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }
}