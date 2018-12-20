package com.cloud.mdd.mybatisplus.mybatisplus.config;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.spring.boot.starter.SpringBootVFS;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/8 14:12
 */
@Slf4j
@EnableConfigurationProperties(MybatisProperties.class)
@Configuration
public class MybatisPlusConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MybatisProperties mybatisProperties;

    @Autowired
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Autowired(required = false)
    private Interceptor[] interceptors;

    @Autowired(required = false)
    private DatabaseIdProvider databaseIdProvider;

    //ID类型
    @Value("${mybatis.configuration.id-type}")
    private Integer idType;
    //字段策略
    @Value("${mybatis.configuration.field-strategy}")
    private Integer  fieldStrategy;
    //驼峰下划线转换
    @Value("${mybatis.configuration.map-underscore-to-camel-case}")
    private boolean  dbColumnUnderline;
    // 是否刷新mapper
    @Value("${mybatis.configuration.refresh-mapper}")
    private boolean isRefresh;
    // 是否大写命名
    @Value("${mybatis.configuration.capital-mode}")
    private boolean isCapitalMode;

    /**
     * 性能插件注入
     *
     * @return
     */
    @Bean
    @Profile("dev") //开发环境生效
    public PerformanceInterceptor performanceInterceptor() {
        log.info("mybatis-plus sql 性能插件 注入");
        return new PerformanceInterceptor();
    }

    /**
     * 分页插件注入
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        log.info("mybatis-plus mysql 分页插件注入");
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType("mysql");
        return paginationInterceptor;
    }

    /**
     * 乐观锁插件
     *
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        log.info("mybatis-plus 乐观锁插件注入");
        return new OptimisticLockerInterceptor();
    }

    /**
     * 这里全部使用mybatis-autoconfigure 已经自动加载的资源。不手动指定
     * 配置文件和mybatis-boot的配置文件同步
     * @return
     */
    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() {
        log.info("mybatis-plus 全局配置==>start");
        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(dataSource);
        mybatisPlus.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.mybatisProperties.getConfigLocation())) {
            mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.mybatisProperties.getConfigLocation()));
        }
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            mybatisPlus.setPlugins(this.interceptors);
        }

        GlobalConfiguration global = new GlobalConfiguration(new LogicSqlInjector());
        global.setDbType(DBType.MYSQL.name());
        global.setIdType(idType);
        global.setCapitalMode(isCapitalMode);
        global.setDbColumnUnderline(dbColumnUnderline);
        global.setFieldStrategy(fieldStrategy);
        //逻辑删除
        global.setLogicDeleteValue("true");
        global.setLogicNotDeleteValue("false");

        //公共字段注入
        global.setMetaObjectHandler(new CloudProviderMetaObjectHandler());

        mybatisPlus.setGlobalConfig(global);

        org.apache.ibatis.session.Configuration configuration  = mybatisProperties.getConfiguration();
        MybatisConfiguration mc = new MybatisConfiguration();
        mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        mc.setSafeRowBoundsEnabled(configuration.isSafeRowBoundsEnabled());
        mc.setSafeResultHandlerEnabled(configuration.isSafeResultHandlerEnabled());
        mc.setMapUnderscoreToCamelCase(configuration.isMapUnderscoreToCamelCase());
        mc.setAggressiveLazyLoading(configuration.isAggressiveLazyLoading());
        mc.setMultipleResultSetsEnabled(configuration.isMultipleResultSetsEnabled());
        mc.setUseGeneratedKeys(configuration.isUseGeneratedKeys());
        mc.setUseColumnLabel(configuration.isUseColumnLabel());
        mc.setCacheEnabled(configuration.isCacheEnabled());
        mc.setCallSettersOnNulls(configuration.isCallSettersOnNulls());
        mc.setUseActualParamName(configuration.isUseActualParamName());
        mc.setReturnInstanceForEmptyRow(configuration.isReturnInstanceForEmptyRow());
        mybatisPlus.setConfiguration(mc);
        if (this.databaseIdProvider != null) {
            mybatisPlus.setDatabaseIdProvider(this.databaseIdProvider);
        }
        if (StringUtils.hasLength(this.mybatisProperties.getTypeAliasesPackage())) {
            mybatisPlus.setTypeAliasesPackage(this.mybatisProperties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(this.mybatisProperties.getTypeHandlersPackage())) {
            mybatisPlus.setTypeHandlersPackage(this.mybatisProperties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(this.mybatisProperties.resolveMapperLocations())) {
            mybatisPlus.setMapperLocations(this.mybatisProperties.resolveMapperLocations());
        }
        log.info("mybatis-plus 全局配置==>end");
        return mybatisPlus;
    }
}
