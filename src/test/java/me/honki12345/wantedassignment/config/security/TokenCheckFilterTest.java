package me.honki12345.wantedassignment.config.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenCheckFilterTest {
    @DisplayName("API 정규표현식 파싱을 한다")
    @Test
    void parsingWithRegex() {
        // Given
        String path1 = "/posts/2";
        String path2 = "/posts/";
        String path3 = "/posts";
        String path4 = "/posts/223232";
        String path5 = "/posts/22323e";

        // When // Then
        String regex = "/posts/\\d+";
        Assertions.assertThat(path1.matches(regex)).isTrue();
        Assertions.assertThat(path2.matches(regex)).isFalse();
        Assertions.assertThat(path3.matches(regex)).isFalse();
        Assertions.assertThat(path4.matches(regex)).isTrue();
        Assertions.assertThat(path5.matches(regex)).isFalse();
    }

}