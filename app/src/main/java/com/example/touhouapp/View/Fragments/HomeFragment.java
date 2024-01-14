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
import com.example.touhouapp.Presenter.HomeFragmentManager;
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

public class HomeFragment extends Fragment implements View.OnClickListener, HomeFragmentManager.OnPickerSelected{
    public static String TAG = "HomeFragment";
    private final int MSG_ADD_LOCAL_MESSAGE = 101;
    private HomeFragmentManager mHomeFragmentManager;
    private ListView personalPageList;
    private PersonalListViewAdapter personalPageAdapter;
    private ArrayList<PersonalPageListBean> personalPageArrayList;
    private View rootView;
    private PaintScaleDialog paintScaleDialog;
    private TextView myView;
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
        mHomeFragmentManager = HomeFragmentManager.getInstance(getContext());
        HomeFragmentManager.createPickerListener(this);
        initView(rootView);
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void initView(View rootView){
        myView = (TextView) rootView.findViewById(R.id.mine_main);
        myView.setOnClickListener(this);
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
                        mHomeFragmentManager.setLivingLocation(view, position);
                        break;
                    case "出生日期":
                        mHomeFragmentManager.setBirthday(view, position);
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

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.mine_main){
            if(paintScaleDialog == null){
                paintScaleDialog = new PaintScaleDialog();
            }
            paintScaleDialog.show(getParentFragmentManager(), "PaintScaleDialog");
        }
    }

    @Override
    public void onSuccessSelected(int pos, String result) {
        if(personalPageArrayList != null) {
            personalPageArrayList.get(pos).setListContent(result);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

