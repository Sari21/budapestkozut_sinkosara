package hu.sinko.bpkozut.API;

import hu.sinko.bpkozut.Model.News;
import hu.sinko.bpkozut.Service.FileStorageService;
import hu.sinko.bpkozut.Service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@CrossOrigin(origins = "*")
@RequestMapping("api/news")
@RestController
public class NewsController {
    private final NewsService newsService;
    private final FileStorageService fileStorageService;

    @Autowired
    public NewsController(NewsService newsService, FileStorageService fileStorageService) {
        this.newsService = newsService;
        this.fileStorageService = fileStorageService;
    }

    @Operation(summary = "Save news", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Missing value(s)", content = @Content)
    })
    @PostMapping
    public News addNews(@RequestBody News news) throws IOException {
        return newsService.addNews(news);
    }

    @Operation(summary = "Save image", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Too large image, missing image or not acceptable file format", content = @Content)
    })
    @PostMapping(path = "/image")
    ResponseEntity<HashMap<String, String>> addImage(@RequestParam("file") MultipartFile file) throws Exception {
        String filename = fileStorageService.storeImage(file);
        HashMap<String, String> map = new HashMap<>();
        map.put("image", filename);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Operation(summary = "Update image", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Too large image, missing image or not acceptable file format", content = @Content)
    })
    @PutMapping(path = "/image/{id}")
    ResponseEntity<HashMap<String, String>> updateImage(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) throws Exception {
        String filename = this.newsService.getNewsById(id).orElseThrow(() -> new RuntimeException("There is no news with this id")).getImage();
        filename = fileStorageService.updateImage(file, filename);
        HashMap<String, String> map = new HashMap<>();
        map.put("image", filename);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Operation(summary = "Get number of news", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
    })
    @GetMapping
    public ResponseEntity<HashMap<String, Long>> getNumberOfNews() {
        HashMap<String, Long> map = new HashMap<>();
        map.put("numberOfNews", this.newsService.getNumberOfNews());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Operation(summary = "Get image by id", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "News not found", content = @Content)
    })
    @GetMapping(path = "{id}")
    public Optional<News> getNewsById(@PathVariable("id") Long id) {
        return newsService.getNewsById(id);
    }

    @Operation(summary = "Get page by page index", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
    })
    @GetMapping(path = "page/{pageNum}")
    public List<News> getPage(@PathVariable int pageNum) {
        return newsService.getPage(pageNum, 9);
    }

    @Operation(summary = "Delete news by id", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
    })
    @DeleteMapping(path = "{id}")
    public void deleteNewsById(@PathVariable("id") Long id) throws Exception {
        newsService.deleteNewsById(id);
    }

    @Operation(summary = "Update news", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content)
    })
    @PutMapping()
    public News updateNews(@RequestBody News news) throws Exception {
        return newsService.updateNews(news);
    }
}
