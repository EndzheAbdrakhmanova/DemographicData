package com.example.endzhe.rertofit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.endzhe.rertofit.database.AppDatabase;
import com.example.endzhe.rertofit.database.DataDao;
import com.example.endzhe.rertofit.database.DataModel;
import com.example.endzhe.rertofit.location.ActivateLocation;

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
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


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
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 0;

    int myYear=2018;
    String myMonth="01";
    String myDay="01";
    final int DIALOG_CODE_rankToday=3;//rank today
    final int DIALOG_CODE_rank=4;//rank
    final int DIALOG_CODE_life=5;//life exp.
    final int DIALOG_CODE_population=6;//population

    ActivateLocation activateLocation;
    SharedPreferences sharedPreferences;

    String []sex={"male","female"};


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
    AutoCompleteTextView editText_country;

    @BindView(R.id.editText_sex)
    AutoCompleteTextView editText_sex;

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
    protected void onStart() {
        //checkAndRequestGeoPermission();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query1);
        ButterKnife.bind(this);

        //////////////////////////////////////////////////////////////////////////////////////////////////
        //адаптер для заполнения пола
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>
                        (this,R.layout.support_simple_spinner_dropdown_item,sex);
        editText_sex.setAdapter(adapter);


        //////////////////////////////////////////////////////////////////////////////////////////////////
        //получаем массив из ресурса
        String countries[]=getResources().getStringArray(R.array.country);
        List<String> countryList= Arrays.asList(countries);
        ArrayAdapter<String> adapterForCountry =
                new ArrayAdapter<>
                        (this,R.layout.support_simple_spinner_dropdown_item,countryList);
        editText_country.setAdapter(adapterForCountry);
        //////////////////////////////////////////////////////////////////////////////////////////////////


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//возврат на предыдущий activity
            }
        });

        //checkAndRequestGeoPermission();

        loadValue();

    }

    //Обработчик долгого нажатия
    //Добавляет в бд результат 1-ого запроса
    //и открывает новое активити с recyclerView
    @OnLongClick({R.id.btn_getRank,R.id.imageView_dob})
    boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.btn_getRank:
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
                Toast.makeText(context, "Write to Database query 1", Toast.LENGTH_SHORT).show();
                //return true;
            case R.id.imageView_dob:
               // showDialog(1);


        }
        return true;
    }

    protected Dialog onCreateDialog(int id){
        switch (id) {
            case 1:
                DatePickerDialog datePickerDialog1 =
                        new DatePickerDialog(this, myCallBack1, myYear,
                                Integer.parseInt(myMonth), Integer.parseInt(myDay));
                return datePickerDialog1;

            case 2:
                DatePickerDialog datePickerDialog2 =
                        new DatePickerDialog(this, myCallBack2, myYear,
                                Integer.parseInt(myMonth), Integer.parseInt(myDay));
                return datePickerDialog2;

            case DIALOG_CODE_rankToday:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Description")
                        .setMessage("Calculates the world population rank" +
                                " of a person with the given date of birth, " +
                                "sex and country of origin as of today.")
                        .setIcon(R.drawable.search)
                        .setPositiveButton("Ok,thanks",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                return builder.create();
                //break;
            case DIALOG_CODE_rank:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Description")
                        .setMessage("Calculates the world population rank of a person with the given date of birth," +
                                " sex and country of origin on a certain date.")
                        .setIcon(R.drawable.search)
                        .setPositiveButton("Ok,thanks",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                return builder2.create();
            case DIALOG_CODE_population:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setTitle("Description")
                        .setMessage("Determines total population for a given country on a given date." +
                                " Valid dates are 2013-01-01 to 2022-12-31.")
                        .setIcon(R.drawable.search)
                        .setPositiveButton("Ok,thanks",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                return builder3.create();

            case DIALOG_CODE_life:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                builder4.setTitle("Description")
                        .setMessage("Calculate total life expectancy of a person with given sex, country, and date of birth.\n" +
                                "\n" +
                                "Note that this function is implemented based on the remaining life expectancy " +
                                "by picking a reference date based on an age of 35 years." +
                                " It is therefore of limited accuracy.")
                        .setIcon(R.drawable.search)
                        .setPositiveButton("Ok,thanks",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                return builder4.create();
        }

        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack2=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myYear=year;
            int newMonth=month+1;
            myMonth=""+newMonth;
            myDay=""+dayOfMonth;

            //myMonth+1 так как январь = 0
            if(dayOfMonth<10)
                myDay="0"+dayOfMonth;
            if(newMonth<10)
                myMonth="0"+newMonth;
            editText_date.setText(myYear+"-"+myMonth+"-"+myDay);
        }
    };
    DatePickerDialog.OnDateSetListener myCallBack1=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myYear=year;
            int newMonth=month+1;
            myMonth=""+newMonth;
            myDay=""+dayOfMonth;

            //myMonth+1 так как январь = 0
            if(dayOfMonth<10)
                 myDay="0"+dayOfMonth;
            if(newMonth<10)
                myMonth="0"+newMonth;
            editText_dob.setText(myYear+"-"+myMonth+"-"+myDay);
        }
    };




    @OnClick({R.id.btn_getRank, R.id.btn_getRankToday,
            R.id.btn_get_life_expectancy, R.id.btn_get_population,
            R.id.btnWriteFile, R.id.btnReadFile,
            R.id.imageView_country,
            R.id.editText_dob,R.id.editText_date,
            R.id.textView_rankToday,R.id.textView_rank,R.id.textView_population,R.id.textView_life_expectancy})
    void onSaveClick(View v) {
        switch (v.getId()) {
            case R.id.textView_rank:
                showDialog(DIALOG_CODE_rank);
                break;
            case R.id.textView_rankToday:
                showDialog(DIALOG_CODE_rankToday);
                break;
            case R.id.textView_population:
                showDialog(DIALOG_CODE_population);
                break;
            case R.id.textView_life_expectancy:
                showDialog(DIALOG_CODE_life);
                break;
            case R.id.editText_dob:
                showDialog(1);
                break;
            case R.id.editText_date:
                showDialog(2);
                break;
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
                    public void onResponse(Call<LifeExpectancyModel> call,
                                           Response<LifeExpectancyModel> response) {
                        if (response.isSuccessful()) {
                            LifeExpectancyModel body = response.body();
                            Float life_expectancy = body.getTotal_life_expectancy();
                            //округление
                            String  answer = String.valueOf((int)Math.round(life_expectancy));
                            //String answer2=String.valueOf(answer);
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
            case R.id.imageView_country:
                checkAndRequestGeoPermission();
                Toast.makeText(this, "Use geolocation", Toast.LENGTH_SHORT).show();
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
                    + "Life expectancy:" + textView_life_expectancy.getText().toString() + "\n"
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

    /**
     * Работа с геолокацией, геокодирование используя класс Geocoder.
     */

    /**
     * Метод проверяет есть ли разрешение и запрашивает его, если нет.
     * Если разрешение есть, то создает объект класса ActivateLocation
     */
    public  void checkAndRequestGeoPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            activateLocation=new ActivateLocation(this);
            String countryName=activateLocation.getCountryName();
            editText_country.setText(countryName);
        }
    }

    /**
     * Решение пользователя получим в методе onRequestPermissionsResult.
     * @param requestCode-идентификатор. Используется, чтобы отличать друг от друга пришедшие результаты
     * @param permissions-массив названий разрешений,которые мы запросили
     * @param grantResults-ответ пользователя
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            //массив ответов не пустой и берем 1 результат из него,т.к. запросили 1 результат.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                activateLocation=new ActivateLocation(this);
                String countryName=activateLocation.getCountryName();
                editText_country.setText(countryName);

            } else {
                // Нет гео
                // Попробуем показать ещё раз
                checkAndRequestGeoPermission();
            }
        }
    }

    @Override
    protected void onDestroy() {
//        activateLocation.unregisterLocationListener();
        Log.v(LOG,"Отписка от изменений геопозиции");
        saveValue();
        super.onDestroy();

    }

    void saveValue(){
        sharedPreferences=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("dob",editText_dob.getText().toString());
        editor.putString("date",editText_date.getText().toString());
        editor.putString("country",editText_country.getText().toString());
        editor.putString("sex",editText_sex.getText().toString());

        editor.putString("life_expectancy",textView_life_expectancy.getText().toString());
        editor.putString("population",textView_population.getText().toString());
        editor.putString("rank",textView_rank.getText().toString());
        editor.putString("rankToday",textView_rankToday.getText().toString());
        editor.apply();
        Log.v(LOG,"Text saved");
    }

    void loadValue(){
        sharedPreferences=getPreferences(MODE_PRIVATE);
        String dob=sharedPreferences.getString("dob","");
        editText_dob.setText(dob);

        String date=sharedPreferences.getString("date","");
        editText_date.setText(date);

        String country=sharedPreferences.getString("country","");
        editText_country.setText(country);

        String sex=sharedPreferences.getString("sex","");
        editText_sex.setText(sex);

        String population=sharedPreferences.getString("population","");
        textView_population.setText(population);

        String life_expectancy=sharedPreferences.getString("life_expectancy","");
        textView_life_expectancy.setText(life_expectancy);

        String rank=sharedPreferences.getString("rank","");
        textView_rank.setText(rank);

        String rankToday=sharedPreferences.getString("rankToday","");
        textView_rankToday.setText(rankToday);

        Log.v(LOG,"Text loaded");

    }

    @Override
    protected void onResume() {


        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("last activity",getClass().getSimpleName());
        editor.apply();
        super.onResume();
    }
}



