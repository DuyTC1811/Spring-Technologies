package com.example.springminio.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponse {
    private String filename;
    private long size;
    private String url;
}
