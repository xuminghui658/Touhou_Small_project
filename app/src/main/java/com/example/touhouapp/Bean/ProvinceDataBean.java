package com.example.touhouapp.Bean;

public class ProvinceDataBean {
    private String code;
    private String name;
    public ProvinceDataBean(){

    }
    public void setProvinceCode(String provinceCode){
        code = provinceCode;
    }
    public String getProvinceCode(){
        return code;
    }
    public void setProvinceName(String provinceName){
        name = provinceName;
    }
    public String getProvinceName(){
        return name;
    }

    public String toString(){
        return "Province code = " + code + ", ProvinceName = " + name;
    }
}
