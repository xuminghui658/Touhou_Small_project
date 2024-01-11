package com.example.touhouapp.Bean;

public class AreaDataBean {
    private String code;
    private String name;
    private String cityCode;
    private String provinceCode;
    public AreaDataBean(){

    }
    public void setAreaCode(String areaCode){
        code = areaCode;
    }
    public String getAreaCode(){
        return code;
    }
    public void setAreaName(String areaName){
        name = areaName;
    }
    public String getAreaName(){
        return name;
    }
    public void setCityCode(String CityCode){
        cityCode = CityCode;
    }
    public String getCityCode(){
        return cityCode;
    }
    public void setProvinceCode(String ProvinceCode){
        provinceCode = ProvinceCode;
    }
    public String getProvinceCode(){
        return provinceCode;
    }

    public String toString(){
        return "City code = " + code + ", CityName = " + name + ", City code = " + cityCode + ", Province code = " + provinceCode;
    }
}
