package com.example.demo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.demo.entity.Memo;
import com.example.demo.repository.MemoRepository;

// @SpringBootTest: 스프링 컨테이너 전체를 실행, 실제 DB 연결, 실제 JPA 동작
@SpringBootTest
public class MemoRepositoryTests {
   
   // 스프링이 MemoRepository 객체를 자동 주입. 내가 new 안 해도 됨
   @Autowired
   private MemoRepository memoRepository;

   // Spring이 만든 실제 구현 클래스 이름 확인
   // @Test
   public void testClass() {
      System.out.println(memoRepository.getClass().getName());
   }
   
   // 100개 데이터 삽입
   // @Test
   public void insertDumies() {
      // IntStream.rangeClosed(1, 100): 1부터 100까지 숫자를 생성
      IntStream.rangeClosed(1, 100).forEach(i -> {
         Memo memo = Memo.builder().memoText("Sample...."+i).build();   // 마지막 .build() 쓰면 그 순간 실제 객체가 만들어짐
         memoRepository.save(memo);
      });
   }
   
   // 데이터 조회
   // @Test
   public void testSelect() {
      // 조회할 키 선언
      long mno = 100L;
      
      // 조회에 사용되는 메서드는 findById를 주로 사용함. findById() 반환 타입: Optional<Memo>
      Optional<Memo> result = memoRepository.findById(mno);
      
      if(result.isPresent()) {
         // 해당 Row의 Entity를 리턴받는다
         Memo memo = result.get();
         // toString()을 호출해서 정보를 확인한다
         System.out.println(memo);
      }
   }
   
   // 데이터 수정.. save(entity)를 이용해서 DB에 수정하는데, 만약 entity의 값에 변화가 있다면,
   // update를 해주고, 없다면 아무 작업도 하지 않는다
   // @Test
   public void testUpdate() {
      // 엔티티의 값을 변경한다
      // mno(100L): PK를 직접 넣음
      Memo memo = Memo.builder().mno(100L).memoText("수정된 내용임").build();
      
      System.out.println(memoRepository.save(memo));
   }
   
   // 데이터 삭제
   // @Test
   public void testDelete() {
      Long mno = 100L;
      
      memoRepository.deleteById(mno);
   }
   
   /*
    * 페이징처리해보기
    * 대표적인 API로는 Pageable과 PageRequest 클래스가 있음
    * 
    * Pageable 인터페이스는 목록을 가져와서 내부적으로 지정된(?) 목록수로 나눠서
    * 각 페이지별로 관리를 하는 객체임
    * 
    * 이 객체를 통해서 Page 객체를 리턴받으면, Page 객체 내부에 목록들이 들어가 있음
    * 
    * Pageable 객체는 PageRequest.of()의 static 메서드를 통해서 얻어낼 수 있음
    * 
    * 이때 파라미터로는 페이지 번호와 개수 등을 줘서 페이징 처리를 요청할 수 있음
    * 오버로딩 되어있으며, 파라미터 개수에 따라서 페이징을 처리하는 데 차이가 있음
    */
   
   // @Test
   public void testpageDefault() {
      // 1페이지당 10개씩
      // 페이지의 번호의 시작은 0부터 시작한다
      Pageable pageable = PageRequest.of(0, 10);
      // 최종 처리된 결과를 page가 갖고 있다. 반환 타입: Page<Memo>
      Page<Memo> page = memoRepository.findAll(pageable);   
      System.out.println(page);
      System.out.println(page.getNumber());      // 현재 페이지 번호
      System.out.println(page.getTotalPages());   // 전체 페이지 수
      System.out.println(page.getTotalElements());   // 전체 데이터 개수
      System.out.println(page.getSize());      // 페이지당 데이터 수
      System.out.println(page.hasNext());      // 다음 페이지 존재하는지
      System.out.println(page.isFirst());         // 첫 페이지 뭔지
      
      for(Memo memo : page.getContent()) {   // getContent(): 실제 데이터 리스트
         System.out.println(memo);
      }
   }
   
   
   // @Test
   // 정렬 조건 추가해보기: sort 객체를 이용해서 Pageable 객체를 얻어낼 때 파라미터로 넘긴다
   public void testSort() {
      // sort.by()를 통해서 객체얻기
      // mno 기준 내림차순 정렬
      Sort sort1 = Sort.by("mno").descending();
      // memoText 기준 오름차순 정렬
      Sort sort2 = Sort.by("memoText").ascending();

      // 정렬 합치기
      Sort sortAll = sort1.and(sort2);

      //0 페이지 10개 데이터 sortAll 정렬
      Pageable pageable = PageRequest.of(0, 10, sortAll);
      // 전체 데이터 조회 + 페이징
      Page<Memo> page = memoRepository.findAll(pageable);
      
      page.get().forEach(memo -> {
         System.out.println(memo);
      });
   }
   
   /*
    * 쿼리 메서드: 메서드 자체를 쿼리화하는 것
    * 조건이나 범위를 지정할 때 가끔씩 사용되는 메서드임
    */
   // @Test
   public void testQueryMethod() {
//      // mno BETWEEN 70 AND 80 mno DESC 정렬
//      List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);
//      for(Memo memo : list) {
//         System.out.println(memo);
//      }
      // 0페이지 10개 mno DESC
      Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
      // mno BETWEEN 10 AND 50
      Page<Memo> page = memoRepository.findByMnoBetween(10L, 50L, pageable);
      
      page.get().forEach(memo -> {
         System.out.println(memo);
      });
   }
   
   // Query를 이용한 select test
   // @Test
   public void querySel() {
      List<Memo> list = memoRepository.getListDesc();
      for(Memo memo : list) {
         System.out.println(memo);
      }
   }
   
   // @Test
   public void queryUp() {
       System.out.println(memoRepository.updateMemoText(99L, "바꿈"));
   }
   
   // @Test
   public void queryUp2() {
      Memo memo = Memo.builder().mno(99L).memoText("바꿈2").build();
      System.out.println(memoRepository.updateMemoText2(memo));
   }
   
   // @Test
   public void queryPage() {
      System.out.println(memoRepository.getListWithQuery(50L, PageRequest.of(0,10)).getContent());
   }
   
   @Test
   public void nativeSql() {
      List<Object[]> list = memoRepository.getNativeResult();
      System.out.println(list);
   }
   
}