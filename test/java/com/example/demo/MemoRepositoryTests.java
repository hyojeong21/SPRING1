package com.example.demo;

import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Memo;
import com.example.demo.repository.MemoRepository;

@SpringBootTest
public class MemoRepositoryTests {
   
   @Autowired
   private MemoRepository memoRepository;
   
   // @Test
   public void testClass() {
      System.out.println(memoRepository.getClass().getName());
   }
   
   // @Test
   public void insertDumies() {
	   IntStream.rangeClosed(1, 100).forEach(i -> {
		   Memo memo = Memo.builder().memoText("Sample...."+i).build();
		   memoRepository.save(memo);
	   });
   }
   
   // 데이터 조회
   // @Test
   public void testSelect() {
	   // 조회할 키 선언
	   long mno = 100L;
	   
	   // 조회에 사용되는 메서드는 findById를 주로 사용함
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
	   Memo memo = Memo.builder().mno(100L).memoText("수정된 내용임").build();
	   
	   System.out.println(memoRepository.save(memo));
   }
   
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
   
   @Test
   public void testpageDefault() {
	   // 1페이지당 10개씩
	   // 페이지의 번호의 시작은 0부터 시작한다
	   Pageable pageable = PageRequest.of(0, 10);
	   Page<Memo> page = memoRepository.findAll(pageable);	//  최종 처리된 결과를 page가 갖고 있다
	   System.out.println(page);
	   System.out.println(page.getNumber());
	   System.out.println(page.getTotalPages());
	   System.out.println(page.getTotalElements());
	   System.out.println(page.getSize());
	   System.out.println(page.hasNext());
	   System.out.println(page.isFirst());
	   
	   for(Memo memo : page.getContent()) {
		   System.out.println(memo);
	   }
   }
   
}