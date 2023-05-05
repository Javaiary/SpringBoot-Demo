package com.example.demo.repository;

import com.example.demo.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
//    @Query("select m, avg(coalesce(r.grade, 0)), count(distinct r) from Movie m "
//            + "left outer join Review r on r.movie = m group by m")
//    @Query("select m, max(mi), avg(coalesce(r.grade,0)), count(distinct r) from Movie m " +
//            "left outer join MovieImage mi on mi.movie = m " +
//            "left outer join Review r on r.movie = m group by m")
    @Query("Select m, mi, avg(coalesce(r.grade, 0)), count(distinct r) from Movie m " +
        "left outer join MovieImage mi on mi.movie = m " +
        "left outer join Review r on r.movie = m group by m, mi")
    Page<Object[]> getListPage(Pageable pageable); // 페이지 처리

//    @Query("Select m, mi " +
//            "from Movie m left outer join MovieImage mi on mi.movie = m " +
//            "where m.mno = :mno")
    @Query("Select m, mi, avg(coalesce(r.grade, 0)), count(distinct(r)) " +
            " from Movie m left outer join MovieImage mi on mi.movie = m " +
            " left outer join Review r on r.movie = m" +
            " where m.mno = :mno group by mi, m")
    List<Object[]> getMovieWithAll(Long mno);   // 특정 영화 조회
}
