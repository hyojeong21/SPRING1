package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Memo;

// JPARepository를 구현하기만 해도 해당 메서드를 이용할 수 있다
// JpaRepository를 상속하면 기본 CRUD 기능이 자동 생성
// 제네릭 타입으로 어떤 엔티티를 대상으로 해당 엔티티의 PK 타입을 제네릭으로 지정해줘야 한다
// JpaRepository<Memo, Long> 이 부분에서 Memo: 엔티티 클래스, Long: 기본키 타입
public interface MemoRepository extends JpaRepository<Memo, Long> {

   // 메서드 쿼리를 사용한 select 해보기
   // mno 값이 from ~ to 사이인 데이터를 조회
   List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
   
   // Pageable과 결합해서 조회한 후 Page 객체를 넘길 수도 있음
   // Page<Memo>: 페이징된 결과
   // Pageable pageable: 페이지 나누기 정보
   Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
   
   // Query(): 내가 원하는 쿼리를 직접 정의해서 사용하는 형태
   // Memo → Entity 클래스, m → 별칭, m.mno → Memo의 mno 필드
   @Query("Select m from Memo m order by m.mno")
   // getListDesc(): 데이터를 데이터베이스로부터 내림차순으로 정렬하여 목록을 가져옴
   List<Memo> getListDesc();
   
   // DB 작업을 하나의 작업으로 묶기
   @Transactional   
   // JPQL은 기본적으로 SELECT만 가능하기 때문에 UPDATE나 DELETE는 실행이 안 돼서 추가
   @Modifying
   // 특정 번호의 memoText 수정
   // :memoText: 값을 메서드에서 받음, @Param("mno") Long mno: JPQL의 :mno와 연결
   @Query("update Memo m set m.memoText=:memoText where m.mno=:mno")
   int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);
   
   @Transactional
   @Modifying
   // :#{#param.memoText}: param.memoText 값 가져오기, :#{#param.mno}: param.mno 값 가져오기
   @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
   int updateMemoText2(@Param("param") Memo memo);
   
   @Query(value = "Select m from Memo m where m.mno > :mno",
         countQuery = "select count(m) from Memo m where m.mno > :mno")
   Page<Memo> getListWithQuery(@Param("mno") Long mno, Pageable pageable);
   
   // Native sql 처리: 일반 쿼리를 직접 수행하도록 하는 설정
   @Query(value = "Select * from tbl_memo where mno > 0", nativeQuery = true)
   // 위처럼 쿼리를 수행할 경우에는 List<Object[]> 형태로 리턴됨
   List<Object[]> getNativeResult();
   
}