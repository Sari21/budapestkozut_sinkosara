package hu.sinko.bpkozut.Repository;

import hu.sinko.bpkozut.Model.News;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends PagingAndSortingRepository<News, Long>, CrudRepository<News, Long> {

}