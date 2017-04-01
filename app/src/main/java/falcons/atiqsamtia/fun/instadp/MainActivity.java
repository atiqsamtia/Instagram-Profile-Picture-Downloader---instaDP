package falcons.atiqsamtia.fun.instadp;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        queue  = Volley.newRequestQueue(this);














    checkPermissions();

    }









    private TextInputLayout mTextInputLayout;
    private EditText mUrl;
    private Button mPaste;
    private Button mDownload;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-03-31 22:33:48 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        mTextInputLayout = (TextInputLayout)findViewById( R.id.textInputLayout );
        mUrl = (EditText)findViewById( R.id.url );
        mPaste = (Button)findViewById( R.id.paste );
        mDownload = (Button)findViewById( R.id.download );

        mPaste.setOnClickListener( this );
        mDownload.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-03-31 22:33:48 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == mPaste ) {

            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

            mUrl.setText(clipboard.getText());

        } else if ( v == mDownload ) {

            String url = mUrl.getText().toString();

            if (url.length() > 0) {

                final ProgressDialog pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading...");
                pDialog.show();

                StringRequest r = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Pattern pattern = Pattern.compile("profile_pic_url\": \"(.*?)\", \"");
                                Matcher matcher = pattern.matcher(response.toString());
                                if (matcher.find())
                                {
                                    try {

                                        String pic = matcher.group(1);

                                        pic = pic.replace("/s150x150", "");

                                        String[] bits = pic.split("/");
                                        String lastOne = bits[bits.length - 1];

                                        DownloadManager dm = (DownloadManager) MainActivity.this.getSystemService(DOWNLOAD_SERVICE);
                                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pic));
                                        request.setTitle("InstDP" + lastOne);
                                        request.setDescription("InstDP" + lastOne);

                                        request.setDestinationInExternalPublicDir("InstaDP", "InstaDP_" + lastOne);

                                        dm.enqueue(request);
                                    }catch (Exception e){
                                        Log.e( "onResponse: ", "");
                                    }


                                }
                                pDialog.dismiss();

                                Toast.makeText(MainActivity.this,"Profile image downloaded.",Toast.LENGTH_SHORT).show();
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pDialog.hide();
                    }
                });

                queue.add(r);


            }


        }
    }










    private void checkPermissions() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
                return;
            }
        }
    }


}
