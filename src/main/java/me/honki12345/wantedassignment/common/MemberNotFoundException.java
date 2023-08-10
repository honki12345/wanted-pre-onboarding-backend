package me.honki12345.wantedassignment.common;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException() {
        super("해당 유저를 찾을 수 없습니다");
    }
}
