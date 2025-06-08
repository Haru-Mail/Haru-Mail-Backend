package com.project.Haru_Mail.domain.category;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 카테고리 ID

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String name; // 카테고리명
}
