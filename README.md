


# Database Design
![Untitled Diagram drawio](https://github.com/user-attachments/assets/31ec784d-b435-4ab8-b74e-c7f6e0c483f0)


### 1. Book Entity Relationships — Summary Table**
`Book` entity with the corresponding JPA annotations and descriptions:

| Related Entity        | Relation Type                  | Owning Side       | JPA Annotation in Book Entity                                                                                                                      | Description                                                       |
| --------------------- | ------------------------------ | ----------------- | -------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------- |
| **Category**          | Many-to-One                    | Book              | `@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "category_id")`                                                                             | Each book belongs to one category                                 |
| **Publisher**         | Many-to-One                    | Book              | `@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "publisher_id")`                                                                            | Each book has one publisher                                       |
| **Author**            | Many-to-Many                   | Book              | `@ManyToMany @JoinTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))` | Books can have multiple authors; authors can write multiple books |
| **BorrowTransaction** | One-to-Many (optional inverse) | BorrowTransaction | (usually mapped on `BorrowTransaction` side as Many-to-One) Optional inverse in Book: `@OneToMany(mappedBy = "book")`                              | One book can have many borrow transactions                        |

---

### Notes:

- **Owning Side** means which entity contains the foreign key or owns the relationship in the database.
    
- `Book` owns the **ManyToOne** relations with `Category` and `Publisher`.
    
- `Book` is the owning side of **ManyToMany** with `Author` via join table `book_authors`.
    
- For `BorrowTransaction`, `Book` is usually the **inverse side** (mapped by `book` in `BorrowTransaction`), but you can add the inverse list in `Book` if needed.
    

---

### 2. **User Entity Relationships — Summary Table**

| Related Entity | Relation Type     | Owning Side | JPA Annotation Example in User Entity | Description                      |
| -------------- | ----------------- | ----------- | ------------------------------------- | -------------------------------- |
| **Role**       | Enum (not entity) | N/A         | `@Enumerated(EnumType.STRING)`        | User has one role stored as Enum |



## Note 

- I did all relation  as unidirectional , because avoid circular problem like A-> B ->A ->B



# Why i don't like  you use Sqlscripte For Sample Data ?


- **Auditing fields are auto-managed by JPA:**  
    With annotations like `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy`, and `@LastModifiedBy`, JPA automatically populates those fields when you persist or update entities.  
    **Manually inserting those fields via SQL scripts can cause inconsistency or be error-prone.**
    
- **Database independence:**
    When using SQL scripts, you need to tailor timestamps and user info manually, which might be error-prone and less portable across environments.



## I handled Error By Custom Exceptions I created its in Exception Folder like :

- Resource not found like when get user by id , if db not contain it , app pass exception this user not found
  
  
  
  
# Roles 


## **which roles can perform which actions** on each controller/entity in your library system, based on the annotations and method restrictions in your controllers:

| **Entity / Feature**  | **Actions**                   | **ADMINISTRATOR** | **LIBRARIAN** | **STAFF**   | **Notes**                                                                             |
| --------------------- | ----------------------------- | ----------------- | ------------- | ----------- | ------------------------------------------------------------------------------------- |
| **User**              | Create user                   | ✔                 | ✘             | ✘           | Only ADMINISTRATOR can create users                                                   |
|                       | View all users                | ✔                 | ✘             | ✘           | Only ADMINISTRATOR                                                                    |
|                       | View user by ID               | ✔                 | ✘             | ✘           | Only ADMINISTRATOR                                                                    |
|                       | View users by role            | ✔                 | ✘             | ✘           | Only ADMINISTRATOR                                                                    |
|                       | Update user                   | ✔                 | ✘             | ✘           | Only ADMINISTRATOR                                                                    |
|                       | Delete user                   | ✔                 | ✘             | ✘           | Only ADMINISTRATOR                                                                    |
| **Author**            | Create author                 | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | View all authors              | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | View author by ID             | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | Update author                 | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | Delete author                 | ✔                 | ✘             | ✘           | ADMINISTRATOR only                                                                    |
| **Book**              | Create book                   | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | View all books                | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | View book by ID               | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | Update book                   | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | Delete book                   | ✔                 | ✘             | ✘           | ADMINISTRATOR only                                                                    |
|                       | View books by author          | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | View books by publisher       | ✔                 | ✔             | ✔           | All roles                                                                             |
| **Borrower**          | Create borrower               | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | View all borrowers            | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | View borrower by ID           | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | Update borrower               | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | Delete borrower               | ✔                 | ✘             | ✘           | ADMINISTRATOR only                                                                    |
| **BorrowTransaction** | Borrow book                   | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | Return book                   | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | View all transactions         | ✔                 | ✔             | ✔ (limited) | STAFF can view transactions but may have limited access (e.g., no completed returned) |
|                       | View transactions by borrower | ✔                 | ✔             | ✔ (limited) | Same limitation as above                                                              |
|                       | Delete transaction            | ✘ (not allowed)   | ✘             | ✘           | Deletion forbidden                                                                    |
| **Category**          | Create category               | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | View all categories           | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | View category by ID           | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | Update category               | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | Delete category               | ✔                 | ✘             | ✘           | ADMINISTRATOR only                                                                    |
| **Publisher**         | Create publisher              | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | View all publishers           | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | View publisher by ID          | ✔                 | ✔             | ✔           | All roles                                                                             |
|                       | Update publisher              | ✔                 | ✔             | ✘           | ADMINISTRATOR and LIBRARIAN                                                           |
|                       | Delete publisher              | ✔                 | ✘             | ✘           | ADMINISTRATOR only                                                                    |


- **ADMINISTRATOR** has **full access** to all operations across all entities.
    
- **LIBRARIAN** has **create/read/update** access to most entities but **cannot delete users, authors, borrowers, publishers, categories, or books**.
    
- **STAFF** has mostly **read-only** access to entities like authors, books, categories, publishers, borrowers, and limited view of borrow transactions.
    
## **Delete operations** are very restricted, mostly allowed only by ADMINISTRATOR.
    
- **Borrow and Return book operations** are allowed for ADMINISTRATOR and LIBRARIAN; STAFF can only return books and view transactions with restrictions.
    



# How RUN Application 


### -First Create Author , Publisher, category 
### - Then Create Book
## For Borrow Transaction

### -Create Borrow
### - then Make Transaction if your role allow that

# I provided Postman Collections in Repository
