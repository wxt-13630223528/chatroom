package EverthingRoot.core.model;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;

/**
 *
 */

/**
 * 使用Lombok工具
 * 1.第一步：在maven的配置中引入Lombok库
 * 2.在IDEA中安装lombok插件 Plugin
 * 3.在IDEA中启用注解处理器
 */

public class Thing {
    /**
     * 文件名称(不含路径)
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件路径深度
     */
    private Integer depth;

    /**
     * 文件类型
     */
    private FileType fileType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
