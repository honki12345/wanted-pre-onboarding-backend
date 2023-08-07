package me.honki12345.wantedassignment.dto;

public record MemberDTO(
        Long id,
        String email,
        String pwd) {
    public static MemberDTO of(String email, String pwd) {
        return new MemberDTO(null, email, pwd);
    }
}
