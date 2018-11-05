package com.example.endzhe.rertofit;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.endzhe.rertofit.database.AppDatabase;
import com.example.endzhe.rertofit.database.DataDao;
import com.example.endzhe.rertofit.database.DataModel;
import com.example.endzhe.rertofit.recyclerView.RoomActivity;
import com.example.endzhe.rertofit.retrofit.Api;
import com.example.endzhe.rertofit.retrofit.Controller;
import com.example.endzhe.rertofit.retrofit.POJO.LifeExpectancyModel;
import com.example.endzhe.rertofit.retrofit.POJO.RankModel;
import com.example.endzhe.rertofit.retrofit.POJO.RankTodayModel;
import com.example.endzhe.rertofit.retrofit.POJO.TotalPopulation;
import com.example.endzhe.rertofit.retrofit.POJO.TotalPopulationModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Query1Activity extends AppCompatActivity {

    final static String LOG = "myLogs";
    final static String FILENAME = "Result.txt";
    final static String DIR_SD = "NEW_DIR";

    //---TextView--
    @BindView(R.id.textView_rank)
    TextView textView_rank;

    @BindView(R.id.textView_rankToday)
    TextView textView_rankToday;

    @BindView(R.id.textView_life_expectancy)
    TextView textView_life_expectancy;

    @BindView(R.id.textView_population)
    TextView textView_population;

    //--EditText--
    @BindView(R.id.editText_dob)
    EditText editText_dob;

    @BindView(R.id.editText_country)
    EditText editText_country;

    @BindView(R.id.editText_sex)
    EditText editText_sex;

    @BindView(R.id.editText_date)
    EditText editText_date;

    //--ImageView--
    @BindView(R.id.imageView_dob)
    ImageView imageView_dob;

    @BindView(R.id.imageView_sex)
    ImageView imageView_sex;

    @BindView(R.id.imageView_country)
    ImageView imageView_country;

    @BindView(R.id.imageView_date)
    ImageView imageView_date;

    //--Button--
    @BindView(R.id.btn_getRank)
    Button btn_getRank;

    @BindView(R.id.btn_getRankToday)
    Button btn_getRankToday;

    @BindView(R.id.btn_get_life_expectancy)
    Button btn_get_life_expectancy;

    @BindView(R.id.btn_get_population)
    Button btn_get_population;

    @BindView(R.id.btnReadFile)
    Button btnReadFile;

    @BindView(R.id.btnWriteFile)
    Button btnWriteFile;


    private final Api api = Controller.createApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query1);
        ButterKnife.bind(this);

    }

    //Обработчик долгого нажатия
    //Добавляет в бд результат 1-ого запроса
    //и открывает новое активити с recyclerView
    @OnLongClick(R.id.btn_getRank)
    boolean onLongClick(View v) {
        Context context = v.getContext();
        final String country = editText_country.getText().toString();
        final String dob = editText_dob.getText().toString();
        final String sex = editText_sex.getText().toString();
        final String date = editText_date.getText().toString();

        final String rank = textView_rank.getText().toString();
        DataModel content = new DataModel(dob, sex, country, date, rank);

        //добавление
        DataDao messageDao = (DataDao) AppDatabase.getInstance(context).dataDao();
        messageDao.insertDataModel(content);

        Intent intent = new Intent(context, RoomActivity.class);
        startActivity(intent);
        return true;
    }


    @OnClick({R.id.btn_getRank, R.id.btn_getRankToday,
            R.id.btn_get_life_expectancy, R.id.btn_get_population,
            R.id.btnWriteFile, R.id.btnReadFile})
    void onSaveClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getRank:
                //создание запроса
                Call<RankModel> call = api.getRank(
                        editText_dob.getText().toString(),
                        editText_sex.getText().toString(),
                        editText_country.getText().toString(),
                        editText_date.getText().toString()
                );
                //отправка запроса(асинхронный)
                call.enqueue(new Callback<RankModel>() {
                    @Override
                    public void onResponse(Call<RankModel> call, Response<RankModel> response) {
                        if (response.isSuccessful()) {

                            RankModel body = response.body();
                            Integer rank = body.getRank();
                            String answer = rank.toString();
                            textView_rank.setText(answer);
                        } else {
                            notSuccessful();
                        }

                    }

                    //не удалось "достучаться" до сервера
                    @Override
                    public void onFailure(Call<RankModel> call, Throwable t) {
                        Log.i(LOG, "error in query" + t);
                        Toast_onFailure();
                    }
                });
                break;

            case R.id.btn_getRankToday:
                //создание запроса
                Call<RankTodayModel> call2 = api.getRankToday(
                        editText_dob.getText().toString(),
                        editText_sex.getText().toString(),
                        editText_country.getText().toString()
                );
                //отправка запроса(асинхронный)
                call2.enqueue(new Callback<RankTodayModel>() {
                    @Override
                    public void onResponse(Call<RankTodayModel> call, Response<RankTodayModel> response) {
                        if (response.isSuccessful()) {

                            RankTodayModel body = response.body();
                            Integer rank = body.getRankToday();
                            String answer = rank.toString();
                            textView_rankToday.setText(answer);
                        } else {
                            notSuccessful();
                        }
                    }

                    //не удалось "достучаться" до сервера
                    @Override
                    public void onFailure(Call<RankTodayModel> call, Throwable t) {
                        Log.i(LOG, "error in query" + t);
                        Toast_onFailure();
                    }
                });
                break;
            case R.id.btn_get_life_expectancy:
                //создание запроса
                Call<LifeExpectancyModel> call3 = api.getTotal_life_expectancy(
                        editText_sex.getText().toString(),
                        editText_country.getText().toString(),
                        editText_dob.getText().toString()
                );
                //отправка запроса(асинхронный)
                call3.enqueue(new Callback<LifeExpectancyModel>() {
                    @Override
                    public void onResponse(Call<LifeExpectancyModel> call, Response<LifeExpectancyModel> response) {
                        if (response.isSuccessful()) {
                            LifeExpectancyModel body = response.body();
                            String life_expectancy = body.getTotal_life_expectancy();
                            String answer = life_expectancy.toString();
                            textView_life_expectancy.setText(answer);
                        } else {
                            notSuccessful();
                        }
                    }

                    //не удалось "достучаться" до сервера
                    @Override
                    public void onFailure(Call<LifeExpectancyModel> call, Throwable t) {
                        Log.i(LOG, "error in query" + t);
                        Toast_onFailure();


                    }
                });

                break;
            case R.id.btn_get_population:
                //создание запроса
                Call<TotalPopulationModel> call4 = api.getPopulation(
                        editText_country.getText().toString(),
                        editText_date.getText().toString()
                );
                //отправка запроса(асинхронный)
                call4.enqueue(new Callback<TotalPopulationModel>() {
                    @Override
                    public void onResponse(Call<TotalPopulationModel> call,
                                           Response<TotalPopulationModel> response) {
                        if (response.isSuccessful()) {

                            TotalPopulationModel body = response.body();
                            TotalPopulation totalPopulation = body.getTotalPopulation();

                            String answer = totalPopulation.getPopulation().toString();
                            textView_population.setText(answer);
                        } else {
                            notSuccessful();
                        }
                    }

                    //не удалось "достучаться" до сервера
                    @Override
                    public void onFailure(Call<TotalPopulationModel> call, Throwable t) {
                        Log.i(LOG, "error in query" + t);
                        Toast_onFailure();
                    }
                });
                break;

            case R.id.btnWriteFile:
                writeFile();
                break;
            case R.id.btnReadFile:
                readFile();
                break;
            default:
                break;
        }
    }

    void writeFile() {
        //Проверка версии SDK устройства
        if (Build.VERSION.SDK_INT >= 23) {
            //динамическое получение прав на WRITE_EXTERNAL_STORAGE
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG, "Permission is granted");

                write();

            } else {
                Log.d(LOG, "Permission is revoked");
                //запрашиваем разрешение
                ActivityCompat.requestPermissions(Query1Activity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {

            write();
        }

    }

    void write() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME);
        try {
            FileWriter writer = new FileWriter(sdFile, true);
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(writer);
            // пишем данные
            bw.write("Dob:" + editText_dob.getText().toString() + "\n"
                    + "Sex:" + editText_sex.getText().toString() + "\n"
                    + "Country:" + editText_country.getText().toString() + "\n"
                    + "Date:" + editText_date.getText().toString() + "\n"
                    + "RESULT\n"
                    + "Rank:" + textView_rank.getText().toString() + "\n"
                    + "Rank today:" + textView_rankToday.getText().toString() + "\n"
                    + "Life expectancy.:" + textView_life_expectancy.getText().toString() + "\n"
                    + "Population:" + textView_population.getText().toString() + "\n"
                    + "---------------------------------------\n\n");
            // закрываем поток
            bw.close();
            Log.d(LOG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void readFile() {
        File file = new File(Environment.getExternalStorageDirectory() + "/NEW_DIR", "Result.txt");
        Uri path = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setDataAndType(path, "text/plain");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d(LOG, "error:" + e);
        }

    }

    void notSuccessful() {
        Log.i(LOG, "http-error of server");
        Toast.makeText(Query1Activity.this, "The error occurred during data, try again!"
                , Toast.LENGTH_LONG).show();

    }

    void Toast_onFailure() {
        Toast.makeText(Query1Activity.this, "The error occurred during networking"
                , Toast.LENGTH_LONG).show();


    }
}
