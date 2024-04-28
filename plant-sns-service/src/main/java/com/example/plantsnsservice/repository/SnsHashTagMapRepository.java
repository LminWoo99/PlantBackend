package com.example.plantsnsservice.repository;

import com.example.plantsnsservice.domain.entity.HashTag;
import com.example.plantsnsservice.domain.entity.SnsHashTagMap;
import com.example.plantsnsservice.domain.entity.SnsPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnsHashTagMapRepository extends JpaRepository<SnsHashTagMap, Long> {
    List<SnsHashTagMap> findAllBySnsPost(SnsPost snsPost);
    List<SnsHashTagMap> findAllByHashTag(HashTag hashTag);
    void deleteBySnsPostId(Long snsPostId);

    //N+1 방지
    @Query("SELECT h FROM SnsHashTagMap m JOIN m.hashTag h GROUP BY h.id, h.name ORDER BY COUNT(m) DESC")
    Slice<HashTag> findTop10SnsHashTag(Pageable pageable);


}
