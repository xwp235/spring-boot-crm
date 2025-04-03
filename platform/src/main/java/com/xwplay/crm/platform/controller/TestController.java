package com.xwplay.crm.platform.controller;

import com.xwplay.crm.common.resp.JsonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("1")
    public Map<String, Object> test3() {
        logger.info("test3 executed! logId is printed");
        var ld1 = LocalDateTime.now();
        var d1 = new Date();
        var zd1 = ZonedDateTime.now();
        return Map.of(
                "d1", d1,
                "zd1", zd1,
                "localDateTime1", ld1, "double1", 1.24,
                "float1", 1.1f, "long1", 100L);
    }

    private static final String TEMP_DIR = "D:\\upload\\";
    private static final String TARGET_DIR = "D:\\upload\\result\\";

    @PostMapping("upload-chunk")
    public JsonResp uploadChunk(
            @RequestParam MultipartFile chunkFile,
            @RequestParam int chunkIndex,
            @RequestParam String filename
    ) throws IOException {
        var chunkFilepath = TEMP_DIR + filename;
        var chunkFileDir = Paths.get(chunkFilepath);
        if (!Files.exists(chunkFileDir)) {
            Files.createDirectories(chunkFileDir);
        }
        var splitChunkFilepath = Paths.get(chunkFilepath, "chunk_" + chunkIndex);
        var createdSplitChunkFilepath = Files.createFile(splitChunkFilepath);
        Files.write(createdSplitChunkFilepath, chunkFile.getBytes());
        return JsonResp.ok();
    }

    @PostMapping("merge-chunks")
    public JsonResp mergeChunks(@RequestParam String filename) throws IOException {
        var dir = new File(TEMP_DIR + filename);
        var targetDirFilepath = Paths.get(TARGET_DIR);
        if (!Files.exists(targetDirFilepath)) {
            Files.createDirectories(targetDirFilepath);
        }
        var mergedFilepath = Paths.get(TARGET_DIR, filename);
        var chunkFiles = dir.listFiles();
        try (var os = Files.newOutputStream(mergedFilepath)) {
            if (Objects.nonNull(chunkFiles)) {
                for (var i = 0; i < chunkFiles.length; i++) {
                    var chunkFile = new File(dir, "chunk_" + i);
                    var filepath = chunkFile.toPath();
                    Files.copy(filepath, os);
                    Files.deleteIfExists(filepath);
                }
            }
        }

        Files.delete(dir.toPath());
        return JsonResp.ok("文件合并完成");
    }
}
