package com.example.touhouapp.Bean;

public class CityDataBean {
        private String code;
        private String name;
        private String provinceCode;
        public CityDataBean(){

        }
        public void setCityCode(String cityCode){
            code = cityCode;
        }
        public String getCityCode(){
            return code;
        }
        public void setCityName(String cityName){
            name = cityName;
        }
        public String getCityName(){
            return name;
        }
        public void setProvinceCode(String ProvinceCode){
            provinceCode = ProvinceCode;
        }
        public String getProvinceCode(){
            return provinceCode;
        }

        public String toString(){
            return "City code = " + code + ", CityName = " + name + ", Province code = " + provinceCode;
        }
}
