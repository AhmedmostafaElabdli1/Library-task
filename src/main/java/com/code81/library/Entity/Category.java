package com.code81.library.Entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"parentCategory", "subCategories", "books"})
@EqualsAndHashCode(exclude = {"parentCategory", "subCategories", "books"})
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // Self-referential hierarchy
// Prevent serializing the parentCategory back up
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    @JsonBackReference
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Category> subCategories = new HashSet<>();



    // Helper method
    public void addSubCategory(Category child) {
        subCategories.add(child);
        child.setParentCategory(this);
    }
}