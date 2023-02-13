package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_memo")   // 테이블 이름 저장
@Entity
public class Memo {
    // tbl_memo 의 컬럼 설정

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //postgre의 AUTO_INCREMENT를 사용
    private Long id;

    @Column(length = 200, nullable = false)
    private String memoText;
}