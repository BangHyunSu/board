package com.study.board.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity { // 시간정보를 다룬다

    @CreationTimestamp // 생성되었을때 시간을 만들어준다
    @Column(updatable = false) //update 할때 관여 안하겠다
    private LocalDateTime createdTime;

    @UpdateTimestamp // 업데이트 되었을때 시간을 만들어준다
    @Column(insertable = false) //insert 할때 관여 안하겠다
    private LocalDateTime updatedTime;

}
