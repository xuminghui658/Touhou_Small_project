package com.example.touhouapp.View.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.Bean.AreaDataBean;
import com.example.touhouapp.Bean.CityDataBean;
import com.example.touhouapp.Bean.PersonalPageListBean;
import com.example.touhouapp.Bean.ProvinceDataBean;
import com.example.touhouapp.R;
import com.example.touhouapp.Utils.JsonReaderUtil;
import com.example.touhouapp.View.Adapters.PersonalListViewAdapter;
import com.example.touhouapp.View.SelfView.PaintScale;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{
    public static String TAG = "HomeFragment";
    private final int MSG_ADD_LOCAL_MESSAGE = 101;
    private PaintScale paintScale;
    private Spinner colorSpinner;
    private Spinner widthSpinner;
    private RadioButton clearPaint;
    private RadioButton saveCanvas;
    private ListView personalPageList;
    private PersonalListViewAdapter personalPageAdapter;
    private ArrayList<PersonalPageListBean> personalPageArrayList;
    private View rootView;
    private OptionsPickerView LocalPickerView;
    private OptionsPickerView DatePickerView;
    //  省份
    private String provinceJson;
    private final ArrayList<String> formalProvinces = new ArrayList<>();
    private ArrayList<ProvinceDataBean> provinceList = new ArrayList<>();
    //  城市
    private String cityJson;
    private final ArrayList<ArrayList<String>> formalCities = new ArrayList<>();
    private ArrayList<CityDataBean> cityList = new ArrayList<>();
    //  区/县
    private String areaJson;
    private final ArrayList<ArrayList<ArrayList<String>>> formalAreas = new ArrayList<>();
    private ArrayList<AreaDataBean> areaList = new ArrayList<>();
    //地区最终选择结果
    private String resultLocation;
    //出生年月日
    private final ArrayList<String> formalYears = new ArrayList<>();
    private final ArrayList<ArrayList<String>> formalMonths = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<String>>> formalDays = new ArrayList<>();
    private String resultDate;
    private static final String[] LIST_TITLE = new String[]{"昵称","id","性别","出生日期","现居住地","邮箱"};
    public static boolean isPermissionOk = false;

    public Handler mhandler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case MSG_ADD_LOCAL_MESSAGE:

                    break;
                default:
                    break;
            }
        }
    };

    public static void setPermissionState(boolean PermissionOk){
        isPermissionOk = PermissionOk;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home,container,false);
        initView(rootView);
        paintScale = new PaintScale(getContext());
        initSpinnerListener();
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void initView(View rootView){
        colorSpinner = (Spinner)rootView.findViewById(R.id.paint_color);
        widthSpinner = (Spinner)rootView.findViewById(R.id.paint_width);
        clearPaint = (RadioButton) rootView.findViewById(R.id.paint_clear);
        clearPaint.setOnClickListener(this);
        saveCanvas = rootView.findViewById(R.id.canvas_save);
        saveCanvas.setOnClickListener(this);
        /**
         * 个人信息这部分的思路：
         * 用户的昵称、id等信息，只要用户输入过就会存放在数据库(MySQL或后端)，这里初始化页面时
         * PersonalPageListBean会逐条查询数据库对应用户的个人信息，把信息放入arrayList
         * 考虑到是耗时操作，这里肯定要新开个线程，在全部查询完成后再页面展示，需要AsyncTask加handler
         */
        personalPageList = (ListView) rootView.findViewById(R.id.personal_message_page);
        initListData();
        personalPageAdapter = new PersonalListViewAdapter(getActivity().getApplicationContext(), R.layout.fragment_home_personal_page_adapter, personalPageArrayList);
        personalPageList.setAdapter(personalPageAdapter);
        personalPageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = personalPageArrayList.get(position).getListTitle();
                switch (title){
                    case "现居住地":
                        selectLocation(view, position);
                        break;
                    case "出生日期":
                        setBirthday(view, position);
                        break;
                }
            }
        });
    }

    private void initListData(){
        personalPageArrayList = new ArrayList<PersonalPageListBean>();
        for(String title : LIST_TITLE){
            PersonalPageListBean bean;
            TouHouApplication.d(TAG,"current title = " + title);
            switch (title){
                case "昵称":
                    bean = new PersonalPageListBean(title,"默认id");
                    break;
                case "id":
                    bean = new PersonalPageListBean(title,"10086");
                    break;
                case "性别":
                    bean = new PersonalPageListBean(title,"");
                    break;
                default:
                    bean = new PersonalPageListBean(title);
                    break;
            }
            personalPageArrayList.add(bean);
        }
    }

    private String selectLocation(View view, int pos){
        String result = "";
        result = changeCurrentLocation(view, pos);
        return result;
    }
    private String changeCurrentLocation(View view, int pos){
        //省份数据解析
        provinceJson = JsonReaderUtil.getJson(getContext(), "provinces.json");
        parseJson(provinceJson, "provinces");
        cityJson = JsonReaderUtil.getJson(getContext(), "cities.json");
        parseJson(cityJson, "cities");
        areaJson = JsonReaderUtil.getJson(getContext(), "areas.json");
        parseJson(areaJson, "areas");
        //修改为pickerView可用的标准
        ArrayList<String>tempCityList;
        ArrayList<String>tempAreaList;
        ArrayList<ArrayList<String>>tempAreaSecondList;
        int cityCount = 0;
        int areaCount = 0;//循环优化，因为json数据是按顺序给的，把循环次数从a*b*c优化到a+b+c
        if(formalProvinces.size() == 0) {
            for (ProvinceDataBean provinceBean : provinceList) {
                String provinceName = provinceBean.getProvinceName();
                String provinceCode = provinceBean.getProvinceCode();
                tempCityList = new ArrayList<>();
                tempAreaSecondList = new ArrayList<>();
                TouHouApplication.d(TAG, "provinceName = " + provinceName);
                for (int i = cityCount; i < cityList.size(); i++) {
                    CityDataBean cityBean = cityList.get(i);
                    String cityName = cityBean.getCityName();
                    String cityCorrespondingProvinceCode = cityBean.getProvinceCode();
                    String cityCode = cityBean.getCityCode();
                    tempAreaList = new ArrayList<>();
                    if (cityCorrespondingProvinceCode.equals(provinceCode)) {
                        //省市位置对应
                        TouHouApplication.d(TAG, "cityName = " + cityName);
                        tempCityList.add(cityName);
                        cityCount++;
                        for (int j = areaCount; j < areaList.size(); j++) {
                            AreaDataBean areaBean = areaList.get(j);
                            String areaName = areaBean.getAreaName();
                            String areaCorrespondingCityCode = areaBean.getCityCode();
                            if (areaCorrespondingCityCode.equals(cityCode)) {
                                //市县位置对应
                                TouHouApplication.d(TAG, "areaName = " + areaName);
                                tempAreaList.add(areaName);
                                areaCount++;
                            } else {
                                break;
                            }
                        }
                        tempAreaSecondList.add(tempAreaList);
                    } else {
                        break;
                    }
                }
                formalProvinces.add(provinceName);//当前省份
                formalCities.add(tempCityList);//当前省份对应城市列表
                formalAreas.add(tempAreaSecondList);//当前省份下，所有城市对应区县列表
            }
        }
        LocalPickerView = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String opt1tx = formalProvinces.size() > 0 ?
                        formalProvinces.get(options1) : "";

                String opt2tx = formalCities.size() > 0
                        && formalCities.get(options1).size() > 0 ?
                        formalCities.get(options1).get(options2) : "";

                String opt3tx = formalAreas.size() > 0
                        && formalAreas.get(options1).size() > 0
                        && formalAreas.get(options1).get(options2).size() > 0 ?
                        formalAreas.get(options1).get(options2).get(options3) : "";

                resultLocation = opt1tx + "-" + opt2tx + "-" + opt3tx;
                TouHouApplication.d(TAG,"selected location result = " + resultLocation);
                //在这里更新数据
                personalPageArrayList.get(pos).setListContent(resultLocation);
                TextView textView = view.findViewById(R.id.personal_page_message_detail);
                textView.setText(resultLocation);
            }
        }).setTitleText("现居住地")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        LocalPickerView.setPicker(formalProvinces, formalCities, formalAreas);//三级选择器
        LocalPickerView.show();
        return resultLocation;
    }

    public void parseJson(String jsonArrayString, String dataSource) {
        //利用Gson进行解析
        Gson gson = new Gson();
        switch (dataSource) {
            case "provinces":
                Type provinceListType = new TypeToken<List<ProvinceDataBean>>() {}.getType();
                provinceList = gson.fromJson(jsonArrayString, provinceListType);
                break;
            case "cities":
                Type cityListType = new TypeToken<List<CityDataBean>>() {}.getType();
                cityList = gson.fromJson(jsonArrayString, cityListType);
                break;
            case "areas":
                Type areaListType = new TypeToken<List<AreaDataBean>>() {}.getType();
                areaList = gson.fromJson(jsonArrayString, areaListType);
                break;
        }
    }

    private String setBirthday(View view, int pos){
        String result = "";
        if(formalYears.size() == 0){
            initDateSource();
        }
        DatePickerView = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String opt1tx = formalYears.size() > 0 ?
                        formalYears.get(options1) : "";

                String opt2tx = formalMonths.size() > 0
                        && formalMonths.get(options1).size() > 0 ?
                        formalMonths.get(options1).get(options2) : "";

                String opt3tx = formalDays.size() > 0
                        && formalDays.get(options1).size() > 0
                        && formalDays.get(options1).get(options2).size() > 0 ?
                        formalDays.get(options1).get(options2).get(options3) : "";

                resultDate = opt1tx + "-" + opt2tx + "-" + opt3tx;
                TouHouApplication.d(TAG,"selected date result = " + resultDate);
                //在这里更新数据
                personalPageArrayList.get(pos).setListContent(resultDate);
                TextView textView = view.findViewById(R.id.personal_page_message_detail);
                textView.setText(resultDate);
            }
        }).setTitleText("出生日期")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20).build();
        DatePickerView.setPicker(formalYears, formalMonths, formalDays);//三级选择器
        DatePickerView.show();
        return result;
    }
    private void initDateSource(){
        Calendar calendar = Calendar.getInstance();
        int years = calendar.get(Calendar.YEAR);
        int months = calendar.get(Calendar.MONTH) + 1; // 月份是从0开始的，所以需要加1
        int days = calendar.get(Calendar.DAY_OF_MONTH);
        ArrayList<String> tempMonthList;
        ArrayList<String> tempDayList;
        ArrayList<ArrayList<String>> tempDaySecondList;
        for(int i = (years - 100); i <= years; i++){
            String currentYear = i + "年";
            formalYears.add(currentYear);
            tempMonthList = new ArrayList<>();
            tempDaySecondList = new ArrayList<>();
            int month = (i == years) ? months : 12;
            for(int j = 1; j <= month; j++){
                String currentMonth = j + "月";
                tempMonthList.add(currentMonth);
                tempDayList = new ArrayList<>();
                switch (j){
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        int bigDay = (i == years && j == months) ? days : 31;
                        for(int k = 1; k <= bigDay; k++){
                            String currentDay = k + "日";
                            tempDayList.add(currentDay);
                        }
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        int smallDay = (i == years && j == months) ? days : 30;
                        for(int k = 1; k <= smallDay; k++){
                            String currentDay = k + "日";
                            tempDayList.add(currentDay);
                        }
                        break;
                    case 2:
                        int day = (i % 4 == 0) ? 29 : 28;
                        int smoothDay = (i == years && j == months) ? days : day;
                        for(int k = 1; k <= smoothDay; k++){
                            String currentDay = k + "日";
                            tempDayList.add(currentDay);
                        }
                        break;
                }
                tempDaySecondList.add(tempDayList);
            }
            formalDays.add(tempDaySecondList);
            formalMonths.add(tempMonthList);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
    }

    private void initSpinnerListener(){
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaintScale scale = (PaintScale) getActivity().findViewById(R.id.paintScale1);
                String selected = parent.getItemAtPosition(position).toString();
                scale.paint.setXfermode(null);
                switch (selected){
                    case "黑色":
                        TouHouApplication.d(TAG,"current color = 黑色");
                        paintScale.setColor(selected);
                        scale.paint.setColor(Color.BLACK);
                        break;
                    case "红色":
                        TouHouApplication.d(TAG,"current color = 红色");
                        paintScale.setColor(selected);
                        scale.paint.setColor(Color.RED);
                        break;
                    case "绿色":
                        TouHouApplication.d(TAG,"current color = 绿色");
                        paintScale.setColor(selected);
                        scale.paint.setColor(Color.GREEN);
                        break;
                    case "蓝色":
                        TouHouApplication.d(TAG,"current color = 蓝色");
                        paintScale.setColor(selected);
                        scale.paint.setColor(Color.BLUE);
                        break;
                }
                colorSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        widthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaintScale scale = (PaintScale) getActivity().findViewById(R.id.paintScale1);
                String selected = parent.getItemAtPosition(position).toString();
                scale.paint.setXfermode(null);
                scale.paint.setStrokeWidth(8);
                switch (selected){
                    case "细线":
                        TouHouApplication.d(TAG,"current color = 细线");
                        paintScale.setWidth(selected);
                        scale.paint.setStrokeWidth(4);
                        break;
                    case "中线":
                        TouHouApplication.d(TAG,"current color = 中线");
                        paintScale.setWidth(selected);
                        scale.paint.setStrokeWidth(8);
                        break;
                    case "粗线":
                        TouHouApplication.d(TAG,"current color = 粗线");
                        paintScale.setWidth(selected);
                        scale.paint.setStrokeWidth(12);
                        break;
                }
                colorSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        PaintScale scale = (PaintScale) getActivity().findViewById(R.id.paintScale1);
        switch (v.getId()){
            case R.id.paint_clear:
                scale.paint.setColor(Color.WHITE);
                scale.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                scale.paint.setStrokeWidth(50);
                paintScale.clear();
                break;
            case R.id.canvas_save:
                try {
                    scale.save("PaintPicture");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}

