package com.carlos.minio.web;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;

/**
 * <p>
 * minio对象
 * </p>
 *
 * @author carlos
 * @date 2021/6/10 14:04
 */
@Data
@AllArgsConstructor
public final class MinioObject implements Serializable {

    /**
     * 所在桶
     */
    private final String bucket;
    /**
     * 对象名称
     */
    private final String object;
    /**
     * 文件名
     */
    private final String attachmentName;


    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {

        private String bucket;
        private String object;
        private String attachName;

        public Builder bucket(String bucket) {
            this.bucket = Objects.requireNonNull(bucket);
            return this;
        }

        public Builder object(String object) {
            this.object = Objects.requireNonNull(object);
            return this;
        }

        public Builder object(Path object) {
            this.object = Objects.requireNonNull(object).toString();
            return this;
        }

        public Builder attachmentName(String attachName) {
            this.attachName = Objects.requireNonNull(attachName);
            return this;
        }

        public MinioObject build() {
            if (this.bucket == null) {
                throw new NullPointerException();
            }
            if (this.object == null) {
                throw new NullPointerException();
            }
            if (this.attachName == null) {
                throw new NullPointerException();
            }

            return new MinioObject(
                    this.bucket,
                    this.object,
                    this.attachName
            );
        }
    }

}
