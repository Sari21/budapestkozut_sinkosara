package hu.sinko.bpkozut.Service;

import hu.sinko.bpkozut.Model.News;
import hu.sinko.bpkozut.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public NewsService(NewsRepository newsRepository, FileStorageService fileStorageService) {
        this.newsRepository = newsRepository;
        this.fileStorageService = fileStorageService;
    }

    public News addNews(News news) throws IOException {
        return newsRepository.save(news);
    }

    public long getNumberOfNews() {
        return newsRepository.count();
    }

    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    public void deleteNewsById(Long id) throws Exception {
        News news = newsRepository.findById(id).orElseThrow(() -> new Exception("No news was found with the given id"));
        fileStorageService.deleteImage(news.getImage());
        newsRepository.deleteById(id);
    }

    public News updateNews(News newNews) throws Exception {
        News originalNews = newsRepository.findById(newNews.getId()).orElseThrow(() -> new Exception("No news was found with the given id"));
        originalNews.setTitle(newNews.getTitle());
        originalNews.setContent(newNews.getContent());
        originalNews.setUpdated(LocalDateTime.now());
        originalNews.setImage(newNews.getImage());
        return newsRepository.save(originalNews);
    }

    public List<News> getPage(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("created").descending());
        Page<News> pagedResult = newsRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<News>();
        }
    }
}
