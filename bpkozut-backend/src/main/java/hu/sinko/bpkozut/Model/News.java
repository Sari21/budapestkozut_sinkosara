package hu.sinko.bpkozut.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
public class News {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @NotNull
    private String title;
    private String image;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime created;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updated;
    @NotNull
    @Column(name = "content", length = 2048)
    private String content;
    private String author;

    public News(String title, String image, LocalDate created, LocalDate updated, String content, String author) {
        this.title = title;
        this.image = image;
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
        this.content = content;
        this.author = "Sari";
    }

    public News() {
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
        this.author = "Sari";
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
