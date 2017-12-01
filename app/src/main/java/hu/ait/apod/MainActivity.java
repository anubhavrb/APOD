package hu.ait.apod;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.ait.apod.data.APODResult;
import hu.ait.apod.network.ApodAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "DoQByy5daS0Cf2hkVVWb2LFaYe7mmgrZyMak8CHT";

    @BindView(R.id.tvData)
    TextView tvData;
    @BindView(R.id.imgAPOD)
    ImageView imgAPOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getAPODInfo(Calendar.getInstance().getTime());

    }

    private void getAPODInfo(Date today) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(today);
        tvData.setText(date);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/planetary/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApodAPI apodAPI = retrofit.create(ApodAPI.class);

        Call<APODResult> callAPOD = apodAPI.getAPOD(date, "true", API_KEY);
        callAPOD.enqueue(new Callback<APODResult>() {
            @Override
            public void onResponse(Call<APODResult> call, Response<APODResult> response) {
                APODResult result = response.body();
                if (result == null)
                    return;
                tvData.setText(result.getExplanation());

                Glide.with(MainActivity.this)
                        .load(result.getHdurl())
                        .into(imgAPOD);
            }

            @Override
            public void onFailure(Call<APODResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
