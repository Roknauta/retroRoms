package com.roknauta.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rom {

    private String extension;
    private String crc32;
    private String md5;
    private String sha1;
    private String sha256;
}
