package com.seven.zion.gatepass;

public class memberModel {

    String name,registerNo,memberType;

    public memberModel() { }

    public memberModel(String name, String registerNo, String memberType) {
        this.name = name;
        this.registerNo = registerNo;
        this.memberType = memberType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }
}
