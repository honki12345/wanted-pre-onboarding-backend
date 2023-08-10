package me.honki12345.wantedassignment.common;

public class PostNotFoundException extends NotFoundException{
    public PostNotFoundException() {
        super("해당 게시글은 존재하지 않습니다");
    }
}
