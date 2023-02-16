package com.example.demo.repository;

import com.example.demo.entity.Guestbook;
import com.example.demo.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test   // DB 300개 생성
    public void insertDummies() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title...." + i)
                    .content("Content..." + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }
    @Test   // 300번 modDate , title, content 변경 확인
    public void updateTest() {
        Optional<Guestbook> result = guestbookRepository.findById(300L);    // 존재하는 번호로 테스트
        if (result.isPresent()){
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed Title...");
            guestbook.changeContent("Changed Content...");

            guestbookRepository.save(guestbook);
        }
    }
    @Test   // 단일검색
    public void testQuery1() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending()); // orderby
        QGuestbook qGuestbook = QGuestbook.guestbook;   // 1
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();  //  2 (select * from table)
        BooleanExpression expression = qGuestbook.title.contains(keyword);  //  3
        builder.and(expression);    //  4 (where)
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);    // 5
        /*
        select *from guestbook
        where title like '%1%'
        order by gno desc limit 10;
        */
        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
    @Test   // 다중검색
    public void testQuery2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);    // 1..........
        builder.and(exAll); // 2...............
        builder.and(qGuestbook.gno.gt(0L)); // 3.........
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
        /*
        select * from guestbook g
        where g.title like '%1%'
        or g."content" like '%1%'
        and g.gno > 0
        order by gno desc limit 10;
         */

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    @Test
    public void testQuery3(){
        /*
        select * from guestbook
        where title like '%Title%'
        and "content" like '%...%'
        and writer = 'user1'
        order by gno asc
        limit 50;
         */
        Pageable pageable = PageRequest.of(0, 50, Sort.by("gno").ascending());
        QGuestbook qGuestbook = QGuestbook.guestbook;
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression ex1 = qGuestbook.title.contains("Title");
        BooleanExpression ex2 = qGuestbook.content.contains("...");
        BooleanExpression ex3 = qGuestbook.writer.eq("user1");
        BooleanExpression exAll = ex1.and(ex2).and(ex3);
        builder.and(exAll);
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }
}