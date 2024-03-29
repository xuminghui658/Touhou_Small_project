package com.example.touhouapp.Presenter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.touhouapp.Base.TouHouApplication;
import com.example.touhouapp.Bean.AreaDataBean;
import com.example.touhouapp.Bean.CityDataBean;
import com.example.touhouapp.Bean.ProvinceDataBean;
import com.example.touhouapp.R;
import com.example.touhouapp.Utils.JsonReaderUtil;
import com.example.touhouapp.View.Fragments.PaintScaleDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 个人信息页面，点击触发选择项时的逻辑在这里
 */
public class HomeFragmentManager {
    private static final String TAG = "HomeFragmentManager";
    private static HomeFragmentManager instance;
    private static Context context;
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
    private static OnPickerSelected PickerListener;

    public interface OnPickerSelected{
        void onSuccessSelected(int pos, String result);
    }

    public static void createPickerListener(OnPickerSelected listener){
        PickerListener = listener;
    }

    private HomeFragmentManager(Context con){
        if(context == null){
            context = con;
        }
    }

    public static HomeFragmentManager getInstance(Context context){
        if(instance == null){
            instance = new HomeFragmentManager(context);
        }
        return instance;
    }

    public void setLivingLocation(View view, int pos){
        //省份数据解析
        provinceJson = JsonReaderUtil.getJson(context, "provinces.json");
        parseJson(provinceJson, "provinces");
        cityJson = JsonReaderUtil.getJson(context, "cities.json");
        parseJson(cityJson, "cities");
        areaJson = JsonReaderUtil.getJson(context, "areas.json");
        parseJson(areaJson, "areas");
        //修改为pickerView可用的标准
        ArrayList<String> tempCityList;
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
        LocalPickerView = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
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
                if(PickerListener != null){
                    PickerListener.onSuccessSelected(pos, resultLocation);
                }

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
    }

    public void setBirthday(View view, int pos){
        if(formalYears.size() == 0){
            initDateSource();
        }
        DatePickerView = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
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
                if(PickerListener != null){
                    PickerListener.onSuccessSelected(pos, resultDate);
                }
                TextView textView = view.findViewById(R.id.personal_page_message_detail);
                textView.setText(resultDate);
            }
        }).setTitleText("出生日期")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20).build();
        DatePickerView.setPicker(formalYears, formalMonths, formalDays);//三级选择器
        DatePickerView.show();
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
}
