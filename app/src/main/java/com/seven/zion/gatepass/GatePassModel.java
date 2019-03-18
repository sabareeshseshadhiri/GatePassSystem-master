package com.seven.zion.gatepass;

public class GatePassModel {
    String name,dept,from,to,reason;
    private boolean staffApproved,HodApproved,staffDeclined,hodDeclined;

    public GatePassModel() {
    }

    public GatePassModel(String name, String dept, String from, String to, String reason, boolean staffApproved, boolean hodApproved, boolean staffDeclined, boolean hodDeclined) {
        this.name = name;
        this.dept = dept;
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.staffApproved = staffApproved;
        HodApproved = hodApproved;
        this.staffDeclined = staffDeclined;
        this.hodDeclined = hodDeclined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isStaffApproved() {
        return staffApproved;
    }

    public void setStaffApproved(boolean staffApproved) {
        this.staffApproved = staffApproved;
    }

    public boolean isHodApproved() {
        return HodApproved;
    }

    public void setHodApproved(boolean hodApproved) {
        HodApproved = hodApproved;
    }

    public boolean isStaffDeclined() {
        return staffDeclined;
    }

    public void setStaffDeclined(boolean staffDeclined) {
        this.staffDeclined = staffDeclined;
    }

    public boolean isHodDeclined() {
        return hodDeclined;
    }

    public void setHodDeclined(boolean hodDeclined) {
        this.hodDeclined = hodDeclined;
    }
}
