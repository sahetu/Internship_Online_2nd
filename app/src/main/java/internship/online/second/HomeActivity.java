package internship.online.second;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    EditText amount;
    Button payNow;
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        amount = findViewById(R.id.home_amount);
        payNow = findViewById(R.id.home_pay_now);

        Checkout.preload(getApplicationContext());

        alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount.getText().toString().trim().equals("")) {
                    amount.setError("Amount Required");
                } else if (amount.getText().toString().trim().equals("0")) {
                    amount.setError("Invalid Amount");
                } else {
                    startPayment();
                }
            }
        });

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        co.setKeyID("rzp_test_xsiOz9lYtWKHgF");
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Rent Payment");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.mipmap.ic_launcher);
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(amount.getText().toString())*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("Payment Failed:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}