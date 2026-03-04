package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Memo;

//JPARepository를 구현하기만 해도 해당 메서드를 이용할 수 있다
//제네릭 타입으로 어떤 엔티티를 대상으로 해당 엔티티의 PK 타입을 제네릭으로 지정해줘야 한다
public interface MemoRepository extends JpaRepository<Memo, Long> {

}