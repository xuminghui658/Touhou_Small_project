package com.example.touhouapp.Bean;

public class PersonalPageListBean {
    private String listTitle;
    private String listContent;
    private String listHintContent;
    public void setListTitle(String title){
        listTitle = title;
    }
    public String getListTitle(){
        return listTitle;
    }
    public void setListContent(String content){
        listContent = content;
    }
    public PersonalPageListBean(String listTitle){
        this(listTitle,"","请填写");
    }
    public PersonalPageListBean(String listTitle, String listContent){
        this(listTitle,listContent,"请填写");
    }
    public PersonalPageListBean(String listTitle, String listContent, String listHintContent){
        this.listTitle = listTitle;
        this.listContent = listContent;
        this.listHintContent = listHintContent;
    }
    public String getListContent(){
        return listContent;
    }
    public void setListHintContent(String content){
        listHintContent = content;
    }
    public String getListHintContent(){
        return listHintContent;
    }
    public String toString(){
        return "listTitle: " + listTitle + ", listContent: " + listContent + ", listHintContent: " + listHintContent;
    }
}
