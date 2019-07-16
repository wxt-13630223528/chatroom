package myeverthingg1.config;

import com.sun.xml.internal.fastinfoset.algorithm.BooleanEncodingAlgorithm;
import sun.awt.image.IntegerInterleavedRaster;

/**
 * everything的配置信息
 */
public class EverythingConfig {
    private static volatile EverythingConfig config;

    /**
     * 1. 索引目录
     * getter
     */
    private HandlerPath handlerPath = HandlerPath.getDefaultHandlerPath();
    public HandlerPath getHandlerPath() {
        return handlerPath;
    }
    /**
     * 2. 最大检索返回的结果数
     */
    //getter setter
    private Integer maxReturn = 30;
    //------------------------------------------
    public Integer getMaxReturn() {
        return maxReturn;
    }

    public void setMaxReturn(Integer maxReturn) {
        this.maxReturn = maxReturn;
    }
    //-------------------------------------------

    /**
     * 3.   是否开启构建索引
     *      默认：程序运行时不开启构建索引
     *      1.运行程序时，指定参数
     *      2.通过运行命令index
     *  getter setter
     */
    private Boolean enableBuildIndex = false;
    //----------------------------------
    public Boolean getEnableBuildIndex() {
        return enableBuildIndex;
    }

    public void setEnableBuildIndex(Boolean enableBuildIndex) {
        this.enableBuildIndex = enableBuildIndex;
    }
    //-----------------------------------

    /**
     * 4.   检索时depth深度的排序规则
     *      true：表示降序
     *      false:表示升序
     *      默认是降序
     *      getter setter
     */
    private Boolean orderByDesc = false;
    //---------------------------------

    public Boolean getOrderByDesc() {
        return orderByDesc;
    }

    public void setOrderByDesc(Boolean orderByDesc) {
        this.orderByDesc = orderByDesc;
    }

    //---------------------------------
    /**
     * 文件监控的间隔时间10s
     */
    private Integer interval = 6000*10;
    //---------------------------

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }


    //--------------------------


    private EverythingConfig(){

    }

    public static EverythingConfig getInstance(){
        if(config == null){
            synchronized (EverythingConfig.class){
                if(config == null){
                    config = new EverythingConfig();
                }
            }
        }
        return config;
    }

    @Override
    public String toString() {
        return "EverythingConfig{" +
                "handlerPath=" + handlerPath +
                ", maxReturn=" + maxReturn +
                ", enableBuildIndex=" + enableBuildIndex +
                ", orderByDepth=" + orderByDesc +
                '}';
    }
//
//    public static void main(String[] args) {
//        System.out.println( EverythingConfig.getInstance());
//    }
}
