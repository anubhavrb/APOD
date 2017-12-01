package hu.ait.apod;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
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
import butterknife.OnClick;
import hu.ait.apod.data.APODResult;
import hu.ait.apod.network.ApodAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "DoQByy5daS0Cf2hkVVWb2LFaYe7mmgrZyMak8CHT";

    @BindView(R.id.btnDate)
    Button btnDate;
    @BindView(R.id.tvData)
    TextView tvData;
    @BindView(R.id.imgAPOD)
    ImageView imgAPOD;

    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            getAPODInfo(myCalendar.getTime());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getAPODInfo(myCalendar.getTime());
    }

    @OnClick(R.id.btnDate)
    public void btnClicked() {
        new DatePickerDialog(MainActivity.this, datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void getAPODInfo(Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date_s = formatter.format(date);
        tvData.setText(date_s);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/planetary/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApodAPI apodAPI = retrofit.create(ApodAPI.class);

        Call<APODResult> callAPOD = apodAPI.getAPOD(date_s, "true", API_KEY);
        callAPOD.enqueue(new Callback<APODResult>() {
            @Override
            public void onResponse(Call<APODResult> call, Response<APODResult> response) {
                APODResult result = response.body();
                if (result == null)
                    return;

                Glide.with(MainActivity.this)
                        .load(result.getHdurl())
                        .into(imgAPOD);

                tvData.setText(result.getExplanation());
            }

            @Override
            public void onFailure(Call<APODResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
