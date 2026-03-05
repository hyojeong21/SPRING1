package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 이 객체는 Entity(테이블) 객체로 사용할거야 라는 선언을 한다. 이 클래스가 JPA 엔티티 클래스라는 뜻이다. 즉, 이 객체는 DB 테이블과 1:1 매핑되는 클래스로, JPA가 이 클래스를 보고 테이블을 생성하거나 조회함
@Entity

// 이 엔티티가 매핑될 테이블 이름을 지정
@Table(name = "tbl_memo")

// Lombok이 자동으로 getter, setter, toString(), equals(), hashCode() 를 만들어줌
@Data

// 모든 필드를 매개변수로 받는 생성자 생성
@AllArgsConstructor

// 기본 생성자 생성
@NoArgsConstructor

// 빌더 패턴 생성
// 빌더 패턴: 생성자를 직접 호출하지 않고 단계적으로 값을 설정 후 마지막에 build()로 객체 생성
@Builder

public class Memo {
// 엔티티 객체는 반드시 PK를 가지고 있어야 함
   @Id   // 이 필드가 Primary Key (기본키) 라는 뜻
   @GeneratedValue(strategy = GenerationType.IDENTITY)   // autoincrement로 증가하라는 의미임 (기본키 자동 증가 설정)
   private Long mno;
   
   // 컬럼을 추가한다 (VARCHAR(200), NOT NULL)
   @Column(length = 200, nullable = false)
   private String memoText;
}