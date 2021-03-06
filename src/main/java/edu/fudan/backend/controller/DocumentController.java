package edu.fudan.backend.controller;

import edu.fudan.backend.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author tyuan@ea.com
 * @Date 8/11/2020 3:36 PM
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class DocumentController {

    private final DocumentService documentService;

    @RequestMapping(value = "/rule", method = RequestMethod.GET)
    public Map<String, Object> getDocList(
            @CookieValue("username") String username,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageNum,
            @RequestParam(value = "sorter", defaultValue = "{}") String sorter,
            @RequestParam(value = "filter", defaultValue = "{}") String filter) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> allDocument = documentService.getAllDocument(current, pageNum, username);
            result.put("data", allDocument.get("list"));
            result.put("total", allDocument.get("total"));
            result.put("success", true);
            result.put("pageSize", pageNum);
            result.put("current", current);
            log.info("list documents");
            return result;
        } catch (Exception e) {
            log.error("get documents errot");
            return result;
        }
    }

    @RequestMapping(value = "/rule", method = RequestMethod.POST)
    public Map<String, Object> uploadDoc(@CookieValue("username") String username, @RequestParam(value = "file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            documentService.uploadFile(file, username);
            result.put("status", "ok");
            log.info("upload document success");
            return result;
        } catch (Exception e) {
            result.put("status", "error");
            log.error("upload document failure", e);
            return result;
        }
    }

    @RequestMapping(value = "/rule", method = RequestMethod.DELETE)
    public Map<String, Object> deleteDoc(@RequestBody Map<String, Object> body) {
        try {
            documentService.deleteFiles((List<Integer>) body.get("key"));
            log.info("delete document:{}", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
}
