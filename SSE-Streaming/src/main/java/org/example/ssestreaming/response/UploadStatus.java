package org.example.ssestreaming.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.ssestreaming.enums.UploadState;

@Getter
@Setter
@AllArgsConstructor
public class UploadStatus {
    private String uploadId;
    private int processed;
    private UploadState state;
    private String message;
}
