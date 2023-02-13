package com.example.demo;

import com.example.demo.entity.Memo;
import com.example.demo.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test   //create
    public void InsertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder()
                    .memoText("Sample....." + i)
                    .build();

            memoRepository.save(memo);

        });


    }

    @Test // read
    public void selectDummies() {

        Long id = 100L;

        Optional<Memo> result = memoRepository.findById(id);

        System.out.println("=================");
        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Test   //update
    public void UpdateDummies() {
        Memo memo = Memo.builder()
                .id(10L)
                .memoText("Update Test")
                .build();
        memoRepository.save(memo);
    }
    @Test
    public void DeleteDummies(){
        Long id = 10L;
        memoRepository.deleteById(id);
    }

    @Test
    public void testPageDefault(){
        // 1페이지 10개
        Pageable pageable = PageRequest.of(0,10);
        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("--------------------------------------");
        System.out.println("Total Pages: " + result.getTotalPages());   //총 몇 페이지
        System.out.println("Total Count: " + result.getTotalElements());    // 전체 개수
        System.out.println("Page Number: " + result.getNumber());   // 현재 페이지 번호
        System.out.println("Page Size: " + result.getSize());     // 페이지당 데이터 개수
        System.out.println("has next page?: " + result.hasNext());  // 다음 페이지
        System.out.println("first page?: " + result.isFirst());     // 시작 페이지(0) 여부

        System.out.println("-------------------------------");
        for (Memo memo:result.getContent()){
            System.out.println(memo);
        }
    }

    @Test
    public void testSort(){
        Sort sort1 = Sort.by("id").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);        // and를 이용한 연결
        Pageable pageable = PageRequest.of(0,10, sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }
    @Test
    public void testQueryMethods() {
        List<Memo> list = memoRepository.findByIdBetweenOrderByIdDesc(70L, 80L);

        for (Memo memo : list){
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPagable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Memo> result = memoRepository.findByIdBetween(10L, 50L, pageable);
        result.get().forEach(memo -> System.out.println(memo));
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods() {
        memoRepository.deleteMemoryByIdLessThan(10L);
    }

}
